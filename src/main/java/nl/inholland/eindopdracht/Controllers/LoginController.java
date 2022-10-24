package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nl.inholland.eindopdracht.Controllers.Events.MouseEvent;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.User;

import java.io.IOException;

public class LoginController extends MouseEvent {
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passCodeTextField;
    @FXML
    public Label errorLabel;

    private final Database DATABASE;

    public LoginController(Database database) {
        this.DATABASE = database;
    }

    @FXML
    public void initialize() {
        // set event listeners for when passcode field changes
        passCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> passCodeTextFieldChanges(newValue));
    }

    private void passCodeTextFieldChanges (String newValue) {
        if (newValue.length() == 4) {
            for (User user : DATABASE.USERS) {
                if (user.username().equals(this.usernameTextField.getText()) && user.passcode().equals(this.passCodeTextField.getText())) {
                    try {
                        openMainWindow(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
    }

    @FXML
    private void loginButtonClick() throws IOException {
        // check if the text fields are empty
        if (this.usernameTextField.getText().isEmpty() || this.passCodeTextField.getText().isEmpty()) {
            this.errorLabel.setText("Please fill in all the fields..");
            this.errorLabel.setVisible(true);
            return;
        }

        // check the username and password
        for (User user : DATABASE.USERS) {
            if (user.username().equals(this.usernameTextField.getText()) && user.passcode().equals(this.passCodeTextField.getText())) {
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
        fxmlLoader.setController(new MainController(DATABASE, user));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add("file:src/main/resources/nl/inholland/eindopdracht/Style/style.css");
        stage.setTitle("Fabula Library");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/nl/inholland/eindopdracht/Images/book.png"));
        stage.setResizable(false);
        stage.show();

        // save database when the stage is closed
        stage.setOnCloseRequest(event -> DATABASE.saveDateBase());

        // close login window
        Stage loginStage = (Stage) this.usernameTextField.getScene().getWindow();
        loginStage.close();
    }
}