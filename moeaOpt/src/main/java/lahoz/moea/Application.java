package lahoz.moea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		// get moea controller and run init and run methods
		// MOEAController controller = new MOEAController();
	}
}
