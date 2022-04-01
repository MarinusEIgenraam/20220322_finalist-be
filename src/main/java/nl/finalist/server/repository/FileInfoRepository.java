package nl.finalist.server.repository;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
    List<FileInfo> findAllByProject(Project project);
}
