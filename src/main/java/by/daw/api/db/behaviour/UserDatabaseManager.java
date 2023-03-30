package by.daw.api.db.behaviour;

import by.daw.api.db.User;

public interface UserDatabaseManager {

    /* El comportamiento que debe tener una clase que maneje
       la base de datos de usuarios.
       ¿Para qué?, por si queremos añadir soporte para una bbdd de otro tipo,
       para que esté abstraido
     */


    /**
     * Valida un usuario en la BBDD
     *
     * @param user el objeto Usuario que envuelve el usuario, contraseña e id
     * @return true si las credenciales coinciden con una entrada en la BBDD
     */
    boolean validateLogin(User user);
    /**
     * Añade un usuario a la BBDD
     * @param user el objeto Usuario que envuelve el usuario, contraseña e id
     */
    void addUser(User user);


}
