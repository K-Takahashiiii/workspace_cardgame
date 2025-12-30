package bean;

import java.io.Serializable;

public class Organizer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int managementNum;
    private String storeName;
    private String representativeName;
    private String password;
    private String name;

    public Organizer() {}

    public Organizer(int managementNum, String storeName, String representativeName, String password, String name) {
        this.managementNum = managementNum;
        this.storeName = storeName;
        this.representativeName = representativeName;
        this.password = password;
        this.name = name;
    }

    public int getManagementNum() {
        return managementNum;
    }
    public void setManagementNum(int managementNum) {
        this.managementNum = managementNum;
    }

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }
    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
