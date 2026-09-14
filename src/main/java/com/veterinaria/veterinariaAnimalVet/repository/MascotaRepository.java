package com.veterinaria.veterinariaAnimalVet.repository;

import com.veterinaria.veterinariaAnimalVet.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

}

