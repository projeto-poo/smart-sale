package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.entity.UserModel;

public class InternalScreenController {

    /**
     * Atributo utilizado para obter o objeto de labelPageTitle com modificador
     * de acesso estático para atualizar o título da página através do método
     * estático setPageTitle().
     */
    private static Label pageTitle;

    @FXML
    private Label labelPageTitle;

    @FXML
    private Label labelSidenavUserName;

    @FXML
    private Button buttonCurrentlyActive;

    @FXML
    private Button buttonSidenavNotification;

    @FXML
    private Button buttonSidenavItemHome;

    @FXML
    private Button buttonSidenavItemSupplier;

    @FXML
    private Button buttonSidenavItemProduct;

    @FXML
    private Button buttonSidenavItemSale;

    @FXML
    private Button buttonSidenavItemPaymentMethod;

    @FXML
    private Button buttonSidenavItemCustomer;

    @FXML
    private Button buttonSidenavItemDeveloper;

    @FXML
    protected void buttonCloseWindowOnAction() {
        Main.closeWindow();
    }

    @FXML
    protected void buttonMinimizeWindowOnAction() {
        Main.minimizeWindow();
    }

    @FXML
    protected void buttonSidenavItemCustomerOnAction() {
        Main.changeScreen("customer");
        toggleSidenavItemActive(buttonSidenavItemCustomer);
    }

    @FXML
    protected void buttonSidenavItemDeveloperOnAction() {
        Main.changeScreen("development");
        toggleSidenavItemActive(buttonSidenavItemDeveloper);
    }

    @FXML
    protected void buttonSidenavItemHomeOnAction() {
        Main.changeScreen("home");
        toggleSidenavItemActive(buttonSidenavItemHome);
    }

    @FXML
    protected void buttonSidenavItemPaymentMethodOnAction() {
        Main.changeScreen("payment-method");
        toggleSidenavItemActive(buttonSidenavItemPaymentMethod);
    }

    @FXML
    protected void buttonSidenavItemProductOnAction() {
        Main.changeScreen("product");
        toggleSidenavItemActive(buttonSidenavItemProduct);
    }

    @FXML
    protected void buttonSidenavItemSaleOnAction() {
        Main.changeScreen("sale");
        toggleSidenavItemActive(buttonSidenavItemSale);
    }

    @FXML
    protected void buttonSidenavItemSupplierOnAction() {
        Main.changeScreen("supplier");
        toggleSidenavItemActive(buttonSidenavItemSupplier);
    }

    @FXML
    protected void buttonSidenavNotificationOnAction() {

    }

    @FXML
    protected void initialize() {
        InternalScreenController.pageTitle = labelPageTitle;

        UserModel userModel = (UserModel) Main.getStage().getUserData();
        labelSidenavUserName.setText(userModel.getName());

        Main.getStage().setUserData(null);

        this.buttonCurrentlyActive = buttonSidenavItemHome;
    }

    /**
     * Define o título principal da janela interna do sistema.
     *
     * @param text Título da página.
     */
    public static void setPageTitle(String text) {
        pageTitle.setText(text);
    }

    /**
     * Troca o estado de ativo/não dos botões de navegação da sidenav.
     *
     * @param button O botão que foi clicado por último.
     */
    private void toggleSidenavItemActive(Button button) {
        if (button.getStyleClass().contains("sidenav-menu-item-active")) {
            return;
        }

        buttonCurrentlyActive.getStyleClass().remove("sidenav-menu-item-active");
        button.getStyleClass().add("sidenav-menu-item-active");

        this.buttonCurrentlyActive = button;
    }
}
