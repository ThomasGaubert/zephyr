package com.texasgamer.zephyr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics firebaseAnalytics;

    private MainAcvitiyReceiver mainAcvitiyReceiver;
    private BottomSheetBehavior bottomSheetBehavior;

    private String serverAddr = "http://127.0.0.1:3753/";
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfFirstRun();

        setupAnalytics();
        startSocketService();
        requestConnectionStatus();

        setupUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NotificationManagerCompat.getEnabledListenerPackages(this).contains("com.texasgamer.zephyr")) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            firebaseAnalytics.logEvent(getString(R.string.analytics_show_enable_notif_perm), null);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        mainAcvitiyReceiver = new MainAcvitiyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.texasgamer.zephyr.MAIN_ACTIVITY");
        registerReceiver(mainAcvitiyReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mainAcvitiyReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                firebaseAnalytics.logEvent(getString(R.string.analytics_tap_settings), null);

                Intent i = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkIfFirstRun() {
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_first_run), true)) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void setupAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupUi() {
        final Button connectBtn = (Button) findViewById(R.id.connectBtn);
        final Button testNotifBtn = (Button) findViewById(R.id.testNotifBtn);
        final EditText serverAddrField = ((EditText) findViewById(R.id.serverAddrField));
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connected && serverAddrField.getText().toString().isEmpty()) {
                    return;
                }

                if(!connected) {
                    serverAddr = serverAddrField.getText().toString();
                    if(connectBtn.getText().toString().equals(getString(R.string.btn_connecting))) {
                        connectBtn.setText(R.string.btn_connect);
                        Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                        i.putExtra("type", "disconnect");
                        sendBroadcast(i);
                    } else {
                        connectBtn.setText(R.string.btn_connecting);
                        Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                        i.putExtra("type", "connect");
                        i.putExtra("address", serverAddrField.getText().toString());
                        sendBroadcast(i);
                    }

                    Bundle b = new Bundle();
                    b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                    firebaseAnalytics.logEvent(getString(R.string.analytics_tap_connect), b);
                } else {
                    connectBtn.setText(R.string.btn_disconnecting);
                    Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                    i.putExtra("type", "disconnect");
                    sendBroadcast(i);

                    Bundle b = new Bundle();
                    b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                    firebaseAnalytics.logEvent(getString(R.string.analytics_tap_disconnect), b);
                }
            }
        });

        testNotifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected) {
                    firebaseAnalytics.logEvent(getString(R.string.analytics_tap_test_notif_connected), null);

                    Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                    i.putExtra("type", "test");
                    sendBroadcast(i);
                } else {
                    firebaseAnalytics.logEvent(getString(R.string.analytics_tap_test_notif_disconnected), null);

                    Snackbar.make(findViewById(R.id.main_content), R.string.snackbar_not_connected, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        serverAddrField.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_last_addr), ""));

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        findViewById(R.id.enableNotificationsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAnalytics.logEvent(getString(R.string.analytics_tap_enable_notif_perm), null);
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
    }

    private void updateConnectBtn() {
        runOnUiThread(new Runnable() {
            public void run() {
                ((Button) findViewById(R.id.connectBtn)).setText(connected ? R.string.btn_disconnect : R.string.btn_connect);
            }
        });
    }

    private void startSocketService() {
        Intent i = new Intent(this, SocketService.class);
        startService(i);
    }

    private void requestConnectionStatus() {
        Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
        i.putExtra("type", "status");
        sendBroadcast(i);
    }

    class MainAcvitiyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            switch (type) {
                case "connected":
                    connected = true;
                    serverAddr = intent.getStringExtra("address");
                    ((EditText) findViewById(R.id.serverAddrField)).setText(serverAddr);
                    updateConnectBtn();
                    if (!intent.getBooleanExtra("silent", false)) {
                        Snackbar.make(findViewById(R.id.main_content), R.string.snackbar_connected, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case "disconnected":
                    connected = false;
                    updateConnectBtn();
                    if (!intent.getBooleanExtra("silent", false)) {
                        Snackbar.make(findViewById(R.id.main_content), R.string.snackbar_disconnected, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case "notif-sent":
                    Snackbar.make(findViewById(R.id.main_content), R.string.snackbar_notif_confirm, Snackbar.LENGTH_SHORT).show();
                    break;
                case "notif-failed":
                    Snackbar.make(findViewById(R.id.main_content), R.string.snackbar_notif_fail, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
