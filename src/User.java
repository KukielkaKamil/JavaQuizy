public class User {
    private final String login;
    private final String haslo;
    private long user_id;


    public User(String login, String haslo, long user_id) {
        this.login = login;
        this.haslo = haslo;
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }


    public long getUser_id() {
        return user_id;
    }

}
