package nl.finalist.server.repository;

import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByProject(Project project);

    Optional<FileInfo> findByFileDirectory(String s);

//    @Query(
//            "SELECT f FROM FileInfo f WHERE f.folder = true")
//    Optional<FileInfo> findByFileDirectoryWhereFolderIsTrue(String fileDirectory);

    Optional<FileInfo> findByFileName(String s);

}
