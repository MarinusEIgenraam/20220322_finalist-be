package nl.finalist.server.service;

import nl.finalist.server.model.Project;
import nl.finalist.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project saveProject(String projectName) {
        Project project = Project.builder()
                .name(projectName)
                .owner("Rinus")
                .build();

        return projectRepository.save(project);
    }
}
