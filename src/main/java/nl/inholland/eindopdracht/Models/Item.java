package nl.inholland.eindopdracht.Models;

import java.util.Calendar;

public class Item {
    public int itemCode;
    public boolean available;
    public String title;
    public String author;
    public Calendar dateOfLending;
    public long daysOverdue;

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
            Calendar today = Calendar.getInstance();
            // get the difference between day of lending and the three-week point
            long difference = dateOfLending.getTimeInMillis() - threeWeeksAgo.getTimeInMillis();
            daysOverdue = difference / (24 * 60 * 60 * 1000);
            // invert - because the difference is negative
            daysOverdue = -daysOverdue;
        }
        this.dateOfLending = null;
        return isOverdue;
    }
}
