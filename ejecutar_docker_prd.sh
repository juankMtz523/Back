#!/bin/bash
# Script para construir y ejecutar contenedor de back en ambiente de producción
CONTAINER_NAME="SOMService"
echo "Inicializando..."
# Verifica si el contenedor existe (corriendo o detenido)
if [ "$(sudo docker ps -a -q -f name=^/${CONTAINER_NAME}$)" ]; then
	echo "Detener el contenedor existente"
	sudo docker stop SOMService || true

	echo "Eliminar el contenedor existente"
	sudo docker rm SOMService || true

	echo "Eliminar la imagen existente"
	sudo docker rmi somservice:1.0 || true
	
	echo "Crear la nueva imagen"
	sudo docker build -t somservice:1.0 . 

	echo "Crear nuevo contenedor"
	sudo docker run --name SOMService -p 8080:8080 somservice:1.0
else
	echo "Crear la nueva imagen"
	sudo docker build -t somservice:1.0 . 

	echo "Crear nuevo contenedor"
	sudo ºdocker run --name SOMService -p 8080:8080 somservice:1.0
fi
echo "Finalizado..."