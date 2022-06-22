package model.entity;

import model.datalayer.DataLayer;

public class SaleItemModel extends DataLayer {

    public SaleItemModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "sale_item";
        this.required = new String[]{
            "sale_id",
            "name",
            "price",
            "amount"
        };
    }

    public String getSaleId() {
        return (String) data.get("sale_id");
    }

    public void setSaleId(String saleId) {
        data.put("sale_id", saleId);
    }

    public String getId() {
        return (String) data.get("id");
    }

    public void setId(String id) {
        data.put("id", id);
    }

    public String getName() {
        return (String) data.get("name");
    }

    public void setName(String name) {
        data.put("name", name);
    }

    public String getPrice() {
        return (String) data.get("price");
    }

    public void setPrice(String price) {
        data.put("price", price);
    }

    public String getAmount() {
        return (String) data.get("amount");
    }

    public void setAmount(String amount) {
        data.put("amount", amount);
    }
}
