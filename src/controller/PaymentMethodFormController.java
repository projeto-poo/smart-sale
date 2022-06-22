package controller;

import helper.Form;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entity.PaymentMethodModel;

public class PaymentMethodFormController {

    private PaymentMethodModel paymentMethodModel;

    @FXML
    private Label labelFeedbackName;

    @FXML
    private TextField textFieldName;

    @FXML
    protected void buttonCancelOnAction() {
        Main.changeScreen("payment-method");
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(labelFeedbackName);
        Form.resetField(textFieldName);

        if (textFieldName.getText().isEmpty()) {
            Form.invalidField(textFieldName, labelFeedbackName, "Obrigatório");
            block = true;
        }

        if (block) {
            return;
        }

        PaymentMethodModel paymentMethodModel = (this.paymentMethodModel != null ? this.paymentMethodModel : new PaymentMethodModel(null));
        paymentMethodModel.setName(textFieldName.getText());

        boolean save = paymentMethodModel.save();

        if (paymentMethodModel.fail() != null) {
            System.out.println(paymentMethodModel.fail().getMessage());
            return;
        }

        if (!save) {
            if (paymentMethodModel.returnMessage().get("name") != null) {
                Form.invalidField(
                    textFieldName,
                    labelFeedbackName,
                    paymentMethodModel.returnMessage().get("name").toString()
                );
            }

            return;
        }

        Main.changeScreen("payment-method");
    }

    @FXML
    protected void initialize() {
        if (Main.getStage().getUserData() == null) {
            InternalScreenController.setPageTitle("Métodos de pagamentos ● Adicionar novo");
        } else {
            this.paymentMethodModel = (PaymentMethodModel) Main.getStage().getUserData();
            Main.getStage().setUserData(null);

            InternalScreenController.setPageTitle(
                "Métodos de pagamentos ● " + paymentMethodModel.getName()
            );

            textFieldName.setText(paymentMethodModel.getName());
        }
    }
}
