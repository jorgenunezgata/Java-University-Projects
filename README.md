# Portfolio de Proyectos Universitarios - Java

Este repositorio contiene una colección de proyectos académicos desarrollados en **Java**, enfocados en sistemas distribuidos, concurrencia y protocolos de red personalizados.

## 📂 Contenido del Repositorio

### 1. Networked Vector Client (Cliente de Dibujo Vectorial en Red)
Sistema cliente-servidor para la edición colaborativa de dibujos vectoriales. Implementa un protocolo binario propio para la transmisión eficiente de datos.

* **Tecnologías:** Java NIO (Non-blocking I/O), Sockets, Serialización binaria manual.
* **Destacado:**
    * **Protocolo Personalizado (`Msg.java`):** Diseño e implementación de un protocolo de capa de aplicación byte a byte (cabeceras, tags, payload) sin usar librerías externas de serialización.
    * **Gestión de Gráficos:** Modelo de objetos para figuras (Círculos, Rectángulos) con soporte para operaciones de movimiento y detección de colisiones.
    * **Arquitectura:** Separación limpia entre la lógica de red (`Client.java`) y la lógica de dominio (`Draw.java`, `Figures.java`).

### 2. Simple Message Broker (Middleware Orientado a Mensajes - MOM)
Implementación de un intermediario de mensajería (Message Broker) que gestiona colas de publicación/suscripción mediante sockets bloqueantes y multihilo.

* **Tecnologías:** Java Sockets (`java.net`), Multithreading (`Thread`, `Runnable`), Sincronización.
* **Destacado:**
    * **Concurrencia (`MomServerThread.java`):** Gestión de múltiples clientes simultáneos mediante hilos dedicados.
    * **Gestión de Canales:** Lógica para crear (`MKCHAN`), borrar (`RMCHAN`) y comunicar mensajes (`WRITE`/`READ`) entre procesos distribuidos.
    * **Modelo Cliente-Servidor:** Arquitectura robusta para la gestión de peticiones síncronas.

---

## 🛠️ Instalación y Uso

Estos proyectos son aplicaciones de consola/desktop estándar en Java.

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/TU-USUARIO/Java-University-Projects.git](https://github.com/TU-USUARIO/Java-University-Projects.git)
