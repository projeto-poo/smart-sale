package controller;

import helper.Form;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.ProductHasSupplierModel;
import model.entity.MeasureTypeModel;
import model.entity.ProductModel;
import model.entity.SupplierModel;

import java.util.ArrayList;

public class ProductFormController {

    private ProductModel productModel;

    @FXML
    private Label labelFeedbackName;

    @FXML
    private Label labelFeedbackStock;

    @FXML
    private Label labelFeedbackMeasureType;

    @FXML
    private Label labelFeedbackAlertEnding;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPrice;

    @FXML
    private TextField textFieldStock;

    @FXML
    private TextField textFieldAlertEnding;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private ComboBox<MeasureTypeModel> comboBoxMeasureType;

    @FXML
    private TableView<SupplierModel> tableViewSupplier;

    @FXML
    private TableColumn<SupplierModel, CheckBox> tableColumnSupplierCheck;

    @FXML
    private TableColumn<SupplierModel, String> tableColumnSupplierName;

    @FXML
    private ObservableList<SupplierModel> observableListSupplier;

    @FXML
    private SortedList<SupplierModel> sortedListSupplier;

    @FXML
    protected void buttonCancelOnAction() {
        Main.changeScreen("product");
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(
            labelFeedbackName,
            labelFeedbackStock,
            labelFeedbackMeasureType,
            labelFeedbackAlertEnding
        );
        Form.resetField(textFieldName, textFieldStock, textFieldAlertEnding);
        Form.resetField(comboBoxMeasureType);

        if (textFieldName.getText().isEmpty()) {
            Form.invalidField(textFieldName, labelFeedbackName, "Obrigatório");
            block = true;
        }

        if (textFieldStock.getText().isEmpty()) {
            Form.invalidField(textFieldStock, labelFeedbackStock, "Obrigatório");
            block = true;
        }

        if (textFieldAlertEnding.getText().isEmpty()) {
            Form.invalidField(
                textFieldAlertEnding,
                labelFeedbackAlertEnding,
                "Obrigatório"
            );

            block = true;
        }

        if (comboBoxMeasureType.getValue() == null) {
            Form.invalidField(
                comboBoxMeasureType,
                labelFeedbackMeasureType,
                "Obrigatório"
            );

            block = true;
        }

        if (block) {
            return;
        }

        ProductModel productModel = (this.productModel != null ? this.productModel : new ProductModel(null));
        productModel.setMeasureTypeId(
            comboBoxMeasureType.getSelectionModel().getSelectedItem().getId()
        );
        productModel.setName(textFieldName.getText());
        productModel.setPrice(textFieldPrice.getText());
        productModel.setStock(textFieldStock.getText());
        productModel.setAlertEnding(textFieldAlertEnding.getText());

        boolean save = productModel.save();

        if (productModel.fail() != null) {
            System.out.println(productModel.fail().getMessage());
            return;
        }

        if (!save) {
            if (productModel.returnMessage().get("name") != null) {
                Form.invalidField(
                    textFieldName,
                    labelFeedbackName,
                    productModel.returnMessage().get("name").toString()
                );
            }

            return;
        }

        ArrayList<ProductHasSupplierModel> productHasSupplierList = (new ProductHasSupplierModel(null)).find(
            "product_id = '" + productModel.getId() + "'"
        ).fetchAll();

        if (productHasSupplierList != null) {
            for (ProductHasSupplierModel productHasSupplierModel : productHasSupplierList) {
                productHasSupplierModel.destroy();
            }
        }

        for (int i = 0; i < tableViewSupplier.getItems().size(); i++) {
            if (tableViewSupplier.getItems().get(i).getCheckBox().isSelected()) {
                ProductHasSupplierModel productHasSupplierModel = new ProductHasSupplierModel(null);
                productHasSupplierModel.setSupplierId(tableViewSupplier.getItems().get(i).getId());
                productHasSupplierModel.setProductId(productModel.getId());
                productHasSupplierModel.save();
            }
        }

        Main.changeScreen("product");
    }

    @FXML
    protected void initialize() {
        ArrayList measureTypeList = (new MeasureTypeModel(null)).find().fetchAll();
        comboBoxMeasureType.setItems(FXCollections.observableList(measureTypeList));

        Form.onlyNumber(textFieldStock);
        Form.onlyNumber(textFieldAlertEnding);

        if (Main.getStage().getUserData() == null) {
            InternalScreenController.setPageTitle("Produtos ● Adicionar novo");
        } else {
            this.productModel = (ProductModel) Main.getStage().getUserData();
            Main.getStage().setUserData(null);

            InternalScreenController.setPageTitle(
                "Produtos ● " + productModel.getName()
            );

            textFieldName.setText(productModel.getName());
            textFieldPrice.setText(productModel.getPrice());
            textFieldStock.setText(productModel.getStock());
            textFieldAlertEnding.setText(productModel.getAlertEnding());

            for (MeasureTypeModel measureTypeModel : comboBoxMeasureType.getItems()) {
                if (measureTypeModel.getId().equals(productModel.getMeasureTypeId())) {
                    comboBoxMeasureType.getSelectionModel().select(measureTypeModel);
                    break;
                }
            }
        }

        tableColumnSupplierCheck.setCellValueFactory(
            new PropertyValueFactory<>("checkBox")
        );
        tableColumnSupplierName.setCellValueFactory(
            new PropertyValueFactory<>("name")
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

                    return supplier.getName().toLowerCase().contains(
                        newValue.toLowerCase()
                    );
                });
            }
        ));

        this.sortedListSupplier = new SortedList<>(filteredList);
        sortedListSupplier.comparatorProperty().bind(
            tableViewSupplier.comparatorProperty()
        );

        tableViewSupplier.setItems(sortedListSupplier);

        if (this.productModel == null) {
            return;
        }

        ArrayList<ProductHasSupplierModel> productHasSupplierList = this.productModel.getSuppliers();

        if (productHasSupplierList == null) {
            return;
        }

        ArrayList supplierIds = new ArrayList();

        for (ProductHasSupplierModel productHasSupplierModel : productHasSupplierList) {
            supplierIds.add(productHasSupplierModel.getSupplierId());
        }

        for (int i = 0; i < tableViewSupplier.getItems().size(); i++) {
            if (supplierIds.contains(tableViewSupplier.getItems().get(i).getId())) {
                tableViewSupplier.getItems().get(i).getCheckBox().setSelected(true);
            }
        }
    }
}
