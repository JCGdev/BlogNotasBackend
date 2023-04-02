package by.daw.api;

import by.daw.api.db.Note;
import by.daw.api.db.User;
import by.daw.api.db.behaviour.NotesDatabaseManager;
import by.daw.api.db.behaviour.UserDatabaseManager;
import by.daw.api.db.xml.XMLNotesDatabaseManager;
import by.daw.api.db.xml.XMLUsersDatabaseManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XMLNotesDatabaseTest {

    public XMLNotesDatabaseTest(){
        System.setProperty("usersDatabase.path", "/home/daw/Escritorio/dbTest/usersBBDD.xml");
        System.setProperty("notesDatabase.path", "/home/daw/Escritorio/dbTest/notesBBDD.xml");
    }

    @Test
    public void addNoteTest(){
        UserDatabaseManager userDB = XMLUsersDatabaseManager.getInstance();
        NotesDatabaseManager notesDB = XMLNotesDatabaseManager.getInstance();

        List<User> users = userDB.getUsers();
        User target;
        if(users.size() < 1){
            target = userDB.addUser("JUNIT3", "JUNIT3");
        } else {
            target = users.get(0);
        }

        Note note = notesDB.addNote(target.getId(), "THIS IS A TEST");

        assertDoesNotThrow(() ->  notesDB.getNote(note.getNoteID()));

    }


    @Test
    public void deleteNoteTest(){
        UserDatabaseManager userDB = XMLUsersDatabaseManager.getInstance();
        NotesDatabaseManager notesDB = XMLNotesDatabaseManager.getInstance();

        List<Note> notes = notesDB.getNotes();
        Note target;
        if(notes.size() > 0){
            target = notes.get(0);
        } else {
            User tmpUser = userDB.addUser("TEST", "TEST");
            target = notesDB.addNote(tmpUser.getId(), "THIS IS A TEST");
        }

        assertDoesNotThrow(() ->  notesDB.deleteNote(target.getNoteID()));

    }



}
