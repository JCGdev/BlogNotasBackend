package by.daw.api;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    /* Para información sobre cómo crear una aplicación en SpringBoot:
     * https://spring.io/projects/spring-boot
     * https://spring.io/guides/gs/spring-boot/
     * https://docs.spring.io/spring-boot/docs/current/api/
     */


    //  The application context will load beans from the specified primary sources
    private final static SpringApplication restAPI = new SpringApplication(Application.class);

    private static ApplicationContext appContext;

    /**
     * Ejecuta la aplicación.
     *
     * @param args los argumentos del programa
     */
    public static void start(String[] args){
        appContext = restAPI.run(args);
    }

    /**
     * Finzaliza la aplicación.
     *
     * @param code código de error
     */
    public static void stop(int code) {
        ExitCodeGenerator exitCode = new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return code;
            }
        };
        restAPI.exit(appContext, exitCode);
    }


    public static SpringApplication getRestAPI() {
        return restAPI;
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }



}
