package nl.finalist.server.service;

import nl.finalist.server.model.DTO.ProjectOutput;
import nl.finalist.server.model.Project;
import nl.finalist.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project saveProject(String projectName) {
        Project project = Project.builder()
                .name(projectName)
                .modifiedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .owner("Rinus")
                .build();

        return projectRepository.save(project);
    }

    public ProjectOutput getProject(Long id) {
        var optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            return ProjectOutput.fromProject(optionalProject.get());
        } else {
            throw new RuntimeException("Project does not exist");
        }
    }
}
