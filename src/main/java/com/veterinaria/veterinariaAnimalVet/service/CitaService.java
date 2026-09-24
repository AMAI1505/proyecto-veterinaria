package com.veterinaria.veterinariaAnimalVet.service;

import com.veterinaria.veterinariaAnimalVet.model.Cita;
import com.veterinaria.veterinariaAnimalVet.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository repo;

    public CitaService(CitaRepository repo) {
        this.repo = repo;
    }

    // Listar todas las citas → SELECT * FROM citas
    public List<Cita> findAll() {
        return repo.findAll();
    }

    // Guardar (INSERT si es nueva, UPDATE si ya tiene ID)
    public Cita save(Cita cita) {
        return repo.save(cita);
    }

    // Buscar por ID → SELECT * FROM citas WHERE id=?
    public Optional<Cita> findById(Long id) {
        return repo.findById(id);
    }

    // Eliminar → DELETE FROM citas WHERE id=?
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Verificar si existe
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public CitaRepository getRepo() {
        return repo;
    }

    public void setRepo(CitaRepository repo) {
        this.repo = repo;
    }
}