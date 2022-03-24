package nl.finalist.server.controller;

import nl.finalist.server.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {

    @Autowired
    FileInfoService fileService;


}
