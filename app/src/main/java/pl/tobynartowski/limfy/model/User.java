package pl.tobynartowski.limfy.model;

import com.xpbytes.gson.hal.HalLink;
import com.xpbytes.gson.hal.HalResource;

import java.io.Serializable;
import java.net.URI;

@HalResource
public class User implements Serializable {

    private static final long serialVersionUID = 5701670123455860532L;

    private String username;
    private String password;

    @HalLink
    private URI self;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, URI self) {
        this.username = username;
        this.password = password;
        this.self = self;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
