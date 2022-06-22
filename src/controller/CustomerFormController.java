package controller;

import helper.Form;
import helper.TextFieldMask;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entity.CustomerModel;

public class CustomerFormController {

    private CustomerModel customerModel;

    private TextFieldMask textFieldMaskPhone;
    private TextFieldMask textFieldMaskZipcode;

    @FXML
    private Label labelFeedbackName;

    @FXML
    private Label labelFeedbackPhone;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPhone;

    @FXML
    private Label labelFeedbackZipcode;

    @FXML
    private Label labelFeedbackAddress;

    @FXML
    private TextField textFieldZipcode;

    @FXML
    private TextField textFieldAddress;

    @FXML
    protected void buttonCancelOnAction() {
        Main.changeScreen("customer");
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(
            labelFeedbackName,
            labelFeedbackPhone,
            labelFeedbackZipcode,
            labelFeedbackAddress
        );
        Form.resetField(
            textFieldName,
            textFieldPhone,
            textFieldZipcode,
            textFieldAddress
        );

        if (textFieldName.getText().isEmpty()) {
            Form.invalidField(textFieldName, labelFeedbackName, "Obrigatório");
            block = true;
        }

        if (textFieldPhone.getText().isEmpty()) {
            Form.invalidField(textFieldPhone, labelFeedbackPhone, "Obrigatório");
            block = true;
        }

        if (textFieldZipcode.getText().isEmpty()) {
            Form.invalidField(textFieldZipcode, labelFeedbackZipcode, "Obrigatório");
            block = true;
        }

        if (textFieldAddress.getText().isEmpty()) {
            Form.invalidField(textFieldAddress, labelFeedbackAddress, "Obrigatório");
            block = true;
        }

        if (block) {
            return;
        }

        CustomerModel customerModel = (this.customerModel != null ? this.customerModel : new CustomerModel(null));
        customerModel.setName(textFieldName.getText());
        customerModel.setPhone(textFieldPhone.getText());
        customerModel.setZipcode(textFieldZipcode.getText());
        customerModel.setAddress(textFieldAddress.getText());

        boolean save = customerModel.save();

        if (customerModel.fail() != null) {
            System.out.println(customerModel.fail().getMessage());
            return;
        }

        if (!save) {
            if (customerModel.returnMessage().get("name") != null) {
                Form.invalidField(
                    textFieldName,
                    labelFeedbackName,
                    customerModel.returnMessage().get("name").toString()
                );
            }

            if (customerModel.returnMessage().get("phone") != null) {
                Form.invalidField(
                    textFieldPhone,
                    labelFeedbackPhone,
                    customerModel.returnMessage().get("phone").toString()
                );
            }

            if (customerModel.returnMessage().get("zipcode") != null) {
                Form.invalidField(
                    textFieldZipcode,
                    labelFeedbackZipcode,
                    customerModel.returnMessage().get("zipcode").toString()
                );
            }

            return;
        }

        Main.changeScreen("customer");
    }

    @FXML
    protected void textFieldPhoneOnKeyReleased() {
        textFieldMaskPhone.setMask(
            (textFieldPhone.getLength() < 15 ? "(##) ####-####" : "(##) #####-####")
        );
        textFieldMaskPhone.formatter();
    }

    @FXML
    protected void textFieldZipcodeOnKeyReleased() {
        textFieldMaskZipcode.setMask("#####-###");
        textFieldMaskZipcode.formatter();
    }

    @FXML
    protected void initialize() {
        if (Main.getStage().getUserData() == null) {
            InternalScreenController.setPageTitle("Clientes ● Adicionar novo");
        } else {
            this.customerModel = (CustomerModel) Main.getStage().getUserData();
            Main.getStage().setUserData(null);

            InternalScreenController.setPageTitle(
                "Clientes ● " + customerModel.getName()
            );

            textFieldName.setText(customerModel.getName());
            textFieldPhone.setText(customerModel.getPhone());
            textFieldZipcode.setText(customerModel.getZipcode());
            textFieldAddress.setText(customerModel.getAddress());
        }

        this.textFieldMaskPhone = new TextFieldMask();
        textFieldMaskPhone.setTextField(textFieldPhone);
        textFieldMaskPhone.setValidCharacters("0123456789");
        textFieldPhoneOnKeyReleased();

        this.textFieldMaskZipcode = new TextFieldMask();
        textFieldMaskZipcode.setTextField(textFieldZipcode);
        textFieldMaskZipcode.setValidCharacters("0123456789");
        textFieldZipcodeOnKeyReleased();
    }
}
