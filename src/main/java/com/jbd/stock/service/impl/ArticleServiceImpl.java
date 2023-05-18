package com.jbd.stock.service.impl;

import com.jbd.stock.domain.Article;
import com.jbd.stock.repository.ArticleRepository;
import com.jbd.stock.repository.CategoryRepository;
import com.jbd.stock.service.ArticleService;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.mapper.ArticleMapper;
import com.jbd.stock.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import com.jbd.stock.web.rest.EXPORT_FILE_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article.setDateCreation(LocalDate.now());
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    @Override
    public ArticleDTO update(ArticleDTO articleDTO) {
        log.debug("Request to update Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    @Override
    public Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO) {
        log.debug("Request to partially update Article : {}", articleDTO);

        return articleRepository
            .findById(articleDTO.getId())
            .map(existingArticle -> {
                articleMapper.partialUpdate(existingArticle, articleDTO);

                return existingArticle;
            })
            .map(articleRepository::save)
            .map(articleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable).map(articleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id).map(articleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }

    @Override
    public File downloadFile(EXPORT_FILE_TYPE fileType) throws Exception {
        switch (fileType){
            case ARTICLE_UPLOAD:
                return Utils.generateCsvFileForUploadArticle();
            case CATEGORY_LIST:
                return Utils.generateCategoryList(categoryRepository.findAll());


        }

        throw  new Exception("File name not reconized"+fileType);
    }
}
