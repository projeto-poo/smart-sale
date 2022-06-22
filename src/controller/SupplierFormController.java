package controller;

import helper.Form;
import helper.TextFieldMask;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entity.SupplierModel;

public class SupplierFormController {

    private SupplierModel supplierModel;

    private TextFieldMask textFieldMaskPhone;

    @FXML
    private Label labelFeedbackName;

    @FXML
    private Label labelFeedbackPhone;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPhone;

    @FXML
    protected void buttonCancelOnAction() {
        Main.changeScreen("supplier");
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(labelFeedbackName, labelFeedbackPhone);
        Form.resetField(textFieldName, textFieldPhone);

        if (textFieldName.getText().isEmpty()) {
            Form.invalidField(textFieldName, labelFeedbackName, "Obrigatório");
            block = true;
        }

        if (textFieldPhone.getText().isEmpty()) {
            Form.invalidField(textFieldPhone, labelFeedbackPhone, "Obrigatório");
            block = true;
        }

        if (block) {
            return;
        }

        SupplierModel supplierModel = (this.supplierModel != null ? this.supplierModel : new SupplierModel(null));
        supplierModel.setName(textFieldName.getText());
        supplierModel.setPhone(textFieldPhone.getText());

        boolean save = supplierModel.save();

        if (supplierModel.fail() != null) {
            System.out.println(supplierModel.fail().getMessage());
            return;
        }

        if (!save) {
            if (supplierModel.returnMessage().get("name") != null) {
                Form.invalidField(
                    textFieldName,
                    labelFeedbackName,
                    supplierModel.returnMessage().get("name").toString()
                );
            }

            if (supplierModel.returnMessage().get("phone") != null) {
                Form.invalidField(
                    textFieldPhone,
                    labelFeedbackPhone,
                    supplierModel.returnMessage().get("phone").toString()
                );
            }

            return;
        }

        Main.changeScreen("supplier");
    }

    @FXML
    protected void textFieldPhoneOnKeyReleased() {
        textFieldMaskPhone.setMask(
            (textFieldPhone.getLength() < 15 ? "(##) ####-####" : "(##) #####-####")
        );
        textFieldMaskPhone.formatter();
    }

    @FXML
    protected void initialize() {
        if (Main.getStage().getUserData() == null) {
            InternalScreenController.setPageTitle("Fornecedores ● Adicionar novo");
        } else {
            this.supplierModel = (SupplierModel) Main.getStage().getUserData();
            Main.getStage().setUserData(null);

            InternalScreenController.setPageTitle(
                "Fornecedores ● " + supplierModel.getName()
            );

            textFieldName.setText(supplierModel.getName());
            textFieldPhone.setText(supplierModel.getPhone());
        }

        this.textFieldMaskPhone = new TextFieldMask();
        textFieldMaskPhone.setTextField(textFieldPhone);
        textFieldMaskPhone.setValidCharacters("0123456789");
        textFieldPhoneOnKeyReleased();
    }
}
