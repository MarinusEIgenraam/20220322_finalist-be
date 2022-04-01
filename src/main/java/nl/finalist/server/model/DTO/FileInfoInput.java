package nl.finalist.server.model.DTO;

import nl.finalist.server.model.FileInfo;

public class FileInfoInput {
    public Long id;
    public String fileName;
    public String fileLocation;
    public String lastEvent;
    public String createdAt;
    public Long modifiedAt;
    public String projectName;

    public FileInfo toFileInfo() {
        var fileInfo = new FileInfo();

        fileInfo.setFileName(fileName);
        fileInfo.setFileLocation(fileLocation);
        fileInfo.setLastEvent(lastEvent);
        fileInfo.setModifiedAt(modifiedAt);

        return fileInfo;
    }
}
