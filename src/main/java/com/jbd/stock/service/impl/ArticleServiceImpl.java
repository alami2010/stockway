package com.jbd.stock.service.impl;

import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.Category;
import com.jbd.stock.repository.ArticleRepository;
import com.jbd.stock.repository.CategoryRepository;
import com.jbd.stock.service.ArticleService;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.mapper.ArticleMapper;
import com.jbd.stock.service.mapper.CategoryMapper;
import com.jbd.stock.utils.Utils;
import com.jbd.stock.web.rest.EXPORT_FILE_TYPE;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final CategoryMapper categoryMapper;

    public ArticleServiceImpl(
        ArticleRepository articleRepository,
        CategoryRepository categoryRepository,
        ArticleMapper articleMapper,
        CategoryMapper categoryMapper
    ) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
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
        switch (fileType) {
            case ARTICLE_UPLOAD:
                return Utils.generateCsvFileForUploadArticle();
            case CATEGORY_LIST:
                return Utils.generateCategoryList(categoryRepository.findAll());
            case INVETAIRE_UPLOAD:
                return Utils.generateIventaireFile();
        }

        throw new Exception("File name not reconized" + fileType);
    }

    @Override
    public List<String> porocessUploadFile(MultipartFile uploadFile, boolean isCsv) throws Exception {
        return isCsv ? uploadCsv(uploadFile) : uploadExcel(uploadFile);
    }

    @Override
    public File downloadHistory(Long categoryId) throws IOException {
        List<Article> articlList = categoryId == null || categoryId == 0
            ? articleRepository.findAll()
            : articleRepository.findByCategorId(categoryId);
        if (articlList == null || articlList.isEmpty()) throw new RuntimeException("aucun article a generer");
        return Utils.generateCsvFile(articlList);
    }

    @Override
    public List<String> porocessIventaire(MultipartFile uploadFile, boolean isCsv) throws IOException {
        return isCsv ? inventaireCsv(uploadFile) : inventaireExcel(uploadFile);
    }

    private List<String> inventaireExcel(MultipartFile uploadFile) {
        return null;
    }

    private List<String> inventaireCsv(MultipartFile uploadFile) throws IOException {
        BufferedReader br;
        List<String> rejectedLine = new ArrayList<>();
        String line;
        InputStream is = uploadFile.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            try {
                if (StringUtils.isNotBlank(line) && !line.contains("Code Article")) {
                    processInvenataireLine(line);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                rejectedLine.add(line);
                log.debug("error to upload line {} : {}", line, e.getMessage());
            }
        }
        return rejectedLine;
    }

    private void processInvenataireLine(String line) {
        String[] values = line.split(",");

        Article article = articleRepository.findById(Long.valueOf(values[0])).orElseThrow();
        article.setQte(Double.valueOf(values[1]));
        articleRepository.save(article);
    }

    private List<String> uploadExcel(MultipartFile uploadFile) {
        return null;
    }

    private List<String> uploadCsv(MultipartFile uploadFile) throws Exception {
        BufferedReader br;
        List<String> rejectedLine = new ArrayList<>();
        String line;
        InputStream is = uploadFile.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            try {
                if (StringUtils.isNotBlank(line) && !line.contains("Nom Article")) {
                    processUploadLine(line);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                rejectedLine.add(line);
                log.debug("error to upload line {} : {}", line, e.getMessage());
            }
        }
        return rejectedLine;
    }

    private void processUploadLine(String line) throws Exception {
        String[] values = line.split(",");
        ArticleDTO article = new ArticleDTO();
        article.setNom(values[0]);
        article.setQte(Double.valueOf(values[1]));
        article.setPrixAchat(Double.valueOf(values[2]));
        Optional<Category> category = this.categoryRepository.findById(Long.valueOf(values[3]));
        article.setCategory(categoryMapper.toDto(category.orElseThrow()));
        article.setDescription(values[4]);
        save(article);
    }
}
