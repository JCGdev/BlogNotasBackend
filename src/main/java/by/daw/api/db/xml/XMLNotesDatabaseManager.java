package by.daw.api.db.xml;

import by.daw.api.db.Note;
import by.daw.api.db.Notes;
import by.daw.api.db.User;
import by.daw.api.db.behaviour.NotesDatabaseManager;
import by.daw.api.db.exceptions.NoSuchNoteException;
import by.daw.api.db.exceptions.RuntimeJAXBException;
import by.daw.api.db.exceptions.UserDoesNotExistException;
import jakarta.xml.bind.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Terminología importante:
 * Marshal -> escribir
 * Unmarshal -> leer
 */

/**
 * Clase Manager de la bases de datos XML de Notas. Para más detalles sobre
 * el diseño de la BBDD, eche un vistazo a resources/samples
 * Este manager depende XMLUsersDatabaseManager, dado que la propia BBDD almacena
 * IDs de usuarios, por lo que es necesario validarlos. En este caso se ha implementado
 * con Singletons.
 *
 */

public class XMLNotesDatabaseManager implements NotesDatabaseManager {
    private static XMLNotesDatabaseManager instance;
    private File notesDB;
    private final JAXBContext jaxbContext;

    public static XMLNotesDatabaseManager getInstance() {
        if (instance == null) {
            instance = new XMLNotesDatabaseManager();
        }
        return instance;
    }

    private XMLNotesDatabaseManager() {
        try {
            this.jaxbContext = JAXBContext.newInstance(Notes.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        this.loadDatabase();
    }

    /**
     * Intenta construir un archivo File que representa las bases de datos,
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
     * @throws RuntimeJAXBException  si hay algún error en el marshalling o unmarshalling
     * @throws UserDoesNotExistException si tal usuario no existe
     * @return un array de objetos de tipo Nota
     */
    @Override
    public List<Note> getUserNotes(User user) throws RuntimeJAXBException,
                                                     UserDoesNotExistException {
        // Si no existe lanza UserDoesNotExistException
        XMLUsersDatabaseManager.getInstance().validateLogin(user.getName(), user.getPassword());

        List<Note> notesInDB = this.getNotes();
        List<Note> specificUserNotes = new ArrayList<>();

        for(Note note: notesInDB){
            if(note.getUserID().equals(user.getId())){
                specificUserNotes.add(note);
            }
        }
        return specificUserNotes;
    }

    /**
     * Devuelve todas las notas de la bbdd.
     *
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @return un array de objetos de tipo Nota
     */

    @Override
    public List<Note> getNotes() throws RuntimeJAXBException {

        Notes notes = this.unmarshallNotes();

        if(notes.getNotes() == null){
            return new ArrayList<Note>();
        }

        return notes.getNotes();
    }

    /**
     * Lee las notas de de la BBDD XML y las deserializa en
     * objetos Note, contenidos en un objeto Notes, que actúa como wrapper.
     * Este método adquiere el candado intrínsico y bloquea el objeto para
     * otros hilos, lo que evita problemas de concurrencia.
     *
     * @return Las notas contenidas en una instancia Notes
     */
    private Notes unmarshallNotes() {
        Notes notes;
        try {
            Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
            synchronized(this) {
                notes = (Notes) unmarshaller.unmarshal(this.notesDB);
            }
            /* Sinceramente, un coñazo que se lance una excepción tan abstracta,
              pero bueno, es lo que hay */
        } catch (JAXBException e) {
            throw new RuntimeJAXBException(e);
        }
        return notes;
    }


    /**
     * Devuelve la nota con tal ID
     *
     * @param noteID ID de la nota
     * @return La nota correspondiente
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws NoSuchNoteException si no existe tal nota
     */
    @Override
    public Note getNote(String noteID) throws RuntimeJAXBException, NoSuchNoteException {
        List<Note> notes = this.getNotes();
        Note target = null;

        for(Note note : notes){
            if(note.getNoteID().equals(noteID)){
                target = note;
            }
        }
        if(target == null){
            throw new NoSuchNoteException("Note: " + noteID + " does not exist");
        }

        return target;
    }


    /**
     * @param userID      el id del usuario dueño de la nota
     * @param noteContent el contenido de la nota
     * @return La nota añadida a la BBDD
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws UserDoesNotExistException si tal usuario no existe
     */
    @Override
    public Note addNote(String userID, String noteContent) throws RuntimeJAXBException,
                                                                  UserDoesNotExistException {

        // Si no existe lanza UserDoesNotExistException
        XMLUsersDatabaseManager.getInstance().matchUserByID(userID);

        Note note = new Note(userID, this.provideNewID(), noteContent);

        List<Note> notes = this.getNotes();
        if(notes == null) {
            notes = new ArrayList<>();
        }
        notes.add(note);
        Notes notesWrapper = new Notes(notes);

        this.marshallNotes(notesWrapper);
        return note;
    }

    /**
     * Serializa una instancia de Notes en formato XML en la BBDD
     * Este método adquiere el candado intrínsico y bloquea el objeto para
     * otros hilos, lo que evita problemas de concurrencia.
     *
     * @param notes las notas a serializar
     */
    private void marshallNotes(Notes notes){
        try {
            Marshaller marshaller = this.jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            synchronized(this) {
                marshaller.marshal(notes, this.notesDB);
            }
        } catch(JAXBException e){
            throw new RuntimeJAXBException(e);
        }
    }


    /**
     * Elimina una nota de la BBDD
     *
     * @param noteID el ID de la nota
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     *         la nota no existe
     * @throws NoSuchNoteException si no existe tal nota
     */
    @Override
    public void deleteNote(String noteID) throws RuntimeJAXBException, NoSuchNoteException {
        Note noteToRemove = this.getNote(noteID);

        List<Note> notes = this.getNotes();
        notes.remove(noteToRemove);
        Notes notesWrapper = new Notes(notes);

        this.marshallNotes(notesWrapper);
    }

    /**
     * Asigna IDs
     *
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @return Devuelve el próximo ID libre
     */
    private String provideNewID() throws RuntimeJAXBException {
        // Ejemplo: n0001
        String lastID = this.getLastID();
        DecimalFormat df = new DecimalFormat("0000");

        if(lastID == null){
            lastID = df.format(1);
        }
        String numberPartFromString = lastID.substring(1);
        int numberPart = Integer.parseInt(numberPartFromString);


        return "n" + df.format(numberPart+1) ;
    }

    /**
     * Devuelve el id del la última nota registrada
     *
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @return el id del último usuario o null si no hay usuarios
     */
    private String getLastID() throws RuntimeJAXBException {
        List<Note> notes = this.getNotes();
        if(notes.size() < 1){
            return null;
        }
        Note lastNote = notes.get(notes.size()-1);

        return lastNote.getNoteID();
    }

}
