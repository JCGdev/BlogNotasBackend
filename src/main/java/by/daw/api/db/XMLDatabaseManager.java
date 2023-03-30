package by.daw.api.db;

import by.daw.api.db.behaviour.NotesDatabaseManager;
import by.daw.api.db.behaviour.UserDatabaseManager;

import java.io.File;

/**
 * Clase manejadora de la base de datos XML de notas y usuarios. Esta clase
 * obtiene las BBDD mediante las propiedades: 'usersDatabase.path' y 'notesDatabase.path'.
 * Por ende, estas propiedades deben pasarse como argumento al programa. En caso contrario, lanzará
 * una excepción de tipo NullPointerException.
 */
public class XMLDatabaseManager implements UserDatabaseManager, NotesDatabaseManager {

    private static XMLDatabaseManager instance;
    private File usersDB;
    private File notesDB;

    /**
     * Patrón singleton, para que solo haya
     * una instancia de la clase.
     *
     * @return la única instancia de la clase
     */
    public static XMLDatabaseManager getInstance(){
        if(instance == null){
            instance = new XMLDatabaseManager();
        }
        return instance;
    }

    /**
     * Constructor principal
     */
    private XMLDatabaseManager(){
       this.loadDatabases();
    }

    /**
     * Intenta construir archivos File que representan las bases de datos,
     * validan que estos existan y las asigna a this.usersDB y this.notesDB
     *
     * @throws NullPointerException si las propiedades usersDatabase.path y notesDatabase.path
     * no están definidas.
     * @throws IllegalArgumentException si los ficheros de las bases de datos no existen.
     */
    private void loadDatabases() throws NullPointerException, IllegalArgumentException{
        String usersDBPath = System.getProperty("usersDatabase.path");
        String notesDBPath = System.getProperty("notesDatabase.path");
        if(usersDBPath == null) {
            throw new NullPointerException("Property 'usersDatabase.path' is not defined");
        } else if(notesDBPath == null){
            throw new NullPointerException("Property 'notesDatabase.path' is not defined");
        }

        this.usersDB = new File(usersDBPath);
        this.notesDB = new File(notesDBPath);
        if(!this.usersDB.exists()){
            throw new IllegalArgumentException("ERROR: " + usersDBPath + " does not exist");
        } else if(!this.notesDB.exists()) {
            throw new IllegalArgumentException("ERROR: " + notesDBPath+ " does not exist");
        }
    }



    @Override
    public boolean validateLogin(User user) {
        return false;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public Note[] getUserNotes(User user) {
        return new Note[0];
    }
}
