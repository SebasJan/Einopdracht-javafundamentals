package nl.inholland.eindopdracht.Controllers;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nl.inholland.eindopdracht.Controllers.Interfaces.MouseEvent;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.User;

import java.io.IOException;

public class LoginController extends MouseEvent {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passCodeTextField;
    @FXML
    private Label errorLabel;

    private Database database;

    @FXML
    public void initialize() {
        // set event listeners for when passcode field changes
        passCodeTextField.textProperty().addListener(this::passCodeTextFieldChanges);
    }

    private void passCodeTextFieldChanges (Observable observable,String oldValue,String newValue) {
        if (newValue.length() == 4) {
            for (User user : database.users) {
                if (user.getUsername().equals(this.usernameTextField.getText()) && user.getPasscode().equals(this.passCodeTextField.getText())) {
                    try {
                        openMainWindow(user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
        }
    }

    @FXML
    private void login() throws IOException {
        // check if the text fields are empty
        if (this.usernameTextField.getText().isEmpty() || this.passCodeTextField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all the fields..");
            this.errorLabel.setVisible(true);
            return;
        }

        // check the username and password
        for (User user : database.users) {
            if (user.getUsername().equals(this.usernameTextField.getText()) && user.getPasscode().equals(this.passCodeTextField.getText())) {
                openMainWindow(user);
                return;
            }
            this.errorLabel.setText("Username or password is incorrect..");
            this.errorLabel.setVisible(true);
            this.passCodeTextField.clear();
        }
    }

    private void openMainWindow(User user) throws IOException {
        // create main screen
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add("file:src/main/resources/nl/inholland/eindopdracht/Style/style.css");
        stage.setTitle("Fabula Library");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/nl/inholland/eindopdracht/Images/book.png"));
        stage.setResizable(false);

        // pass database
        fxmlLoader.<MainController>getController().setData(database, user);
        stage.show();

        // method for when stage is closed
        stage.setOnCloseRequest(event -> {
            database.saveDateBase();
        });

        // close login window
        Stage loginStage = (Stage) this.usernameTextField.getScene().getWindow();
        loginStage.close();
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}