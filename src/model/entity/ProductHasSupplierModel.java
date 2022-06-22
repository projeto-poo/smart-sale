package model.entity;

import model.datalayer.DataLayer;

public class ProductHasSupplierModel extends DataLayer {

    public ProductHasSupplierModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "product_has_supplier";
        this.required = new String[]{
            "product_id",
            "supplier_id"
        };
    }

    public String getProductId() {
        return (String) data.get("product_id");
    }

    public void setProductId(String productId) {
        data.put("product_id", productId);
    }

    public String getSupplierId() {
        return (String) data.get("supplier_id");
    }

    public void setSupplierId(String supplierId) {
        data.put("supplier_id", supplierId);
    }

    public String getId() {
        return (String) data.get("id");
    }

    public void setId(String id) {
        data.put("id", id);
    }
}
