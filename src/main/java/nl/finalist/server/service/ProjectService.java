package nl.finalist.server.service;

import nl.finalist.server.model.Project;
import nl.finalist.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project saveProject(String projectName) {
        Project project = Project.builder()
                .name(projectName)
                .createdAt(Instant.now())
                .owner("Rinus")
                .build();

        return projectRepository.save(project);
    }
}
