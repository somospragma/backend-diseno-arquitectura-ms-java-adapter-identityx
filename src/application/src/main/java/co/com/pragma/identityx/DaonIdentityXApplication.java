package co.com.pragma.identityx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@Slf4j
public class DaonIdentityXApplication {
    public static void main(String[] args) {
        SpringApplication.run(DaonIdentityXApplication.class,args);
    }
}