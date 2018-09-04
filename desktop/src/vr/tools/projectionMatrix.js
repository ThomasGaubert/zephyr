// A class to encode/decode HmdMatrix34_t values based on position/rotation.

function deg2rad (val) {
  return val * (Math.PI / 180);
}

function rad2deg (val) {
  return val / (Math.PI / 180);
}

class Vector3 {
  constructor (x, y, z, { isRadians = false } = {}) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.isRadians = isRadians;
  }

  static get zero () {
    return new Vector3(0, 0, 0);
  }

  static get one () {
    return new Vector3(1, 1, 1);
  }

  mul (x, y = null, z = null) {
    if (y == null && z == null) {
      // vector x linear math, ezgg
      return new Vector3(this.x * x, this.y * x, this.z * x);
    }

    if (x instanceof Vector3) {
      // vector math!
      const v = x;
      x = v.x;
      y = v.y;
      z = v.z;
    }

    return new Vector3(this.x * x, this.y * y, this.z * z);
  }

  get radians () {
    if (this.isRadians) {
      return this;
    }

    return new Vector3(
      deg2rad(this.x),
      deg2rad(this.y),
      deg2rad(this.z),
      { isRadians: true }
    );
  }

  get degrees () {
    if (!this.isRadians) {
      return this;
    }

    return new Vector3(
      rad2deg(this.x),
      rad2deg(this.y),
      rad2deg(this.z),
      { isRadians: false }
    );
  }
}

class Quaterion {
  constructor (x, y, z, w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  static get identity () {
    return new Quaterion(0, 0, 0, 1);
  }

  // vec is a Tait-Bryan angle set, X is forward/roll, Y is right/pitch, Z is down/yaw
  // if this is different to OpenVR's handedness, i will correct it.
  static fromEuler (vec) {
    const halfVec = vec.mul(0.5);

    const cx = Math.cos(halfVec.x);
    const sx = Math.sin(halfVec.x);
    const cy = Math.cos(halfVec.y);
    const sy = Math.sin(halfVec.y);
    const cz = Math.cos(halfVec.z);
    const sz = Math.sin(halfVec.z);

    return new Quaterion(
      // c s c - s c s
      cz * sx * cy - sz * cx * sy,
      // c c s + s s c
      cz * cx * sy + sz * sx * cy,
      // s c c - c s s
      sz * cx * cy - cz * sx * sy,
      // c c c + s s s
      cz * cx * cy + sz * sx * sy
    );
  }

  get rotationMatrix () {
    const { x, y, z, w } = this;

    const xx = x * x;
    const xy = x * y;
    const xz = x * z;
    const xw = x * w;

    const yy = y * y;
    const yz = y * z;
    const yw = y * w;

    const zz = z * z;
    const zw = z * w;

    return Matrix3x4.fromArrays([
      [
        1 - 2 * (yy + zz), // 0
        2 * (xy + zw), // 4
        2 * (xz - yw), // 8
        0
      ],
      [
        2 * (xy - zw), // 1
        1 - 2 * (xx + zz), // 5
        2 * (yz + xw), // 9
        0
      ],
      [
        2 * (xz + yw), // 2
        2 * (yz - xw), // 6
        1 - 2 * (xx + yy), // 10
        0
      ]
    ]);
  }
}

class Matrix3x4 {
  constructor (from) {
    // console.log({from})
    if (from.length !== 3 || from[0].length !== 4 || from[1].length !== 4 || from[2].length !== 4) {
      return TypeError('Matrix3x4 requires 3 sets of 4 linear numbers');
    }

    this.data = from;
  }

  static get identity () {
    return Matrix3x4.fromTransform({ T: Vector3.zero, R: Quaterion.identity, S: Vector3.one });
  }

  static fromArrays (from) {
    return new Matrix3x4(from);
  }

  static fromTransform ({ T, R, S, translation, rotation, scale }) {
    T = T || translation || Vector3.zero;
    R = R || rotation || Quaterion.identity;
    S = S || scale || Vector3.one;

    const tMat = new Matrix3x4([
      [0, 0, 0, T.x],
      [0, 0, 0, T.y],
      [0, 0, 0, T.z]
    ]);

    if (R instanceof Vector3) {
      R = Quaterion.fromEuler(R);
    } else if (!(R instanceof Quaterion)) {
      throw new TypeError('Matrix3x4.rotate needs a Vector3 or Quaternion for R/rotation');
    }

    const rMat = R.rotationMatrix;

    const sMat = new Matrix3x4([
      [S.x, 0, 0, 0],
      [0, S.y, 0, 0],
      [0, 0, S.z, 0]
    ]);

    return tMat.add(rMat.mul(sMat));
  }

  rotate (R) {
    if (R instanceof Vector3) {
      R = Quaterion.fromEuler(R);
    } else if (!(R instanceof Quaterion)) {
      throw new TypeError('Matrix3x4.rotate needs a Vector3 or Quaternion for R/rotation');
    }

    const rm = R.rotationMatrix;
    return this.mul(rm);
  }

  mul (b) {
    if (b instanceof Matrix3x4) {
      b = b.data;
    }
    return Matrix3x4._mul(this.data, b);
  }

  add (b) {
    if (b instanceof Matrix3x4) {
      b = b.data;
    }
    return Matrix3x4._add(this.data, b);
  }

  sub (b) {
    if (b instanceof Matrix3x4) {
      b = b.data;
    }
    return Matrix3x4._sub(this.data, b);
  }

  static _mul (a, b) {
    // console.log({a, b})

    const out = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]];

    for (var r = 0; r < a.length; ++r) {
      for (var c = 0; c < b[0].length; ++c) {
        for (var i = 0; i < a[0].length; ++i) {
          b[i] = b[i] || [0, 0, 0, 0];
          // console.log({ r, c, i, o: out[r], a: a[r], b: b[i] })
          out[r][c] += a[r][i] * b[i][c];
        }
      }
    }

    return new Matrix3x4(out);
  }

  static _add (a, b) {
    const out = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]];

    for (var r = 0; r < a.length; ++r) {
      for (var c = 0; c < b[0].length; ++c) {
        out[r][c] = a[r][c] + b[r][c];
      }
    }

    return Matrix3x4.fromArrays(out);
  }

  static _sub (a, b) {
    const out = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]];

    for (var r = 0; r < a.length; ++r) {
      for (var c = 0; c < b[0].length; ++c) {
        out[r][c] = a[r][c] - b[r][c];
      }
    }

    return Matrix3x4.fromArrays(out);
  }

  toHmdMatrix34 () {
    return this.data;
  }
}

export default {
  Vector3,
  Quaterion,
  Matrix3x4
};
