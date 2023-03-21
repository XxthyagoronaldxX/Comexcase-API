package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.entities.ParticularityEntity;

public interface IParticularityService {
    Iterable<ParticularityEntity> findAll();
}
