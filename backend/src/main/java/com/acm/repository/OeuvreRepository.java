package com.acm.repository;

import com.acm.entity.Oeuvre;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OeuvreRepository extends JpaRepository<Oeuvre, Long> {

    @Override
    @EntityGraph(attributePaths = "artiste")
    List<Oeuvre> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = "artiste")
    Optional<Oeuvre> findById(Long id);

    @EntityGraph(attributePaths = "artiste")
    List<Oeuvre> findByArtisteId(Long artisteId, Sort sort);
    
    @EntityGraph(attributePaths = "artiste")
    List<Oeuvre> findTop3ByOrderByIdDesc();
    
    long countByArtisteId(Long artisteId);

    @Query("select o.artiste.id as artisteId, count(o) as total from Oeuvre o group by o.artiste.id")
    List<ArtisteCount> countByArtiste();

    interface ArtisteCount {
        Long getArtisteId();
        Long getTotal();
    }
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Oeuvre o where o.artiste.id = :artisteId")
    int deleteByArtisteId(@Param("artisteId") Long artisteId);

    @Query("select o.technique as technique, count(o) as total from Oeuvre o group by o.technique order by count(o) desc")
    List<TechniqueStat> countByTechnique();

    interface TechniqueStat {
        String getTechnique();
        Long getTotal();
    }
}