package OOProject;

public class Membership {
	private String restaurantId;
    private String memberId;
    private String name;
    private String phoneNumber;
    private String preference;
    private String address;
    private int credits;
    private String membershipLevel;

    public Membership(String restaurantId, String memberId, String name, String phoneNumber) {
    	this.restaurantId = restaurantId;
        this.memberId = memberId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preference = null;
        this.address = null;
        this.credits = 0;
        this.membershipLevel = "Standard";
    }
    
    public Membership(String restaurantId, String memberId, String name, String phoneNumber, String preference, String address) {
    	this.restaurantId = restaurantId;
        this.memberId = memberId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preference = preference;
        this.address = address;
        this.credits = 0;
        this.membershipLevel = "Standard";
    }

    // Getters
    public String getRestaurantId() {return restaurantId;}
    public String getMemberId() { return memberId;}
    public String getName() { return name;}
    public String getPhoneNumber() { return phoneNumber;}
    public String getPreference() { return preference;}
    public String getAddress() {return address;}
    public int getCredits() { return credits;}
    public String getMembershipLevel() { return membershipLevel;}

    // Setters
    public void setName(String name) { this.name = name;}
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}
    public void setPreference(String preference) { this.preference = preference.replace(",", ";");}
    public void setAddress(String address) {this.address = address.replace(",", ";"); }
    
    public void addCredits(int amount) {
        if (amount > 0) {
            this.credits += amount;
            updateMembershipLevel();
        }
    }

    public void useCredits(int amount) {
        if (amount <= credits) {
            this.credits -= amount;
            updateMembershipLevel();
        } else {
            System.out.println("Not enough credits.");
        }
    }

    private void updateMembershipLevel() {
        if (credits >= 1000) {
            membershipLevel = "Platinum";
        } else if (credits >= 500) {
            membershipLevel = "Gold";
        } else if (credits >= 200) {
            membershipLevel = "Silver";
        } else {
            membershipLevel = "Standard";
        }
    }

    @Override
    public String toString() {
        return "Membership ID: " + memberId +
               "\nName: " + name +
               "\nPhone: " + phoneNumber +
               "\nPreference: " + preference +
               "\nAddress: " + address +
               "\nCredits: " + credits +
               "\nLevel: " + membershipLevel;
    }
}
