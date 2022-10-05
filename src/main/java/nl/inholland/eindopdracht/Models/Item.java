package nl.inholland.eindopdracht.Models;

import java.io.Serializable;
import java.util.Calendar;

public class Item implements Serializable {
    private final int itemCode;
    private boolean available;
    private String title;
    private String author;
    private Calendar dateOfLending;
    private long daysOverdue;

    public Item(int itemCode, boolean available, String title, String author) {
        this.itemCode = itemCode;
        this.available = available;
        this.title = title;
        this.author = author;
    }

    public boolean itemIsOverdue() {
        // if the date of lending is more than 3 weeks ago, the method will return true
        Calendar threeWeeksAgo = Calendar.getInstance();
        threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
        boolean isOverdue = dateOfLending.before(threeWeeksAgo);
        // calculate the amount of days overdue
        if (isOverdue) {
            // get the difference between day of lending and the three-week point
            long difference = dateOfLending.getTimeInMillis() - threeWeeksAgo.getTimeInMillis();
            daysOverdue = difference / (24 * 60 * 60 * 1000);
            // invert - because the difference is negative
            daysOverdue = -daysOverdue;
        }
        this.dateOfLending = null;
        return isOverdue;
    }

    public long getDaysOverdue() {
        return daysOverdue;
    }

    public void setDateOfLending(Calendar dateOfLending) {
        this.dateOfLending = dateOfLending;
    }

    public int getItemCode() {
        return itemCode;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
