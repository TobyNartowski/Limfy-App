package pl.tobynartowski.limfy.activity;

import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class ConnectActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ViewUtils.makeFullscreen(getWindow());

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ViewUtils.showToast(this, getResources().getString(R.string.error_not_supported));
            finish();
        }

        new Handler().postDelayed(this::initBluetooth, 500);

        findViewById(R.id.connect_progress).setVisibility(View.INVISIBLE);
        ((ImageView) findViewById(R.id.connect_image)).setImageResource(R.drawable.dummy_connect_off);
        findViewById(R.id.connect_image).setOnClickListener((view) -> {
            if (bluetoothNotInitialized()) {
                initBluetooth();
            } else {
                View progressBar = findViewById(R.id.connect_progress);
                progressBar.setVisibility(View.VISIBLE);

                ScanCallback scanCallback = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if ("LimfyDevice".equals(result.getDevice().getName())) {
                            ((ImageView) view).setImageResource(R.drawable.dummy_connect_on);
                            progressBar.setVisibility(View.INVISIBLE);

                            BluetoothUtils.setBluetoothGatt(result.getDevice()
                                    .connectGatt(ConnectActivity.this, false, BluetoothUtils.getGattCallback()));
                            bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                            connected = true;

                            new Handler().postDelayed(() -> startActivity(new Intent(ConnectActivity.this, AppActualActivity.class),
                                    ActivityOptions.makeSceneTransitionAnimation(ConnectActivity.this).toBundle()), 1000);
                        }
                    }
                };

                bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
                new Handler().postDelayed(() -> {
                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                    if (!connected) {
                        ViewUtils.showToast(this, getResources().getString(R.string.error_device_not_found));
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 10000);
            }
        });
    }

    private boolean bluetoothNotInitialized() {
        return bluetoothAdapter == null || !bluetoothAdapter.isEnabled();
    }

    private void initBluetooth() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();

            int requestEnableBT = 0;
            if (bluetoothNotInitialized()) {
                Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothIntent, requestEnableBT);
            }
        } else {
            ViewUtils.showToast(this, getResources().getString(R.string.error_internal));
            finish();
        }
    }
}
