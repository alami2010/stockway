package com.jbd.stock.service.impl;

import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.repository.TypeChargeRepository;
import com.jbd.stock.service.TypeChargeService;
import com.jbd.stock.service.dto.TypeChargeDTO;
import com.jbd.stock.service.mapper.TypeChargeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeCharge}.
 */
@Service
@Transactional
public class TypeChargeServiceImpl implements TypeChargeService {

    private final Logger log = LoggerFactory.getLogger(TypeChargeServiceImpl.class);

    private final TypeChargeRepository typeChargeRepository;

    private final TypeChargeMapper typeChargeMapper;

    public TypeChargeServiceImpl(TypeChargeRepository typeChargeRepository, TypeChargeMapper typeChargeMapper) {
        this.typeChargeRepository = typeChargeRepository;
        this.typeChargeMapper = typeChargeMapper;
    }

    @Override
    public TypeChargeDTO save(TypeChargeDTO typeChargeDTO) {
        log.debug("Request to save TypeCharge : {}", typeChargeDTO);
        TypeCharge typeCharge = typeChargeMapper.toEntity(typeChargeDTO);
        typeCharge = typeChargeRepository.save(typeCharge);
        return typeChargeMapper.toDto(typeCharge);
    }

    @Override
    public TypeChargeDTO update(TypeChargeDTO typeChargeDTO) {
        log.debug("Request to update TypeCharge : {}", typeChargeDTO);
        TypeCharge typeCharge = typeChargeMapper.toEntity(typeChargeDTO);
        typeCharge = typeChargeRepository.save(typeCharge);
        return typeChargeMapper.toDto(typeCharge);
    }

    @Override
    public Optional<TypeChargeDTO> partialUpdate(TypeChargeDTO typeChargeDTO) {
        log.debug("Request to partially update TypeCharge : {}", typeChargeDTO);

        return typeChargeRepository
            .findById(typeChargeDTO.getId())
            .map(existingTypeCharge -> {
                typeChargeMapper.partialUpdate(existingTypeCharge, typeChargeDTO);

                return existingTypeCharge;
            })
            .map(typeChargeRepository::save)
            .map(typeChargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeChargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeCharges");
        return typeChargeRepository.findAll(pageable).map(typeChargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeChargeDTO> findOne(Long id) {
        log.debug("Request to get TypeCharge : {}", id);
        return typeChargeRepository.findById(id).map(typeChargeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeCharge : {}", id);
        typeChargeRepository.deleteById(id);
    }
}
