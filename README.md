# Proyecto de Simulación de Pelotas con ID
https://github.com/ffernandoss/CONCURRENTE2.git

Este proyecto simula la caída de pelotas en un tablero con pines, formando una distribución normal. Utiliza tecnologías como Java, Spring Boot, WebSockets, RabbitMQ y D3.js.

## Estructura del Proyecto

### `ValorNormal.java`
Define la entidad `ValorNormal` que representa un valor normal en la base de datos.

- **Atributos:**
  - `id`: Identificador único de la entidad.
  - `valor`: Valor numérico de la entidad.

### `ValorNormalRepository.java`
Interfaz que extiende `JpaRepository` para realizar operaciones CRUD en la entidad `ValorNormal`.

### `BrowserLauncher.java`
Clase que abre el navegador web al iniciar la aplicación.

- **Métodos:**
  - `onApplicationEvent`: Abre el navegador cuando se inicia el contexto de la aplicación.
  - `openBrowser`: Abre el navegador con la URL especificada.

### `DatabaseInitializer.java`
Clase que inicializa la base de datos al iniciar la aplicación.

- **Métodos:**
  - `run`: Limpia la base de datos y carga datos desde un archivo CSV.

### `RabbitMQConfig.java`
Clase de configuración para RabbitMQ.

- **Métodos:**
  - `queue`: Define una cola llamada `databaseQueue`.

### `RabbitMQListener.java`
Clase que escucha mensajes de RabbitMQ.

- **Métodos:**
  - `listen`: Procesa los mensajes recibidos en la cola `databaseQueue`.

### `CsvController.java`
Controlador REST para manejar las solicitudes de inicio y detención de la carga de datos.

- **Métodos:**
  - `stopLoadingNormal`: Detiene la carga de datos normales.
  - `resumeLoadingNormal`: Reanuda la carga de datos normales.

### `CsvService.java`
Servicio que maneja la carga de datos desde un archivo CSV.

- **Métodos:**
  - `clearDatabase`: Limpia la base de datos.
  - `loadCsvData`: Carga datos desde un archivo CSV y los guarda en la base de datos.
  - `stopLoadingNormal`: Detiene la carga de datos normales.
  - `resumeLoadingNormal`: Reanuda la carga de datos normales.

### `WebSocketConfig.java`
Clase de configuración para WebSockets.

- **Métodos:**
  - `registerWebSocketHandlers`: Registra el manejador de WebSocket en la ruta `/notifications`.

### `WebSocketHandler.java`
Manejador de WebSocket que envía mensajes a todos los clientes conectados.

- **Métodos:**
  - `afterConnectionEstablished`: Añade una sesión WebSocket a la lista de sesiones.
  - `handleTextMessage`: Envía un mensaje a todas las sesiones WebSocket.
  - `sendMessageToAll`: Envía un mensaje a todas las sesiones WebSocket.

### `Concurrente2Application.java`
Clase principal de la aplicación Spring Boot.

- **Métodos:**
  - `main`: Método principal que inicia la aplicación.
  - `run`: Abre el navegador web al iniciar la aplicación.
  - `openBrowser`: Abre el navegador con la URL especificada.

### `app.js`
Archivo JavaScript que maneja la lógica de la simulación en el navegador.

- **Funciones:**
  - `createPins`: Crea los pines en el tablero.
  - `createBall`: Crea una pelota y maneja su caída.
  - `socket.onmessage`: Maneja los mensajes recibidos a través del WebSocket.

### `index.html`
Archivo HTML que define la estructura de la página web.

- **Elementos:**
  - Botones para detener y reanudar el flujo normal.
  - Secciones de instrucciones y descripción del proyecto.

### `styless.css`
Archivo CSS que define los estilos para las pelotas y otros elementos de la página.

- **Clases:**
  - `.ball`: Estilo para las pelotas.
  - `.pin`: Estilo para los pines.
  - `.column-line`: Estilo para las líneas de separación entre columnas.


## Tecnologías Utilizadas

- Java
- Spring Boot
- WebSockets
- RabbitMQ
- D3.js
- Maven
- OpenCSV
