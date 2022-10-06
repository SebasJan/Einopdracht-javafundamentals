package nl.inholland.eindopdracht.Controllers;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class MouseEvent {
    public void mouseEnteredButton(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(javafx.scene.Cursor.HAND);
    }

    public void mouseExitedButton(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #1d6882; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(Cursor.DEFAULT);
    }
}
