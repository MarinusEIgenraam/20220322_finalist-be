package nl.finalist.server.model.DTO;

import com.sun.istack.Nullable;
import nl.finalist.server.model.Project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectOutput {
    public Long id;
    public String name;
    public String owner;
    public String createdAt;
    @Nullable
    public String modifiedAt;
    public List<FileInfoOutput> fileInfos;

    public static ProjectOutput fromProject(Project project) {
        var dto = new ProjectOutput();

        dto.id = project.getId();
        dto.name = project.getName();
        dto.owner = project.getOwner();
        dto.modifiedAt = project.getModifiedAt() != null ? project.getModifiedAt().toString() : null;
        dto.createdAt = project.getCreatedAt().toString();
        dto.fileInfos = project.getFileInfos().stream().map(f -> FileInfoOutput.fromFileInfo(f)).collect(Collectors.toList());

        return dto;
    }
}
