package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.MeasurementAverage;
import pl.tobynartowski.limfy.utils.DataUtils;

public class AppDetailsFragment extends Fragment {

    private boolean chartShown = false;

    public enum AppDetailsWhich {APP_DETAILS_HEARTBEAT, APP_DETAILS_ACTIVITY}

    private LineChart chart;
    private AppDetailsWhich which;

    public AppDetailsFragment(AppDetailsWhich which) {
        this.which = which;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        if (!chartShown) {
            chart.setVisibility(View.VISIBLE);
            chart.animateY(1000, Easing.EaseOutCubic);
            chartShown = true;
        }

        chart.invalidate();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.app_details_name);

        TextView firstValue = view.findViewById(R.id.app_details_value1);
        TextView firstValueDescription = view.findViewById(R.id.app_details_value1_description);

        TextView secondValue = view.findViewById(R.id.app_details_value2);
        TextView secondValueDescription = view.findViewById(R.id.app_details_value2_description);
        ImageView secondValueIcon = view.findViewById(R.id.app_details_value2_icon);

        TextView thirdValue = view.findViewById(R.id.app_details_value3);
        TextView thirdValueDescription = view.findViewById(R.id.app_details_value3_description);
        ImageView thirdValueIcon = view.findViewById(R.id.app_details_value3_icon);

        if (getContext() != null) {
            if (which == AppDetailsWhich.APP_DETAILS_HEARTBEAT) {
                Double average = Optional.ofNullable(DataUtils.getInstance().getMeasurements())
                        .orElse(Collections.emptyList())
                        .stream()
                        .mapToDouble(MeasurementAverage::getHeartbeatAverage)
                        .average()
                        .orElse(0.0);
                MeasurementAverage todayMeasurements = DataUtils.getInstance().getTodayMeasurement();
                List<MeasurementAverage> thisWeekMeasurements = DataUtils.getInstance().getThisWeekMeasurements();

                name.setText(getResources().getString(R.string.app_details_heartbeat_name));

                firstValue.setText(String.format(Locale.getDefault(), "%.2f bpm", average));
                firstValueDescription.setText(getResources().getString(R.string.app_details_heartbeat_average));

                if (todayMeasurements == null) {
                    secondValue.setText("0%");
                    secondValueDescription.setText(getResources().getString(R.string.app_details_no_data_today));
                    secondValueIcon.setVisibility(View.INVISIBLE);
                } else {
                    Double percentage = -((average - todayMeasurements.getHeartbeatAverage()) * 100 / average);
                    secondValue.setText(String.format(Locale.getDefault(), "%.2f%%", percentage));
                    if (percentage > 0) {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_higher_today));
                        secondValueIcon.setImageResource(R.drawable.drawable_angle_arrow_up);
                        secondValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRed));
                    } else if (percentage < 0) {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_lower_today));
                        secondValueIcon.setImageResource(R.drawable.drawable_angle_arrow_down);
                        secondValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    } else {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_no_changes));
                        secondValueIcon.setVisibility(View.INVISIBLE);
                    }
                }

                if (thisWeekMeasurements == null) {
                    thirdValue.setText("0%");
                    thirdValueDescription.setText(getResources().getString(R.string.app_details_no_data_week));
                    thirdValueIcon.setVisibility(View.INVISIBLE);
                } else {
                    Double thisWeekAverage = thisWeekMeasurements
                            .stream()
                            .mapToDouble(MeasurementAverage::getHeartbeatAverage)
                            .average()
                            .orElse(0.0);

                    Double percentage = -((average - thisWeekAverage) * 100 / average);
                    thirdValue.setText(String.format(Locale.getDefault(), "%.2f%%", percentage));

                    if (percentage > 0) {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_higher_week));
                        thirdValueIcon.setImageResource(R.drawable.drawable_angle_arrow_up);
                        thirdValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRed));
                    } else if (percentage < 0) {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_lower_week));
                        thirdValueIcon.setImageResource(R.drawable.drawable_angle_arrow_down);
                        thirdValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    } else {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_no_changes));
                        thirdValueIcon.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                Double average = Optional.ofNullable(DataUtils.getInstance().getMeasurements())
                        .orElse(Collections.emptyList())
                        .stream()
                        .mapToDouble(MeasurementAverage::getStepsSum)
                        .average()
                        .orElse(0.0);
                MeasurementAverage todayMeasurements = DataUtils.getInstance().getTodayMeasurement();
                List<MeasurementAverage> thisWeekMeasurements = DataUtils.getInstance().getThisWeekMeasurements();

                name.setText(getResources().getString(R.string.app_details_activity_name));

                firstValue.setText(String.format(Locale.getDefault(), "%d", Math.round(average)));
                firstValueDescription.setText(getResources().getString(R.string.app_details_steps_average));

                if (todayMeasurements == null) {
                    secondValue.setText("0%");
                    secondValueDescription.setText(getResources().getString(R.string.app_details_no_data_today));
                    secondValueIcon.setVisibility(View.INVISIBLE);
                } else {
                    double percentage = -((average - todayMeasurements.getStepsSum()) * 100 / average);
                    secondValue.setText(String.format(Locale.getDefault(), "%d%%", Math.round(percentage)));

                    if (percentage > 0) {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_higher_today));
                        secondValueIcon.setImageResource(R.drawable.drawable_angle_arrow_up);
                        secondValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    } else if (percentage < 0) {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_lower_today));
                        secondValueIcon.setImageResource(R.drawable.drawable_angle_arrow_down);
                        secondValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRed));
                    } else {
                        secondValueDescription.setText(getResources().getString(R.string.app_details_no_changes));
                        secondValueIcon.setVisibility(View.INVISIBLE);
                    }
                }

                if (thisWeekMeasurements == null) {
                    thirdValue.setText("0%");
                    thirdValueDescription.setText(getResources().getString(R.string.app_details_no_data_week));
                    thirdValueIcon.setVisibility(View.INVISIBLE);
                } else {
                    Double thisWeekAverage = thisWeekMeasurements
                            .stream()
                            .mapToDouble(MeasurementAverage::getStepsSum)
                            .average()
                            .orElse(0.0);

                    double percentage = -((average - thisWeekAverage) * 100 / average);
                    thirdValue.setText(String.format(Locale.getDefault(), "%d%%", Math.round(percentage)));

                    if (percentage > 0) {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_higher_week));
                        thirdValueIcon.setImageResource(R.drawable.drawable_angle_arrow_up);
                        thirdValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    } else if (percentage < 0) {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_lower_week));
                        thirdValueIcon.setImageResource(R.drawable.drawable_angle_arrow_down);
                        thirdValueIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRed));
                    } else {
                        thirdValueDescription.setText(getResources().getString(R.string.app_details_no_changes));
                        thirdValueIcon.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        chart = view.findViewById(R.id.line_chart);
        chart.setVisibility(View.INVISIBLE);
        chart.setNoDataText("");
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);
        addChartData();

        super.onViewCreated(view, savedInstanceState);
    }

    private void addChartData() {
        List<Entry> values = new ArrayList<>();

        if (DataUtils.getInstance().getMeasurements() != null) {
            AtomicInteger counter = new AtomicInteger(0);
            if (which == AppDetailsWhich.APP_DETAILS_HEARTBEAT) {
                values = DataUtils.getInstance().getMeasurements()
                        .stream()
                        .map(m -> new Entry(counter.incrementAndGet(), (float) m.getHeartbeatAverage()))
                        .collect(Collectors.toList());
            } else {
                values = DataUtils.getInstance().getMeasurements()
                        .stream()
                        .map(m -> new Entry(counter.incrementAndGet(), (float) m.getStepsSum()))
                        .collect(Collectors.toList());
            }
        }

        LineDataSet lineDataSet;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, "DataSet");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(1.8f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setColor(ContextCompat.getColor(Limfy.getContext(), R.color.colorAccent));
            lineDataSet.setFillColor(ContextCompat.getColor(Limfy.getContext(), R.color.colorAccent));
            lineDataSet.setFillAlpha(100);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

            LineData data = new LineData(lineDataSet);
            data.setDrawValues(false);

            chart.setData(data);
        }
    }
}
