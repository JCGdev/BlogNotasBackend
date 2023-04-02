package by.daw.api.db.xml;

import by.daw.api.db.User;
import by.daw.api.db.Users;
import by.daw.api.db.behaviour.UserDatabaseManager;
import by.daw.api.db.exceptions.RuntimeJAXBException;
import by.daw.api.db.exceptions.UserAlreadyExistsException;
import by.daw.api.db.exceptions.UserDoesNotExistException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Terminología importante:
 * Marshal -> escribir
 * Unmarshal -> leer
 */

// TODO -> Implementar un sistema de valicacion usando DTDs. Apache xerces es una opción
public class XMLUsersDatabaseManager implements UserDatabaseManager {
    private static XMLUsersDatabaseManager instance;
    private File usersDB;
    private final JAXBContext jaxbContext;

    public static XMLUsersDatabaseManager getInstance(){
        if(instance == null){
            instance = new XMLUsersDatabaseManager();
        }
        return instance;
    }

    private XMLUsersDatabaseManager(){
        try {
            this.jaxbContext = JAXBContext.newInstance(Users.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        this.loadDatabase();
    }

    /**
     * Intenta construir un archivo Fil que representa las bases de datos,
     * valida que esta exista y las asigna a this.usersDB
     *
     * @throws NullPointerException si la propiedad usersDatabase.path no está definida.
     * @throws IllegalArgumentException si el ficher de las base de datos no existe.
     */
    private void loadDatabase() throws NullPointerException, IllegalArgumentException{
        String usersDBPath = System.getProperty("usersDatabase.path");
        if(usersDBPath == null) {
            throw new NullPointerException("Property 'usersDatabase.path' is not defined");
        }
        this.usersDB = new File(usersDBPath);
        if(!this.usersDB.exists()){
            throw new IllegalArgumentException("ERROR: " + usersDBPath + " does not exist");
        }
    }

    /*
     * En un proyecto 'real', lo suyo sería usar SAX (Simple API For XML) para
     * parsear el XML sin cargar el fichero entero en memoria. Por simplicidad
     * y como el proyecto no tendrá muchos usuarios, no lo implementaré así.
     */


    /**
     * Devuelve la lista de usuarios
     *
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @return Una lista conteniendo los usuarios de la BBDD
     */
    @Override
    public List<User> getUsers() throws RuntimeJAXBException {
        Users users;
        try {
            Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
            users = (Users) unmarshaller.unmarshal(this.usersDB);

        } catch(JAXBException e){
            throw new RuntimeJAXBException(e);
        }

        if(users.getUserList() == null){
            return new ArrayList<User>();
        }

        return users.getUserList();
    }

    /**
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws UserDoesNotExistException si el usuario no existe
     * @return EL usuario de la base de datos
     */
    @Override
    public User validateLogin(String username, String password) throws RuntimeJAXBException,
                                                                        UserDoesNotExistException{
        User target = null;
        for(User user : this.getUsers()){
            if(user.getName().equals(username) &&
               user.getPassword().equals(password)){
                target = user;
            }
        }

        if(target == null){
            throw new UserDoesNotExistException("user: " + username + " does not exist .");
        }
        return target;
    }


    /**
     * Devuelve el usuario que coincida con ese id
     *
     * @param id id del usuario
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws UserDoesNotExistException si el usuario no existe
     * @return un Usuario si se encuentra o una runtime Exception en caso de
     *         que no exista
     */
    public User matchUserByID(String id) throws RuntimeJAXBException, UserDoesNotExistException {
        List<User> users = this.getUsers();
        User target = null;

        for(User tmpUserInDB : users){
            if(tmpUserInDB.getId().equals(id)){
                target = tmpUserInDB;
            }
        }

        if(target == null){
            throw new UserDoesNotExistException("user with id: " + id  + " does not exist .");
        }

        return target;
    }

    /**
     * Añade un usuario a la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws UserAlreadyExistsException si el usuario existe
     * @return el usuario creado
     */
    @Override
    public User addUser(String username, String password) throws RuntimeJAXBException, UserAlreadyExistsException {
        if(this.userExists(username, password)){
            throw new UserAlreadyExistsException("User " + username + " already exists!");
        }

        User newUser = new User(username, password, this.provideNewID());

        List<User> userList = this.getUsers();
        userList.add(newUser);
        Users users = new Users(userList);

        try {
            Marshaller marshaller = this.jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(users, this.usersDB);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return newUser;
    }

    private boolean userExists(String username, String password){
        try {
            this.validateLogin(username, password);
        } catch (UserDoesNotExistException e){
            return false;
        }
        return true;
    }


    /**
     * Elimina a un usuario de la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @throws UserDoesNotExistException si no existe el usuario
     */
    @Override
    public void deleteUser(String username, String password) throws RuntimeJAXBException,
                                                                    UserDoesNotExistException {
        User userToDelete = this.validateLogin(username, password);

        List<User> usersList = this.getUsers();
        usersList.remove(userToDelete);

        Users users = new Users(usersList);

        try {
            Marshaller marshaller = this.jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(users, this.usersDB);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Devuelve el siguiente ID disponible
     *
     * @return el id disponible
     *
     */
    private String provideNewID() {
        // Ejemplo: u0001
        String lastID = this.getLastID();
        DecimalFormat df = new DecimalFormat("0000");

        if(lastID == null){
            lastID = "u" + df.format(1);
        }

        String numberPartFromString = lastID.substring(1);
        int numberPart = Integer.parseInt(numberPartFromString);

        return "u" + df.format(numberPart+1) ;
    }

    /**
     * Devuelve el id del último usuario registrado
     *
     * @throws RuntimeJAXBException si hay algún error en el marshalling o unmarshalling
     * @return el id del último usuario o null si no hay usuarios
     */
    private String getLastID() throws RuntimeJAXBException {
        List<User> users = this.getUsers();
        if(users.size() < 1){
            return null;
        }
        User lastUser = users.get(users.size()-1);
        return lastUser.getId();
    }

}
