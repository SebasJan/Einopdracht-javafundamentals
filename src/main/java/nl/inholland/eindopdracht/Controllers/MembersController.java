package nl.inholland.eindopdracht.Controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.Item;
import nl.inholland.eindopdracht.Models.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MembersController {
    @FXML
    private TableView<Member> memberTable;
    @FXML
    protected TableColumn<Member, Integer> memberIDColumn;
    @FXML
    private TableColumn<Member, String>  firstNameColumn;
    @FXML
    private TableColumn<Member, String>  lastNameColumn;
    @FXML
    protected TableColumn<Member, String>  birthDateColumn;
    @FXML
    protected Label errorLabel;
    @FXML
    private TextField memberIDDeleteField;
    @FXML
    private TextField firstNameField;
    @FXML
    protected TextField searchField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker birthDatePicker;

    private Database database;

    @FXML
    public void initialize() {
        // add event listener for search function
        searchField.textProperty().addListener(this::searchTextFieldChanges);

        memberTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // make table editable
        memberTable.setEditable(true);

        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    private void searchTextFieldChanges(Observable observable, String oldValue, String newValue) {
        ArrayList<Member> allMembers = (ArrayList<Member>) this.database.members;
        ArrayList<Member> matchingMembers = new ArrayList<>();

        // find the items that start with the search query
        for (Member member : allMembers) {
            if (member.getFirstName().toLowerCase().startsWith(newValue.toLowerCase()) || member.getLastName().toLowerCase().startsWith(newValue.toLowerCase())) {
                matchingMembers.add(member);
            }
        }

        // add these items to the new list
        setTableItems(matchingMembers);
    }

    private void setOnEditEventHandlers() {
        firstNameColumn.setOnEditCommit(event -> {
            Member member = event.getRowValue();
            member.setFirstName(event.getNewValue());
            this.database.editMember(member);
        });

        lastNameColumn.setOnEditCommit(event -> {
            Member member = event.getRowValue();
            member.setLastName(event.getNewValue());
            this.database.editMember(member);
        });
    }

    public void setDatabase(Database database) {
        this.database = database;

        setTableItems(this.database.members);
    }

    private void setTableItems(List<Member> members) {
        members = FXCollections.observableArrayList(members);
        memberTable.setItems((ObservableList<Member>) members);
    }

    public void addMemberButton() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(birthDatePicker.getValue().getYear(), birthDatePicker.getValue().getMonthValue(), birthDatePicker.getValue().getDayOfMonth());

        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.getEditor().clear();

        this.database.addMember(firstName, lastName, birthDate);
        setTableItems(this.database.members);
    }

    public void deleteMemberButton() {
        int memberID = Integer.parseInt(memberIDDeleteField.getText());

        // show prompt to confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete item");
        alert.setHeaderText("Are you sure you want to delete item " + memberID + " ?");
        alert.setContentText("This action cannot be undone");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.database.deleteMember(memberID);
                setTableItems(this.database.members);
            }
        });
        memberIDDeleteField.clear();
    }

    public void mouseEnteredButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(javafx.scene.Cursor.HAND);
    }

    public void mouseExitedButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #1d6882; -fx-text-fill: #ffffff;");
        button.getScene().setCursor(Cursor.DEFAULT);
    }
}
