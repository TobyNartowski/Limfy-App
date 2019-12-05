package pl.tobynartowski.limfy.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.utils.BluetoothUtils;

public class AppTodayFragment extends Fragment implements Observer {

    private boolean heartShow = false;

    private void loadData(int heartbeat, int steps) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00##");
        Activity activity = getActivity();
        View view = getView();
        if (activity != null && view != null) {
            activity.runOnUiThread(() -> {
                ((TextView) view.findViewById(R.id.app_today_heartbeat_value)).setText(getResources().getString(R.string.app_today_value_heartbeat, heartbeat));
                ((TextView) view.findViewById(R.id.app_today_steps_value)).setText(String.format(Locale.getDefault(), "%d", steps));
                ((TextView) view.findViewById(R.id.app_today_distance_value)).setText(getResources().getString(R.string.app_today_value_distance, decimalFormat.format(0.792 * steps)));
                ((TextView) view.findViewById(R.id.app_today_calories_value)).setText(getResources().getString(R.string.app_today_value_calories, decimalFormat.format(steps / 20.0)));
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BluetoothData.getInstance().addObserver(this);
        loadData(BluetoothData.getInstance().getHeartbeat(), BluetoothData.getInstance().getTotalSteps());

        Timer heartTimer = new Timer();
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Activity parentActivity = getActivity();
                if (parentActivity != null) {
                    parentActivity.runOnUiThread(() -> {
                        View image = view.findViewById(R.id.app_today_heartbeat_icon);
                        if (BluetoothUtils.isConnected()) {
                            if (heartShow) {
                                image.setVisibility(View.VISIBLE);
                            } else {
                                image.setVisibility(View.INVISIBLE);
                            }
                            heartShow = !heartShow;
                        } else {
                            image.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }, 600, 600);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BluetoothData) {
            BluetoothData bluetoothData = (BluetoothData) o;

            /* DEVELOPMENT
            if (bluetoothData.isDisconnected()) {
                BluetoothUtils.drawable_disconnect();
                runOnUiThread(() -> {
                    Intent connectionBrokenIntent = new Intent(AppActualActivity.this, ConnectActivity.class);
                    connectionBrokenIntent.putExtra("error", "connection");
                    startActivity(connectionBrokenIntent,
                            ActivityOptions.makeSceneTransitionAnimation(AppActualActivity.this).toBundle());
                });
            }
            */
            loadData(bluetoothData.getHeartbeat(), BluetoothData.getInstance().getTotalSteps());
        }
    }
}
