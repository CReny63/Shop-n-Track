import java.util.ArrayList;
import java.util.List;

/**
 * User: A simple model for user accounts.
 */
class User {
    String username;
    String password;
    List<String> purchaseHistory;
    int rewardPoints;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.purchaseHistory = new ArrayList<>();
        this.rewardPoints = 0;
    }
}