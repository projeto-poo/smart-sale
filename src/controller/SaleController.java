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
import model.entity.SaleModel;

import java.util.ArrayList;

public class SaleController {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<SaleModel> tableViewSale;

    @FXML
    private TableColumn<SaleModel, String> tableColumnSaleCode;

    @FXML
    private TableColumn<SaleModel, String> tableColumnSaleCustomer;

    @FXML
    private TableColumn<SaleModel, String> tableColumnSalePrice;

    @FXML
    private TableColumn<SaleModel, String> tableColumnSaleData;

    @FXML
    private ObservableList observableListSale;

    @FXML
    private SortedList<SaleModel> sortedListSale;

    @FXML
    protected void buttonAddOnAction() {
        Main.changeScreen("sale-form");
    }

    @FXML
    protected void buttonDeleteOnAction() {
        Table.removeModelItem(
            tableViewSale,
            observableListSale,
            sortedListSale
        );
    }

    @FXML
    protected void buttonEditOnAction() {
        Table.editModelItem(tableViewSale, "sale");
    }

    @FXML
    protected void initialize() {
        InternalScreenController.setPageTitle("Vendas");

        tableColumnSaleCode.setCellValueFactory(
            new PropertyValueFactory<>("code")
        );
        tableColumnSaleCustomer.setCellValueFactory(
            new PropertyValueFactory<>("customerName")
        );
        tableColumnSalePrice.setCellValueFactory(
            new PropertyValueFactory<>("totalItems")
        );
        tableColumnSalePrice.setCellValueFactory(
            new PropertyValueFactory<>("priceFull")
        );
        tableColumnSaleData.setCellValueFactory(
            new PropertyValueFactory<>("date")
        );

        loadTableContentSale();
    }

    /**
     * Lista todos os pedidos realizados na tabela e ativa o campo de busca
     * para filtrar os dados ao digitar.
     */
    private void loadTableContentSale() {
        ArrayList<SaleModel> saleList = (new SaleModel(null)).find().order("id DESC").fetchAll();

        if (saleList == null) {
            return;
        }

        this.observableListSale = FXCollections.observableList(saleList);
        tableViewSale.setItems(observableListSale);

        FilteredList<SaleModel> filteredList = new FilteredList<>(
            tableViewSale.getItems()
        );

        textFieldSearch.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                filteredList.setPredicate(sale -> {
                    if (newValue.isEmpty()) {
                        return true;
                    }

                    String newValueLowerCase = newValue.toLowerCase();

                    if (sale.getCode().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (sale.getCustomerName().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    } else if (sale.getPriceFull().toLowerCase().contains(newValueLowerCase)) {
                        return true;
                    }

                    return sale.getDate().toLowerCase().contains(newValueLowerCase);
                });
            }
        ));

        this.sortedListSale = new SortedList<>(filteredList);
        sortedListSale.comparatorProperty().bind(
            tableViewSale.comparatorProperty()
        );

        tableViewSale.setItems(sortedListSale);
    }
}
