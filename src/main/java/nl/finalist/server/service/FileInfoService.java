package nl.finalist.server.service;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileInfoService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    private final Path root = Paths.get("uploads");

    public FileInfo findById(Long id) {
        Optional<FileInfo> product = fileInfoRepository.findById(id);
        if (!product.isPresent()) {
            throw new IllegalStateException("Product could not found for given id:" + id);
        }
        return product.get();
    }

    public Iterable <FileInfo> findAll() {
        return fileInfoRepository.findAll();
    }

    public void save(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }
}