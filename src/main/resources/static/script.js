document.addEventListener('DOMContentLoaded', function () {
    const startSimulationButton = document.getElementById('startSimulation');
    const ctx = document.getElementById('gaussianChart').getContext('2d');
    let chartData = {
        labels: [],
        datasets: [{
            label: 'Distribución Gaussiana',
            data: [],
            backgroundColor: 'rgba(54, 162, 235, 0.5)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    };

    const gaussianChart = new Chart(ctx, {
        type: 'line',
        data: chartData,
        options: {
            scales: {
                x: {
                    beginAtZero: true
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    function connectWebSocket() {
        const socket = new SockJS('/gs-guide-websocket');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/gaussian-distribution', function (message) {
                const value = parseFloat(message.body);
                updateChart(value);
            });
        });
    }

    function updateChart(value) {
        chartData.labels.push(chartData.labels.length + 1);
        chartData.datasets[0].data.push(value);
        gaussianChart.update();
    }

    startSimulationButton.addEventListener('click', function () {
        connectWebSocket();
        fetch('/simulate', { method: 'POST' }) // Envía una solicitud POST para iniciar la simulación
            .then(response => {
                if (!response.ok) {
                    console.error('Error al iniciar la simulación');
                }
            });
    });
});
