package nl.inholland.eindopdracht.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nl.inholland.eindopdracht.Index;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CollectionController {
    private Database database;
    public void setDatabase(Database database) {
        this.database = database;
    }
}
