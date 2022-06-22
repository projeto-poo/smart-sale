package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static java.lang.Integer.parseInt;

public class Form {

    /**
     * Atributos com classes de validação
     */
    private static final String isInvalidClass = "is-invalid";
    private static final String isValidClass = "is-valid";

    /**
     * Aplica restrição a um textField para receber somente números.
     *
     * @param textField TextField que irá receber a restrição.
     */
    public static void onlyNumber(TextField textField) {
        textField.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                try {
                    if (!newValue.equals("")) {
                        parseInt(newValue);
                    }
                } catch (Exception exception) {
                    textField.setText(oldValue);
                }
            }
        ));
    }


    /**
     * Aplica restrição a um textField para receber somente caracteres
     * não-numéricos.
     *
     * @param textField TextField que irá receber a restrição.
     */
    public static void onlyCharacter(TextField textField) {
        textField.textProperty().addListener((
            (observableValue, oldValue, newValue) -> {
                try {
                    if (!newValue.equals("")) {
                        parseInt(newValue.substring(newValue.length() - 1));
                        textField.setText(oldValue);
                    }
                } catch (Exception exception) {
                    textField.setText(newValue);
                }
            }
        ));
    }

    /**
     * Retorna o componente Label de feedback para o estado inicial removendo as
     * classes de estilização de validação e texto.
     *
     * @param labels Objeto composto de vários componentes Label de feedbacks.
     */
    public static void resetFeedback(Label... labels) {
        for (Label label : labels) {
            label.getStyleClass().remove(isInvalidClass);
            label.getStyleClass().remove(isValidClass);
            label.setText("");
        }
    }

    /**
     * Remove as classes de estilização de validação de um componente TextField.
     *
     * @param textFields Objeto composto de vários componentes TextField.
     */
    public static void resetField(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.getStyleClass().remove(isInvalidClass);
            textField.getStyleClass().remove(isValidClass);
        }
    }

    /**
     * Remove as classes de estilização de validação de um componente
     * PasswordField.
     *
     * @param passwordFields Objeto composto de vários componentes
     * passwordField.
     */
    public static void resetField(PasswordField... passwordFields) {
        for (PasswordField passwordField : passwordFields) {
            passwordField.getStyleClass().remove(isInvalidClass);
            passwordField.getStyleClass().remove(isValidClass);
        }
    }

    /**
     * Remove as classes de estilização de validação de um componente ComboBox.
     *
     * @param comboBoxes Objeto composto de vários componentes comboBox.
     */
    public static void resetField(ComboBox... comboBoxes) {
        for (ComboBox comboBox : comboBoxes) {
            comboBox.getStyleClass().remove(isInvalidClass);
            comboBox.getStyleClass().remove(isValidClass);
        }
    }

    /**
     * Aplica a um componente Label de feedback uma mensagem e uma classe de
     * estilização de validação.
     *
     * @param label Componente que será modificado.
     * @param message Mensagem que será exibida.
     * @param validationClass Classe de estilização de validação.
     */
    private static void highlightFeedback(
        Label label,
        String message,
        String validationClass
    ) {
        label.getStyleClass().add(validationClass);
        label.setText(message);
    }

    /**
     * Aplica a um componente TextField uma classe de estilização de validação.
     *
     * @param textField Componente que será modificado.
     * @param validationClass Classe de estilização de validação.
     */
    private static void highlightField(
        TextField textField,
        String validationClass
    ) {
        textField.getStyleClass().add(validationClass);
    }

    /**
     * Aplica a um componente PasswordField uma classe de estilização de
     * validação.
     *
     * @param passwordField Componente que será modificado.
     * @param validationClass Classe de estilização de validação.
     */
    private static void highlightField(
        PasswordField passwordField,
        String validationClass
    ) {
        passwordField.getStyleClass().add(validationClass);
    }

    /**
     * Aplica a um componente ComboBox uma classe de estilização de validação.
     *
     * @param comboBox Componente que será modificado.
     * @param validationClass Classe de estilização de validação.
     */
    private static void highlightField(
        ComboBox comboBox,
        String validationClass
    ) {
        comboBox.getStyleClass().add(validationClass);
    }

    /**
     * Destaca um componente TextField com aparência de campo inválido.
     *
     * @param textField Componente que será destacado.
     */
    public static void invalidField(TextField textField) {
        highlightField(textField, isInvalidClass);
    }

    /**
     * Destaca um componente TextField e Label de feedback com aparência de
     * campo inválido exibindo uma mensagem de erro no feedback.
     *
     * @param textField Componente TextField que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void invalidField(
        TextField textField,
        Label feedback,
        String message
    ) {
        highlightField(textField, isInvalidClass);
        highlightFeedback(feedback, message, isInvalidClass);
    }

    /**
     * Destaca um componente PasswordField com aparência de campo inválido.
     *
     * @param passwordField Componente que será destacado.
     */
    public static void invalidField(PasswordField passwordField) {
        highlightField(passwordField, isInvalidClass);
    }

    /**
     * Destaca um componente PasswordField e Label de feedback com aparência de
     * campo inválido exibindo uma mensagem de erro no feedback.
     *
     * @param passwordField Componente PasswordField que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void invalidField(
        PasswordField passwordField,
        Label feedback,
        String message
    ) {
        highlightField(passwordField, isInvalidClass);
        highlightFeedback(feedback, message, isInvalidClass);
    }

    /**
     * Destaca um componente ComboBox com aparência de campo inválido.
     *
     * @param comboBox Componente que será destacado.
     */
    public static void invalidField(ComboBox comboBox) {
        highlightField(comboBox, isInvalidClass);
    }

    /**
     * Destaca um componente ComboBox e Label de feedback com aparência de campo
     * inválido exibindo uma mensagem de erro no feedback.
     *
     * @param comboBox Componente ComboBox que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void invalidField(
        ComboBox comboBox,
        Label feedback,
        String message
    ) {
        highlightField(comboBox, isInvalidClass);
        highlightFeedback(feedback, message, isInvalidClass);
    }

    /**
     * Destaca um componente TextField com aparência de campo válido.
     *
     * @param textField Componente que será destacado.
     */
    public static void validField(TextField textField) {
        highlightField(textField, isValidClass);
    }

    /**
     * Destaca um componente TextField e Label de feedback com aparência de
     * campo válido exibindo uma mensagem de sucesso no feedback.
     *
     * @param textField Componente TextField que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void validField(
        TextField textField,
        Label feedback,
        String message
    ) {
        highlightField(textField, isValidClass);
        highlightFeedback(feedback, message, isValidClass);
    }

    /**
     * Destaca um componente PasswordField com aparência de campo válido.
     *
     * @param passwordField Componente que será destacado.
     */
    public static void validField(PasswordField passwordField) {
        highlightField(passwordField, isValidClass);
    }

    /**
     * Destaca um componente PasswordField e Label de feedback com aparência de
     * campo válido exibindo uma mensagem de sucesso no feedback.
     *
     * @param passwordField Componente PasswordField que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void validField(
        PasswordField passwordField,
        Label feedback,
        String message
    ) {
        highlightField(passwordField, isValidClass);
        highlightFeedback(feedback, message, isValidClass);
    }

    /**
     * Destaca um componente ComboBox com aparência de campo válido.
     *
     * @param comboBox Componente que será destacado.
     */
    public static void validField(ComboBox comboBox) {
        highlightField(comboBox, isValidClass);
    }

    /**
     * Destaca um componente ComboBox e Label de feedback com aparência de campo
     * válido exibindo uma mensagem de sucesso no feedback.
     *
     * @param comboBox Componente ComboBox que será destacado.
     * @param feedback Componente Label que será destacado.
     * @param message Mensagem que será aplicada ao componente de feedback.
     */
    public static void validField(
        ComboBox comboBox,
        Label feedback,
        String message
    ) {
        highlightField(comboBox, isValidClass);
        highlightFeedback(feedback, message, isValidClass);
    }
}
