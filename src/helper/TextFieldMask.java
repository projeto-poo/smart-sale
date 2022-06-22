package helper;

import javafx.scene.control.TextField;

public class TextFieldMask {

    private TextField textField;

    private String validCharacters;
    private String mask;

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public void setValidCharacters(String validCharacters) {
        this.validCharacters = validCharacters;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    /**
     * Método responsável por aplicar a máscara.
     * Este método deve ser chamado dentro de um evento de keyUp.
     */
    public void formatter() {
        if (textField.getLength() == 0) {
            return;
        }

        StringBuilder onlyValidCharacters = new StringBuilder();
        StringBuilder masked = new StringBuilder();

        for (int i = 0; i < textField.getLength(); i++) {
            if (validCharacters.indexOf(textField.getText().charAt(i)) != -1) {
                onlyValidCharacters.append(textField.getText().charAt(i));
            }
        }

        int k = 0;

        for (
            int i = 0;
            i <= mask.length() - 1 && k < onlyValidCharacters.length();
            i++
        ) {
            if (mask.charAt(i) == '#') {
                try {
                    masked.append(onlyValidCharacters.charAt(k++));
                } catch (IndexOutOfBoundsException ignored) {
                }
            } else {
                try {
                    masked.append(mask.charAt(i));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        textField.setText(masked.toString());

        for (int i = 0; i < textField.getLength(); i++) {
            textField.forward();
        }
    }
}
