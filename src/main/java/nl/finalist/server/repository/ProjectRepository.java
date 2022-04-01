package nl.finalist.server.repository;

import nl.finalist.server.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Optional<Project> findByName(String projectName);
}
