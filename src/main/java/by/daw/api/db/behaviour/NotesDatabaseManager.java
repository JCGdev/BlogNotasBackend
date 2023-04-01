package by.daw.api.db.behaviour;

import by.daw.api.db.Note;
import by.daw.api.db.User;

import java.util.List;

public interface NotesDatabaseManager {

    /* El comportamiento que debe tener una clase que maneje
       la base de datos con la notas.
       ¿Para qué?, por si queremos añadir soporte para una bbdd de otro tipo,
       para que esté abstraido
     */


    /**
     * Devuelve las notas que estén vinculadas al id
     * de un usuario.
     * @param user el objeto Usuario que envuelve el usuario, contraseña e id
     * @return un array de objetos de tipo Nota
     */
    List<Note> getUserNotes(User user);

}
