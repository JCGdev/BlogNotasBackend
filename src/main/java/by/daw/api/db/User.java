package by.daw.api.db;

import java.util.Arrays;

public final class User {
    private final String name;
    private final char[] password;
    private final String id;

    public User(String name, char[] password, String id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    /**
     * Sobrescribe la contraseña para que
     * esta esté en memoria el menor tiempo posible.
     */
    public void overridePassword(){
        Arrays.fill(this.password, 'a');
    }

    public String getName() {
        return name;
    }

    public char[] getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

}
