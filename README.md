# LVLUPG_APIUSUARIOS

API para la gestión de usuarios para el proyecto LevelUpGamer DuocUC.

---

## Información General

- **Lenguaje:** Java + Spring Boot
- **Puerto por defecto:** 8080
- **Base URL:** `http://localhost:8080/api/usuarios`

---

## Estructura del DTO de Usuario

{
"idUsuario": 1,
"nombre": "Juan Pérez",
"rut": "11.111.111-1",
"email": "juan@levelupgamer.cl",
"fechaNacimiento": "2000-01-01",
"puntos": 50,
"codigoReferido": "ABC123",
"idRol": 2,
"nombreRol": "admin",
"fotoNombre": "nombre.jpg",
"fotoTipo": "image/jpeg",
"fotoTamano": 1200,
"fotoBase64": "(string base64)"
}

text

---

## Endpoints

### Registrar usuario

**POST** `/api/usuarios/registro`
- **Tipo de body:** `multipart/form-data`
- **Campos requeridos:**  
  - nombre (string)
  - rut (string)
  - email (string)
  - contrasena (string)
  - confirmarContrasena (string)
  - fechaNacimiento (yyyy-MM-dd)
  - fotoPerfil (archivo, obligatorio)
  - idRol (número, opcional)
  - referidoPor (string, opcional)

**Ejemplo en Postman:**  
Selecciona `POST`, URL `http://localhost:8080/api/usuarios/registro`, body en `form-data`.
Agrega los campos y selecciona archivo para `fotoPerfil`.

**Respuesta exitosa**
{
"success": true,
"message": "Usuario registrado exitosamente",
"data": { ...UsuarioDTO... },
"code": 201
}

text
**Errores comunes:**  
- Email ya registrado
- Las contraseñas no coinciden
- El usuario ya existe con ese rut
- Fotografía falta o peso excesivo (>5MB)
- Formato de imagen no permitido

---

### Login

**POST** `/api/usuarios/login`
- **Body:** JSON
{
"email": "juan@levelupgamer.cl",
"password": "123456"
}

text
- Password se envía en texto plano, pero es validada contra hash bcrypt.

**Respuesta exitosa**
{
"success": true,
"message": "Login exitoso",
"data": { ...UsuarioDTO... },
"code": 200
}

text

---

### Consultar usuario por ID

**GET** `/api/usuarios/{id}`

**Respuesta ejemplo**
{
"success": true,
"message": "Usuario encontrado",
"data": { ...UsuarioDTO... },
"code": 200
}

text

---

### Consultar usuario por email

**GET** `/api/usuarios/email/{email}`

---

### Actualizar usuario

**PUT** `/api/usuarios/{id}`
- **Body:** UsuarioDTO (JSON)

---

### Actualizar foto de perfil

**PUT** `/api/usuarios/{id}/foto`
- **Tipo:** `multipart/form-data`
- **Campo:** fotoPerfil (archivo imagen)

---

### Eliminar usuario

**DELETE** `/api/usuarios/{id}`

---

## Pruebas rápidas con curl

Registro ejemplo:
curl -X POST http://localhost:8080/api/usuarios/registro
-F "nombre=Juan Perez"
-F "rut=11.111.111-1"
-F "email=juan@levelupgamer.cl"
-F "contrasena=123456"
-F "confirmarContrasena=123456"
-F "fechaNacimiento=2000-01-01"
-F "fotoPerfil=@/ruta/a/foto.jpg"

text

Login ejemplo:
curl -X POST http://localhost:8080/api/usuarios/login
-H "Content-Type: application/json"
-d '{"email":"juan@levelupgamer.cl","password":"123456"}'

text

---

## Validaciones y seguridad

- Todos los passwords se almacenan con bcrypt.
- Login solo será exitoso si password es correcta (se recomienda mínimo 6 caracteres).
- Los emails deben ser únicos (no se repiten).
- La foto de perfil es obligatoria al crear usuario.
- Los usuarios deben ser mayores de edad (mínimo 18 años).

---

## Notas finales

- Si recibes un response con `"success": false`, revisa el campo `message` para saber el motivo.
- Todos los endpoints retornan las respuestas envueltas en un objeto ApiResponse.
- Ante cualquier duda revisa este README o consulta el código del controller.

---