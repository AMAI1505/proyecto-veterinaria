package com.veterinaria.veterinariaAnimalVet.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La fecha y hora son obligatorias")
    @Column(nullable = false, unique = true)
    private LocalDateTime fechaHora;
    
    @NotBlank(message = "El motivo es obligatorio")
    @Size(min=3, max = 100, message = "El motivo debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length=100)
    private String motivo;
    
    @NotBlank(message = "El estado de la cita es obligatorio")
    private String estado;
}