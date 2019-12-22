package pl.tobynartowski.limfy.utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import pl.tobynartowski.limfy.model.Analysis;
import pl.tobynartowski.limfy.model.BodyData;
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
    private BodyData bodyData;
    private List<Analysis> analyses;

    public void setMeasurements(List<MeasurementAverage> measurements) {
        this.measurements = measurements;
        if (measurements != null) {
            Collections.reverse(this.measurements);
        }
    }

    public List<MeasurementAverage> getMeasurements() {
        return measurements;
    }

    public MeasurementAverage getTodayMeasurement() {
        if (measurements == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();

        return measurements
                .stream()
                .filter(m -> m.getTimestamp().after(today))
                .findAny()
                .orElse(null);
    }

    public List<MeasurementAverage> getThisWeekMeasurements() {
        if (measurements == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date monday = calendar.getTime();
        Date nextMonday = new Date(monday.getTime() + 7 * 24 * 60 * 60 * 1000);

        return measurements
                .stream()
                .filter(m -> m.getTimestamp().after(monday) && m.getTimestamp().before(nextMonday))
                .collect(Collectors.toList());
    }

    public BodyData getBodyData() {
        return bodyData;
    }

    public void setBodyData(BodyData bodyData) {
        this.bodyData = bodyData;
    }

    public List<Analysis> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<Analysis> analyses) {
        this.analyses = analyses;
    }
}
