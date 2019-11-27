package pl.tobynartowski.limfy.model;

import java.util.Observable;

public class BluetoothData extends Observable {

    public enum ChangeType {HEARTBEAT, STEPS, SHAKINESS, DISCONNECT, NONE};

    private int heartbeat;
    private int shakiness;
    private int steps;
    private boolean disconnected;
    private int totalSteps = 0;
    private ChangeType change = ChangeType.NONE;

    private static BluetoothData instance;

    public static BluetoothData getInstance() {
        if (instance == null) {
            instance = new BluetoothData();
        }
        return instance;
    }

    private BluetoothData() {
        disconnected = false;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        change = ChangeType.HEARTBEAT;
        notifyObservers();
    }

    public int getShakiness() {
        return shakiness;
    }

    public void setShakiness(int shakiness) {
        this.shakiness = shakiness;
        change = ChangeType.SHAKINESS;
        notifyObservers();
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
        change = ChangeType.STEPS;
        totalSteps += steps;
        notifyObservers();
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        change = ChangeType.DISCONNECT;
        notifyObservers();
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public ChangeType getChange() {
        return change;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
