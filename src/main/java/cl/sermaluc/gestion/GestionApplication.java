package cl.sermaluc.gestion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionApplication {

    @Value("${correo.regex}")
    private String regex;
    
	public static void main(String[] args) {
		SpringApplication.run(GestionApplication.class, args);
	}

}
