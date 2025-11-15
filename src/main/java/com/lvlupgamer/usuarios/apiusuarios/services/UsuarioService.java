package com.lvlupgamer.usuarios.apiusuarios.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioDTO;
import com.lvlupgamer.usuarios.apiusuarios.dto.UsuarioRegistroDTO;
import com.lvlupgamer.usuarios.apiusuarios.mapper.UsuarioMapper;
import com.lvlupgamer.usuarios.apiusuarios.model.Usuario;
import com.lvlupgamer.usuarios.apiusuarios.model.Rol;
import com.lvlupgamer.usuarios.apiusuarios.repository.UsuarioRepository;
import com.lvlupgamer.usuarios.apiusuarios.repository.RolRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioMapper usuarioMapper;

    private static final String[] EXTENSIONES_PERMITIDAS = {"jpg", "jpeg", "png", "gif"};
    private static final long TAMANIO_MAXIMO = 5 * 1024 * 1024;

    public UsuarioDTO registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO, MultipartFile fotoPerfil) throws Exception {
        log.info("Iniciando registro de usuario: {}", usuarioRegistroDTO.getEmail());

        validarDatosRegistro(usuarioRegistroDTO);

        if (usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())) {
            throw new Exception("El email ya se encuentra registrado");
        }

        if (usuarioRepository.existsByRut(usuarioRegistroDTO.getRut())) {
            throw new Exception("El RUT ya se encuentra registrado");
        }


        validarMayorDeEdad(usuarioRegistroDTO.getFechaNacimiento());

        if (fotoPerfil == null || fotoPerfil.isEmpty()) {
            throw new Exception("La foto de perfil es obligatoria");
        }

        validarFoto(fotoPerfil);

        // OBTENER ROL (NUEVO)
        Rol rol = rolRepository.findById(usuarioRegistroDTO.getIdRol() != null ? usuarioRegistroDTO.getIdRol() : 1L)
                .orElseThrow(() -> new Exception("Rol no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(usuarioRegistroDTO.getNombre())
                .rut(usuarioRegistroDTO.getRut())
                .email(usuarioRegistroDTO.getEmail())
                .contrasena(passwordEncoder.encode(usuarioRegistroDTO.getContrasena()))
                .fechaNacimiento(usuarioRegistroDTO.getFechaNacimiento())
                .puntos(0)
                .referidoPor(usuarioRegistroDTO.getReferidoPor())
                .rol(rol)  // AGREGAR ROL (NUEVO)
                .build();

        procesarFoto(usuario, fotoPerfil);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente: {}", usuarioGuardado.getIdUsuario());

        return usuarioMapper.toDTO(usuarioGuardado);
    }

    private void procesarFoto(Usuario usuario, MultipartFile fotoPerfil) throws IOException {
        usuario.setFoto(fotoPerfil.getBytes());
        usuario.setFotoNombre(fotoPerfil.getOriginalFilename());
        usuario.setFotoTipo(fotoPerfil.getContentType());
        usuario.setFotoTamano(fotoPerfil.getSize());
    }

    private void validarFoto(MultipartFile archivo) throws Exception {
        if (archivo.getSize() > TAMANIO_MAXIMO) {
            throw new Exception("El archivo es demasiado grande. Tamaño máximo: 5 MB");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!esExtensionPermitida(extension)) {
            throw new Exception("Extensión de archivo no permitida. Use: jpg, jpeg, png, gif");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean esExtensionPermitida(String extension) {
        for (String ext : EXTENSIONES_PERMITIDAS) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) throws Exception {
        long anos = ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.now());
        if (anos < 18) {
            throw new Exception("Debe ser mayor de 18 años para registrarse. Edad actual: " + anos + " años");
        }
    }

    private void validarDatosRegistro(UsuarioRegistroDTO usuarioRegistroDTO) throws Exception {
        if (usuarioRegistroDTO.getNombre() == null || usuarioRegistroDTO.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre es requerido");
        }

        if (usuarioRegistroDTO.getRut() == null || usuarioRegistroDTO.getRut().trim().isEmpty()) {
            throw new Exception("El RUT es requerido");
        }

        if (usuarioRegistroDTO.getEmail() == null || usuarioRegistroDTO.getEmail().trim().isEmpty()) {
            throw new Exception("El email es requerido");
        }

        if (usuarioRegistroDTO.getContrasena() == null || usuarioRegistroDTO.getContrasena().length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }

        if (usuarioRegistroDTO.getFechaNacimiento() == null) {
            throw new Exception("La fecha de nacimiento es requerida");
        }
    }

    public UsuarioDTO obtenerUsuarioById(Long idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO obtenerUsuarioPorEmail(String email) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO actualizar(Long idUsuario, UsuarioDTO usuarioDTO) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }

        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new Exception("El email ya está registrado");
            }
            usuario.setEmail(usuarioDTO.getEmail());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado: {}", idUsuario);
        return usuarioMapper.toDTO(actualizado);
    }

    public UsuarioDTO actualizarFotoPerfil(Long idUsuario, MultipartFile fotoPerfil) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        validarFoto(fotoPerfil);
        procesarFoto(usuario, fotoPerfil);

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Foto de perfil actualizada para usuario: {}", idUsuario);
        return usuarioMapper.toDTO(actualizado);
    }

    public void eliminar(Long idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        usuarioRepository.deleteById(idUsuario);
        log.info("Usuario eliminado: {}", idUsuario);
    }

    public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRut(usuario.getRut());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setPuntos(usuario.getPuntos());
        dto.setCodigoReferido(usuario.getCodigoReferido());
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        dto.setNombreRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);

        // Asumiendo que el campo fotoPerfil es un byte[] en Usuario
        if (usuario.getFoto() != null) {
            dto.setFotoNombre(usuario.getFotoNombre());
            dto.setFotoTipo(usuario.getFotoTipo());
            dto.setFotoTamano((long) usuario.getFoto().length);
            dto.setFotoFromBytes(usuario.getFoto());
        }
        return dto;
    }


public UsuarioDTO login(String email, String plainPassword) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    if (!passwordEncoder.matches(plainPassword, usuario.getContrasena())) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    return convertirAUsuarioDTO(usuario);
}


}
