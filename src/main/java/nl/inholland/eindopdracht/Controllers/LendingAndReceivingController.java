package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.inholland.eindopdracht.Controllers.Events.MouseEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.Item;
import nl.inholland.eindopdracht.Models.User;

public class LendingAndReceivingController extends MouseEvent {
    @FXML
    public Label welcomeText;
    @FXML
    public TextField itemCodeLendField;
    @FXML
    public TextField memberIdLendField;
    @FXML
    public TextField itemCodeReceiveField;
    @FXML
    public Label feedbackText;
    @FXML
    public Label errorItemCodeLendLabel;
    @FXML
    public Label errorMemberIDLabel;
    @FXML
    public Label errorItemCodeReceive;
    @FXML
    public Label errorLabel;

    private final Database DATABASE;
    private final User USER;

    public LendingAndReceivingController(Database database, User user) {
        this.DATABASE = database;
        this.USER = user;
    }

    @FXML
    public void initialize() {
        welcomeText.setText("Welcome " + USER.fullName());
    }

    @FXML
    public void lendItemButtonClick() {
        // check if the fields are empty
        if (itemCodeLendField.getText().isEmpty() || memberIdLendField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all fields");
            this.errorLabel.setVisible(true);
            return;
        }

        // check if the item/member code is a number
        try {
            lendItem();

            // clear text fields
            this.itemCodeLendField.setText("");
            this.memberIdLendField.setText("");
        } catch (NumberFormatException e) {
            this.errorLabel.setText("Please enter a valid item/member code");
            this.errorLabel.setVisible(true);
        }
    }

    private void lendItem() {
        int itemCode = Integer.parseInt(itemCodeLendField.getText());
        int memberId = Integer.parseInt(memberIdLendField.getText());
        // check if the item/member exists
        if (this.DATABASE.lendItem(itemCode, memberId) == null) {
            this.feedbackText.setText("Item successfully lent!");
            this.feedbackText.setVisible(true);

        // if the item/member doesn't exist, show the error
        } else if (this.DATABASE.lendItem(itemCode, memberId).equals("noItem")) {
            this.errorItemCodeLendLabel.setText("Item not found or already lent");
            this.errorItemCodeLendLabel.setVisible(true);
        } else if (this.DATABASE.lendItem(itemCode, memberId).equals("noMember")) {
            this.errorMemberIDLabel.setText("That user does not exist");
            this.errorMemberIDLabel.setVisible(true);
        }
    }

    @FXML
    public void receiveItemButtonClick() {
        // check if the field is empty
        if (itemCodeReceiveField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all fields");
            this.errorLabel.setVisible(true);
            return;
        }

        receiveItem();
    }

    private void receiveItem() {
        // try to parse the item code
        try {
            int itemCode = Integer.parseInt(itemCodeReceiveField.getText());

            // receive item and check if the item exists
            Item receivedItem = this.DATABASE.receiveItem(itemCode);
            if (receivedItem == null) {
                this.errorItemCodeReceive.setText("Item not found or not lent");
                this.errorItemCodeReceive.setVisible(true);
                this.itemCodeReceiveField.setText("");
                return;
            }

            // check if the item is overdue
            if (receivedItem.itemIsOverdue()) {
                this.feedbackText.setText("Item is overdue by " + receivedItem.getDaysOverdue() + " days, item is received");
            } else {
                this.feedbackText.setText("Item successfully received!");
            }
            receivedItem.setDateOfLending(null);

            this.feedbackText.setVisible(true);
            this.itemCodeReceiveField.setText("");
        } catch (NumberFormatException e) {
            this.errorItemCodeReceive.setText("Please enter a valid item code");
            this.errorItemCodeReceive.setVisible(true);
        }
    }
    @FXML
    public void newTextIsEntered() {
        this.feedbackText.setText("");
        this.feedbackText.setVisible(false);

        // hide all error labels
        this.errorItemCodeLendLabel.setVisible(false);
        this.errorMemberIDLabel.setVisible(false);
        this.errorItemCodeReceive.setVisible(false);
        this.errorLabel.setVisible(false);
    }
}
