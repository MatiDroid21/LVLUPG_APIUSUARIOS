package com.lvlupgamer.usuarios.apiusuarios.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "SEQ_ROLES", allocationSize = 1)
    @Column(name = "id_rol")
    private Long idRol;
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
    @Column(name = "descripcion", length = 200)
    private String descripcion;
}
