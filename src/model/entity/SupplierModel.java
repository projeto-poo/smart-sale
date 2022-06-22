package model.entity;

import helper.Is;
import javafx.scene.control.CheckBox;
import model.datalayer.DataLayer;

public class SupplierModel extends DataLayer {

    private final CheckBox checkBox;

    public SupplierModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "supplier";
        this.required = new String[]{
            "name",
            "phone"
        };

        this.checkBox = new CheckBox();
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

    public String getPhone() {
        return (String) data.get("phone");
    }

    public void setPhone(String phone) {
        data.put("phone", phone.trim());
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public boolean save() {
        if (!Is.phone(getPhone())) {
            returnMessage.put("phone", "Formato inválido");
            return false;
        }

        SupplierModel supplierModel = new SupplierModel(null);

        String generalTerms = (data.get("id") == null ? "" : " AND id != '" + getId() + "'");
        String terms = "name = '" + getName() + "'";

        // Verifica se o nome já existe
        if (supplierModel.find(terms + generalTerms).fetch() != null) {
            returnMessage.put("name", "Já existe");
            return false;
        }

        return super.save();
    }
}
