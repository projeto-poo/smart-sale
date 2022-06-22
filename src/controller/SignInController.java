package controller;

import helper.Form;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.entity.UserModel;

public class SignInController {

    @FXML
    private Label labelFeedbackUsername;

    @FXML
    private Label labelFeedbackPassword;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

    @FXML
    protected void buttonCloseWindowOnAction() {
        Main.closeWindow();
    }

    @FXML
    protected void buttonMinimizeWindowOnAction() {
        Main.minimizeWindow();
    }

    @FXML
    protected void buttonSubmitOnAction() {
        boolean block = false;

        Form.resetFeedback(labelFeedbackUsername, labelFeedbackPassword);
        Form.resetField(textFieldUsername, passwordFieldPassword);

        if (textFieldUsername.getText().isEmpty()) {
            Form.invalidField(
                textFieldUsername,
                labelFeedbackUsername,
                "Obrigatório"
            );

            block = true;
        }

        if (passwordFieldPassword.getText().isEmpty()) {
            Form.invalidField(
                passwordFieldPassword,
                labelFeedbackPassword,
                "Obrigatório"
            );

            block = true;
        }

        if (block) {
            return;
        }

        UserModel userModel = (UserModel) (new UserModel(null)).find(
            "username = '" + textFieldUsername.getText() + "'"
        ).fetch();

        if (userModel == null) {
            Form.invalidField(
                textFieldUsername,
                labelFeedbackUsername,
                "Usuário não encontrado"
            );

            return;
        }

        if (!userModel.getPassword().equals(passwordFieldPassword.getText())) {
            Form.invalidField(
                passwordFieldPassword,
                labelFeedbackPassword,
                "Senha inválida"
            );

            return;
        }

        Main.getStage().setUserData(userModel);
        Main.showInternalScreen();
    }
}
