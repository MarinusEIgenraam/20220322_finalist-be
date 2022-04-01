package nl.finalist.server.model.DTO;

import lombok.Builder;
import nl.finalist.server.model.FileInfo;

public class FileInfoOutput {
    public Long id;
    public String fileName;
    public String fileLocation;
    public String lastEvent;
    public String createdAt;
    public String modifiedAt;
    public String projectName;

    public static FileInfoOutput fromFileInfo(FileInfo fileInfo) {
        var dto = new FileInfoOutput();

        dto.id = fileInfo.getId();
        dto.fileName = fileInfo.getFileName();
        dto.fileLocation = fileInfo.getFileLocation();
        dto.lastEvent = fileInfo.getLastEvent();
        dto.createdAt = fileInfo.getCreatedAt().toString();
        dto.modifiedAt = fileInfo.getModifiedAt().toString();
        dto.projectName = fileInfo.getProject().getName();

        return dto;
    }
}
