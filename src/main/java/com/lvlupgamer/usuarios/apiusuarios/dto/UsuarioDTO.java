package com.lvlupgamer.usuarios.apiusuarios.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.Base64;

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
    
    // CAMPOS DE ROL (NUEVO)
    private Long idRol;
    private String nombreRol;
    
    private String fotoNombre;
    private String fotoTipo;
    private Long fotoTamano;
    private String fotoBase64;
    
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
    
    public void setFotoFromBytes(byte[] fotoBytes) {
        if (fotoBytes != null) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
        }
    }
}
