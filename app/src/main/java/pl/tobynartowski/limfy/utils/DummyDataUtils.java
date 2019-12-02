package pl.tobynartowski.limfy.utils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.tobynartowski.limfy.model.BluetoothData;

public class DummyDataUtils {

    private static DummyDataUtils instance;

    private final static long INTERVAL_HEARTBEAT = 8000;
    private final static long INTERVAL_ACCELEROMETER = 2000;

    private Timer heartbeatTimer;
    private Timer accelerometerTimer;
    private Random random;

    private DummyDataUtils() {
        random = new Random(47);
    }

    public void initTimers() {
        heartbeatTimer = new Timer();
        accelerometerTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new DummyHeartbeatDevice(), 2000 + INTERVAL_HEARTBEAT, INTERVAL_HEARTBEAT);
        accelerometerTimer.scheduleAtFixedRate(new DummyAccelerometerDevice(), 2000, INTERVAL_ACCELEROMETER);
    }

    public void stopTimers() {
        heartbeatTimer.cancel();
        accelerometerTimer.cancel();
    }

    public static DummyDataUtils getInstance() {
        if (instance == null) {
            instance = new DummyDataUtils();
        }
        return instance;
    }

    private class DummyHeartbeatDevice extends TimerTask {

        @Override
        public void run() {
            simulateHeartbeat();
        }

        private void simulateHeartbeat() {
            BluetoothData.getInstance().setHeartbeat((int) Math.round(random.nextGaussian() * 25 + 70));
        }
    }

    private class DummyAccelerometerDevice extends TimerTask {

        @Override
        public void run() {
            simulateSteps();
            simulateShakiness();
        }


        private void simulateShakiness() {
            BluetoothData.getInstance().setShakiness((int) Math.round(random.nextGaussian() * 40 + 50));
        }

        private void simulateSteps() {
            BluetoothData.getInstance().setSteps(random.nextBoolean() ? 0 : random.nextInt(3));
        }
    }
}
