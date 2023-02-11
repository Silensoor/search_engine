package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "lemma")
public class EntityLemma implements Comparable<EntityLemma> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private EntitySite site;
    @Column(columnDefinition = "VARCHAR(255), UNIQUE KEY lemma_and_site(lemma,site_id)")
    private String lemma;
    @Column(nullable = false)
    private Integer frequency;

    @Override
    public int compareTo(EntityLemma entityLemma) {
        return this.frequency.compareTo(entityLemma.frequency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityLemma that = (EntityLemma) o;
        return site.equals(that.site) && lemma.equals(that.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, lemma);
    }
}
