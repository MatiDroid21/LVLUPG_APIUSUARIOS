package com.lvlupgamer.usuarios.apiusuarios.mapper;

import com.lvlupgamer.usuarios.apiusuarios.model.Usuario;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .rut(usuario.getRut())
                .email(usuario.getEmail())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .puntos(usuario.getPuntos())
                .codigoReferido(usuario.getCodigoReferido())
                // MAPEA EL ROL
                .idRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null)
                .nombreRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .fotoNombre(usuario.getFotoNombre())
                .fotoTipo(usuario.getFotoTipo())
                .fotoTamano(usuario.getFotoTamano())
                .build();

        if (usuario.getFoto() != null) {
            dto.setFotoFromBytes(usuario.getFoto());
        }

        return dto;
    }

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .idUsuario(dto.getIdUsuario())
                .nombre(dto.getNombre())
                .rut(dto.getRut())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .puntos(dto.getPuntos())
                .codigoReferido(dto.getCodigoReferido())
                .fotoNombre(dto.getFotoNombre())
                .fotoTipo(dto.getFotoTipo())
                .fotoTamano(dto.getFotoTamano())
                // No se mapea el Rol completo aquí (se debe hacer en el Service)
                .build();
    }
}
