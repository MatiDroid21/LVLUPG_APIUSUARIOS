package com.lvlupgamer.usuarios.apiusuarios.services;

import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioRegistroDTO;
import com.lvlupgamer.usuarios.apiusuarios.mapper.UsuarioMapper;
import com.lvlupgamer.usuarios.apiusuarios.model.Rol;
import com.lvlupgamer.usuarios.apiusuarios.model.Usuario;
import com.lvlupgamer.usuarios.apiusuarios.repository.RolRepository;
import com.lvlupgamer.usuarios.apiusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioRegistroDTO registroDTO) throws Exception {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }
        if (usuarioRepository.existsByRut(registroDTO.getRut())) {
            throw new RuntimeException("RUT ya registrado");
        }
        Usuario usuario = usuarioMapper.toEntity(registroDTO);
        Rol rol = rolRepository.findById(registroDTO.getIdRol())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);
        usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));

        MultipartFile fotoMultipart = registroDTO.getFoto();
        usuario.setFoto(fotoMultipart.getBytes());
        usuario.setFotoNombre(fotoMultipart.getOriginalFilename());
        usuario.setFotoTipo(fotoMultipart.getContentType());
        usuario.setFotoTamano(fotoMultipart.getSize());

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(usuarioMapper::toDTO)
            .toList();
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(dto.getNombre());
        usuario.setRut(dto.getRut());
        usuario.setEmail(dto.getEmail());
        usuario.setCodigoReferido(dto.getCodigoReferido());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        // No se actualizan puntos ni foto aquí por claridad

        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(actualizado);
    }

    @Transactional
    public UsuarioDTO actualizarFoto(Long id, MultipartFile nuevaFoto) throws Exception {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setFoto(nuevaFoto.getBytes());
        usuario.setFotoNombre(nuevaFoto.getOriginalFilename());
        usuario.setFotoTipo(nuevaFoto.getContentType());
        usuario.setFotoTamano(nuevaFoto.getSize());
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(actualizado);
    }

    public ResponseEntity<byte[]> obtenerFoto(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok()
            .header("Content-Type", usuario.getFotoTipo())
            .header("Content-Disposition", "inline; filename=\"" + usuario.getFotoNombre() + "\"")
            .body(usuario.getFoto());
    }

    public UsuarioDTO login(String email, String contrasena) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
        throw new RuntimeException("Contraseña incorrecta");
    }
    return usuarioMapper.toDTO(usuario);
}


}
