package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.User;

import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane dockPane;

    private Database database;
    private User user;

    public void setData(Database database, User user) throws IOException {
        this.database = database;
        this.user = user;
        loadLndRcvScreen();
    }

    private void loadLndRcvScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("LendingReceivingView.fxml"));
        AnchorPane pane = fxmlLoader.load();
        fxmlLoader.<LendingAndReceivingController>getController().setData(database, user);
        dockPane.getChildren().setAll(pane);
    }

    public void buttonLndRcvClick() throws IOException {
        loadLndRcvScreen();
    }

    public void buttonCollectionClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("CollectionView.fxml"));
        AnchorPane pane = fxmlLoader.load();
        fxmlLoader.<CollectionController>getController().setDatabase(database);
        dockPane.getChildren().setAll(pane);
    }

    public void buttonMembersClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("MembersView.fxml"));
        AnchorPane pane = fxmlLoader.load();
        fxmlLoader.<MembersController>getController().setDatabase(database);
        dockPane.getChildren().setAll(pane);
    }

    public void mouseEnteredButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(javafx.scene.Cursor.HAND);
    }

    public void mouseExitedButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #1d6882; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(Cursor.DEFAULT);
    }
}
