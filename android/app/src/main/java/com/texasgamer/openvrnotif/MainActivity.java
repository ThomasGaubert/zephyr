package com.texasgamer.openvrnotif;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private String serverAddr = "http://127.0.0.1:3753/";
    private String clientId = "android";
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupUi() {
        final Button connectBtn = (Button) findViewById(R.id.connectBtn);
        final EditText serverAddrField = ((EditText) findViewById(R.id.serverAddrField));
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connected) {
                    serverAddr = serverAddrField.getText().toString();
                    connectBtn.setText(R.string.btn_disconnect);
                    setupSocketConnection();
                } else {
                    connectBtn.setText(R.string.btn_connect);
                    socket.disconnect();
                }

                connected = !connected;
            }
        });

        serverAddrField.setText(getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.pref_last_addr), ""));
    }

    private void setupSocketConnection() {
        try {
            socket = IO.socket("http://" + serverAddr + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("version", getVersionInfo().toString());
            }
        }).on(clientId, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // TODO: Actually verify version
                Snackbar.make(findViewById(R.id.snackbarPosition), R.string.snackbar_connected, Snackbar.LENGTH_SHORT).show();
                getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.pref_last_addr), serverAddr).apply();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Snackbar.make(findViewById(R.id.snackbarPosition), R.string.snackbar_disconnected, Snackbar.LENGTH_SHORT).show();
            }

        });
        socket.connect();
    }

    private JSONObject getVersionInfo() {
        try {
            JSONObject versionInfo = new JSONObject();
            JSONObject metadata = new JSONObject();
            metadata.put("version", 1);
            metadata.put("type", "version");
            metadata.put("from", clientId);
            metadata.put("to", "");
            JSONObject payload = new JSONObject();
            payload.put("name", "Android Client");
            payload.put("version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            payload.put("versionCode", getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            payload.put("versions", new JSONArray());
            versionInfo.put("metadata", metadata);
            versionInfo.put("payload", payload);

            return versionInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
}
