package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.util.Observable;

import pl.tobynartowski.limfy.utils.DataUtils;

public class BluetoothData extends Observable implements Serializable {

    private static final long serialVersionUID = -2734520847664508134L;

    public enum ChangeType {HEARTBEAT, STEPS, SHAKINESS, DISCONNECT, FALL, NONE}

    private int heartbeat;
    private double totalHeartbeat;
    private int shakiness;
    private int steps;
    private int totalSteps = 0;
    private boolean fall = false;
    private boolean disconnected;
    private ChangeType changeType = ChangeType.NONE;

    private static BluetoothData instance;

    public synchronized static BluetoothData getInstance() {
        if (instance == null) {
            instance = new BluetoothData();
        }
        return instance;
    }

    private BluetoothData() {
        MeasurementAverage todayMeasurements = DataUtils.getInstance().getTodayMeasurement();
        if (todayMeasurements != null) {
            heartbeat = (int) Math.floor(todayMeasurements.getHeartbeatAverage());
            totalSteps = todayMeasurements.getStepsSum();
        }

        disconnected = false;
    }

    public synchronized double getTotalHeartbeat() {
        return totalHeartbeat;
    }

    public synchronized void setTotalHeartbeat(double totalHeartbeat) {
        this.totalHeartbeat = totalHeartbeat;
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

    public synchronized boolean isFall() {
        return fall;
    }

    public synchronized void setFall(boolean fall) {
        this.fall = fall;
        changeType = ChangeType.FALL;
        notifyObservers();
    }

    public synchronized int getTotalSteps() {
        return totalSteps;
    }

    public synchronized boolean isDisconnected() {
        return disconnected;
    }

    public synchronized void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        changeType = ChangeType.DISCONNECT;
        notifyObservers();
    }

    public synchronized ChangeType getChange() {
        return changeType;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    public synchronized void clearData() {
        heartbeat = 0;
        shakiness = 0;
        steps = 0;
        totalSteps = 0;
        totalHeartbeat = 0;
    }
}
