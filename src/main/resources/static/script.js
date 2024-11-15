
/* Archivo: script.js */
document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('gaussChart').getContext('2d');
    const gaussChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Array.from({ length: 20 }, (_, i) => `Contenedor ${i + 1}`),
            datasets: [{
                label: 'Cantidad de Bolas',
                data: Array.from({ length: 20 }, () => 0),
                backgroundColor: 'rgba(0, 123, 255, 0.5)',
                borderColor: 'rgba(0, 123, 255, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    let intervalId;
    let data = gaussChart.data.datasets[0].data;

    function updateChart() {
        // Simular la caída de una bola a un contenedor aleatorio
        const index = Math.floor(Math.random() * data.length);
        data[index] += 1;
        gaussChart.update();
    }

    document.getElementById('startSimulation').addEventListener('click', function() {
        if (!intervalId) {
            intervalId = setInterval(updateChart, 500);
        }
    });

    document.getElementById('pauseSimulation').addEventListener('click', function() {
        clearInterval(intervalId);
        intervalId = null;
    });

    document.getElementById('resetSimulation').addEventListener('click', function() {
        clearInterval(intervalId);
        intervalId = null;
        data.fill(0);
        gaussChart.update();
    });
});
