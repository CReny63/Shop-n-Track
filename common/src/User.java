package common.src;

import java.util.ArrayList;
import java.util.List;

/**
 * common.src.User: A simple model for user accounts.
 */
public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> purchaseHistory;
    private int rewardPoints;


    // CSV format: username,password,email,points,firstName,lastName,<history items...>
    public User(String username, String password, String email, int rewardPoints, String first, String last, List<String> purchaseHistory) {
        this.username = username;
        this.password = password;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.rewardPoints = rewardPoints;
        this.purchaseHistory = new ArrayList<>(purchaseHistory);
    }

    public String getUser(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public List<String> getPurchaseHistory(){
        return purchaseHistory;
    }
    public int getRewardPoints(){
        return rewardPoints;
    }

    public void changeRewardPoints(int points){
        rewardPoints = points;
    }

}