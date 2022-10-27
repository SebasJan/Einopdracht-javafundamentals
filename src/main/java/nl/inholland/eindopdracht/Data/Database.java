package nl.inholland.eindopdracht.Data;

import nl.inholland.eindopdracht.Models.Item;
import nl.inholland.eindopdracht.Models.Member;
import nl.inholland.eindopdracht.Models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database {
    private static final String ITEMS_FILE = "items.dat";
    private static final String MEMBERS_FILE = "members.dat";
    private final List<User> USERS;
    private final List<Item> ITEMS;
    private final List<Member> MEMBERS;

    public Database() {
        this.USERS = new ArrayList<>();
        loadUsers();

        this.ITEMS = new ArrayList<>();
        loadItems();

        this.MEMBERS = new ArrayList<>();
        loadMembers();
    }

    private void loadMembers() {
        try (FileInputStream fis = new FileInputStream(MEMBERS_FILE);) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            // read items
            readMembers(ois ,fis);
            ois.close();
        } catch (IOException e) {
            // if the file doesn't exist load the default members
            Calendar calendar = Calendar.getInstance();
            calendar.set(2002, Calendar.SEPTEMBER, 6);
            getMEMBERS().add(new Member(1, "Sebastiaan", "van Vliet", calendar));

            calendar.set(2002, Calendar.JUNE, 2);
            getMEMBERS().add(new Member(2, "Luc", "Moetwil", calendar));

            calendar.set(2002, Calendar.AUGUST, 15);
            getMEMBERS().add(new Member(3, "Lars", "Hartendorp", calendar));
        }
    }

    private void readMembers(ObjectInputStream ois, FileInputStream fis) throws IOException {
        while (true) {
            try {
                // check if the ois can read an object
                if (fis.available() == 0)
                    break;
                Member member = (Member) ois.readObject();
                getMEMBERS().add(member);
            } catch (EOFException | ClassNotFoundException e) {
                throw new IOException(e);
            }
        }
    }

    private void loadUsers() {
        // add 2 users
        this.getUSERS().add(new User("eros", "0512", "Eros Adamos"));
        this.getUSERS().add(new User("hestia", "0609", "Hestia Argyros"));
    }

    private void loadItems() {
        try (FileInputStream fis = new FileInputStream(ITEMS_FILE);) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            // read items
            readItems(ois, fis);
            ois.close();
        } catch (IOException e) {
            // if the file doesn't exist load the default items
            this.getITEMS().add(new Item(1, true, "De vrouwenslagerij", "Ilja Gort"));
            this.getITEMS().add(new Item(2, true, "Godendrank", "Ilja Gort"));
            this.getITEMS().add(new Item(3, true, "Een tweede leven met Formule 1", "Olav Mol"));
        }
    }

    private void readItems(ObjectInputStream ois, FileInputStream fis) throws IOException {
        while (true) {
            try {
                // check if the ois can read an object
                if (fis.available() == 0)
                    break;
                Item item = (Item) ois.readObject();
                getITEMS().add(item);
            } catch (EOFException | ClassNotFoundException e) {
                throw new IOException(e);
            }
        }
    }

    // this method return a string so the controller can see what went wrong if something went wrong
    public String lendItem(int itemCode, int memberId) {
        // check if the item exists
        for (Item item : getITEMS()) {
            if (item.getItemCode() == itemCode && item.getAvailable()) {
                // check if the member exists, only then lend the item
                Member member = getMemberById(memberId);
                if (member != null) {
                    item.setAvailable(false);
                    item.setDateOfLending(Calendar.getInstance());
                    return null;
                }
                return "noMember";
            }
        }
        return "noItem";
    }

    public Item receiveItem(int itemCode) {
        for (Item item : getITEMS()) {
            if (item.getItemCode() == itemCode && !item.getAvailable()) {
                item.setAvailable(true);
                return item;
            }
        }
        return null;
    }

    public void editItem(Item item) {
        for (Item i : getITEMS()) {
            if (i.getItemCode() == item.getItemCode()) {
                i.setTitle(item.getTitle());
                i.setAuthor(item.getAuthor());
            }
        }
    }

    public void deleteItem(int itemCode) {
        for (Item item : getITEMS()) {
            if (item.getItemCode() == itemCode) {
                getITEMS().remove(item);
                break;
            }
        }
    }

    public void addItem(String title, String author) {
        int itemCode = getITEMS().size() + 1;
        getITEMS().add(new Item(itemCode, true, title, author));
    }

    public void addMember(String firstName, String lastName, Calendar dateOfBirth) {
        int memberId = getMEMBERS().size() + 1;
        getMEMBERS().add(new Member(memberId, firstName, lastName, dateOfBirth));
    }

    public void deleteMember(int memberId) {
        for (Member member : getMEMBERS()) {
            if (member.getId() == memberId) {
                getMEMBERS().remove(member);
                break;
            }
        }
    }

    public void editMember(Member member) {
        for (Member m : getMEMBERS()) {
            if (m.getId() == member.getId()) {
                m.setFirstName(member.getFirstName());
                m.setLastName(member.getLastName());
                m.setDateOfBirth(member.getDateOfBirth());
            }
        }
    }

    private Member getMemberById(int memberId) {
        for (Member member : getMEMBERS()) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }

    public void saveDateBase() {
        saveItems();
        saveMembers();
    }

    private void saveMembers() {
        try (FileOutputStream outputStream = new FileOutputStream(MEMBERS_FILE, false)) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            for (Member member : getMEMBERS()) {
                objectOutputStream.writeObject(member);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveItems() {
        try (FileOutputStream outputStream = new FileOutputStream(ITEMS_FILE, false)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            for (Item item : getITEMS()) {
                objectOutputStream.writeObject(item);
            }
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean itemExists(int itemCode) {
        for (Item item : getITEMS()) {
            if (item.getItemCode() == itemCode) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUSERS() {
        return USERS;
    }

    public List<Item> getITEMS() {
        return ITEMS;
    }

    public List<Member> getMEMBERS() {
        return MEMBERS;
    }
}
