package nl.finalist.server.controller;

import nl.finalist.server.constants.MessageConstants;
import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.ResponseMessage;
import nl.finalist.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping
    public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {

        String message = "";

        try {
            List<String> fileDetails = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {

                fileService.saveFile(file);
                fileDetails.add(file.getOriginalFilename());
            });

            message = MessageConstants.FILE_UPLOADED + fileDetails;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            message = MessageConstants.FILE_FAIL_UPLOAD;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileInfo>> getAllFiles() {

        List<FileInfo> fileInfoList = fileService.loadAllFile().map(path -> {

            String fileName = path.getFileName().toString();
            String fileLocation = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                    path.getFileName().toString()).build().toString();
            return new FileInfo(fileName, fileLocation);

        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.loadFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\"" + file.getFilename() + "\"").body(file);

    }
}
