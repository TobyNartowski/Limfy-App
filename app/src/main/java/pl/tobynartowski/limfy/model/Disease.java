package pl.tobynartowski.limfy.model;

import java.io.Serializable;

public class Disease implements Serializable {

    private static final long serialVersionUID = -4405233302245011286L;

    private String name;
    private int healthInfluence;

    public Disease(String name, int healthInfluence) {
        this.name = name;
        this.healthInfluence = healthInfluence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthInfluence() {
        return healthInfluence;
    }

    public void setHealthInfluence(int healthInfluence) {
        this.healthInfluence = healthInfluence;
    }
}
