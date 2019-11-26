package pl.tobynartowski.limfy.model;

import java.util.Observable;

public class BluetoothData extends Observable {

    private int heartbeat;
    private int shakiness;
    private int steps;
    private boolean newSteps;
    private boolean disconnected;

    private static BluetoothData instance;

    public static BluetoothData getInstance() {
        if (instance == null) {
            instance = new BluetoothData();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private BluetoothData() {
        newSteps = false;
        disconnected = false;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        notifyObservers();
    }

    public int getShakiness() {
        return shakiness;
    }

    public void setShakiness(int shakiness) {
        this.shakiness = shakiness;
        notifyObservers();
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
        setNewSteps(true);
        notifyObservers();
    }

    public boolean isNewSteps() {
        return newSteps;
    }

    public void setNewSteps(boolean newSteps) {
        this.newSteps = newSteps;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
