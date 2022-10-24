package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import nl.inholland.eindopdracht.Controllers.Events.MouseEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Models.User;

import java.io.IOException;

public class MainController extends MouseEvent {
    @FXML
    public AnchorPane dockPane;

    private final Database DATABASE;
    public User user;

    public MainController(Database database, User user) {
        this.DATABASE = database;
        this.user = user;
    }

    @FXML
    public void initialize() throws IOException {
        loadLndRcvScreen();
    }

    private void loadLndRcvScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("LendingReceivingView.fxml"));
        fxmlLoader.setController(new LendingAndReceivingController(DATABASE, user));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }

    public void buttonLndRcvClick() throws IOException {
        loadLndRcvScreen();
    }

    public void buttonCollectionClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("CollectionView.fxml"));
        fxmlLoader.setController(new CollectionController(DATABASE));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }

    public void buttonMembersClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("MembersView.fxml"));
        fxmlLoader.setController(new MembersController(DATABASE));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }
}
