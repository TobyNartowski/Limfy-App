package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.net.URI;

public class BodyData implements Serializable {

    private static final long serialVersionUID = 6778794138531954221L;

    private Gender gender;
    private int weight;
    private int height;
    private int age;
    private URI user;

    public BodyData(Gender gender, int weight, int height, int age, URI user) {
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.user = user;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
