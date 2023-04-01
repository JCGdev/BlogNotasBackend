package by.daw.api.db;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.Objects;

/*
 * Las anotaciones de JAXB son simplemente metadatos para poder serializar los objetos
 * Users y desserializar las entradas en el XML.
 *
 * Es decir, esta clase puede ser usada en otro contexto independientemente de que
 * contenga estas anotaciones.
 */

public final class User {
    private String name;
    private String password;
    private String id;

    /**
     * Constructor reservado para el proceso de marshalling (escritura) y unmarshalling (lectura)
     * de JABX. Este no debería usarse en otro contexto.
     */
    public User(){
        this.name = null;
        this.password = null;
        this.id = null;
    }

    public User(String name, String password, String id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    /**
     * Sobrescribe la contraseña para que
     * esta esté en memoria el menor tiempo posible.
     */
    public void shredPassword(){
       this.password = null;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    @XmlElement(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, id);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password=" + password +
                ", id='" + id + '\'' +
                '}';
    }
}
