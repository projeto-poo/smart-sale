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
import model.entity.PaymentMethodModel;

import java.util.ArrayList;

public class PaymentMethodController {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<PaymentMethodModel> tableViewPaymentMethod;

    @FXML
    private TableColumn<PaymentMethodModel, String> tableColumnPaymentMethodName;

    @FXML
    private ObservableList<PaymentMethodModel> observableListPaymentMethod;

    @FXML
    private SortedList<PaymentMethodModel> sortedListPaymentMethod;

    @FXML
    protected void buttonAddOnAction() {
        Main.changeScreen("payment-method-form");
    }

    @FXML
    protected void buttonDeleteOnAction() {
        Table.removeModelItem(
            tableViewPaymentMethod,
            observableListPaymentMethod,
            sortedListPaymentMethod
        );
    }

    @FXML
    protected void buttonEditOnAction() {
        Table.editModelItem(tableViewPaymentMethod, "payment-method");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Métodos de pagamentos");

        tableColumnPaymentMethodName.setCellValueFactory(
            new PropertyValueFactory<>("name")
        );

        loadTableContentPaymentMethod();
    }

    /**
     * Lista todos os métodos de pagamentos cadastrados na tabela e ativa o
     * campo de busca para filtrar os dados ao digitar.
     */
    private void loadTableContentPaymentMethod() {
        ArrayList paymentMethodList = (new PaymentMethodModel(null)).find().order("id DESC").fetchAll();

        if (paymentMethodList == null) {
            return;
        }

        this.observableListPaymentMethod = FXCollections.observableList(paymentMethodList);
        tableViewPaymentMethod.setItems(observableListPaymentMethod);

        FilteredList<PaymentMethodModel> filteredList = new FilteredList<>(
            tableViewPaymentMethod.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(paymentMethod -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    return paymentMethod.getName().toLowerCase().contains(newValue.toLowerCase());
                });
            }
        ));

        this.sortedListPaymentMethod = new SortedList<>(filteredList);
        sortedListPaymentMethod.comparatorProperty().bind(
            tableViewPaymentMethod.comparatorProperty()
        );

        tableViewPaymentMethod.setItems(sortedListPaymentMethod);
    }
}
