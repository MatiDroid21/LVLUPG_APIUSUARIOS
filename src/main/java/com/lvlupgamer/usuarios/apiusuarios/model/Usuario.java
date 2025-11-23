package com.lvlupgamer.usuarios.apiusuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "SEQ_USUARIOS", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Column(name = "rut", nullable = false, unique = true)
    private String rut;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Past
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @Size(max = 20)
    @Column(name = "codigo_referido", unique = true)
    private String codigoReferido;

    @Column(name = "referido_por")
    private Long referidoPor;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto", nullable = false)
    private byte[] foto;

    @Column(name = "foto_nombre", length = 200)
    private String fotoNombre;

    @Column(name = "foto_tipo", length = 100)
    private String fotoTipo;

    @Column(name = "foto_tamano")
    private Long fotoTamano;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "uid_firebase", length = 100, unique = true)
    private String uidFirebase;
    
}
