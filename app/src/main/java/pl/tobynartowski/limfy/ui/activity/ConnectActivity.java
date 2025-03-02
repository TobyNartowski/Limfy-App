package pl.tobynartowski.limfy.ui.activity;

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
import pl.tobynartowski.limfy.api.RestUpdater;
import pl.tobynartowski.limfy.misc.SwipeTouchListener;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class ConnectActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private boolean openBluetoothWindow = true;
    private boolean connected = false;
    private Handler loadingHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ViewUtils.makeFullscreen(getWindow());

        if (getIntent().getStringExtra("error") != null) {
            ViewUtils.showToast(this, getResources().getString(R.string.error_device_disconnected));
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ViewUtils.showToast(this, getResources().getString(R.string.error_not_supported));
            finish();
        }

        new Handler().postDelayed(this::initBluetooth, 500);

        findViewById(R.id.connect_arrow_back).setOnClickListener((v) -> onBackPressed());
        findViewById(R.id.connect_layout).setOnTouchListener(new SwipeTouchListener(this) {
            @Override
            public void onSwipeBottom() {
                if (!connected) {
                    onBackPressed();
                }
            }
        });

        findViewById(R.id.connect_progress).setVisibility(View.INVISIBLE);
        ImageView connectImage = findViewById(R.id.connect_image);
        connectImage.setImageResource(R.drawable.dummy_connect_off);
        connectImage.setOnClickListener((view) -> {
            if (bluetoothNotInitialized()) {
                initBluetooth();
            } else {
                View progressBar = findViewById(R.id.connect_progress);
                progressBar.setVisibility(View.VISIBLE);
    
                ScanCallback scanCallback = new ScanCallback() {

                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if ("LimfyDevice".equals(result.getDevice().getName()) && !BluetoothUtils.isConnected()) {
                            if (loadingHandler != null) {
                                loadingHandler.removeCallbacksAndMessages(null);
                            }
                            bluetoothAdapter.getBluetoothLeScanner().stopScan(this);

                            ((ImageView) view).setImageResource(R.drawable.dummy_connect_on);
                            progressBar.setVisibility(View.INVISIBLE);

                            BluetoothUtils.setBluetoothGatt(result.getDevice()
                                    .connectGatt(ConnectActivity.this, false, BluetoothUtils.getGattCallback()));

                            startService(new Intent(ConnectActivity.this, RestUpdater.class));
                            new Handler().postDelayed(ConnectActivity.this::onBackPressed, 1000);
                        }
                    }
                };

                bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
                loadingHandler.postDelayed(() -> {
                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                    if (!BluetoothUtils.isConnected()) {
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
        if (openBluetoothWindow) {
            openBluetoothWindow = false;
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
            openBluetoothWindow = true;
        }
    }

    @Override
    public void onBackPressed() {
        loadingHandler.removeCallbacksAndMessages(null);

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
