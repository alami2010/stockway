package com.jbd.stock.repository;

import com.jbd.stock.domain.Article;
import com.jbd.stock.service.dto.StatisticsCount;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    @Query("select a from Article a where a.category.id = :id")
    List<Article> findByCategorId(@Param("id") Long id);

    Optional<Article> findByCode(String code);

    @Query(
        "SELECT new com.jbd.stock.service.dto.StatisticsCount(a.category.libelle, COUNT(a)) " +
        "FROM  Article a " +
        "GROUP BY a.category.libelle"
    )
    List<StatisticsCount> findStatCategorie();

    @Query("select a from Article a where a.qteAlert >= a.qte and a.qteAlert <> 0")
    List<Article> findArticleOutOfStock();
}
