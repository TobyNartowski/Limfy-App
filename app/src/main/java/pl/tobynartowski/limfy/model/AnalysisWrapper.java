package pl.tobynartowski.limfy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnalysisWrapper implements Serializable {

    private static final long serialVersionUID = 7050277535049093076L;

    @SerializedName("_embedded")
    private AnalysisEmbedded embedded;

    public AnalysisWrapper(AnalysisEmbedded embedded) {
        this.embedded = embedded;
    }

    public void setEmbedded(AnalysisEmbedded embedded) {
        this.embedded = embedded;
    }

    public AnalysisEmbedded getEmbedded() {
        return embedded;
    }
}
