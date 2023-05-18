package com.jbd.stock.service.impl;

import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.repository.BonReceptionItemRepository;
import com.jbd.stock.service.BonReceptionItemService;
import com.jbd.stock.service.dto.BonReceptionItemDTO;
import com.jbd.stock.service.mapper.BonReceptionItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BonReceptionItem}.
 */
@Service
@Transactional
public class BonReceptionItemServiceImpl implements BonReceptionItemService {

    private final Logger log = LoggerFactory.getLogger(BonReceptionItemServiceImpl.class);

    private final BonReceptionItemRepository bonReceptionItemRepository;

    private final BonReceptionItemMapper bonReceptionItemMapper;

    public BonReceptionItemServiceImpl(
        BonReceptionItemRepository bonReceptionItemRepository,
        BonReceptionItemMapper bonReceptionItemMapper
    ) {
        this.bonReceptionItemRepository = bonReceptionItemRepository;
        this.bonReceptionItemMapper = bonReceptionItemMapper;
    }

    @Override
    public BonReceptionItemDTO save(BonReceptionItemDTO bonReceptionItemDTO) {
        log.debug("Request to save BonReceptionItem : {}", bonReceptionItemDTO);
        BonReceptionItem bonReceptionItem = bonReceptionItemMapper.toEntity(bonReceptionItemDTO);
        bonReceptionItem = bonReceptionItemRepository.save(bonReceptionItem);
        return bonReceptionItemMapper.toDto(bonReceptionItem);
    }

    @Override
    public BonReceptionItemDTO update(BonReceptionItemDTO bonReceptionItemDTO) {
        log.debug("Request to update BonReceptionItem : {}", bonReceptionItemDTO);
        BonReceptionItem bonReceptionItem = bonReceptionItemMapper.toEntity(bonReceptionItemDTO);
        bonReceptionItem = bonReceptionItemRepository.save(bonReceptionItem);
        return bonReceptionItemMapper.toDto(bonReceptionItem);
    }

    @Override
    public Optional<BonReceptionItemDTO> partialUpdate(BonReceptionItemDTO bonReceptionItemDTO) {
        log.debug("Request to partially update BonReceptionItem : {}", bonReceptionItemDTO);

        return bonReceptionItemRepository
            .findById(bonReceptionItemDTO.getId())
            .map(existingBonReceptionItem -> {
                bonReceptionItemMapper.partialUpdate(existingBonReceptionItem, bonReceptionItemDTO);

                return existingBonReceptionItem;
            })
            .map(bonReceptionItemRepository::save)
            .map(bonReceptionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BonReceptionItemDTO> findAll() {
        log.debug("Request to get all BonReceptionItems");
        return bonReceptionItemRepository
            .findAll()
            .stream()
            .map(bonReceptionItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BonReceptionItemDTO> findOne(Long id) {
        log.debug("Request to get BonReceptionItem : {}", id);
        return bonReceptionItemRepository.findById(id).map(bonReceptionItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BonReceptionItem : {}", id);
        bonReceptionItemRepository.deleteById(id);
    }
}
