package nl.inholland.eindopdracht.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.Item;

public class CollectionController {
    @FXML
    public TableView<Item> itemTable;
    @FXML
    public TableColumn<Item, Integer> itemCodeColumn;
    @FXML
    public TableColumn<Item, String> availableColumn;
    @FXML
    public TableColumn<Item, String> titleColumn;
    @FXML
    public TableColumn<Item, String> authorColumn;
    @FXML
    public Label errorLabel;
    private Database database;

    @FXML
    public void initialize() {
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // make table editable
        itemTable.setEditable(true);

        // set cell factories to allow editing
        availableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    private void setOnEditEventHandlers() {
        availableColumn.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            // check if the input is valid
            if (event.getNewValue().equals("Yes") || event.getNewValue().equals("No")) {
                item.setAvailable(event.getNewValue());
                this.database.editItem(item);
            } else {
                errorLabel.setText("Please enter 'Yes' or 'No'");
                errorLabel.setVisible(true);
            }
        });

        titleColumn.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setTitle(event.getNewValue());
            this.database.editItem(item);
        });

        authorColumn.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setAuthor(event.getNewValue());
            this.database.editItem(item);
        });
    }

    public void setDatabase(Database database) {
        this.database = database;

        setTableItems();
    }

    private void setTableItems() {
        ObservableList<Item> items = FXCollections.observableArrayList(this.database.items);
        itemTable.setItems(items);
    }
}
