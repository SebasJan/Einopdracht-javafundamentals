package nl.inholland.eindopdracht.Models;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database {
    public List<User> users;
    public List<Item> items;
    public List<Member> members;

    public Database() {
        this.users = new ArrayList<>();
        loadUsers();

        this.items = new ArrayList<>();
        loadItems();

        this.members = new ArrayList<>();
        loadMembers();
    }

    private void loadMembers() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2002, Calendar.SEPTEMBER, 6);
        members.add(new Member(1, "Sebastiaan", "van Vliet", calendar));

        calendar.set(2002, Calendar.JUNE, 8);
        members.add(new Member(2, "Luc", "Moetwil", calendar));

        calendar.set(2002, Calendar.AUGUST, 15);
        members.add(new Member(3, "Lars", "Hartendorp", calendar));
    }

    private void loadUsers() {
        // add 2 users
        this.users.add(new User("admin", "1234"));
        this.users.add(new User("sebas", "1234"));
    }

    private void loadItems() {
        // add 2 items
        this.items.add(new Item(1, true, "Harry Potter", "J.K. Rowling"));
        this.items.add(new Item(2, true, "Lord of the Rings", "J.R.R. Tolkien"));
    }

    public String lendItem(int itemCode, int memberId) {
        // check if the item exists
        for (Item item : items) {
            if (item.itemCode == itemCode && item.available) {
                // check if the member exists, only then lend the item
                Member member = getMemberById(memberId);
                if (member != null) {
                    member.addLentItem(item);
                    item.available = false;
                    item.dateOfLending = Calendar.getInstance();
                    return null;
                }
                return "noMember";
            }
        }
        return "noItem";
    }

    public Item receiveItem(int itemCode) {
        for (Item item : items) {
            if (item.itemCode == itemCode && !item.available) {
                item.available = true;
                return item;
            }
        }
        return null;
    }

    public void editItem(Item item) {
        for (Item i : items) {
            if (i.itemCode == item.itemCode) {
                i.title = item.title;
                i.author = item.author;
                i.available = item.available;
            }
        }
    }

    private Member getMemberById(int memberId) {
        for (Member member : members) {
            if (member.id == memberId) {
                return member;
            }
        }
        return null;
    }
}
