package controller;

import helper.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.CustomerModel;

import java.util.ArrayList;

public class CustomerController {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<CustomerModel> tableViewCustomer;

    @FXML
    private TableColumn<CustomerModel, String> tableColumnCustomerName;

    @FXML
    private TableColumn<CustomerModel, String> tableColumnCustomerPhone;

    @FXML
    private TableColumn<CustomerModel, String> tableColumnCustomerZipcode;

    @FXML
    private TableColumn<CustomerModel, String> tableColumnCustomerAddress;

    @FXML
    private ObservableList observableListCustomer;

    @FXML
    private SortedList<CustomerModel> sortedListCustomer;

    @FXML
    protected void buttonAddOnAction() {
        Main.changeScreen("customer-form");
    }

    @FXML
    protected void buttonDeleteOnAction() {
        Table.removeModelItem(
            tableViewCustomer,
            observableListCustomer,
            sortedListCustomer
        );
    }

    @FXML
    protected void buttonEditOnAction() {
        Table.editModelItem(tableViewCustomer, "customer");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Clientes");

        tableColumnCustomerName.setCellValueFactory(
            new PropertyValueFactory<>("name")
        );
        tableColumnCustomerPhone.setCellValueFactory(
            new PropertyValueFactory<>("phone")
        );
        tableColumnCustomerZipcode.setCellValueFactory(
            new PropertyValueFactory<>("zipcode")
        );
        tableColumnCustomerAddress.setCellValueFactory(
            new PropertyValueFactory<>("address")
        );

        loadTableContentCustomer();
    }

    /**
     * Lista todos os clientes cadastrados na tabela e ativa o campo de
     * busca para filtrar os dados ao digitar.
     */
    private void loadTableContentCustomer() {
        ArrayList<CustomerModel> customerList = (new CustomerModel(null)).find().order("id DESC").fetchAll();

        if (customerList == null) {
            return;
        }

        this.observableListCustomer = FXCollections.observableList(customerList);
        tableViewCustomer.setItems(observableListCustomer);

        FilteredList<CustomerModel> filteredList = new FilteredList<>(
            tableViewCustomer.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(customer -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    String newValueLowerCase = newValue.toLowerCase();

                    if (customer.getName().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (customer.getPhone().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (customer.getZipcode().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    }

                    return customer.getAddress().toLowerCase().contains(newValueLowerCase);
                });
            }
        ));

        this.sortedListCustomer = new SortedList<>(filteredList);
        sortedListCustomer.comparatorProperty().bind(
            tableViewCustomer.comparatorProperty()
        );

        tableViewCustomer.setItems(sortedListCustomer);
    }
}
