package com.lvlupgamer.usuarios.apiusuarios.services;

import com.lvlupgamer.usuarios.apiusuarios.dto.RolDTO;
import com.lvlupgamer.usuarios.apiusuarios.model.Rol;
import com.lvlupgamer.usuarios.apiusuarios.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public RolDTO crear(RolDTO rolDTO) throws Exception {
        log.info("Creando rol: {}", rolDTO.getNombre());

        if (rolRepository.existsByNombre(rolDTO.getNombre())) {
            throw new Exception("El rol " + rolDTO.getNombre() + " ya existe");
        }

        Rol rol = Rol.builder()
                .nombre(rolDTO.getNombre())
                .descripcion(rolDTO.getDescripcion())
                .build();

        Rol guardado = rolRepository.save(rol);
        log.info("Rol creado: {}", guardado.getIdRol());

        return convertirADTO(guardado);
    }

    public RolDTO obtenerPorId(Long idRol) throws Exception {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new Exception("Rol no encontrado"));
        return convertirADTO(rol);
    }

    public RolDTO obtenerPorNombre(String nombre) throws Exception {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new Exception("Rol no encontrado"));
        return convertirADTO(rol);
    }

    public List<RolDTO> obtenerTodos() {
        return rolRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public RolDTO actualizar(Long idRol, RolDTO rolDTO) throws Exception {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new Exception("Rol no encontrado"));

        if (rolDTO.getNombre() != null && !rolDTO.getNombre().equals(rol.getNombre())) {
            if (rolRepository.existsByNombre(rolDTO.getNombre())) {
                throw new Exception("El nombre de rol ya existe");
            }
            rol.setNombre(rolDTO.getNombre());
        }

        if (rolDTO.getDescripcion() != null) {
            rol.setDescripcion(rolDTO.getDescripcion());
        }

        Rol actualizado = rolRepository.save(rol);
        log.info("Rol actualizado: {}", idRol);

        return convertirADTO(actualizado);
    }

    public void eliminar(Long idRol) throws Exception {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new Exception("Rol no encontrado"));

        rolRepository.deleteById(idRol);
        log.info("Rol eliminado: {}", idRol);
    }

    private RolDTO convertirADTO(Rol rol) {
        return RolDTO.builder()
                .idRol(rol.getIdRol())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .build();
    }
}
