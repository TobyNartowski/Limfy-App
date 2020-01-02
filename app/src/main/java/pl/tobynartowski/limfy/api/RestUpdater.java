package pl.tobynartowski.limfy.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.model.Measurement;
import pl.tobynartowski.limfy.utils.UserUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestUpdater extends Service implements Observer {

    private Set<Integer> steps = new HashSet<>();
    private Set<Integer> shakiness = new HashSet<>();

    public RestUpdater() {
        BluetoothData.getInstance().addObserver(this);
    }

    private int calculateSum(Set<Integer> collection) {
        if (collection.size() == 0) {
            return 0;
        }

        int sum = 0;
        for (int val : collection) {
            sum += val;
        }
        return sum;
    }

    private int calculateAverage(Set<Integer> collection) {
        if (collection.size() == 0) {
            return 0;
        }
        return calculateSum(collection) / collection.size();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BluetoothData) {
            BluetoothData dataObject = (BluetoothData) o;
            if (!dataObject.isDisconnected()) {
                switch (dataObject.getChange()) {
                    case HEARTBEAT:
                        if (dataObject.getHeartbeat() < 40) {
                            steps = new HashSet<>();
                            shakiness = new HashSet<>();
                            break;
                        }

                        Call<Void> measurementCall = RetrofitClient.getInstance().getApi().postMeasurements(new Measurement(
                                dataObject.getHeartbeat(),
                                calculateSum(steps),
                                calculateAverage(shakiness),
                                RetrofitClient.API_URL + "api/v1/users/" + UserUtils.getInstance(Limfy.getContext()).getId()
                        ));
                        measurementCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.i(getClass().getName(), "Measurements has been sent: " + response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e(getClass().getName(), "Cannot send measurements", t);
                            }
                        });

                        steps = new HashSet<>();
                        shakiness = new HashSet<>();
                        break;
                    case STEPS:
                        steps.add(dataObject.getSteps());
                        break;
                    case SHAKINESS:
                        shakiness.add(dataObject.getShakiness());
                        break;
                }
            } else {
                stopService(new Intent(RestUpdater.this, RestUpdater.class));
            }
        }
    }

    @Override
    public void onDestroy() {
        BluetoothData.getInstance().deleteObserver(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
