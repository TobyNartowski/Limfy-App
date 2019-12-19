package pl.tobynartowski.limfy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeasurementAverageWrapper implements Serializable {

    private static final long serialVersionUID = -1315830502254187713L;

    @SerializedName("_embedded")
    private MeasurementAverageEmbedded embedded;

    public MeasurementAverageWrapper(MeasurementAverageEmbedded embedded) {
        this.embedded = embedded;
    }

    public MeasurementAverageEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(MeasurementAverageEmbedded embedded) {
        this.embedded = embedded;
    }
}
