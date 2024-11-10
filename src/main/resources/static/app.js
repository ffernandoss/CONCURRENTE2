const socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function(event) {
    const message = JSON.parse(event.data);
    createBall(message.id, message.valor);
};

function createPins() {
    const board = d3.select('#board');
    const pinSpacing = 40;
    const rows = 10;
    const cols = 15;

    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            const x = col * pinSpacing + (row % 2 === 0 ? pinSpacing / 2 : 0);
            const y = row * pinSpacing;
            board.append('div')
                .attr('class', 'pin')
                .style('left', `${x}px`)
                .style('top', `${y}px`);
        }
    }
}

function createBall(id, valor) {
    const board = d3.select('#board');
    const ball = board.append('div')
        .attr('class', 'ball')
        .style('left', '50%')
        .style('top', '0px')
        .text(`ID: ${id}`);

    let x = board.node().offsetWidth / 2;
    let y = 0;
    const pinSpacing = 40;
    const rows = 10;
    const interval = setInterval(() => {
        if (y >= board.node().offsetHeight - 20) {
            clearInterval(interval);
            // Almacenar la bola en la columna correspondiente
            const column = Math.round(x / 20);
            const columnBalls = board.selectAll(`.column-${column}`).size();
            ball.style('top', `${board.node().offsetHeight - (columnBalls + 1) * 20}px`)
                .attr('class', `ball column-${column}`);
        } else {
            y += 10; // Incremento más pequeño para una caída más fluida
            if (y < rows * pinSpacing) {
                x += Math.random() < 0.5 ? -10 : 10; // Rebotar en los pines
            }
            ball.style('left', `${x}px`).style('top', `${y}px`);
        }
    }, 100); // Intervalo más corto para una caída más fluida
}

// Crear los pines al cargar la página
createPins();