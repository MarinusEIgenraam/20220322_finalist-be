package nl.finalist.server.model.DTO;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Project;

public class ProjectInput {
    public Long id;
    public String name;
    public String owner;
    public Long createdAt;

    public Project toProject() {
        var project = new Project();

        project.setName(name);
        project.setOwner(owner);

        return project;
    }
}
