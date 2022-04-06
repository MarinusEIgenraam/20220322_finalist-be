package nl.finalist.server.model.DTO;

import lombok.Builder;
import nl.finalist.server.model.FileInfo;

import java.time.LocalDateTime;

@Builder
public class FileInfoInput {
    public Long id;
    public String fileName;
    public String fileDirectory;
    public String savedDirectory;

    public long fileSize;
    public String fileType;

    public String lastEvent;
    public String projectName;

    public FileInfo toFileInfo() {
        var fileInfo = new FileInfo();

        fileInfo.setFileName(fileName);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileDirectory(fileDirectory);
        fileInfo.setSavedDirectory(savedDirectory);
        fileInfo.setLastEvent(lastEvent);
        fileInfo.setModifiedAt(LocalDateTime.now());

        return fileInfo;
    }
}
