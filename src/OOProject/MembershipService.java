package OOProject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MembershipService {
//	private String restaurantId;
    private Map<String, Membership> membershipMap;
    private String filePath = "C:/Users/SerenaC/eclipse-workspace/CourseHW/src/OOProject/membership.csv";

    public MembershipService() {
        //this.restaurantId = restaurantId;
        this.membershipMap = new HashMap<>();
        loadFromFile();
    }

    public void loadFromFile() {
        membershipMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 7) {
                	String restaurantId = tokens[0];
                    String memberId = tokens[1];
                    String name = tokens[2];
                    String phone = tokens[3];
                    String preference = tokens[4];
                    String address = tokens[5];
                    int credits = Integer.parseInt(tokens[6]);

                    Membership member = new Membership(restaurantId, memberId, name, phone, preference, address);
                    for (int i = 0; i < credits; i++) member.addCredits(1); // simulate adding credits
                    String key = restaurantId + "-" + name + "-" + phone;
                    membershipMap.put(key, member);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading memberships: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Membership m : membershipMap.values()) {
                writer.println(String.join(",",
                		m.getRestaurantId(),
                        m.getMemberId(),
                        m.getName(),
                        m.getPhoneNumber(),
                        m.getPreference(),
                        m.getAddress(),
                        String.valueOf(m.getCredits())
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving memberships: " + e.getMessage());
        }
    }

    public boolean addMember(Membership member) {
        String key = member.getRestaurantId() + "-" + member.getName() + "-" + member.getPhoneNumber();
        if (!membershipMap.containsKey(key)) {
            membershipMap.put(key, member);
            return true;
        }
        return false;
    }

    public List<Membership> getAllMembers() {
        return new ArrayList<>(membershipMap.values());
    }
    
    public Membership getMember(String restaurantId, String name, String phone){
    	for (Membership m : membershipMap.values()) {
    		if (m.getRestaurantId().equalsIgnoreCase(restaurantId) && m.getName().equalsIgnoreCase(name) && m.getPhoneNumber().equals(phone)) {
    			return m;
    		}
    	}
		return null;
    }


}

