
[README.md](https://github.com/user-attachments/files/23554784/README.md)

[Informe](https://drive.google.com/drive/folders/1WFV2twfzVmcbaicEgzlbB7V5q2kvYao4)

[Video](https://www.youtube.com/watch?v=97VeXGVqqCc)

# Sistema de Inventario — README Oficial
Este proyecto implementa un **sistema de inventario básico** desarrollado en **Java + MySQL**, con soporte para:
- Gestión de productos  
- Generación automática de códigos de barras  
- Consultas avanzadas (JOIN, GROUP BY, vistas)  
- Pruebas de integridad  
- Simulación de concurrencia con deadlocks  
- Seguridad (usuario con mínimos privilegios, vistas seguras, SQL parametrizado)

# 📌 Descripción del Dominio
El dominio elegido es un **sistema de inventario para pequeñas y medianas empresas**.  
Permite administrar productos mediante operaciones básicas (CRUD), controlar existencias, generar códigos de barras, validar reglas de negocio y ejecutar consultas avanzadas para análisis de inventario.  
El sistema está pensado para entornos académicos y productivos simples, donde se necesita:  
- Registrar productos  
- Mantener un stock consistente  
- Consultar existencia y valor de inventario  
- Probar concurrencia simulada y restricciones  
- Practicar buenas prácticas de seguridad en SQL  
El propósito central es ofrecer un proyecto educativo robusto y realista sobre **Java + MySQL + JDBC**, aplicando patrones, transacciones y roles con mínimos privilegios.

## UML
<img width="988" height="552" alt="image" src="https://github.com/user-attachments/assets/35c377e3-db65-4b52-ab03-902fb682f167" />

#1. Requisitos

## Software Necesario

| Herramienta | Versión recomendada |
|-------------|----------------------|
| Java JDK | 17+ |
| MySQL Server | 8+ |
| IDE | NetBeans / IntelliJ / Eclipse |
| JDBC | mysql-connector-j |
| dotenv | dotenv-java |

---

# 2. Clonar el repositorio

```bash
git clone https://github.com/usuario/repositorio.git
cd repositorio
```

3. Crear archivo `.env`

Crear el archivo `.env` y agregar:

```plaintext
DB_URL=jdbc:mysql://localhost:3306/basedatos_tpintegrador?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASS=tu_contraseña
```

 4. Crear la Base de Datos

Los scripts se encuentran en `/sql`.

### Opción A — MySQL Workbench

1. Abrir Workbench
2. File → Open SQL Script
3. Ejecutar en orden:
   ```bash
   SoloEstructura.sql
   RegistrosDePrueba.sql  # opcional
   ```

### Opción B — Terminal

```bash
mysql -u root -p < sql/SoloEstructura.sql
mysql -u root -p < sql/RegistrosDePrueba.sql
```

5. Crear usuario con mínimos privilegios (opcional)

```sql
CREATE USER IF NOT EXISTS 'app_user'@'localhost' IDENTIFIED BY 'Secure123!';
GRANT SELECT, INSERT, UPDATE ON basedatos_tpintegrador.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
```

Actualizar `.env`:

```plaintext
DB_USER=app_user
DB_PASS=Secure123!
```

6. Importar el proyecto en tu IDE

### NetBeans

1. File → Open Project
2. Seleccionar carpeta del repositorio
3. Verificar librerías:
   ```markdown
   - mysql-connector-j
   - dotenv-java
   ```

### IntelliJ/Eclipse

1. Abrir carpeta del proyecto
2. Configurar classpath con las librerías necesarias

🔨 7. Compilar el Proyecto

#### Desde el IDE

1. Presionar Run |>

#### Desde terminal

```bash
javac -cp "lib/*;src" -d build/classes src/**/*.java
```

*(En Linux/MacOS reemplazar `;` por `:`)*

8. Ejecutar el Sistema

Menú principal:
```bash
java -cp "build/classes;lib/*" main.Main
```

Prueba de concurrencia:
```bash
java -cp "build/classes;lib/*" tests.concurrencia
```

Prueba de seguridad + vistas:
```bash
java -cp "build/classes;lib/*" tests.seguridad
```

9. Pruebas Incluidas

El proyecto incluye pruebas de:

- Conexión a la BD
- Inserción correcta de datos
- Validación de reglas de negocio
- Consultas avanzadas (JOIN, GROUP BY, HAVING, subconsulta)
- Vistas seguras
- Deadlocks reales con retry automático
- SQL seguro mediante PreparedStatement

10. Errores Comunes

| Error | Motivo | Solución |
|-------|--------|----------|
| Unknown database | La BD no existe | Ejecutar scripts SQL |
| Unknown column | Campos distintos | Revisar nombres reales en la BD |
| Communications link failure | MySQL apagado | Iniciar servicio |
| ClassNotFoundException | Falta JDBC | Agregar mysql-connector-j al proyecto |

---

## Contribuciones

Las contribuciones son bienvenidas. Para realizar cambios significativos, por favor abra una issue primero para discutir los cambios propuestos.

## Licencia

Este proyecto está bajo la licencia [MIT](LICENSE.md).
