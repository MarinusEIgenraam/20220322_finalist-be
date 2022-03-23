package nl.finalist.server.service;

import nl.finalist.server.constants.MessageConstants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileService {

    private final Path root = Paths.get("uploads");

    public void initializeDirectory() {
        try {
            Files.createDirectory(root);
        } catch (Exception e) {
            throw new RuntimeException(MessageConstants.FILE_PATH_NOT_CREATED);
        }
    }

    public void saveFile(MultipartFile multipartFile) {
        try {
            Files.copy(multipartFile.getInputStream(), this.root.resolve(multipartFile.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException(MessageConstants.FILE_NOT_CREATED + e.getMessage());
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path path = root.resolve(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException(MessageConstants.FILE_NOT_READ);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Stream<Path> loadAllFile() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);

        } catch (Exception e) {
            throw new RuntimeException(MessageConstants.FILE_NOT_LOADED);
        }
    }
}