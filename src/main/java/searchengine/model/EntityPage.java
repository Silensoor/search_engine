package searchengine.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "page")
public class EntityPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private EntitySite site;

    @Column(columnDefinition = "TEXT NOT NULL, UNIQUE KEY uk_site_path(path(1000),site_id)")
    private String path;
    @Column(nullable = false)
    private Integer code;
    @Column(columnDefinition = "MEDIUMTEXT CHARACTER SET  utf8mb4 COLLATE  utf8mb4_general_ci", nullable = false)
    private String content;

}
