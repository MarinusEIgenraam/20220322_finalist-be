package nl.finalist.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfo {

    private String fileName;
    private String fileLocation;

}
