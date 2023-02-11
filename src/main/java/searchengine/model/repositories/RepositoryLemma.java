package searchengine.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.EntityLemma;
import searchengine.model.EntitySite;

import java.util.List;
import java.util.Set;


public interface RepositoryLemma extends JpaRepository<EntityLemma, Integer> {

    EntityLemma findByLemmaAndSite(String lemma, EntitySite entitySite);

    Integer countBySite(EntitySite entitySite);

    @Query("select a from EntityLemma as a where a.frequency<300 and a.lemma in (:lemmas) and a.site=:entitySite")
    List<EntityLemma> selectLemmasBySite(@Param("lemmas") Set<String> lemmas, @Param("entitySite") EntitySite site);

}
