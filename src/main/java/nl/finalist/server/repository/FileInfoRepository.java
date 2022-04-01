package nl.finalist.server.repository;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
    List<FileInfo> findAllByProject(Project project);
}
