package nl.inholland.eindopdracht.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import nl.inholland.eindopdracht.Models.Database;

public class CollectionController {

    @FXML
    public TreeTableView itemTable;
    @FXML
    public TreeTableColumn itemCodeColumn;
    @FXML
    public TreeTableColumn availableColumn;
    @FXML
    public TreeTableColumn titleColumn;
    @FXML
    public TreeTableColumn authorColumn;
    private Database database;
    public void setDatabase(Database database) {
        this.database = database;
    }

    private void fillTable() {

    }
}
