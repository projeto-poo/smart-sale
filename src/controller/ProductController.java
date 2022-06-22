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
import model.entity.ProductModel;

import java.util.ArrayList;

public class ProductController {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<ProductModel> tableViewProduct;

    @FXML
    private TableColumn<ProductModel, String> tableColumnProductName;

    @FXML
    private TableColumn<ProductModel, String> tableColumnProductPrice;

    @FXML
    private TableColumn<ProductModel, String> tableColumnProductStock;

    @FXML
    private TableColumn<ProductModel, String> tableColumnProductAlertEnding;

    @FXML
    private ObservableList<ProductModel> observableListProduct;

    @FXML
    private SortedList<ProductModel> sortedListProduct;

    @FXML
    protected void buttonAddOnAction() {
        Main.changeScreen("product-form");
    }

    @FXML
    protected void buttonDeleteOnAction() {
        Table.removeModelItem(
            tableViewProduct,
            observableListProduct,
            sortedListProduct
        );
    }

    @FXML
    protected void buttonEditOnAction() {
        Table.editModelItem(tableViewProduct, "product");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Produtos");

        tableColumnProductName.setCellValueFactory(
            new PropertyValueFactory<>("name")
        );
        tableColumnProductPrice.setCellValueFactory(
            new PropertyValueFactory<>("priceFull")
        );
        tableColumnProductStock.setCellValueFactory(
            new PropertyValueFactory<>("stockFull")
        );
        tableColumnProductAlertEnding.setCellValueFactory(
            new PropertyValueFactory<>("alertEndingFull")
        );

        loadTableContentProduct();
    }

    /**
     * Lista todos os Produtos cadastrados na tabela e ativa o campo de busca
     * para filtrar os dados ao digitar.
     */
    private void loadTableContentProduct() {
        ArrayList productList = (new ProductModel(null)).find().order("id DESC").fetchAll();

        if (productList == null) {
            return;
        }

        this.observableListProduct = FXCollections.observableList(productList);
        tableViewProduct.setItems(observableListProduct);

        FilteredList<ProductModel> filteredList = new FilteredList<>(
            tableViewProduct.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(product -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    String newValueLowerCase = newValue.toLowerCase();

                    if (product.getName().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (product.getPriceFull().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (product.getStockFull().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    }

                    return product.getAlertEndingFull().toLowerCase().contains(newValueLowerCase);
                });
            }
        ));

        this.sortedListProduct = new SortedList<>(filteredList);
        sortedListProduct.comparatorProperty().bind(
            tableViewProduct.comparatorProperty()
        );

        tableViewProduct.setItems(sortedListProduct);
    }
}
