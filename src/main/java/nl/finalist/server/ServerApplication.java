package nl.finalist.server;

import nl.finalist.server.service.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class ServerApplication {

    @Resource
    FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
