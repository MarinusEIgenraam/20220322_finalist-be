package nl.finalist.server.service;

import nl.finalist.server.model.DTO.FileInfoInput;
import nl.finalist.server.model.DTO.FileInfoOutput;
import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Message;
import nl.finalist.server.model.Project;
import nl.finalist.server.repository.FileInfoRepository;
import nl.finalist.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FileInfoService {

    @Autowired
    FileInfoRepository fileInfoRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    private final Path root = Paths.get("uploads");

    public FileInfoOutput findById(Long id) {
        Optional<FileInfo> fileInfo = fileInfoRepository.findById(id);
        if (fileInfo.isEmpty()) {
            throw new IllegalStateException("File could not found for given id:" + id);
        }
        return FileInfoOutput.fromFileInfo(fileInfo.get());
    }

    public List<FileInfoOutput> findAllByProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            throw new IllegalStateException("There is no project with id: " + id);
        } else {
            List<FileInfo> fileInfos =  fileInfoRepository.findAllByProject(optionalProject.get());
            return fileInfos.stream().map(FileInfoOutput::fromFileInfo).collect(Collectors.toList());
        }
    }

    public List <FileInfoOutput> findAll() {
        List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findAll();
        return files.stream().map(FileInfoOutput::fromFileInfo).collect(Collectors.toList());
    }


    public static void example(Message bodyIn) {
        bodyIn.setName( "Hello, " + bodyIn.getName() );
        bodyIn.setId(bodyIn.getId()*10);
    }

    public void save(FileInfoInput fileInfoInput) {
        FileInfo fileInfo = fileInfoInput.toFileInfo();
        String name = new String("");
        String parentName = new String("");
        if (fileInfoInput.fileLocation.split("/").length >= 2) {
            parentName = fileInfoInput.fileLocation.split("/")[fileInfoInput.fileLocation.split("/").length - 2];
        }

        if (fileInfoInput.fileLocation.equals("")) {
            name = fileInfoInput.fileName;
        } else {
            name = fileInfoInput.fileLocation.split("/")[0];
        }
        Optional<Project> optionalProject = projectRepository.findByName(name);
        Optional<FileInfo> optionalFileInfo = fileInfoRepository.findByFileName(parentName);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            fileInfo.setProject(optionalProject.get());
            project.getFileInfos().add(fileInfo);
            project.setModifiedAt(LocalDateTime.now());
            projectRepository.save(project);
        } else {
            fileInfo.setProject(projectService.saveProject(name));
        }
        if (optionalFileInfo.isPresent()) {
            FileInfo parentInfo = optionalFileInfo.get();
            fileInfo.setParentFolder(parentInfo);
            FileInfo child = fileInfoRepository.save(fileInfo);
            fileInfoRepository.save(fileInfo);
        } else {
            fileInfoRepository.save(fileInfo);
        }
    }
}

