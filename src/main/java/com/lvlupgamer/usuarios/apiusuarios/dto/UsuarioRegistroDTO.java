package com.lvlupgamer.usuarios.apiusuarios.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegistroDTO {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    private String rut;

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @Size(min = 6, max = 100)
    @NotBlank
    private String contrasena;

    @Past
    private LocalDate fechaNacimiento;

    @NotNull
    private Long idRol;

    private String codigoReferido;

    @NotNull
    private MultipartFile foto; // para recibir la imagen en multipart/form-data
    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

}
