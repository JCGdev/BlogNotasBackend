package by.daw.api.db.xml;

import by.daw.api.db.Note;
import by.daw.api.db.User;
import by.daw.api.db.behaviour.NotesDatabaseManager;

import java.io.File;
import java.util.List;

// TODO -> IMPLEMENT THIS CLASS
public class XMLNotesDatabaseManager implements NotesDatabaseManager {

    private static XMLNotesDatabaseManager instance;
    private File notesDB;

    public static XMLNotesDatabaseManager getInstance() {
        if (instance == null) {
            instance = new XMLNotesDatabaseManager();
        }
        return instance;
    }

    private XMLNotesDatabaseManager() {

    }

    /**
     * Intenta construir un archivo Fil que representa las bases de datos,
     * valida que esta exista y las asigna a this.usersDB
     *
     * @throws NullPointerException     si la propiedad notesDatabase.path no está definida.
     * @throws IllegalArgumentException si el ficher de las base de datos no existe.
     */
    private void loadDatabase() throws NullPointerException, IllegalArgumentException {
        String notesDBPath = System.getProperty("notesDatabase.path");
        if (notesDBPath == null) {
            throw new NullPointerException("Property 'notesDatabase.path' is not defined");
        }
        this.notesDB = new File(notesDBPath);
        if (!this.notesDB.exists()) {
            throw new IllegalArgumentException("ERROR: " + notesDBPath + " does not exist");
        }

    }

    /**
     * Devuelve las notas que estén vinculadas al id
     * de un usuario.
     *
     * @param user el objeto Usuario que envuelve el usuario, contraseña e id
     * @return un array de objetos de tipo Nota
     */
    @Override
    public List<Note> getUserNotes(User user) {
        return null;
    }

}
