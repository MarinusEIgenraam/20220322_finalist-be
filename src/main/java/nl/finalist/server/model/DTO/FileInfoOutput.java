package nl.finalist.server.model.DTO;

import lombok.Builder;
import nl.finalist.server.model.FileInfo;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class FileInfoOutput {
    public Long id;
    public String name;
    public String fileDirectory;
    public String fileType;
    public long fileSize;
    public String lastEvent;

    public String savedDirectory;
    @Nullable
    public String projectName;

    @Nullable
    public String modifiedAt;
    @Nullable
    public String createdAt;
    @Nullable
    public List<FileInfoOutput> children;
    @Nullable
    public Long parentId;

    public static FileInfoOutput fromFileInfo(FileInfo fileInfo) {
        var dto = new FileInfoOutput();

        dto.id = fileInfo.getId();
        dto.name = fileInfo.getFileName();
        dto.fileDirectory = fileInfo.getFileDirectory();
        dto.savedDirectory = fileInfo.getSavedDirectory();
        dto.lastEvent = fileInfo.getLastEvent();
        dto.fileType = fileInfo.getFileType();
        dto.fileSize = fileInfo.getFileSize();
        dto.projectName = fileInfo.getProject() != null ? fileInfo.getProject().getName() : null;

        dto.createdAt = fileInfo.getCreatedAt() != null ? fileInfo.getCreatedAt().toString() : null;
        dto.modifiedAt = fileInfo.getModifiedAt() != null ? fileInfo.getModifiedAt().toString() : null;

        dto.children = fileInfo.getFileList().stream().map(f->FileInfoOutput.fromFileInfo(f)).collect(Collectors.toList());
        dto.parentId = fileInfo.getParentFolder() != null ? fileInfo.getParentFolder().getId() : null;

        return dto;
    }
}
