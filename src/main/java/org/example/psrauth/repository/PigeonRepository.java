package org.example.psrauth.repository;

import org.example.psrauth.model.entity.Pigeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface PigeonRepository extends JpaRepository<Pigeon,Long> {
    Optional<Pigeon> findByRingNumber(String ringNumber);
}
