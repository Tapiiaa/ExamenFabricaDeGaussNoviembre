Simulación de Producción Concurrente con Spring Boot WebFlux y RabbitMQ en la Fábrica de Campanas de Gauss
Descripción del Proyecto
En un universo paralelo del año 1850, Sir Francis Galton revolucionó el mundo con su "máquina de la distribución normal". Esta máquina, inspirada en su tablero de Galton, demostraba cómo las bolas al caer a través de clavos distribuidos formaban una curva en forma de campana, conocida como distribución normal o gaussiana. Para demostrar este fenómeno, Galton fundó la fábrica "Campanas de Gauss", donde estaciones de trabajo independientes producían componentes de estas máquinas, ensamblados posteriormente en una línea de producción.

Como ingeniero de software, tu misión es simular este proceso de fabricación utilizando Spring Boot WebFlux y RabbitMQ, modelando la concurrencia y sincronización necesarias para coordinar las estaciones de trabajo.

Características Principales
Simulación de estaciones de trabajo con Spring Boot WebFlux:

Cada estación de trabajo es un componente independiente que produce piezas específicas de las máquinas.
Las estaciones de trabajo operan de forma concurrente para optimizar la producción.
Sincronización con RabbitMQ:

Las estaciones de trabajo y la línea de ensamblaje se comunican a través de colas de RabbitMQ.
Se implementa un modelo de productor-consumidor para coordinar la producción y ensamblaje.
Programación paralela y distribuida:

Uso de Spring Boot WebFlux para gestionar múltiples hilos y nodos en un sistema distribuido.
Las tareas se distribuyen utilizando un algoritmo de scheduling personalizado.
Visualización en tiempo real:

Representación gráfica del fenómeno gaussiano en el proceso de producción.
Muestra cómo la distribución final de las bolas en los contenedores se aproxima a la curva normal.
Tecnologías Utilizadas
Spring Boot WebFlux: Para modelar estaciones de trabajo concurrentes.
RabbitMQ: Para la sincronización y comunicación entre componentes.
Programación Reactiva: Para manejar la concurrencia y las operaciones asíncronas.
Algoritmo de Scheduling: Para distribuir eficientemente las tareas de producción.
Visualización Gráfica: Para mostrar el fenómeno gaussiano en tiempo real.
Instalación y Ejecución
Prerrequisitos
Java 17+: Asegúrate de tener instalado Java Development Kit (JDK) 17 o superior.
RabbitMQ: Instala y ejecuta un servidor RabbitMQ.
Maven: Instala Maven para gestionar las dependencias del proyecto.
Node.js y npm (opcional): Si la visualización incluye un frontend basado en JavaScript.
