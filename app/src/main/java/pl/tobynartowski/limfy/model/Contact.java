package pl.tobynartowski.limfy.model;

import java.io.Serializable;
import java.net.URI;

public class Contact implements Serializable {

    private static final long serialVersionUID = 7702749801879085594L;

    private String number;
    private URI user;

    public Contact(String number) {
        this.number = number;
    }

    public Contact(String number, URI user) {
        this.number = number;
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
