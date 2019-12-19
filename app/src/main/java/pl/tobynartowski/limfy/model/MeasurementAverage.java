package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.util.Date;

public class MeasurementAverage implements Serializable {

    private static final long serialVersionUID = -8380052950086930746L;

    private double heartbeatAverage;
    private int stepsSum;
    private Date timestamp;

    public MeasurementAverage(double heartbeatAverage, int stepsSum, Date timestamp) {
        this.heartbeatAverage = heartbeatAverage;
        this.stepsSum = stepsSum;
        this.timestamp = timestamp;
    }

    public double getHeartbeatAverage() {
        return heartbeatAverage;
    }

    public void setHeartbeatAverage(double heartbeatAverage) {
        this.heartbeatAverage = heartbeatAverage;
    }

    public int getStepsSum() {
        return stepsSum;
    }

    public void setStepsSum(int stepsSum) {
        this.stepsSum = stepsSum;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
