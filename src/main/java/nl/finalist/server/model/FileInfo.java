package nl.finalist.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "files")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nullable
    private String fileName;
    @Nullable
    private String fileDirectory;
    @Nullable
    private String fileType;
    @Nullable
    private long fileSize;
    @Nullable
    private String savedDirectory;
    @Nullable
    private String lastEvent;

    @Nullable
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne
    @JsonBackReference("projectFiles")
    private Project project;

    @Nullable
    @ManyToOne
    @JsonBackReference("folderFiles")
    private FileInfo parentFolder;

    @Nullable
    @OneToMany(mappedBy = "parentFolder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference("folderFiles")
    private List<FileInfo> fileList;
}
