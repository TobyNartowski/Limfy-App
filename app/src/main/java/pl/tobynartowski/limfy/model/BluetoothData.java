package pl.tobynartowski.limfy.model;

import android.icu.util.Measure;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import pl.tobynartowski.limfy.utils.DataUtils;

public class BluetoothData extends Observable implements Serializable {

    private static final long serialVersionUID = -2734520847664508134L;

    public enum ChangeType {HEARTBEAT, STEPS, SHAKINESS, DISCONNECT, NONE};

    private int heartbeat;
    private int shakiness;
    private int steps;
    private boolean disconnected;
    private int totalSteps = 0;
    private ChangeType changeType = ChangeType.NONE;

    private static BluetoothData instance;

    public synchronized static BluetoothData getInstance() {
        if (instance == null) {
            instance = new BluetoothData();
        }
        return instance;
    }

    private BluetoothData() {
        if (DataUtils.getInstance().getMeasurements() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date today = calendar.getTime();

            MeasurementAverage initial = null;

            for (MeasurementAverage m : DataUtils.getInstance().getMeasurements()) {
                if (m.getTimestamp().after(today)) {
                    initial = m;
                }
            }

            if (initial != null) {
                heartbeat = (int) Math.floor(initial.getHeartbeatAverage());
                totalSteps = initial.getStepsSum();
            }
        }

        disconnected = false;
    }

    public synchronized int getHeartbeat() {
        return heartbeat;
    }

    public synchronized void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        changeType = ChangeType.HEARTBEAT;
        notifyObservers();
    }

    public synchronized int getShakiness() {
        return shakiness;
    }

    public synchronized void setShakiness(int shakiness) {
        this.shakiness = shakiness;
        changeType = ChangeType.SHAKINESS;
        notifyObservers();
    }

    public synchronized int getSteps() {
        return steps;
    }

    public synchronized void setSteps(int steps) {
        this.steps = steps;
        totalSteps += steps;
        changeType = ChangeType.STEPS;
        notifyObservers();
    }

    public synchronized boolean isDisconnected() {
        return disconnected;
    }

    public synchronized void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        changeType = ChangeType.DISCONNECT;
        notifyObservers();
    }

    public synchronized int getTotalSteps() {
        return totalSteps;
    }

    public synchronized ChangeType getChange() {
        return changeType;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
