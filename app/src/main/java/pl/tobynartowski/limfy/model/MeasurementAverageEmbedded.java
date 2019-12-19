package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.util.List;

public class MeasurementAverageEmbedded implements Serializable {

    private static final long serialVersionUID = -4261310228081393584L;

    private List<MeasurementAverage> measurements;

    public MeasurementAverageEmbedded(List<MeasurementAverage> measurements) {
        this.measurements = measurements;
    }

    public List<MeasurementAverage> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementAverage> measurements) {
        this.measurements = measurements;
    }
}
