package model.entity;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import model.datalayer.DataLayer;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ProductModel extends DataLayer {

    private MeasureTypeModel currentMeasureTypeModel;

    private final ComboBox comboBoxAmount;

    public ProductModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "product";
        this.required = new String[]{
            "name",
            "price",
            "stock",
            "alert_ending"
        };

        this.comboBoxAmount = new ComboBox<>();
        comboBoxAmount.setPrefWidth(150);
    }

    public String getMeasureTypeId() {
        return (String) data.get("measure_type_id");
    }

    public void setMeasureTypeId(String measureTypeId) {
        data.put("measure_type_id", measureTypeId.trim());
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

    public String getPrice() {
        return (String) data.get("price");
    }

    public void setPrice(String price) {
        data.put("price", price.trim());
    }

    public String getPriceFull() {
        return String.format("R$ %.2f", Double.parseDouble(data.get("price").toString()));
    }

    public String getStock() {
        return (String) data.get("stock");
    }

    public String getStockFull() {
        this.currentMeasureTypeModel = (MeasureTypeModel) (
            new MeasureTypeModel(null)
        ).findByPrimaryKey(getMeasureTypeId());

        return treatStock(getStock());
    }

    public void setStock(String stock) {
        data.put("stock", stock.trim());
    }

    public String getAlertEnding() {
        return (String) data.get("alert_ending");
    }

    public String getAlertEndingFull() {
        return treatStock(getAlertEnding());
    }

    public void setAlertEnding(String alertEnding) {
        data.put("alert_ending", alertEnding.trim());
    }

    public ComboBox getComboBoxAmount() {
        ArrayList stock = new ArrayList();

        for (int i = 0; i <= parseInt(getStock()); i++) {
            stock.add(i);
        }

        comboBoxAmount.setItems(FXCollections.observableList(stock));
        return comboBoxAmount;
    }

    public ArrayList getSuppliers() {
        return (new ProductHasSupplierModel(null)).find(
            "product_id = '" + this.getId() + "'"
        ).fetchAll();
    }

    @Override
    public boolean save() {
        ProductModel productModel = new ProductModel(null);

        String generalTerms = (data.get("id") == null ? "" : " AND id != '" + getId() + "'");
        String terms = "name = '" + getName() + "'";

        // Verifica se o nome já existe
        if (productModel.find(terms + generalTerms).fetch() != null) {
            returnMessage.put("name", "Já existe");
            return false;
        }

        return super.save();
    }

    private String treatStock(String value) {
        if (currentMeasureTypeModel.getAcronymMaximum() == null) {
            return value + " (" + this.currentMeasureTypeModel.getAcronymMinimum() + ")";
        }

        double valueConverted = Double.parseDouble(value);

        if (valueConverted < 1000) {
            return String.format(
                "%.0f (%s)",
                valueConverted,
                this.currentMeasureTypeModel.getAcronymMinimum()
            );
        }

        return (valueConverted / 1000) + " (" + this.currentMeasureTypeModel.getAcronymMaximum() + ")";
    }
}
