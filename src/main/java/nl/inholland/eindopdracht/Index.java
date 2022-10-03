package nl.inholland.eindopdracht;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nl.inholland.eindopdracht.Controllers.LoginController;
import nl.inholland.eindopdracht.Models.Database;

import java.io.IOException;

public class Index extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // create database object
        Database database = new Database();

        // create login screen
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Fabula Library");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/nl/inholland/eindopdracht/Images/book.png"));
        stage.setResizable(false);

        // add database to controller
        fxmlLoader.<LoginController>getController().setDatabase(database);

        // show stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}