package nl.finalist.server.service;

import nl.finalist.server.exception.BadRequestException;
import nl.finalist.server.exception.RecordNotFoundException;
import nl.finalist.server.model.DTO.FileInfoOutput;
import nl.finalist.server.model.DTO.ProjectOutput;
import nl.finalist.server.model.Project;
import nl.finalist.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProjectOutput> getProjects() {
        List<Project> projects = (List<Project>) projectRepository.findAll();
        return projects.stream().map(ProjectOutput::fromProject).collect(Collectors.toList());
    }
    public void updateProject(String projectName) {
        var optionalProjectToUpdate = projectRepository.findByName(projectName);
        if (optionalProjectToUpdate.isPresent()) {
            Project projectToUpdate = optionalProjectToUpdate.get();
            projectToUpdate.setModifiedAt(LocalDateTime.now());
            projectRepository.save(projectToUpdate);
        } else {
            throw new RecordNotFoundException("This project does not exist");
        }
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public void deleteProjectByFile(String projectName) {
        var optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isPresent()) {
            if (optionalProject.get().getFileInfo() == null) {
                projectRepository.delete(optionalProject.get());
            } else {
                System.out.println("this project still contains files");
            }
        } else {
            throw new BadRequestException("this project does not exist");
        }
    }
}
