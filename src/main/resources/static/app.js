const socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function(event) {
    const message = JSON.parse(event.data);
    createBall(message.id, message.valor);
};

function createBall(id, valor) {
    const ballContainer = document.getElementById('ball-container');
    const ball = document.createElement('div');
    ball.className = 'ball';
    ball.textContent = `ID: ${id}, Valor: ${valor}`;
    ballContainer.appendChild(ball);
}