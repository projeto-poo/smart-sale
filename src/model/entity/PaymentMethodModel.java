package model.entity;

import model.datalayer.DataLayer;

public class PaymentMethodModel extends DataLayer {

    public PaymentMethodModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "payment_method";
        this.required = new String[]{
            "name"
        };
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return (String) data.get("id");
    }

    public void setId(String id) {
        data.put("id", id.trim());
    }

    public String getName() {
        return (String) data.get("name");
    }

    public void setName(String name) {
        data.put("name", name.trim());
    }

    @Override
    public boolean save() {
        PaymentMethodModel paymentMethodModel = new PaymentMethodModel(null);

        String generalTerms = (data.get("id") == null ? "" : " AND id != '" + getId() + "'");
        String terms = "name = '" + getName() + "'";

        // Verifica se o nome já existe
        if (paymentMethodModel.find(terms + generalTerms).fetch() != null) {
            returnMessage.put("name", "Já existe");
            return false;
        }

        return super.save();
    }
}
