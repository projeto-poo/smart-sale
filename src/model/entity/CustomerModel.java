package model.entity;

import helper.Is;
import javafx.scene.control.CheckBox;
import model.datalayer.DataLayer;

public class CustomerModel extends DataLayer {

    private final CheckBox checkBox;

    public CustomerModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "customer";
        this.required = new String[]{
            "name",
            "phone",
            "zipcode",
            "address"
        };

        this.checkBox = new CheckBox();
    }

    @Override
    public String toString() {
        return getPhone() + " - " + getName();
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

    public String getZipcode() {
        return (String) data.get("zipcode");
    }

    public void setZipcode(String zipcode) {
        data.put("zipcode", zipcode.trim());
    }

    public String getAddress() {
        return (String) data.get("address");
    }

    public void setAddress(String address) {
        data.put("address", address.trim());
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public boolean save() {
        boolean block = false;

        if (!Is.phone(getPhone())) {
            returnMessage.put("phone", "Formato inválido");
            block = true;
        }

        if (!Is.zipcode(getZipcode())) {
            returnMessage.put("zipcode", "CEP inválido");
            block = true;
        }

        if (block) {
            return false;
        }

        CustomerModel customerModel = new CustomerModel(null);

        String generalTerms = (data.get("id") == null ? "" : " AND id != '" + getId() + "'");
        String terms = "name = '" + getName() + "'";

        // Verifica se o nome já existe
        if (customerModel.find(terms + generalTerms).fetch() != null) {
            returnMessage.put("name", "Já existe");
            return false;
        }

        return super.save();
    }
}
