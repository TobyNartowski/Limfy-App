package pl.tobynartowski.limfy.utils;

import java.util.List;

import pl.tobynartowski.limfy.model.MeasurementAverage;

public class DataUtils {

    private static DataUtils instance;

    public static DataUtils getInstance() {
        if (instance == null) {
            instance = new DataUtils();
        }
        return instance;
    }

    private List<MeasurementAverage> measurements;

    public void setMeasurements(List<MeasurementAverage> measurements) {
        this.measurements = measurements;
    }

    public List<MeasurementAverage> getMeasurements() {
        return measurements;
    }
}
