package com.lvlupgamer.usuarios.apiusuarios.controller;

import com.lvlupgamer.usuarios.apiusuarios.dto.LoginRequest;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioRegistroDTO;
import com.lvlupgamer.usuarios.apiusuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequest login) {
    UsuarioDTO dto = usuarioService.login(login.getEmail(), login.getContrasena());
    // si usas JWT, aquí devuelves el token
    return ResponseEntity.ok(dto);
    }

    // 1. REGISTRAR USUARIO (con foto)
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(
        @Valid @ModelAttribute UsuarioRegistroDTO registroDTO
    ) throws Exception {
        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroDTO);
        return ResponseEntity.status(201).body(usuarioDTO);
    }

    // 2. OBTENER USUARIO POR EMAIL
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(@PathVariable String email) {
        UsuarioDTO usuarioDTO = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(usuarioDTO);
    }

    // 3. LISTAR TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // 4. ELIMINAR USUARIO POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // 5. ACTUALIZAR DATOS USUARIO (excepto foto y password)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
        @PathVariable Long id, @RequestBody UsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // 6. ACTUALIZAR SOLO LA FOTO
    @PatchMapping("/{id}/foto")
    public ResponseEntity<UsuarioDTO> actualizarFoto(
        @PathVariable Long id,
        @RequestParam("foto") MultipartFile nuevaFoto
    ) throws Exception {
        UsuarioDTO actualizado = usuarioService.actualizarFoto(id, nuevaFoto);
        return ResponseEntity.ok(actualizado);
    }

    // 7. DESCARGAR/DESPLEGAR FOTO
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {
        return usuarioService.obtenerFoto(id);
    }
}
