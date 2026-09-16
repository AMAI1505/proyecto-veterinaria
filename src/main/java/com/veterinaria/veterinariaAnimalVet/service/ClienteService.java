package com.veterinaria.veterinariaAnimalVet.service;

import com.veterinaria.veterinariaAnimalVet.model.Cliente;
import com.veterinaria.veterinariaAnimalVet.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    // Listar todos los clientes → SELECT * FROM clientes
    public List<Cliente> findAll() {
        return repo.findAll();
    }

    // Guardar (INSERT si es nuevo, UPDATE si ya tiene ID)
    public Cliente save(Cliente cliente) {
        return repo.save(cliente);
    }

    // Buscar por ID → SELECT * FROM clientes WHERE id=?
    public Optional<Cliente> findById(Long id) {
        return repo.findById(id);
    }

    // Eliminar → DELETE FROM clientes WHERE id=?
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Verificar si existe
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }
}