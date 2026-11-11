package entity;

public class Menu {

    public Menu(String description, int ID, int price, String itemName) {
        this.description = description;
        this.ID = ID;
        this.price = price;
        this.itemName = itemName;
    }

    private int ID;
    private int price;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String description;
    private String itemName;
}


