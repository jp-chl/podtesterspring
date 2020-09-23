package nectia.com.podtesterspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PodtesterspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PodtesterspringApplication.class, args);
	} // end void main(String[] args)

	@RequestMapping("/")
	public String home() {
		return "Hello Docker World";
	}

} // end PodtesterspringApplication
