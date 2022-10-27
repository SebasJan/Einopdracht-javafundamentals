package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import nl.inholland.eindopdracht.Controllers.Events.MouseHoverEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Models.User;
import java.io.IOException;

public class MainController extends MouseHoverEvent {
    public static final String LENDING_RECEIVING_VIEW_FXML = "LendingReceivingView.fxml";
    public static final String COLLECTION_VIEW_FXML = "CollectionView.fxml";
    public static final String MEMBERS_VIEW_FXML = "MembersView.fxml";
    @FXML public AnchorPane dockPane;

    private final Database DATABASE;
    private final User USER;

    public MainController(Database database, User user) {
        this.DATABASE = database;
        this.USER = user;
    }

    @FXML
    public void initialize() throws IOException {
        loadLndRcvScreen();
    }

    private void loadLndRcvScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource(LENDING_RECEIVING_VIEW_FXML));
        fxmlLoader.setController(new LendingAndReceivingController(DATABASE, USER));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }


    @FXML
    public void buttonLndRcvClick() throws IOException {
        loadLndRcvScreen();
    }

    @FXML
    public void buttonCollectionClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource(COLLECTION_VIEW_FXML));
        fxmlLoader.setController(new CollectionController(DATABASE));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }

    @FXML
    public void buttonMembersClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource(MEMBERS_VIEW_FXML));
        fxmlLoader.setController(new MembersController(DATABASE));
        AnchorPane pane = fxmlLoader.load();
        dockPane.getChildren().setAll(pane);
    }
}
