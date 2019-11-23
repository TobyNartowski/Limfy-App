package pl.tobynartowski.limfy.activity;

import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
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

import java.util.List;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class ConnectActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ViewUtils.makeFullscreen(getWindow());

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ViewUtils.showToast(this, getResources().getString(R.string.error_not_supported));
            finish();
        }

        new Handler().postDelayed(this::initBluetooth, 1000);

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
                        }
                    }
                };

                bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
                new Handler().postDelayed(() -> {
                    // TODO: add if not connected boolean
                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                    ViewUtils.showToast(this, getResources().getString(R.string.error_device_not_found));
                    progressBar.setVisibility(View.INVISIBLE);
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
