package vn.edu.hcmuaf.fit.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeLeaveApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeLeaveApiApplication.class, args);
	}

}
