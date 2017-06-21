package bucket.me.yeppo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class BucketApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(BucketApplication.class, args);
		System.out.println("================ APP START ================");
	}
}
