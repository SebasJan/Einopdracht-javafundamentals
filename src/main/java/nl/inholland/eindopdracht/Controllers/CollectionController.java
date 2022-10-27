package nl.inholland.eindopdracht.Controllers;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import nl.inholland.eindopdracht.Controllers.Events.MouseHoverEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.Item;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CollectionController extends MouseHoverEvent {
    @FXML public TableView<Item> itemTable;
    @FXML public TableColumn<Item, Integer> itemCodeColumn;
    @FXML public TableColumn<Item, String> availableColumn;
    @FXML public TableColumn<Item, String> titleColumn;
    @FXML public TableColumn<Item, String> authorColumn;
    @FXML public Label errorLabel;
    @FXML public TextField itemCodeDeleteField;
    @FXML public TextField newAuthorField;
    @FXML public TextField newTitleField;
    @FXML public TextField searchField;
    @FXML public Node root;
    private final Database DATABASE;

    public CollectionController(Database database) {
        this.DATABASE = database;
    }

    @FXML
    public void initialize() {
        setTableItems(this.DATABASE.getITEMS());
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
        ArrayList<Item> allItems = (ArrayList<Item>) this.DATABASE.getITEMS();
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
                this.DATABASE.editItem(item);
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Available can only be Yes or No");
            }
        });

        titleColumn.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setTitle(event.getNewValue());
            this.DATABASE.editItem(item);
        });

        authorColumn.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setAuthor(event.getNewValue());
            this.DATABASE.editItem(item);
        });
    }

    // fill the table with items
    private void setTableItems(List<Item> items) {
        items = FXCollections.observableArrayList(items);
        itemTable.setItems((ObservableList<Item>) items);
    }

    @FXML
    public void deleteItemButtonClick() {
        errorLabel.setVisible(false);
        // check if the item code field is empty and exist in the database
        if (itemCodeDeleteField.getText().isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Please enter an item code");
            return;
        } else if (!itemCodeDeleteField.getText().matches("\\d")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Item code can only be a number");
            return;
        } else if (!DATABASE.itemExists(Integer.parseInt(itemCodeDeleteField.getText()))) {
            errorLabel.setVisible(true);
            errorLabel.setText("Item does not exist");
            return;
        }
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
                this.DATABASE.deleteItem(itemCode);
                setTableItems(this.DATABASE.getITEMS());
            }
        });

        itemCodeDeleteField.clear();
    }

    @FXML
    public void addItemButton() {
        errorLabel.setVisible(false);
        // check if the title and author fields are not empty
        if (!newTitleField.getText().isEmpty() && !newAuthorField.getText().isEmpty()) {
            String title = newTitleField.getText();
            String author = newAuthorField.getText();
            this.DATABASE.addItem(title, author);

            // clear the text fields
            newTitleField.clear();
            newAuthorField.clear();

            setTableItems(this.DATABASE.getITEMS());
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Title and author cannot be empty");
        }
    }

    @FXML
    public void loadItemsButtonClick() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open CSV file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));

            File chosenFile = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (chosenFile != null) {
                Files.readAllLines(chosenFile.toPath()).stream().skip(1).forEach(line -> {
                    String[] splitLine = line.split(";");
                    DATABASE.addItem(splitLine[0], splitLine[1]);
                    setTableItems(this.DATABASE.getITEMS());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
