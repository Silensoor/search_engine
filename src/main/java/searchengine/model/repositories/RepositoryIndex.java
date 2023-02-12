package searchengine.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.EntityIndex;
import searchengine.model.EntityLemma;
import searchengine.model.EntityPage;

import java.util.List;

@Repository
public interface RepositoryIndex extends JpaRepository<EntityIndex, Integer> {


    @Query("SELECT i from EntityIndex i WHERE i.lemma IN :lemmas AND i.page IN :pages")
    List<EntityIndex> findByPagesAndLemmas(@Param("lemmas") List<EntityLemma> lemmaListId,
                                           @Param("pages") List<EntityPage> pageListId);

    EntityIndex findByPageAndLemma(EntityPage entityPage, EntityLemma entityLemma);
}
