package bell.avishkar.recog.project.storagemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StorageManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageManagementApplication.class, args);
	}

	//Bucket Name:
	//bellavishkar-s3master

	//Clean Up:
	//http://localhost:8080/api/v1/individual/clear-every-thing

}