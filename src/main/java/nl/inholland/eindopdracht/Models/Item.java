package nl.inholland.eindopdracht.Models;

import java.io.Serializable;
import java.util.Calendar;

public class Item implements Serializable {
    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;
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
        if (dateOfLending == null)
            return false;
        // if the date of lending is more than 3 weeks ago, the method will return true
        Calendar threeWeeksAgo = Calendar.getInstance();
        threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
        boolean isOverdue = dateOfLending.before(threeWeeksAgo);
        // calculate the amount of days overdue
        if (isOverdue) {
            // get the difference between day of lending and the three-week point
            long difference = dateOfLending.getTimeInMillis() - threeWeeksAgo.getTimeInMillis();
            daysOverdue = difference / (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND);
            // invert - because the difference is negative
            daysOverdue = -daysOverdue;
        }
        this.dateOfLending = null;
        return isOverdue;
    }

    // word gebruikt door FXML!
    public String getExpectedReturnDate() {
        // check if the item is available, if that is the case return nothing
        // also check if the date of lending is null, if that is the case return nothing. This is so no errors will occur when a user tries to return an item that is not lent
        if (available || dateOfLending == null) {
            return "";
        }
        // set the expected return date the date of lending + 3 weeks
        Calendar expectedReturnDate = Calendar.getInstance();
        expectedReturnDate.setTime(dateOfLending.getTime());
        expectedReturnDate.add(Calendar.WEEK_OF_YEAR, 3);

        // return the expected return date as string
        return expectedReturnDate.get(Calendar.DAY_OF_MONTH) + "-" + (expectedReturnDate.get(Calendar.MONTH) + 1) + "-" + expectedReturnDate.get(Calendar.YEAR);
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

    public Calendar getDateOfLending() {
        return dateOfLending;
    }
}
