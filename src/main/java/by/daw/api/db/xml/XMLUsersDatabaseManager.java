package by.daw.api.db.xml;

import by.daw.api.db.User;
import by.daw.api.db.Users;
import by.daw.api.db.behaviour.UserDatabaseManager;
import by.daw.api.db.exceptions.UserAlreadyExistsException;
import by.daw.api.db.exceptions.UserDoesNotExistException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/*
 * Terminología importante:
 * Marshal -> escribir
 * Unmarshal -> leer
 */
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
     * @return Una lista conteniendo los usuarios de la BBDD XML
     */
    @Override
    public List<User> getUsers() throws RuntimeException {
        Users users;
        try {
            Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
            users = (Users) unmarshaller.unmarshal(this.usersDB);

        } catch(JAXBException e){
            throw new RuntimeException(e);
        }
        return users.getUserList();
    }

    /**
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @return EL usuario de la base de datos o null si no se encuentra
     */
    @Override
    public User validateLogin(String username, String password) {
        User target = null;
        for(User user : this.getUsers()){
            if(user.getName().equals(username) &&
               user.getPassword().equals(password)){
                target = user;
            }
        }
        return target;
    }


    public User matchUserByID(String id){
        List<User> users = this.getUsers();
        User target = null;

        for(User tmpUserInDB : users){
            if(tmpUserInDB.getId().equals(id)){
                target = tmpUserInDB;
            }
        }
        return target;
    }

    /**
     * Añade un usuario a la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return el usuario creado
     */
    @Override
    public User addUser(String username, String password) throws UserAlreadyExistsException {
        if(this.validateLogin(username, password) != null){
            throw new UserAlreadyExistsException("User already exists!. Aborting registration");
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


    /**
     * Elimina a un usuario de la base de datos
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     */
    @Override
    public void deleteUser(String username, String password) throws UserAlreadyExistsException {
        User userToDelete;
        if ((userToDelete = this.validateLogin(username, password)) == null) {
            throw new UserDoesNotExistException("User doesn't not exist! Aborting deletion");
        }

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
     */
    private String provideNewID(){
        // Ejemplo: u0001
        String lastID = this.getLastID();
        String numberPartFromString = lastID.substring(1);
        int numberPart = Integer.parseInt(numberPartFromString);

        DecimalFormat df = new DecimalFormat("0000");
        return "u" + df.format(numberPart+1) ;
    }

    /**
     * Devuelve el id del último usuario registrado
     *
     * @return el id del último usuario
     */
    private String getLastID(){
        List<User> users = this.getUsers();
        User lastUser = users.get(users.size()-1);

        return lastUser.getId();
    }

}
