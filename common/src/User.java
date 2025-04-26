package common.src;

import java.util.ArrayList;
import java.util.List;

/**
 * common.src.User: A simple model for user accounts.
 */
public class User {
    public String username;
    public String password;
    public List<String> purchaseHistory;
    public int rewardPoints;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.purchaseHistory = new ArrayList<>();
        this.rewardPoints = 0;
    }
}