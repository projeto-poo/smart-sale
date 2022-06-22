package model.entity;

import model.datalayer.DataLayer;

public class UserModel extends DataLayer {

    public UserModel(String behaviorToSave) {
        super(behaviorToSave);

        this.table = "user";
        this.required = new String[]{
            "name",
            "username",
            "password"
        };
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

    public String getUsername() {
        return (String) data.get("username");
    }

    public void setUsername(String username) {
        data.put("username", username);
    }

    public String getPassword() {
        return (String) data.get("password");
    }

    public void setPassword(String password) {
        data.put("password", password);
    }
}
