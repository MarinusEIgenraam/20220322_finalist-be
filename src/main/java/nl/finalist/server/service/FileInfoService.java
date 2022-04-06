package nl.finalist.server.service;

import nl.finalist.server.exception.RecordNotFoundException;
import nl.finalist.server.model.DTO.FileInfoInput;
import nl.finalist.server.model.DTO.FileInfoOutput;
import nl.finalist.server.model.FileInfo;
import nl.finalist.server.model.Message;
import nl.finalist.server.model.Project;
import nl.finalist.server.repository.FileInfoRepository;
import nl.finalist.server.repository.ProjectRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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


    public void updateFile(FileInfoInput fileInfoInput) {
        FileInfo fileInfo = fileInfoInput.toFileInfo();
        var optionalInfoToUpdate = fileInfoRepository.findByFileDirectory(fileInfoInput.fileDirectory);
        if (optionalInfoToUpdate.isPresent()) {
            FileInfo fileInfoToUpdate = optionalInfoToUpdate.get();
            fileInfoToUpdate.setLastEvent(fileInfoInput.lastEvent);
            fileInfoToUpdate.setModifiedAt(LocalDateTime.now());
            fileInfoRepository.save(fileInfoToUpdate);
            projectService.updateProject(fileInfoToUpdate.getFileDirectory().split("/")[0]);
        } else {
            throw new RecordNotFoundException("You can not update this file since it does not exist");
        }
    }

    public void deleteFile(FileInfoInput fileInfoInput) {
        var fileInfoToDelete = fileInfoRepository.findByFileDirectory(fileInfoInput.fileDirectory);
        if (fileInfoToDelete.isPresent()) {
            fileInfoRepository.delete(fileInfoToDelete.get());

            projectService.deleteProjectByFile(fileInfoToDelete.get().getFileDirectory().split("/")[0]);

        } else {
            throw new RecordNotFoundException("this fileInfo does not exist");
        }
    }

    public void saveFileInfo(FileInfoInput fileInfoInput) {
        FileInfo fileInfo;
        if (fileInfoRepository.findByFileDirectory(fileInfoInput.fileDirectory).isPresent()) {
            fileInfo = fileInfoRepository.findByFileDirectory(fileInfoInput.fileDirectory).get();
        } else {
            fileInfo = fileInfoInput.toFileInfo();
        }


        File file = new File(fileInfoInput.savedDirectory);
        fileInfo.setFileSize(FileUtils.sizeOf(file));

        // Folder
        // Folder/Folder/Folder/File.js
        String separator = "/";
        String[] folderFileStructure = fileInfoInput.fileDirectory.split(separator);
        // [Folder] 1
        // ["Folder","Folder","Folder","File.js"] 4

        for (int i = 0; i < folderFileStructure.length; i++) {
            String[] arrayToSave = Arrays.copyOf(folderFileStructure, i + 1);
            String dirToSave = StringUtils.join(arrayToSave, separator);
            FileInfo savedFileInfo;

            if (!fileInfoRepository.findByFileDirectory(dirToSave).isPresent()) {
                savedFileInfo = saveFolder(dirToSave);
            } else {
                savedFileInfo = fileInfoRepository.findByFileDirectory(dirToSave).get();
            }

            String[] parentArray = Arrays.copyOf(arrayToSave, arrayToSave.length - 1);
            String parentString = StringUtils.join(parentArray, separator);

            if (i == 0) {
                var optionalProject = projectRepository.findByName(folderFileStructure[0]);
                if (optionalProject.isPresent()) {
                    savedFileInfo.setProject(optionalProject.get());
                } else {
                    Project newProject = projectService.saveProject(folderFileStructure[0]);
                    savedFileInfo.setProject(newProject);
                }
            }

            if (i > 0) {
                var optionalParent = fileInfoRepository.findByFileDirectory(parentString);
                if (optionalParent.isPresent()) {
                    savedFileInfo.setParentFolder(optionalParent.get());
                }
            }
            fileInfoRepository.save(savedFileInfo);
        }

        if (fileInfoInput.fileName.contains(".")) {
            fileInfo.setFileType(getExtensionByApacheCommonLib(fileInfoInput.fileName));
        } else {
            fileInfo.setFileType("folder");
        }

        String[] parentArray = Arrays.copyOf(folderFileStructure, folderFileStructure.length - 1);
        String parentString = StringUtils.join(parentArray, separator);

        var optionalParent = fileInfoRepository.findByFileDirectory(parentString);
        if (optionalParent.isPresent()) {
            fileInfo.setParentFolder(optionalParent.get());
        }

        var optionalFileInfo = fileInfoRepository.findByFileDirectory(fileInfo.getFileDirectory());

        if (optionalFileInfo.isPresent()) {
            fileInfo.setId(optionalFileInfo.get().getId());
            fileInfo.setCreatedAt(optionalFileInfo.get().getCreatedAt());
            fileInfoRepository.save(fileInfo);
        } else {
            fileInfo.setCreatedAt(LocalDateTime.now());
            fileInfoRepository.save(fileInfo);
        }
    }

    public static void example(Message bodyIn) {
        bodyIn.setName("Hello, " + bodyIn.getName());
        bodyIn.setId(bodyIn.getId() * 10);
    }

    public FileInfo saveFolder(String folderDirectory) {
        var optionalFileInfo = fileInfoRepository.findByFileDirectory(folderDirectory);
        if (optionalFileInfo.isPresent()) {
            return optionalFileInfo.get();
        } else {
            FileInfo fileInfo = FileInfo.builder()
                    .fileDirectory(folderDirectory)
                    .build();
            return fileInfoRepository.save(fileInfo);
        }
    }

    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}

