package com.veterinaria.veterinariaAnimalVet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(min=3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    @Column(nullable = false, length = 40)
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(min=3, max = 30, message = "La especie debe tener entre 3 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    @Size(min=3, max = 40, message = "La raza debe tener entre 3 y 40 caracteres")
    @Column(nullable = false, length = 40)
    private String raza;

    @NotBlank(message = "El peso es obligatorio")
    @Size(max = 6, message = "El peso no puede exceder los 6 caracteres")
    @Column(nullable = false, length = 6)
    private String peso;

    @NotBlank(message = "El color es obligatorio")
    @Size(min=3, max = 30, message = "El color debe tener entre 3 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String color;

}