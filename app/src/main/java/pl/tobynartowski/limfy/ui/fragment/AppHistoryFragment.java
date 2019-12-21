package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.MeasurementAverage;
import pl.tobynartowski.limfy.utils.DataUtils;

public class AppHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        if (DataUtils.getInstance().getMeasurements() != null) {
            List<MeasurementAverage> measurementHistory = DataUtils.getInstance().getMeasurements();
            Collections.reverse(measurementHistory);
            for (MeasurementAverage measurement : measurementHistory) {
                LinearLayout entry = (LinearLayout) View.inflate(getContext(), R.layout.layout_history_entry, null);
                ((TextView) entry.findViewById(R.id.app_history_entry_heartbeat)).setText(String.format(Locale.getDefault(), "%.2f", measurement.getHeartbeatAverage()));
                ((TextView) entry.findViewById(R.id.app_history_entry_activity)).setText(String.format(Locale.getDefault(), "%d", measurement.getStepsSum()));
                ((TextView) entry.findViewById(R.id.app_history_entry_date)).setText(formatter.format(measurement.getTimestamp()));
                ((LinearLayout) view.findViewById(R.id.app_history_entry_container)).addView(entry);
            }
        } else {
            view.findViewById(R.id.app_history_no_data).setVisibility(View.VISIBLE);
            view.findViewById(R.id.app_history_measurements).setVisibility(View.GONE);
        }

        super.onViewCreated(view, savedInstanceState);
    }
}
