package nl.finalist.server.model.DTO;

import nl.finalist.server.model.Project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectOutput {
    public Long id;
    public String name;
    public String owner;
    public String createdAt;
    public List<FileInfoOutput> fileInfos;

    public static ProjectOutput fromProject(Project project) {
        var dto = new ProjectOutput();

        dto.id = project.getId();
        dto.name = project.getName();
        dto.owner = project.getOwner();
        dto.createdAt = project.getCreatedAt().toString();
        dto.fileInfos = project.getFileInfos().stream().map(f -> FileInfoOutput.fromFileInfo(f)).collect(Collectors.toList());

        return dto;
    }
}
