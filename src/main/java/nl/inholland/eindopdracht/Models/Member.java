package nl.inholland.eindopdracht.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Member implements Serializable {
    private final int id;
    private String firstName;
    private String lastName;
    private Calendar dateOfBirth;
    private String birthDateString;
    private List<Item> lentItems;

    public Member(int id, String firstName, String lastName, Calendar dateOfBirth) {
        this.lentItems = new ArrayList<>();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public void addLentItem(Item item) {
        this.lentItems.add(item);
    }

    public String getBirthDateString() {
        return this.dateOfBirth.get(Calendar.DAY_OF_MONTH) + "-" + (this.dateOfBirth.get(Calendar.MONTH) + 1) + "-" + this.dateOfBirth.get(Calendar.YEAR);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
