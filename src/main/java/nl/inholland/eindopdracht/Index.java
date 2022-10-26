package nl.inholland.eindopdracht;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nl.inholland.eindopdracht.Controllers.LoginController;
import nl.inholland.eindopdracht.Data.Database;

import java.io.IOException;

public class Index extends Application {

    public static final String LOGIN_VIEW_FXML = "LoginView.fxml";
    public static final String STYLE_CSS = "file:src/main/resources/nl/inholland/eindopdracht/Style/style.css";
    public static final String FAVICON_IMAGE = "file:src/main/resources/nl/inholland/eindopdracht/Images/book.png";

    @Override
    public void start(Stage stage) throws IOException {
        // create THE database object
        Database database = new Database();

        // create login screen
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource(LOGIN_VIEW_FXML));
        fxmlLoader.setController(new LoginController(database));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        scene.getStylesheets().add(STYLE_CSS);
        stage.setTitle("Fabula Library");
        stage.setScene(scene);
        stage.getIcons().add(new Image(FAVICON_IMAGE));
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}