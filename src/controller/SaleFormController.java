package controller;

import helper.Form;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class SaleFormController {

    @FXML
    private Label labelFeedbackCustomer;

    @FXML
    private ComboBox<CustomerModel> comboBoxCustomer;

    @FXML
    private Label labelFeedbackPaymentMethod;

    @FXML
    private ComboBox<PaymentMethodModel> comboBoxPaymentMethod;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<ProductModel> tableViewProduct;

    @FXML
    private TableColumn<ProductModel, String> tableColumnProductName;

    @FXML
    private TableColumn<ProductModel, ComboBox> tableColumnProductAmount;

    @FXML
    private ObservableList<ProductModel> observableListProduct;

    @FXML
    private SortedList<ProductModel> sortedListProduct;

    @FXML
    protected void buttonCancelOnAction() {
        Main.changeScreen("product");
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(labelFeedbackCustomer, labelFeedbackPaymentMethod);
        Form.resetField(comboBoxCustomer, comboBoxPaymentMethod);

        if (comboBoxCustomer.getValue() == null) {
            Form.invalidField(
                comboBoxCustomer,
                labelFeedbackCustomer,
                "Obrigatório"
            );

            block = true;
        }

        if (comboBoxPaymentMethod.getValue() == null) {
            Form.invalidField(
                comboBoxPaymentMethod,
                labelFeedbackPaymentMethod,
                "Obrigatório"
            );

            block = true;
        }

        if (block) {
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        SaleModel saleModel = new SaleModel(null);
        saleModel.setCustomerId(
            comboBoxCustomer.getSelectionModel().getSelectedItem().getId()
        );
        saleModel.setPaymentMethodId(
            comboBoxPaymentMethod.getSelectionModel().getSelectedItem().getId()
        );
        saleModel.setDate(dateTimeFormatter.format(LocalDateTime.now()));

        boolean save = saleModel.save();

        if (saleModel.fail() != null || !save) {
            System.out.println(saleModel.fail().getMessage());
            return;
        }

        double totalPrice = 0;

        for (int i = 0; i < tableViewProduct.getItems().size(); i++) {
            if (
                tableViewProduct.getItems().get(i).getComboBoxAmount().getValue() == null
                    || parseInt(tableViewProduct.getItems().get(i).getComboBoxAmount().getValue().toString()) == 0
            ) {
                continue;
            }

            int currentStock = parseInt(tableViewProduct.getItems().get(i).getStock());
            int totalItem = parseInt(tableViewProduct.getItems().get(i).getComboBoxAmount().getValue().toString());

            totalPrice += (Double.parseDouble(tableViewProduct.getItems().get(i).getPrice()) * parseInt(tableViewProduct.getItems().get(i).getComboBoxAmount().getValue().toString()));

            SaleItemModel saleItemModel = new SaleItemModel(null);
            saleItemModel.setSaleId(saleModel.getId());
            saleItemModel.setName(tableViewProduct.getItems().get(i).getName());
            saleItemModel.setPrice(tableViewProduct.getItems().get(i).getPrice());
            saleItemModel.setAmount(tableViewProduct.getItems().get(i).getComboBoxAmount().getValue().toString());
            saleItemModel.save();

            tableViewProduct.getItems().get(i).setStock((currentStock - totalItem) + "");
            tableViewProduct.getItems().get(i).save();
        }

        saleModel.setPrice(totalPrice + "");
        saleModel.save();

        Main.changeScreen("sale");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Vendas ● Adicionar nova");

        ArrayList customerList = (new CustomerModel(null)).find().fetchAll();
        if (customerList != null) {
            comboBoxCustomer.setItems(FXCollections.observableList(customerList));
        }

        ArrayList paymentMethodList = (new PaymentMethodModel(null)).find().fetchAll();
        if (paymentMethodList != null) {
            comboBoxPaymentMethod.setItems(FXCollections.observableList(paymentMethodList));
        }

        tableColumnProductName.setCellValueFactory(
            new PropertyValueFactory<>("name")
        );
        tableColumnProductAmount.setCellValueFactory(
            new PropertyValueFactory<>("comboBoxAmount")
        );

        loadTableContentProduct();
    }

    /**
     * Lista todos os produtos cadastrados na tabela e ativa o campo de
     * busca para filtrar os dados ao digitar.
     */
    private void loadTableContentProduct() {
        ArrayList productList = (new ProductModel(null)).find().order("id DESC").fetchAll();

        if (productList == null) {
            return;
        }

        this.observableListProduct = FXCollections.observableList(productList);
        tableViewProduct.setItems(observableListProduct);

        FilteredList<ProductModel> filteredList = new FilteredList(
            tableViewProduct.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(product -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    return product.getName().toLowerCase().contains(
                        newValue.toLowerCase()
                    );
                });
            }
        ));

        this.sortedListProduct = new SortedList(filteredList);
        sortedListProduct.comparatorProperty().bind(
            tableViewProduct.comparatorProperty()
        );

        tableViewProduct.setItems(sortedListProduct);
    }
}
