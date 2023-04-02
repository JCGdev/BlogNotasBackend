package by.daw.api.db.behaviour;

import by.daw.api.db.Note;
import by.daw.api.db.User;
import by.daw.api.db.exceptions.NoSuchNoteException;
import by.daw.api.db.exceptions.UserDoesNotExistException;

import java.util.List;

public interface NotesDatabaseManager {

    /* El comportamiento que debe tener una clase que maneje
       la base de datos con la notas.
       ¿Para qué?, por si queremos añadir soporte para una bbdd de otro tipo,
       para que esté abstraido
     */


    /**
     * Devuelve todas las notas de la bbdd.
     *
     * @return un array de objetos de tipo Nota
     */
    List<Note> getNotes();


    /**
     * Devuelve la nota con tal ID
     *
     * @param noteID ID de la nota
     * @return La nota correspondiente
     * @throws NoSuchNoteException si no existe tal nota
     */
    Note getNote(String noteID) throws  NoSuchNoteException;

    /**
     * Devuelve las notas que estén vinculadas al id
     * de un usuario.
     *
     * @param user el objeto Usuario que envuelve el usuario, contraseña e id
     * @throws UserDoesNotExistException si tal usuario no existe
     * @return un array de objetos de tipo Nota
     */
    List<Note> getUserNotes(User user) throws UserDoesNotExistException;


    /**
     * @param userID      el id del usuario dueño de la nota
     * @param noteContent el contenido de la nota
     * @throws UserDoesNotExistException si tal usuario no existe
     * @return La nota añadida a la BBDD
     */
    Note addNote(String userID, String noteContent) throws UserDoesNotExistException;


    /**
     * Elimina una nota de la BBDD
     *
     * @param noteID el ID de la nota
     * @throws NoSuchNoteException si no existe tal nota
     */
    void deleteNote(String noteID) throws NoSuchNoteException;


}
