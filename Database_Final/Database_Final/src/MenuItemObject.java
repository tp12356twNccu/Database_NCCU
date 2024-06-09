public class MenuItemObject {
	private int menuID;
    private String item;
    private int price;
    private int cost;

    public MenuItemObject(int menuID, String item,int price,int cost) {
    	this.menuID = menuID;
        this.item = item;
        this.price = price;
        this.cost = cost;
    }
    
    public int getmenuID() {
    	return menuID;
    }

    public String getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public int getCost() {
        return cost;
    }
}