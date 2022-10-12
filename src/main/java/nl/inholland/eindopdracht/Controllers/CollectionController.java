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
    private TableView<Item> itemTable;
    @FXML
    protected TableColumn<Item, Integer> itemCodeColumn;
    @FXML
    private TableColumn<Item, String> availableColumn;
    @FXML
    private TableColumn<Item, String> titleColumn;
    @FXML
    private TableColumn<Item, String> authorColumn;
    @FXML
    protected Label errorLabel;
    @FXML
    private TextField itemCodeDeleteField;
    @FXML
    private TextField newAuthorField;
    @FXML
    private TextField newTitleField;
    @FXML
    private TextField searchField;
    private Database database;

    @FXML
    public void initialize() {
        availableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvailable() ? "Yes" : "No"));

        // add event listener for search function
        searchField.textProperty().addListener(this::searchTextFieldChanges);

        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemTable.setPlaceholder(new Label("Er zijn geen boeken gevonden"));

        // make table editable
        itemTable.setEditable(true);

        // set cell factories to allow editing
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        availableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    private void searchTextFieldChanges(Observable ignoredObservable, String ignoredOldValue, String newValue) {
        ArrayList<Item> allItems = (ArrayList<Item>) this.database.items;
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

    public void setDatabase(Database database) {
        this.database = database;

        setTableItems(this.database.items);
    }

    private void setTableItems(List<Item> items) {
        items = FXCollections.observableArrayList(items);
        itemTable.setItems((ObservableList<Item>) items);
    }

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
                    setTableItems(this.database.items);
                }
            });

            itemCodeDeleteField.clear();
        } else {
            errorLabel.setText("Please enter an (valid) item code");
        }
    }

    public void addItemButton() {
        // check if the title and author fields are not empty
        if (!newTitleField.getText().isEmpty() && !newAuthorField.getText().isEmpty()) {
            String title = newTitleField.getText();
            String author = newAuthorField.getText();
            this.database.addItem(title, author);

            // clear the text fields
            newTitleField.clear();
            newAuthorField.clear();

            setTableItems(this.database.items);
        } else {
            errorLabel.setText("Title and author cannot be empty");
        }
    }
}
