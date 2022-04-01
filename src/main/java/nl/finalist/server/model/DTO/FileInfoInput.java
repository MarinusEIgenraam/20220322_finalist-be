package nl.finalist.server.model.DTO;

import lombok.Builder;
import nl.finalist.server.model.FileInfo;

import java.time.LocalDateTime;

@Builder
public class FileInfoInput {
    public Long id;
    public String fileName;
    public String fileLocation;
    public String lastEvent;
    public String projectName;

    public FileInfo toFileInfo() {
        var fileInfo = new FileInfo();

        fileInfo.setFileName(fileName);
        fileInfo.setFileLocation(fileLocation);
        fileInfo.setLastEvent(lastEvent);
        fileInfo.setModifiedAt(LocalDateTime.now());

        return fileInfo;
    }
}
