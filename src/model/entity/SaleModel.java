package model.entity;

import model.datalayer.DataLayer;

import static java.lang.Integer.parseInt;

public class SaleModel extends DataLayer {

    public SaleModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "sale";
        this.required = new String[]{
            "customer_id",
            "payment_method_id",
            "date"
        };
    }

    public String getCustomerId() {
        return (String) data.get("customer_id");
    }

    public void setCustomerId(String customerId) {
        data.put("customer_id", customerId.trim());
    }

    public String getPaymentMethodId() {
        return (String) data.get("payment_method_id");
    }

    public void setPaymentMethodId(String paymentMethodId) {
        data.put("payment_method_id", paymentMethodId.trim());
    }

    public String getId() {
        return (String) data.get("id");
    }

    public void setId(String id) {
        data.put("id", id.trim());
    }

    public String getPrice() {
        return (String) data.get("price");
    }

    public void setPrice(String price) {
        data.put("price", price.trim());
    }

    public String getPriceFull() {
        return String.format("R$ %.2f", Double.parseDouble(data.get("price").toString()));
    }

    public String getDate() {
        return (String) data.get("date");
    }

    public void setDate(String date) {
        data.put("date", date.trim());
    }

    public String getCode() {
        return String.format("%07d", parseInt(data.get("id").toString()));
    }

    public String getCustomerName() {
        CustomerModel customerModel = (CustomerModel) (new CustomerModel(null)).find(
            "id = '" + getCustomerId() + "'"
        ).fetch();

        return customerModel.getName();
    }
}
