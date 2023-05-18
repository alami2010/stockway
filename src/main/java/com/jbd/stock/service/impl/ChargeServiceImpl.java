package com.jbd.stock.service.impl;

import com.jbd.stock.domain.Charge;
import com.jbd.stock.repository.ChargeRepository;
import com.jbd.stock.service.ChargeService;
import com.jbd.stock.service.dto.ChargeDTO;
import com.jbd.stock.service.mapper.ChargeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Charge}.
 */
@Service
@Transactional
public class ChargeServiceImpl implements ChargeService {

    private final Logger log = LoggerFactory.getLogger(ChargeServiceImpl.class);

    private final ChargeRepository chargeRepository;

    private final ChargeMapper chargeMapper;

    public ChargeServiceImpl(ChargeRepository chargeRepository, ChargeMapper chargeMapper) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
    }

    @Override
    public ChargeDTO save(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    @Override
    public ChargeDTO update(ChargeDTO chargeDTO) {
        log.debug("Request to update Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    @Override
    public Optional<ChargeDTO> partialUpdate(ChargeDTO chargeDTO) {
        log.debug("Request to partially update Charge : {}", chargeDTO);

        return chargeRepository
            .findById(chargeDTO.getId())
            .map(existingCharge -> {
                chargeMapper.partialUpdate(existingCharge, chargeDTO);

                return existingCharge;
            })
            .map(chargeRepository::save)
            .map(chargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Charges");
        return chargeRepository.findAll(pageable).map(chargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChargeDTO> findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        return chargeRepository.findById(id).map(chargeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.deleteById(id);
    }
}
