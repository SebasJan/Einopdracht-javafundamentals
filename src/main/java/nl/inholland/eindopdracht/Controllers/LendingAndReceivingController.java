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
    private Label welcomeText;
    @FXML
    private TextField itemCodeLendField;
    @FXML
    private TextField memberIdLendField;
    @FXML
    private TextField itemCodeReceiveField;
    @FXML
    private Label feedbackText;
    @FXML
    private Label errorItemCodeLendLabel;
    @FXML
    private Label errorMemberIDLabel;
    @FXML
    private Label errorItemCodeReceive;
    @FXML
    private Label errorLabel;

    private Database database;

    public void setData(Database database, User user) {
        // set data and welcome the user
        this.database = database;
        welcomeText.setText("Welcome " + user.getUsername());
    }

    public void lendItemButtonClick() {
        // check if the fields are empty
        if (itemCodeLendField.getText().isEmpty() || memberIdLendField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all fields");
            this.errorLabel.setVisible(true);
            return;
        }

        // check if the item/member code is a number
        try {
            int itemCode = Integer.parseInt(itemCodeLendField.getText());
            int memberId = Integer.parseInt(memberIdLendField.getText());
            // check if the item/member exists
            if (this.database.lendItem(itemCode, memberId) == null) {
                this.feedbackText.setText("Item successfully lent!");
                this.feedbackText.setVisible(true);
            } else if (this.database.lendItem(itemCode, memberId).equals("noItem")) {
                this.errorItemCodeLendLabel.setText("Item not found or already lent");
                this.errorItemCodeLendLabel.setVisible(true);
            } else if (this.database.lendItem(itemCode, memberId).equals("noMember")) {
                this.errorMemberIDLabel.setText("That user does not exist");
                this.errorMemberIDLabel.setVisible(true);
            }

            // clear text fields
            this.itemCodeLendField.setText("");
            this.memberIdLendField.setText("");
        } catch (NumberFormatException e) {
            this.errorLabel.setText("Please enter a valid item/member code");
            this.errorLabel.setVisible(true);
        }
    }

    public void receiveItemButtonClick() {
        // check if the field is empty
        if (itemCodeReceiveField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all fields");
            this.errorLabel.setVisible(true);
            return;
        }

        // try to parse the item code
        try {
            int itemCode = Integer.parseInt(itemCodeReceiveField.getText());

            // receive item and check if the item exists
            Item receivedItem = this.database.receiveItem(itemCode);
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
            this.feedbackText.setVisible(true);
            this.itemCodeReceiveField.setText("");
        } catch (NumberFormatException e) {
            this.errorItemCodeReceive.setText("Please enter a valid item code");
            this.errorItemCodeReceive.setVisible(true);
        }
    }

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
