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
