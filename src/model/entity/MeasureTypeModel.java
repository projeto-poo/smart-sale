package model.entity;

import model.datalayer.DataLayer;

public class MeasureTypeModel extends DataLayer {

    public MeasureTypeModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "measure_type";
        this.required = new String[]{
            "name",
            "acronym_minimum"
        };
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getAcronymMinimum() + ")";
    }

    public String getId() {
        return (String) this.data.get("id");
    }

    public void setId(String id) {
        this.data.put("id", id);
    }

    public String getName() {
        return (String) this.data.get("name");
    }

    public void setName(String name) {
        this.data.put("name", name);
    }

    public String getAcronymMinimum() {
        return (String) this.data.get("acronym_minimum");
    }

    public void setAcronymMinimum(String acronymMinimum) {
        this.data.put("acronym_minimum", acronymMinimum);
    }

    public String getAcronymMaximum() {
        return (String) this.data.get("acronym_maximum");
    }

    public void setAcronymMaximum(String acronymMaximum) {
        this.data.put("acronym_maximum", acronymMaximum);
    }
}
