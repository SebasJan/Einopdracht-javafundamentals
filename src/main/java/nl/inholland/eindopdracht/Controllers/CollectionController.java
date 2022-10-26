package nl.inholland.eindopdracht.Controllers;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import nl.inholland.eindopdracht.Controllers.Events.MouseEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.Item;
import java.util.ArrayList;
import java.util.List;

public class CollectionController extends MouseEvent {
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
    @FXML
    public TextField itemCodeDeleteField;
    @FXML
    public TextField newAuthorField;
    @FXML
    public TextField newTitleField;
    @FXML
    public TextField searchField;
    private Database database;

    public CollectionController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        setTableItems(this.database.ITEMS);
        availableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvailable() ? "Yes" : "No"));

        // add event listener for search function
        searchField.textProperty().addListener(this::searchTextFieldChanges);

        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemTable.setPlaceholder(new Label("Er zijn geen boeken gevonden"));

        // make table editable
        // set cell factories to allow editing
        itemTable.setEditable(true);
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        availableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    // Search in items list when the search field changes
    private void searchTextFieldChanges(Observable ignoredObservable, String ignoredOldValue, String newValue) {
        ArrayList<Item> allItems = (ArrayList<Item>) this.database.ITEMS;
        ArrayList<Item> matchingItems = new ArrayList<>();

        // find the items that start with the search query
        for (Item item : allItems) {
            if (item.getTitle().toLowerCase().startsWith(newValue.toLowerCase()) || item.getAuthor().toLowerCase().startsWith(newValue.toLowerCase())) {
                matchingItems.add(item);
            }
        }

        // add these items to the new list
        setTableItems(matchingItems);
    }

    // edit event handlers for the editable columns
    private void setOnEditEventHandlers() {
        availableColumn.setOnEditCommit(event -> {
            // check if the new value is No or Yes
            if (event.getNewValue().equalsIgnoreCase("yes") || event.getNewValue().equalsIgnoreCase("no")) {
                // get the item that was edited
                Item item = event.getRowValue();

                // set the available property of the item
                item.setAvailable(event.getNewValue().equalsIgnoreCase("yes"));

                // update the item in the database
                this.database.editItem(item);
            } else {
                // show error message
                errorLabel.setText("Available can only be Yes or No");
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

    // fill the table with items
    private void setTableItems(List<Item> items) {
        items = FXCollections.observableArrayList(items);
        itemTable.setItems((ObservableList<Item>) items);
    }

    @FXML
    public void deleteItemButtonClick() {
        // check if the item code field is empty
        if (!itemCodeDeleteField.getText().isEmpty() && itemCodeDeleteField.getText().matches("[0-9]+") && database.itemExists(Integer.parseInt(itemCodeDeleteField.getText()))) {
            // get the item code from the text field
            int itemCode = Integer.parseInt(itemCodeDeleteField.getText());

            // show prompt to confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete item");
            alert.setHeaderText("Are you sure you want to delete item " + itemCode + " ?");
            alert.setContentText("This action cannot be undone");

            // if the user confirms, delete the item
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    this.database.deleteItem(itemCode);
                    setTableItems(this.database.ITEMS);
                }
            });

            itemCodeDeleteField.clear();
        } else {
            errorLabel.setText("Please enter an (valid) item code");
        }
    }

    @FXML
    public void addItemButton() {
        // check if the title and author fields are not empty
        if (!newTitleField.getText().isEmpty() && !newAuthorField.getText().isEmpty()) {
            String title = newTitleField.getText();
            String author = newAuthorField.getText();
            this.database.addItem(title, author);

            // clear the text fields
            newTitleField.clear();
            newAuthorField.clear();

            setTableItems(this.database.ITEMS);
        } else {
            errorLabel.setText("Title and author cannot be empty");
        }
    }
}
