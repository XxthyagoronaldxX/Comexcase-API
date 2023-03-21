package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.ParticularityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IParticularityRepository extends JpaRepository<ParticularityEntity, UUID> {

}
