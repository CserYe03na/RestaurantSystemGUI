
package OOProject;

public class MenuItem {
    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String description;
    
    public MenuItem(String id, String name, double price, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}
