package pl.tobynartowski.limfy.model;

import java.util.Observable;

public class DeviceData extends Observable {

    private int heartbeat;
    private int shakiness;
    private int steps;

    private static DeviceData instance;

    public static DeviceData getInstance() {
        if (instance == null) {
            instance = new DeviceData();
        }
        return instance;
    }

    private DeviceData() {}

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
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
