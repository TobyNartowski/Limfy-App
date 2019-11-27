package pl.tobynartowski.limfy.model;

public class Measurement {

    private int heartbeat;
    private int steps;
    private int shakiness;
    private String user;

    public Measurement(int heartbeat, int steps, int shakiness, String user) {
        this.heartbeat = heartbeat;
        this.steps = steps;
        this.shakiness = shakiness;
        this.user = user;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getShakiness() {
        return shakiness;
    }

    public void setShakiness(int shakiness) {
        this.shakiness = shakiness;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
