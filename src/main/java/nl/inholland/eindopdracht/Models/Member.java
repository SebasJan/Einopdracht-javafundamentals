package nl.inholland.eindopdracht.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Member {
    public int id;
    public String firstName;
    public String lastName;
    public Calendar dateOfBirth;
    public List<Item> lentItems;

    public Member(int id, String firstName, String lastName, Calendar dateOfBirth) {
        this.lentItems = new ArrayList<Item>();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public void addLentItem(Item item) {
        this.lentItems.add(item);
    }
}
