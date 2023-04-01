package by.daw.api.db;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/*
 * Las anotaciones de JAXB son simplemente metadatos para poder serializar los objetos
 * Users y desserializar las entradas en el XML.
 *
 * Es decir, esta clase puede ser usada en otro contexto independientemente de que
 * contenga estas anotaciones.
 */

@XmlRootElement(name = "users")
public class Users {

    private List<User> users;

    /**
     * Constructor reservado para el proceso de marshalling (escritura) y unmarshalling (lectura)
     * de JABX. Este no debería usarse en otro contexto.
     */
    public Users(){
        this.users = new ArrayList<>();
    }

    public Users(List<User> users){
        this.users = users;
    }

    @XmlElement(name = "user")
    public List<User> getUserList() {
        return this.users;
    }

    public void setUserList(List<User> list){
        this.users = list;
    }

}
