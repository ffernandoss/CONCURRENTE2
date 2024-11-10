const socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function(event) {
    const message = JSON.parse(event.data);
    createBall(message.id);
};

function createBall(id) {
    const ballContainer = document.getElementById('ball-container');
    const ball = document.createElement('div');
    ball.className = 'ball';
    ball.textContent = id;
    ballContainer.appendChild(ball);
}