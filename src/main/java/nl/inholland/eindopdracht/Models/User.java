package nl.inholland.eindopdracht.Models;

public class User {
    private final String username;
    private final String passcode;

    public User(String username, String passcode) {
        this.username = username;
        this.passcode = passcode;
    }

    public String getUsername() {
        return username;
    }

    public String getPasscode() {
        return passcode;
    }
}
