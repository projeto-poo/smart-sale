package helper;

public class Is {

    /**
     * Valida se uma determinada string possui o total de digitos númericos com
     * DDD de um telefone normal (não-celular) ou de um celular.
     *
     * @param phone String com um número de telefone.
     * @return True se for válido, false caso contrário.
     */
    public static boolean phone(String phone) {
        phone = phone.replaceAll("[^0-9]", "");
        return phone.length() == 10 || phone.length() == 11;
    }

    /**
     * Valida o formato de um CEP brasileiro.
     *
     * @param zipcode String com um cep.
     * @return True se for válido, false caso contrário.
     */
    public static boolean zipcode(String zipcode) {
        return zipcode.replaceAll("[^0-9]", "").length() == 8;
    }
}
