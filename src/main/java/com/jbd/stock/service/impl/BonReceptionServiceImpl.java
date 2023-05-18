package com.jbd.stock.service.impl;

import com.jbd.stock.domain.BonReception;
import com.jbd.stock.repository.BonReceptionRepository;
import com.jbd.stock.service.BonReceptionService;
import com.jbd.stock.service.dto.BonReceptionDTO;
import com.jbd.stock.service.mapper.BonReceptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BonReception}.
 */
@Service
@Transactional
public class BonReceptionServiceImpl implements BonReceptionService {

    private final Logger log = LoggerFactory.getLogger(BonReceptionServiceImpl.class);

    private final BonReceptionRepository bonReceptionRepository;

    private final BonReceptionMapper bonReceptionMapper;

    public BonReceptionServiceImpl(BonReceptionRepository bonReceptionRepository, BonReceptionMapper bonReceptionMapper) {
        this.bonReceptionRepository = bonReceptionRepository;
        this.bonReceptionMapper = bonReceptionMapper;
    }

    @Override
    public BonReceptionDTO save(BonReceptionDTO bonReceptionDTO) {
        log.debug("Request to save BonReception : {}", bonReceptionDTO);
        BonReception bonReception = bonReceptionMapper.toEntity(bonReceptionDTO);
        bonReception = bonReceptionRepository.save(bonReception);
        return bonReceptionMapper.toDto(bonReception);
    }

    @Override
    public BonReceptionDTO update(BonReceptionDTO bonReceptionDTO) {
        log.debug("Request to update BonReception : {}", bonReceptionDTO);
        BonReception bonReception = bonReceptionMapper.toEntity(bonReceptionDTO);
        bonReception = bonReceptionRepository.save(bonReception);
        return bonReceptionMapper.toDto(bonReception);
    }

    @Override
    public Optional<BonReceptionDTO> partialUpdate(BonReceptionDTO bonReceptionDTO) {
        log.debug("Request to partially update BonReception : {}", bonReceptionDTO);

        return bonReceptionRepository
            .findById(bonReceptionDTO.getId())
            .map(existingBonReception -> {
                bonReceptionMapper.partialUpdate(existingBonReception, bonReceptionDTO);

                return existingBonReception;
            })
            .map(bonReceptionRepository::save)
            .map(bonReceptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BonReceptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BonReceptions");
        return bonReceptionRepository.findAll(pageable).map(bonReceptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BonReceptionDTO> findOne(Long id) {
        log.debug("Request to get BonReception : {}", id);
        return bonReceptionRepository.findById(id).map(bonReceptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BonReception : {}", id);
        bonReceptionRepository.deleteById(id);
    }
}
