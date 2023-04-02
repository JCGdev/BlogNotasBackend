package by.daw.api.db;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

import java.util.Objects;

public class Note {
    private String userID;
    private String noteID;
    private String content;

    /**
     * Constructor reservado para el proceso de marshalling (escritura) y unmarshalling (lectura)
     * de JABX. Este no debería usarse en otro contexto.
     */
    public Note(){

    }

    public Note(String userID, String noteID, String content) {
        this.userID = userID;
        this.noteID = noteID;
        this.content = content;
    }

    @XmlAttribute(name = "userRef")
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @XmlValue
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @XmlAttribute(name = "id")
    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    @Override
    public String toString() {
        return "Note{" +
                "userID='" + userID + '\'' +
                ", noteID='" + noteID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(userID, note.userID) && Objects.equals(noteID, note.noteID) && Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, noteID, content);
    }
}
