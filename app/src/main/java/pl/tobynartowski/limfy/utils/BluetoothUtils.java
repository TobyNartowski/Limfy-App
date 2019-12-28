package pl.tobynartowski.limfy.utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import pl.tobynartowski.limfy.model.BluetoothData;

public class BluetoothUtils {

    private static final UUID SERVICE_UUID = UUID.fromString("43428fb9-22dd-4d10-96e1-b32477365024");
    private static final UUID CHARACTERISTIC_HEARTBEAT_UUID  = UUID.fromString("5168996b-625b-4d5d-ad14-fc30d0b91fcc");
    private static final UUID CHARACTERISTIC_STEPS_UUID  = UUID.fromString("72a590ba-09fe-4e88-a8d0-508d6c001b43");
    private static final UUID CHARACTERISTIC_SHAKINESS_UUID  = UUID.fromString("cc4616ac-b55a-4d4d-8cd4-034ba13dd56a");
    private static final UUID CHARACTERISTIC_FALL_UUID = UUID.fromString("ec8357c6-6bde-4275-b03b-bbfa16cd2a47");

    private static Queue<BluetoothGattCharacteristic> characteristics = new LinkedList<>();
    private static BluetoothGatt bluetoothGatt;

    private static boolean connected = false;

    private static final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGatt.discoverServices();
                BluetoothData.getInstance().setDisconnected(false);
                connected = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                synchronized (this) {
                    if (!BluetoothData.getInstance().isDisconnected()) {
                        BluetoothData.getInstance().setDisconnected(true);
                        disconnect();
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            characteristics.addAll(gatt.getService(SERVICE_UUID).getCharacteristics());
            requestCharacteristics(gatt);
        }

        private void requestCharacteristics(BluetoothGatt gatt) {
            gatt.setCharacteristicNotification(characteristics.peek(), true);
            gatt.readCharacteristic(characteristics.peek());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(CHARACTERISTIC_HEARTBEAT_UUID)) {
                BluetoothData.getInstance().setHeartbeat(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            } else if (characteristic.getUuid().equals(CHARACTERISTIC_STEPS_UUID)) {
                BluetoothData.getInstance().setSteps(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            } else if (characteristic.getUuid().equals(CHARACTERISTIC_SHAKINESS_UUID)) {
                BluetoothData.getInstance().setShakiness(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            } else if (characteristic.getUuid().equals(CHARACTERISTIC_FALL_UUID)) {
                if (characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0) != 0) {
                    BluetoothData.getInstance().setFall(true);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                characteristics.remove();

                if (characteristics.size() > 0) {
                    requestCharacteristics(gatt);
                }
            }
        }
    };

    public static void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        BluetoothUtils.bluetoothGatt = bluetoothGatt;
    }

    public static BluetoothGattCallback getGattCallback() {
        return gattCallback;
    }

    public static void disconnect() {
        if (connected && bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            connected = false;
        }
    }

    public static boolean isConnected() {
        return connected;
    }
}
