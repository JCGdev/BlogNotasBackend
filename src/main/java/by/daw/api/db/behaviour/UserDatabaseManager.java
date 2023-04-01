package by.daw.api.db.behaviour;

import by.daw.api.db.User;
import by.daw.api.db.exceptions.UserAlreadyExistsException;
import by.daw.api.db.exceptions.UserDoesNotExistException;

import java.util.List;

public interface UserDatabaseManager {

    /* El comportamiento que debe tener una clase que maneje
       la base de datos de usuarios.
       ¿Para qué?, por si queremos añadir soporte para una bbdd de otro tipo,
       para que esté abstraido
     */


    /**
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @return el usuario de la base de datos o null si no se encuentra
     */
    User validateLogin(String username, String password);

    /**
     * Añade un usuario a la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @throws UserAlreadyExistsException si ya existe un usuario con esas credenciales
     * @return el usuario creado
     */
    User addUser(String username, String password) throws UserAlreadyExistsException;


    /**
     * Elimina a un usuario de la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @throws UserDoesNotExistException si el usuario no existe
     */
    void deleteUser(String username, String password) throws UserDoesNotExistException;


    /**
     * Devuelve la lista de usuarios
     *
     * @return Una lista conteniendo los usuarios de la BBDD XML
     */
    List<User> getUsers();


}
