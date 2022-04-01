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

    private String fileName;

    private String fileLocation;

    private String lastEvent;

    private LocalDateTime createdAt = LocalDateTime.now();
    @Nullable
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JsonBackReference("projectFiles")
    private Project project;

    @Nullable
    @ManyToOne
    @JsonBackReference("folderFiles")
    private FileInfo parentFolder;

    @Nullable
    @OneToMany(mappedBy = "parentFolder", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("folderFiles")
    private List<FileInfo> fileList;
}
