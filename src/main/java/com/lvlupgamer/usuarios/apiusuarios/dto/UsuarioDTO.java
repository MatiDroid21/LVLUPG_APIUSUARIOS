package com.lvlupgamer.usuarios.apiusuarios.dto;

import java.time.LocalDate;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String rut;
    private String email;
    private LocalDate fechaNacimiento;
    private Integer puntos;
    private String codigoReferido;
    private Long referidoPor;
    private String rolNombre;

    // Agrega estos campos NUEVOS
    private String direccion;
    private String telefono;

    // Información de la foto
    private String fotoNombre;
    private String fotoTipo;
    private Long fotoTamano;
}
