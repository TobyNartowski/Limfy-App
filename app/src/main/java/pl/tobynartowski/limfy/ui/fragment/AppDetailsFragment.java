package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
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

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;

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
        TextView overall = view.findViewById(R.id.app_details_overall);

        TextView firstValue = view.findViewById(R.id.app_details_value1);
        TextView firstValueDescription = view.findViewById(R.id.app_details_value1_description);

        TextView secondValue = view.findViewById(R.id.app_details_value2);
        TextView secondValueDescription = view.findViewById(R.id.app_details_value2_description);
        ImageView secondValueIcon = view.findViewById(R.id.app_details_value2_icon);

        TextView thirdValuie = view.findViewById(R.id.app_details_value3);
        TextView thirdValueDescription = view.findViewById(R.id.app_details_value3_description);
        ImageView thirdValueIcon = view.findViewById(R.id.app_details_value3_icon);

        if (which == AppDetailsWhich.APP_DETAILS_HEARTBEAT) {
            name.setText(getResources().getString(R.string.app_details_heartbeat_name));
        } else {
            name.setText(getResources().getString(R.string.app_details_activity_name));
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
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random()) + 20;
            values.add(new Entry(i, val));
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
