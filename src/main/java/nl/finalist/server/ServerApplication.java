package nl.finalist.server;

import nl.finalist.server.service.FileInfoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class ServerApplication {

    @Resource
    FileInfoService fileService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
