package nl.finalist.server.service;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Message;
import nl.finalist.server.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
public class FileInfoService {

    @Autowired
    FileInfoRepository fileInfoRepository;

    private final Path root = Paths.get("uploads");

    public FileInfo findById(Long id) {
        Optional<FileInfo> fileInfo = fileInfoRepository.findById(id);
        if (!fileInfo.isPresent()) {
            throw new IllegalStateException("File could not found for given id:" + id);
        }
        return fileInfo.get();
    }

    public Iterable <FileInfo> findAll() {
        return fileInfoRepository.findAll();
    }

    public void save(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }
    public static void example(Message bodyIn) {
        bodyIn.setName( "Hello, " + bodyIn.getName() );
        bodyIn.setId(bodyIn.getId()*10);
    }
}