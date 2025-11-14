package com.lvlupgamer.usuarios.apiusuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lvlupgamer.usuarios.apiusuarios.dto.ApiResponse;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioRegistroDTO;
import com.lvlupgamer.usuarios.apiusuarios.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuarioDTO>> registrar(
            @Valid @ModelAttribute UsuarioRegistroDTO usuarioRegistroDTO,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil) {

        try {
            log.info("Solicitud de registro para email: {}", usuarioRegistroDTO.getEmail());
            
            UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(usuarioRegistroDTO, fotoPerfil);
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(true)
                    .message("Usuario registrado exitosamente")
                    .data(usuarioDTO)
                    .code(201)
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage());
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuarioById(id);
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(true)
                    .message("Usuario encontrado")
                    .data(usuarioDTO)
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener usuario: {}", e.getMessage());
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorEmail(@PathVariable String email) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuarioPorEmail(email);
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(true)
                    .message("Usuario encontrado")
                    .data(usuarioDTO)
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener usuario por email: {}", e.getMessage());
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
            
            ApiResponse<List<UsuarioDTO>> response = ApiResponse.<List<UsuarioDTO>>builder()
                    .success(true)
                    .message("Usuarios obtenidos correctamente")
                    .data(usuarios)
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener usuarios: {}", e.getMessage());
            
            ApiResponse<List<UsuarioDTO>> response = ApiResponse.<List<UsuarioDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizar(id, usuarioDTO);
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(true)
                    .message("Usuario actualizado exitosamente")
                    .data(usuarioActualizado)
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}/foto")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizarFoto(
            @PathVariable Long id,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil) {

        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarFotoPerfil(id, fotoPerfil);
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
                    .success(true)
                    .message("Foto de perfil actualizada exitosamente")
                    .data(usuarioActualizado)
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al actualizar foto: {}", e.getMessage());
            
            ApiResponse<UsuarioDTO> response = ApiResponse.<UsuarioDTO>builder()
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
            usuarioService.eliminar(id);
            
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Usuario eliminado exitosamente")
                    .code(200)
                    .build();
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage());
            
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
