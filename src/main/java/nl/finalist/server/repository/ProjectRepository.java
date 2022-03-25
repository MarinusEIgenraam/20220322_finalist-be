package nl.finalist.server.repository;

import nl.finalist.server.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
