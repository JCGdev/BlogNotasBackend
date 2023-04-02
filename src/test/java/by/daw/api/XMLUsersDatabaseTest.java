package by.daw.api;

import by.daw.api.db.User;
import by.daw.api.db.behaviour.UserDatabaseManager;
import by.daw.api.db.exceptions.UserDoesNotExistException;
import by.daw.api.db.xml.XMLUsersDatabaseManager;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class XMLUsersDatabaseTest {

    public XMLUsersDatabaseTest(){
        System.setProperty("usersDatabase.path", "/home/daw/Escritorio/dbTest/usersBBDD.xml");
    }

    @Test
    public void addUserTest(){
        UserDatabaseManager dbManager = XMLUsersDatabaseManager.getInstance();

        assertDoesNotThrow( () -> dbManager.addUser("JUNIT", "JUNIT"));

        assertNotNull(dbManager.validateLogin("JUNIT", "JUNIT"));
    }

    @Test
    public void deleteUserTest(){
        UserDatabaseManager dbManager = XMLUsersDatabaseManager.getInstance();

        List<User> users = dbManager.getUsers();
        User target;
        if(users.size() > 0) {
            target = users.get(0);
        } else {
            target = dbManager.addUser("JUNIT2", "JUNIT2");
        }

        assertDoesNotThrow( () -> dbManager.deleteUser(target.getName(), target.getPassword()));

        assertThrows( UserDoesNotExistException.class,
                     () -> dbManager.validateLogin(target.getName(), target.getPassword()));

    }

    @Test
    public void getUsersTest(){
        // Se presupone que existe algún usuario en la BBDD
       UserDatabaseManager dbManager = XMLUsersDatabaseManager.getInstance();
       List<User> users = dbManager.getUsers();

       assertTrue(users.size() > 0);
    }



}
