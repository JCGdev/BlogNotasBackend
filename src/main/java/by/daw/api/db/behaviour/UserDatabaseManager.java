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
     * @throws UserDoesNotExistException si el usuario no existe
     * @return EL usuario de la base de datos
     */
    User validateLogin(String username, String password) throws UserDoesNotExistException;


    /**
     * Devuelve el usuario que coincida con ese id
     *
     * @param id id del usuario
     * @throws UserDoesNotExistException si el usuario no existe
     * @return un Usuario si se encuentra o una runtime Exception en caso de
     *         que no exista
     */
    User matchUserByID(String id) throws UserDoesNotExistException;

    /**
     * Añade un usuario a la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @throws UserAlreadyExistsException si el usuario existe
     * @return el usuario creado
     */
    User addUser(String username, String password) throws UserAlreadyExistsException;

    /**
     * Elimina a un usuario de la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @throws UserDoesNotExistException si no existe el usuario
     */
    void deleteUser(String username, String password) throws UserDoesNotExistException;


    /**
     * Devuelve la lista de usuarios
     *
     * @return Una lista conteniendo los usuarios de la BBDD
     */
    List<User> getUsers();


}
