package nl.finalist.server.repository;

import nl.finalist.server.model.FileInfo;
import org.springframework.data.repository.CrudRepository;

public interface FileInfoRepository extends CrudRepository<FileInfo, String> {
}
