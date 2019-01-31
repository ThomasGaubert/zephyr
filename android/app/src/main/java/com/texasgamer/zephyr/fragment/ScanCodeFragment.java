package com.texasgamer.zephyr.fragment;

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
import com.otaliastudios.cameraview.CameraView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanCodeFragment extends RoundedBottomSheetDialogFragment {

    private static String LOG_TAG = "ScanCodeFragment";

    private FirebaseVisionBarcodeDetector barcodeDetector;
    private AtomicBoolean isDetectorRunning;

    @BindView(R.id.scan_confirmation)
    LinearLayout scanConfirmation;
    @BindView(R.id.scanned_value)
    TextView scannedValue;
    @BindView(R.id.camera)
    CameraView cameraView;

    @Inject
    ILogger logger;
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IConfigManager configManager;

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

        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();

        barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        isDetectorRunning = new AtomicBoolean(false);

        cameraView.setLifecycleOwner(getViewLifecycleOwner());

        cameraView.addFrameProcessor(frame -> {
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

    @OnClick(R.id.confirm_button)
    public void onClickConfirm() {
        preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, scannedValue.getText().toString());
        dismiss();
    }

    @OnClick(R.id.scan_button)
    public void onClickScan() {
        startScanning();
    }

    private void startScanning() {
        scanConfirmation.setVisibility(View.GONE);
        cameraView.setVisibility(View.VISIBLE);
        cameraView.open();
        isDetectorRunning.set(false);
    }

    private void stopScanning() {
        isDetectorRunning.set(true);
        cameraView.close();
        cameraView.setVisibility(View.GONE);
        scanConfirmation.setVisibility(View.VISIBLE);
    }

    private void scanQrCode(@NonNull byte[] bytes, @NonNull FirebaseVisionImageMetadata metadata) {
        if (isDetectorRunning.get()) {
            return;
        }

        FirebaseVisionImage visionImage = FirebaseVisionImage.fromByteArray(bytes, metadata);

        isDetectorRunning.set(true);
        barcodeDetector.detectInImage(visionImage).addOnSuccessListener(firebaseVisionBarcodes -> {
            for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes) {
                String barcodeValue = barcode.getDisplayValue();
                if (barcodeValue == null) {
                    continue;
                }

                logger.log(LogPriority.DEBUG, LOG_TAG, "Barcode found: " + barcodeValue);
                if (NetworkUtils.isValidJoinCode(barcodeValue)) {
                    logger.log(LogPriority.DEBUG, LOG_TAG, barcodeValue + " is a valid join code.");
                    scannedValue.setText(barcodeValue);
                    stopScanning();
                    return;
                }
            }
            isDetectorRunning.set(false);
        }).addOnFailureListener(e -> logger.log(LogPriority.ERROR, LOG_TAG, "Error while scanning for QR code."));
    }
}
