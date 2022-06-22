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
import model.entity.SupplierModel;

import java.util.ArrayList;

public class SupplierController {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<SupplierModel> tableViewSupplier;

    @FXML
    private TableColumn<SupplierModel, String> tableColumnSupplierName;

    @FXML
    private TableColumn<SupplierModel, String> tableColumnSupplierPhone;

    @FXML
    private ObservableList<SupplierModel> observableListSupplier;

    @FXML
    private SortedList<SupplierModel> sortedListSupplier;

    @FXML
    protected void buttonAddOnAction() {
        Main.changeScreen("supplier-form");
    }

    @FXML
    protected void buttonDeleteOnAction() {
        Table.removeModelItem(
            tableViewSupplier,
            observableListSupplier,
            sortedListSupplier
        );
    }

    @FXML
    protected void buttonEditOnAction() {
        Table.editModelItem(tableViewSupplier, "supplier");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Fornecedores");

        tableColumnSupplierName.setCellValueFactory(
            new PropertyValueFactory<>("name")
        );
        tableColumnSupplierPhone.setCellValueFactory(
            new PropertyValueFactory<>("phone")
        );

        loadTableContentSupplier();
    }

    /**
     * Lista todos os fornecedores cadastrados na tabela e ativa o campo de
     * busca para filtrar os dados ao digitar.
     */
    private void loadTableContentSupplier() {
        ArrayList supplierList = (new SupplierModel(null)).find().order("id DESC").fetchAll();

        if (supplierList == null) {
            return;
        }

        this.observableListSupplier = FXCollections.observableList(supplierList);
        tableViewSupplier.setItems(observableListSupplier);

        FilteredList<SupplierModel> filteredList = new FilteredList<>(
            tableViewSupplier.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(supplier -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    String newValueLowerCase = newValue.toLowerCase();

                    if (supplier.getName().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    }

                    return supplier.getPhone().toLowerCase().contains(newValueLowerCase);
                });
            }
        ));

        this.sortedListSupplier = new SortedList<>(filteredList);
        sortedListSupplier.comparatorProperty().bind(
            tableViewSupplier.comparatorProperty()
        );

        tableViewSupplier.setItems(sortedListSupplier);
    }
}
