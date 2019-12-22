package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnalysisEmbedded implements Serializable {

    private static final long serialVersionUID = -592646209428094122L;

    private List<Analysis> analyses = new ArrayList<>();

    public AnalysisEmbedded(List<Analysis> analyses) {
        this.analyses = analyses;
    }

    public List<Analysis> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<Analysis> analyses) {
        this.analyses = analyses;
    }
}
