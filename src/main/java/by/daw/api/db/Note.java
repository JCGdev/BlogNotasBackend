package by.daw.api.db;

public class Note {
    private final String userID;
    private final String content;

    public Note(String userID, String content) {
        this.userID = userID;
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

}
