package nl.inholland.eindopdracht.Controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.Item;

import java.util.ArrayList;
import java.util.List;

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
    @FXML
    public TextField itemCodeDeleteField;
    @FXML
    public TextField newAuthorField;
    @FXML
    public TextField newTitleField;
    @FXML
    public TextField searchField;
    private Database database;

    @FXML
    public void initialize() {
        // add event listener for search function
        searchField.textProperty().addListener(this::searchTextFieldChanges);

        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // make table editable
        itemTable.setEditable(true);

        // set cell factories to allow editing
        //availableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    private void searchTextFieldChanges(Observable observable,String oldValue,String newValue) {
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
//        availableColumn.setOnEditCommit(event -> {
//            Item item = event.getRowValue();
//            // check if the input is valid
//            if (event.getNewValue().equals("Yes") || event.getNewValue().equals("No")) {
//                item.setAvailable(event.getNewValue());
//                this.database.editItem(item);
//            } else {
//                errorLabel.setText("Please enter 'Yes' or 'No'");
//                errorLabel.setVisible(true);
//            }
//        });

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
    }


    // ZORGEN DAT ALS JE NIKS INVULT HIJ NIET CRASHT!!
    public void addItemButton() {
        // get the title and author from the text fields
        String title = newTitleField.getText();
        String author = newAuthorField.getText();

        // create a new item and add it to the database
        this.database.addItem(title, author);

        // clear the text fields
        newTitleField.clear();
        newAuthorField.clear();

        // update the table
        setTableItems(this.database.items);
    }
}
