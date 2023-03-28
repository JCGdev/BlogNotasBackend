package by.daw.api.controllers;

import by.daw.api.utils.FileIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;

/* @RestController indica que la clase es
 * un controlador de Spring MVC
 */
@RestController
public class TestController {
    /*
     * La anotación "@Autowired" es una anotación de inyección de dependencias en
     * tiempo de ejecución de Spring Boot
     *
     * Cuando se aplica la anotación "@Autowired" a una propiedad, Spring Boot se encarga
     * de buscar una instancia de la dependencia correspondiente y la asigna automáticamente
     *  a la propiedad. Esto se hace mediante la resolución de la dependencia a partir
     *  del contexto de la aplicación de Spring Boot.
     */
    @Autowired
    private Environment env;

    @GetMapping(value ="/test", produces = MediaType.TEXT_HTML_VALUE)
    // La función que se ejecutará el acceder a la ruta relativa.
    public String getTestPage(){
        ClassPathResource resource = new ClassPathResource("html/test.html",
                                                           this.getClass().getClassLoader());
        InputStream stream;
        try {
            stream = resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return FileIO.read(stream, 1024);

    }



}
