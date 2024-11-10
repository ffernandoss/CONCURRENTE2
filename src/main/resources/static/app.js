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

document.getElementById('stop-exponential-button').addEventListener('click', function() {
    fetch('/stop-loading-exponential', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Flujo exponencial detenido');
            } else {
                console.error('Error al detener el flujo exponencial');
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

document.getElementById('resume-exponential-button').addEventListener('click', function() {
    fetch('/resume-loading-exponential', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Flujo exponencial reanudado');
            } else {
                console.error('Error al reanudar el flujo exponencial');
            }
        });
});

const socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function(event) {
    const data = JSON.parse(event.data);
    const ballContainer = document.getElementById('ball-container');
    const ball = document.createElement('div');
    ball.classList.add('ball');
    ball.textContent = `ID: ${data.id}\nValor: ${data.valorN !== undefined ? data.valorN : data.valorE}`;

    if (data.valorN !== undefined) {
        ball.classList.add('normal');
    } else if (data.valorE !== undefined) {
        ball.classList.add('exponential');
    }

    ballContainer.appendChild(ball);
};