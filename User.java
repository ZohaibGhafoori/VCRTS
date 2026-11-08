/* Project: VCRTS
 * Class: User.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * The parent class for owners and clients.
 */
public abstract class User {

    private String userID;

    public User(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public abstract String getUserType();
}
