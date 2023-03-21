package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.entities.ParticularityEntity;
import com.startup.comexcase_api.domain.repositories.IParticularityRepository;
import com.startup.comexcase_api.domain.services.IParticularityService;
import org.springframework.stereotype.Service;

@Service
public class ParticularityService implements IParticularityService {
    private final IParticularityRepository particularityRepository;

    public ParticularityService(IParticularityRepository particularityRepository) {
        this.particularityRepository = particularityRepository;
    }

    @Override
    public Iterable<ParticularityEntity> findAll() {
        return particularityRepository.findAll();
    }
}
