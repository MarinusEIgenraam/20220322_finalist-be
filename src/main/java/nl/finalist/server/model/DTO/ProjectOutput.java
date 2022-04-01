package nl.finalist.server.model.DTO;

import nl.finalist.server.model.Project;

import java.time.LocalDateTime;

public class ProjectOutput {
    public Long id;
    public String name;
    public String owner;
    public String createdAt;

    public static ProjectOutput fromProject(Project project) {
        var dto = new ProjectOutput();

        dto.id = project.getId();
        dto.name = project.getName();
        dto.owner = project.getOwner();
        dto.createdAt = project.getCreatedAt().toString();

        return dto;
    }
}
