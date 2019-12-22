package pl.tobynartowski.limfy.model;

import com.xpbytes.gson.hal.HalLink;
import com.xpbytes.gson.hal.HalResource;

import java.io.Serializable;
import java.net.URI;

@HalResource
public class Analysis implements Serializable {

    private static final long serialVersionUID = 5843048634792285168L;

    @HalLink(name = "disease")
    private URI diseaseURI;
    private double percentage;
    private Disease disease;

    public Analysis(double percentage, URI diseaseURI) {
        this.percentage = percentage;
        this.diseaseURI = diseaseURI;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public URI getDiseaseURI() {
        return diseaseURI;
    }

    public void setDiseaseURI(URI diseaseURI) {
        this.diseaseURI = diseaseURI;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }
}
