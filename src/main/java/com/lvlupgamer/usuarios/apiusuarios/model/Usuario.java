package com.lvlupgamer.usuarios.apiusuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.lvlupgamer.usuarios.apiusuarios.model.Rol;

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
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 100, message = "El RUT no puede exceder 100 caracteres")
    @Column(name = "rut", nullable = false, unique = true, length = 100)
    private String rut;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "puntos", nullable = false)
    @Builder.Default
    private Integer puntos = 0;
    
    @Size(max = 20, message = "El código de referido no puede exceder 20 caracteres")
    @Column(name = "codigo_referido", unique = true, length = 20)
    private String codigoReferido;
    
    @Column(name = "referido_por")
    private Long referidoPor;
    
    @Column(name = "fecha_registro")
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    // ============================================
    // RELACIÓN CON ROL (NUEVO)
    // ============================================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
    
    // Foto BLOB (OBLIGATORIO)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto", nullable = false)
    @NotNull(message = "La foto es obligatoria")
    private byte[] foto;
    
    @Column(name = "foto_nombre", length = 200)
    private String fotoNombre;
    
    @Column(name = "foto_tipo", length = 100)
    private String fotoTipo;
    
    @Column(name = "foto_tamano")
    private Long fotoTamano;
    
    @Transient
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
    
    @Transient
    public boolean esDuocUser() {
        return email != null && email.toLowerCase().endsWith("@duocuc.cl");
    }
}
