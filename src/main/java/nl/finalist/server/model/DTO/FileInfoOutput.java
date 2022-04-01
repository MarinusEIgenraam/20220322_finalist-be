package nl.finalist.server.model.DTO;

import lombok.Builder;
import nl.finalist.server.model.FileInfo;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class FileInfoOutput {
    public Long id;
    public String fileName;
    public String fileLocation;
    public String lastEvent;
    public String createdAt;
    @Nullable
    public String modifiedAt;
    public String projectName;
    @Nullable
    public String parentFolder;
    public List<FileInfoOutput> fileList;

    public static FileInfoOutput fromFileInfo(FileInfo fileInfo) {
        var dto = new FileInfoOutput();

        dto.id = fileInfo.getId();
        dto.fileName = fileInfo.getFileName();
        dto.fileLocation = fileInfo.getFileLocation();
        dto.lastEvent = fileInfo.getLastEvent();
        dto.createdAt = fileInfo.getCreatedAt().toString();
        dto.modifiedAt = fileInfo.getModifiedAt() != null ? fileInfo.getModifiedAt().toString() : null;
        dto.projectName = fileInfo.getProject().getName();
        dto.parentFolder = fileInfo.getParentFolder() != null ? fileInfo.getParentFolder().getFileName() : null;
        dto.fileList = fileInfo.getFileList().stream().map(f->FileInfoOutput.fromFileInfo(f)).collect(Collectors.toList());

        return dto;
    }
}
