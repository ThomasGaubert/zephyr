package com.texasgamer.zephyr.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.service.QuickSettingService;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.VibrationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.view.ScannerOverlayView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

/**
 * Fragment used when scanning a join code.
 */
public class ScanCodeFragment extends RoundedBottomSheetDialogFragment {

    private static final String LOG_TAG = "ScanCodeFragment";

    @Inject
    ILogger logger;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IConfigManager configManager;

    private LinearLayout mPermissionDeniedAlert;
    private LinearLayout mScanConfirmation;
    private TextView mScannedValue;
    private ProgressBar mSpinner;
    private ScannerOverlayView mOverlay;
    private CameraView mCameraView;
    private Button mConfirmButton;
    private Button mScanButton;
    private Button mOpenPermissionsButton;

    private BarcodeScanner mBarcodeScanner;
    private AtomicBoolean mIsDetectorRunning;
    private boolean mDidAlertInvalidCode;
    private boolean mEnableQrCodeIndicators;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan_code, container, false);
        mPermissionDeniedAlert = root.findViewById(R.id.permission_denied_alert);
        mScanConfirmation = root.findViewById(R.id.scan_confirmation);
        mScannedValue = root.findViewById(R.id.scanned_value);
        mSpinner = root.findViewById(R.id.spinner);
        mOverlay = root.findViewById(R.id.scanner_overlay);
        mCameraView = root.findViewById(R.id.camera);
        mConfirmButton = root.findViewById(R.id.confirm_button);
        mScanButton = root.findViewById(R.id.scan_button);
        mOpenPermissionsButton = root.findViewById(R.id.open_permissions_button);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        if (!configManager.isQrCodeScanningEnabled()) {
            logger.log(LogLevel.WARNING, LOG_TAG, "QR code scanning is disabled!");
            Toast.makeText(getContext(), R.string.menu_scan_qr_error_not_supported, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        mConfirmButton.setOnClickListener(v -> {
            preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, mScannedValue.getText().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                QuickSettingService.updateQuickSettingTile(requireContext());
            }
            dismiss();
        });

        mScanButton.setOnClickListener(v -> startScanning());

        mOpenPermissionsButton.setOnClickListener(v -> NavigationUtils.openZephyrAppInfo(requireContext()));

        mEnableQrCodeIndicators = configManager.isQrCodeIndicatorsEnabled();

        Permissions.check(getContext(), Manifest.permission.CAMERA, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                logger.log(LogLevel.INFO, LOG_TAG, "Camera permission granted, starting camera...");
                mPermissionDeniedAlert.setVisibility(View.GONE);
                mCameraView.setVisibility(View.VISIBLE);
                setupCamera();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                logger.log(LogLevel.INFO, LOG_TAG, "Camera permission denied.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                logger.log(LogLevel.INFO, LOG_TAG, "Camera permission blocked.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                logger.log(LogLevel.INFO, LOG_TAG, "Camera permission just blocked.");
                mCameraView.setVisibility(View.GONE);
                mPermissionDeniedAlert.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupCamera() {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();

        mBarcodeScanner = BarcodeScanning.getClient(options);
        mIsDetectorRunning = new AtomicBoolean(false);

        mCameraView.setLifecycleOwner(getViewLifecycleOwner());

        mCameraView.addCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened(@NonNull CameraOptions options) {
                mSpinner.setVisibility(View.GONE);
                super.onCameraOpened(options);
            }
        });

        mCameraView.addFrameProcessor(frame -> {
            try {
                scanQrCode(frame.getData(),
                        frame.getSize().getWidth(),
                        frame.getSize().getHeight(),
                        90,
                        frame.getFormat());
            } catch (Exception e) {
                logger.log(LogLevel.ERROR, LOG_TAG, "Error while scanning for QR code.", e);
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
        mSpinner.setVisibility(View.GONE);
        mScanConfirmation.setVisibility(View.VISIBLE);
    }

    private void scanQrCode(@NonNull byte[] bytes, int width, int height, int rotationDegrees, int format) {
        if (mIsDetectorRunning.get()) {
            return;
        }

        InputImage visionImage = InputImage.fromByteArray(bytes, width, height, rotationDegrees, format);

        mIsDetectorRunning.set(true);
        mBarcodeScanner.process(visionImage).addOnSuccessListener(barcodes -> {
            if (mEnableQrCodeIndicators) {
                Rect[] boundingBoxes = new Rect[barcodes.size()];
                for (int x = 0; x < barcodes.size(); x++) {
                    boundingBoxes[x] = barcodes.get(x).getBoundingBox();
                }
                mOverlay.setBoundingBoxes(boundingBoxes);
            }

            for (Barcode barcode : barcodes) {
                String barcodeValue = barcode.getDisplayValue();
                if (barcodeValue == null) {
                    continue;
                }

                logger.log(LogLevel.DEBUG, LOG_TAG, "Barcode found: " + barcodeValue);
                if (NetworkUtils.isValidJoinCode(barcodeValue)) {
                    logger.log(LogLevel.DEBUG, LOG_TAG, barcodeValue + " is a valid join code.");
                    VibrationUtils.vibrate(getContext());
                    mScannedValue.setText(barcodeValue);
                    stopScanning();
                    return;
                }
            }

            if (!mDidAlertInvalidCode && barcodes.size() > 0) {
                Toast.makeText(getContext(), R.string.menu_scan_qr_invalid_code_toast, Toast.LENGTH_SHORT).show();
                mDidAlertInvalidCode = true;
            }

            mIsDetectorRunning.set(false);
        }).addOnFailureListener(e -> logger.log(LogLevel.ERROR, LOG_TAG, "Error while scanning for QR code.", e));
    }
}
