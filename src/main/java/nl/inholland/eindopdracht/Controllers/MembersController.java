package nl.inholland.eindopdracht.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import nl.inholland.eindopdracht.Controllers.Events.MouseEvent;
import nl.inholland.eindopdracht.Data.Database;
import nl.inholland.eindopdracht.Models.Member;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MembersController extends MouseEvent {
    @FXML
    public TableView<Member> memberTable;
    @FXML
    public TableColumn<Member, String>  firstNameColumn;
    @FXML
    public TableColumn<Member, String>  lastNameColumn;
    @FXML
    public TableColumn<Member, String>  birthDateColumn;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField memberIDDeleteField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField searchField;
    @FXML
    public TextField lastNameField;
    @FXML
    public DatePicker birthDatePicker;

    private Database database;

    public MembersController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        setTableItems(this.database.members);
        // add event listener for search function
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchTextFieldChanges(newValue));

        memberTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        memberTable.setPlaceholder(new Label("Er zijn geen medewerkers gevonden"));

        // make table editable
        memberTable.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        birthDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        setOnEditEventHandlers();
    }

    private void searchTextFieldChanges(String newValue) {
        ArrayList<Member> allMembers = (ArrayList<Member>) this.database.members;
        ArrayList<Member> matchingMembers = new ArrayList<>();

        // find the items that start with the search query
        for (Member member : allMembers) {
            if (member.getFirstName().toLowerCase().startsWith(newValue.toLowerCase()) || member.getLastName().toLowerCase().startsWith(newValue.toLowerCase())) {
                matchingMembers.add(member);
            }
        }
        setTableItems(matchingMembers);
    }

    private void setOnEditEventHandlers() {
        birthDateColumn.setOnEditCommit(event -> {
            // check if the birthdate is in the correct format (dd-MM-yyyy)
            if (event.getNewValue().matches("\\d{2}-\\d{2}-\\d{4}")) {
                Member member = event.getRowValue();

                // parse the string to a date
                String[] dateParts = event.getNewValue().split("-");
                Calendar dateOfBirth = Calendar.getInstance();
                dateOfBirth.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
                dateOfBirth.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
                dateOfBirth.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));

                member.setDateOfBirth(dateOfBirth);
                database.editMember(member);
            } else {
                errorLabel.setText("Birthdate must be in the format dd-MM-yyyy");
            }
        });

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

    private void setTableItems(List<Member> members) {
        members = FXCollections.observableArrayList(members);
        memberTable.setItems((ObservableList<Member>) members);
    }

    public void addMemberButton() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || birthDatePicker.getValue() == null) {
            errorLabel.setText("Please fill in all fields");
            return;
        }
        createAndSaveNewMember();
        setTableItems(this.database.members);
    }

    private void createAndSaveNewMember() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(birthDatePicker.getValue().getYear(), birthDatePicker.getValue().getMonthValue(), birthDatePicker.getValue().getDayOfMonth());

        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.getEditor().clear();

        this.database.addMember(firstName, lastName, birthDate);
    }

    public void deleteMemberButton() {
        if (memberIDDeleteField.getText().isEmpty()) {
            errorLabel.setText("Please fill in the member ID");
            return;
        }
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
}
