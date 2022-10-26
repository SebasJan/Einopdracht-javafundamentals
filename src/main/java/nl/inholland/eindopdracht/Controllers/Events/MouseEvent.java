package nl.inholland.eindopdracht.Controllers.Events;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public abstract class MouseEvent {
    // add event handlers for mouse events
    // set the color and cursor for hovering a button the button
    // these methods are used in the controllers
    @FXML
    protected void mouseEnteredButton(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(javafx.scene.Cursor.HAND);
    }

    @FXML
    protected void mouseExitedButton(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #1d6882; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(Cursor.DEFAULT);
    }
}
