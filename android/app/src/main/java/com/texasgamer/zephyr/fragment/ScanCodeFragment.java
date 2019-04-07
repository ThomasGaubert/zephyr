package com.texasgamer.zephyr.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.otaliastudios.cameraview.CameraView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment used when scanning a join code.
 */
public class ScanCodeFragment extends RoundedBottomSheetDialogFragment {

    private static final String LOG_TAG = "ScanCodeFragment";

    @BindView(R.id.permission_denied_alert)
    LinearLayout mPermissionDeniedAlert;
    @BindView(R.id.scan_confirmation)
    LinearLayout mScanConfirmation;
    @BindView(R.id.scanned_value)
    TextView mScannedValue;
    @BindView(R.id.camera)
    CameraView mCameraView;

    @Inject
    ILogger logger;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IConfigManager configManager;

    private FirebaseVisionBarcodeDetector mBarcodeDetector;
    private AtomicBoolean mIsDetectorRunning;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan_code, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        if (!configManager.isQrCodeScanningEnabled()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "QR code scanning is disabled!");
            Toast.makeText(getContext(), R.string.menu_scan_qr_error_not_supported, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        Permissions.check(getContext(), Manifest.permission.CAMERA, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                logger.log(LogPriority.INFO, LOG_TAG, "Camera permission granted, starting camera...");
                mPermissionDeniedAlert.setVisibility(View.GONE);
                mCameraView.setVisibility(View.VISIBLE);
                setupCamera();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                logger.log(LogPriority.INFO, LOG_TAG, "Camera permission denied.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                logger.log(LogPriority.INFO, LOG_TAG, "Camera permission blocked.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                logger.log(LogPriority.INFO, LOG_TAG, "Camera permission just blocked.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.confirm_button)
    public void onClickConfirm() {
        preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, mScannedValue.getText().toString());
        dismiss();
    }

    @OnClick(R.id.scan_button)
    public void onClickScan() {
        startScanning();
    }

    @OnClick(R.id.open_permissions_button)
    public void onClickGrantPermission() {
        NavigationUtils.openZephyrAppInfo(getContext());
    }

    private void setupCamera() {
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();

        mBarcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        mIsDetectorRunning = new AtomicBoolean(false);

        mCameraView.setLifecycleOwner(getViewLifecycleOwner());

        mCameraView.addFrameProcessor(frame -> {
            try {
                FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                        .setWidth(frame.getSize().getWidth())
                        .setHeight(frame.getSize().getHeight())
                        .setFormat(frame.getFormat())
                        .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
                        .build();

                scanQrCode(frame.getData(), metadata);
            } catch (Exception e) {
                logger.log(LogPriority.ERROR, LOG_TAG, "Error while scanning for QR code.", e);
            }
        });
    }

    private void startScanning() {
        mScanConfirmation.setVisibility(View.GONE);
        mCameraView.setVisibility(View.VISIBLE);
        mCameraView.open();
        mIsDetectorRunning.set(false);
    }

    private void stopScanning() {
        mIsDetectorRunning.set(true);
        mCameraView.close();
        mCameraView.setVisibility(View.GONE);
        mScanConfirmation.setVisibility(View.VISIBLE);
    }

    private void scanQrCode(@NonNull byte[] bytes, @NonNull FirebaseVisionImageMetadata metadata) {
        if (mIsDetectorRunning.get()) {
            return;
        }

        FirebaseVisionImage visionImage = FirebaseVisionImage.fromByteArray(bytes, metadata);

        mIsDetectorRunning.set(true);
        mBarcodeDetector.detectInImage(visionImage).addOnSuccessListener(firebaseVisionBarcodes -> {
            for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes) {
                String barcodeValue = barcode.getDisplayValue();
                if (barcodeValue == null) {
                    continue;
                }

                logger.log(LogPriority.DEBUG, LOG_TAG, "Barcode found: " + barcodeValue);
                if (NetworkUtils.isValidJoinCode(barcodeValue)) {
                    logger.log(LogPriority.DEBUG, LOG_TAG, barcodeValue + " is a valid join code.");
                    mScannedValue.setText(barcodeValue);
                    stopScanning();
                    return;
                }
            }
            mIsDetectorRunning.set(false);
        }).addOnFailureListener(e -> logger.log(LogPriority.ERROR, LOG_TAG, "Error while scanning for QR code."));
    }
}
