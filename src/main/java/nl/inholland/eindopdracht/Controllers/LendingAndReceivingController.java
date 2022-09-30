package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.Item;
import nl.inholland.eindopdracht.Models.User;

public class LendingAndReceivingController {
    @FXML
    public Label welcomeText;
    @FXML
    public TextField itemCodeLendField;
    @FXML
    public TextField memberIdLendField;
    @FXML
    public TextField itemCodeReceiveField;
    @FXML
    public Label errorLabel;
    @FXML
    public Label feedbackText;

    private Database database;

    public void setData(Database database, User user) {
        // set data and welcome the user
        this.database = database;
        welcomeText.setText("Welcome " + user.username);
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
            if (this.database.lendItem(itemCode, memberId)) {
                this.errorLabel.setVisible(false);
                this.feedbackText.setText("Item successfully lent!");
                this.feedbackText.setVisible(true);
            } else {
                this.errorLabel.setText("Item/user not found or already lent");
                this.errorLabel.setVisible(true);
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
                this.errorLabel.setText("Item not found");
                this.errorLabel.setVisible(true);
                this.itemCodeReceiveField.setText("");
                return;
            }

            // check if the item is overdue
            if (receivedItem.itemIsOverdue()) {
                this.feedbackText.setText("Item is overdue by " + receivedItem.daysOverdue + " days, item is received");
            } else {
                this.errorLabel.setVisible(false);
                this.feedbackText.setText("Item successfully received!");
            }
            this.feedbackText.setVisible(true);
            this.itemCodeReceiveField.setText("");
        } catch (NumberFormatException e) {
            this.errorLabel.setText("Please enter a valid item code");
            this.errorLabel.setVisible(true);
        }
    }

    public void newTextIsEntered() {
        this.feedbackText.setText("");
        this.feedbackText.setVisible(false);
    }
}
