package com.lvlupgamer.usuarios.apiusuarios.mapper;

import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioRegistroDTO;
import com.lvlupgamer.usuarios.apiusuarios.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

	@Mapping(source = "rol.nombre", target = "rolNombre")
	@Mapping(target = "fotoNombre", source = "fotoNombre")
	@Mapping(target = "fotoTipo", source = "fotoTipo")
	@Mapping(target = "fotoTamano", source = "fotoTamano")
	// AGREGA LOS NUEVOS CAMPOS
	@Mapping(target = "direccion", source = "direccion")
	@Mapping(target = "telefono", source = "telefono")
	UsuarioDTO toDTO(Usuario usuario);

	@Mapping(target = "idUsuario", ignore = true)
	@Mapping(target = "fechaRegistro", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "puntos", constant = "0")
	@Mapping(target = "rol", ignore = true)  // se asigna en servicio
	@Mapping(target = "foto", ignore = true) // se asigna en servicio
	@Mapping(target = "fotoNombre", ignore = true)
	@Mapping(target = "fotoTipo", ignore = true)
	@Mapping(target = "fotoTamano", ignore = true)
	// AGREGA LOS NUEVOS CAMPOS
    @Mapping(source = "direccion", target = "direccion")
    @Mapping(source = "telefono", target = "telefono")
	Usuario toEntity(UsuarioRegistroDTO dto);
}
