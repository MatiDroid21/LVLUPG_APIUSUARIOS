package com.lvlupgamer.usuarios.apiusuarios.controller;

import com.lvlupgamer.usuarios.apiusuarios.dto.ApiResponse;
import com.lvlupgamer.usuarios.apiusuarios.dto.RolDTO;
import com.lvlupgamer.usuarios.apiusuarios.services.RolService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RolController {

    @Autowired
    private RolService rolService;

    @PostMapping
    public ResponseEntity<ApiResponse<RolDTO>> crear(@Valid @RequestBody RolDTO rolDTO) {
        try {
            log.info("Solicitud para crear rol: {}", rolDTO.getNombre());
            RolDTO creado = rolService.crear(rolDTO);

            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(true)
                    .message("Rol creado exitosamente")
                    .data(creado)
                    .code(201)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error al crear rol: {}", e.getMessage());
            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            RolDTO rol = rolService.obtenerPorId(id);
            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(true)
                    .message("Rol encontrado")
                    .data(rol)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener rol: {}", e.getMessage());
            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolDTO>>> obtenerTodos() {
        try {
            List<RolDTO> roles = rolService.obtenerTodos();
            ApiResponse<List<RolDTO>> response = ApiResponse.<List<RolDTO>>builder()
                    .success(true)
                    .message("Roles obtenidos correctamente")
                    .data(roles)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener roles: {}", e.getMessage());
            ApiResponse<List<RolDTO>> response = ApiResponse.<List<RolDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RolDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolDTO rolDTO) {
        try {
            RolDTO actualizado = rolService.actualizar(id, rolDTO);
            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(true)
                    .message("Rol actualizado exitosamente")
                    .data(actualizado)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al actualizar rol: {}", e.getMessage());
            ApiResponse<RolDTO> response = ApiResponse.<RolDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            rolService.eliminar(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Rol eliminado exitosamente")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al eliminar rol: {}", e.getMessage());
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

