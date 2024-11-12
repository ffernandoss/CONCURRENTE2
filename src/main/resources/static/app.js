const socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function(event) {
    const data = JSON.parse(event.data);
    createBall(data.id, data.valorN);
};

function createPins() {
    const board = document.getElementById('board');
    const pinSpacing = 40;
    const rows = 10;
    const cols = 15;
    const boardWidth = board.offsetWidth;

    // Crear líneas de separación
    for (let col = 0; col <= cols; col++) {
        const x = col * pinSpacing + (boardWidth - (cols * pinSpacing)) / 2;
        const line = document.createElement('div');
        line.classList.add('column-line');
        line.style.left = `${x}px`;
        line.style.top = `${rows * pinSpacing}px`; // Empieza donde terminan los pines
        board.appendChild(line);
    }

    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            const x = col * pinSpacing + (row % 2 === 0 ? pinSpacing / 2 : 0) + (boardWidth - (cols * pinSpacing)) / 2;
            const y = row * pinSpacing;
            const pin = document.createElement('div');
            pin.classList.add('pin');
            pin.style.left = `${x}px`;
            pin.style.top = `${y}px`;
            board.appendChild(pin);
        }
    }
}

function createBall(id, valor) {
    const board = document.getElementById('board');
    const ball = document.createElement('div');
    ball.classList.add('ball');
    ball.style.left = `${board.offsetWidth / 2}px`;
    ball.style.top = '0px';
    ball.textContent = `ID: ${id}`;

    let x = board.offsetWidth / 2;
    let y = 0;
    let velocityY = 0;
    const gravity = 0.98; // Gravedad
    const resistance = 0.01; // Resistencia del aire
    const pinSpacing = 40;
    const rows = 10;

    const interval = setInterval(() => {
        if (y >= board.offsetHeight - 20) {
            clearInterval(interval);
            // Almacenar la bola en la columna correspondiente
            const column = Math.round(x / pinSpacing);
            const columnBalls = board.querySelectorAll(`.column-${column}`).length;
            ball.style.top = `${board.offsetHeight - (columnBalls + 1) * 20}px`;
            ball.style.left = `${column * pinSpacing}px`; // Alinear con la columna
            ball.classList.add(`column-${column}`);
        } else {
            velocityY += gravity - resistance * velocityY; // Aplicar gravedad y resistencia
            y += velocityY; // Actualizar posición vertical

            if (y < rows * pinSpacing) {
                x += Math.random() < 0.5 ? -pinSpacing / 2 : pinSpacing / 2; // Rebotar en los pines
                x = Math.max(0, Math.min(board.offsetWidth - pinSpacing, x)); // Asegurar que x esté dentro del tablero
            }
            ball.style.left = `${x}px`;
            ball.style.top = `${y}px`;
        }
    }, 20); // Intervalo más corto para una caída más fluida

    board.appendChild(ball);
}

// Crear los pines al cargar la página
createPins();

document.getElementById('stop-normal-button').addEventListener('click', function() {
    fetch('/stop-loading-normal', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Flujo normal detenido');
            } else {
                console.error('Error al detener el flujo normal');
            }
        });
});

document.getElementById('resume-normal-button').addEventListener('click', function() {
    fetch('/resume-loading-normal', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Flujo normal reanudado');
            } else {
                console.error('Error al reanudar el flujo normal');
            }
        });
});