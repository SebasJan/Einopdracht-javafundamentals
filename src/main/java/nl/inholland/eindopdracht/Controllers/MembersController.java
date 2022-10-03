package nl.inholland.eindopdracht.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import nl.inholland.eindopdracht.Models.Database;
import nl.inholland.eindopdracht.Models.Member;

import java.util.Calendar;
import java.util.List;

public class MembersController {
    @FXML
    public TableView<Member> memberTable;
    @FXML
    public TableColumn<Member, Integer> memberIDColumn;
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

    @FXML
    public void initialize() {
        memberTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // make table editable
        memberTable.setEditable(true);

        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
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
        memberIDDeleteField.clear();

        this.database.deleteMember(memberID);
        setTableItems(this.database.members);
    }
}
