package nl.finalist.server.service;

import nl.finalist.server.model.DTO.FileInfoInput;
import nl.finalist.server.model.DTO.FileInfoOutput;
import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Message;
import nl.finalist.server.model.Project;
import nl.finalist.server.repository.FileInfoRepository;
import nl.finalist.server.repository.ProjectRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
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
            List<FileInfo> fileInfos = fileInfoRepository.findAllByProject(optionalProject.get());
            return fileInfos.stream().map(FileInfoOutput::fromFileInfo).collect(Collectors.toList());
        }
    }

    public List<FileInfoOutput> findAll() {
        List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findAll();
        return files.stream().map(FileInfoOutput::fromFileInfo).collect(Collectors.toList());
    }


    public static void example(Message bodyIn) {
        bodyIn.setName("Hello, " + bodyIn.getName());
        bodyIn.setId(bodyIn.getId() * 10);
    }

    public FileInfo saveFile(String fileName) {
        var optionalFileInfo = fileInfoRepository.findByFileName(fileName);
        if (optionalFileInfo.isPresent()) {
            return optionalFileInfo.get();
        } else {
            FileInfo fileInfo = FileInfo.builder()
                    .fileName(fileName)
                    .build();
            return fileInfoRepository.save(fileInfo);
        }
    }

    // folder/folder/folder/folder/file.file
    public void saveFileInfo(FileInfoInput fileInfoInput) {
        FileInfo fileInfo = fileInfoInput.toFileInfo();
        File file = new File(fileInfoInput.savedDirectory);
        fileInfo.setFileSize(FileUtils.sizeOf(file));

        String[] folderFileStructure = fileInfoInput.fileDirectory.split("/");
        String[] folderStructure = Arrays.copyOf(folderFileStructure, folderFileStructure.length - 1);

        // For every folder check if file info exists
        for (int i = 0; i < folderStructure.length; i++) {
            FileInfo savedFileInfo = saveFile(folderStructure[i]);

            if (i == 0) {
                var optionalProject = projectRepository.findByName(folderStructure[0]);
                if (optionalProject.isPresent()) {
                    var optionalFileInfo = fileInfoRepository.findByFileName(folderStructure[0]);
                    if (optionalFileInfo.isPresent() && optionalFileInfo.get().getProject() == null) {
                        FileInfo newFileInfo = optionalFileInfo.get();
                        newFileInfo.setProject(optionalProject.get());
                        fileInfoRepository.save(newFileInfo);
                    }
                } else {
                    Project newProject = projectService.saveProject(folderStructure[0]);
                    savedFileInfo.setProject(newProject);
                    fileInfoRepository.save(savedFileInfo);
                }
            }

            if (i > 0) {
                var optionalParent = fileInfoRepository.findByFileName(folderFileStructure[i - 1]);
                if (optionalParent.isPresent()) {
                    optionalParent.get().getFileList().add(savedFileInfo);
                }
            }
        }

        // Set filetype
        if (fileInfoInput.fileName.contains(".")) {
            fileInfo.setFileType(getExtensionByApacheCommonLib(fileInfoInput.fileName));
        } else {
            fileInfo.setFileType("folder");
        }


        if (folderStructure.length >= 1) {
            var optionalParent = fileInfoRepository.findByFileName(folderStructure[folderStructure.length - 1]);
            if (optionalParent.isPresent()) {
                fileInfo.setParentFolder(optionalParent.get());
            }
        }
        fileInfo.setCreatedAt(LocalDateTime.now());
        fileInfoRepository.save(fileInfo);
    }

    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}

