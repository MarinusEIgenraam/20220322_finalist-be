package nl.finalist.server.model.DTO;

import nl.finalist.server.model.Project;
import java.time.LocalDateTime;

public class ProjectInput {
    public Long id;
    public String name;
    public String owner;
    public LocalDateTime modifiedAt;

    public Project toProject() {
        var project = new Project();

        project.setName(name);
        project.setOwner(owner);
        project.setModifiedAt(modifiedAt);

        return project;
    }
}
