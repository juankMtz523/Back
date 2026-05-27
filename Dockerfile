FROM amazoncorretto:21-alpine3.23-jdk
WORKDIR /app

# Establecer la zona horaria
ENV TZ=America/Mexico_City
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Install fontconfig and ttf-dejavu packages to fix RuntimeException: Fontconfig head is null
RUN apk add --no-cache fontconfig ttf-dejavu \
    && fc-cache -f -v

COPY recursos/fuentes/inter/ /usr/share/fonts/inter/
COPY recursos/fuentes/outfit/ /usr/share/fonts/outfit/

RUN fc-cache -f -v

ENV JAR_FILE=target/service-orders-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} service-orders-0.0.1-SNAPSHOT.jar
COPY recursos/somGTIMPDF.jasper somGTIMPDF.jasper
COPY recursos/somGTIMPDF.jrxml somGTIMPDF.jrxml
COPY recursos/fondo_gtim.png fondo_gtim.png
COPY recursos/logo_gtim.png logo_gtim.png
COPY recursos/fondopdf.jpg fondopdf.jpg
COPY recursos/iconopdf.png iconopdf.png
COPY recursos/inter2.jar inter2.jar
COPY recursos/outfit2.jar outfit2.jar



EXPOSE 8080/tcp
CMD ["java","-jar","service-orders-0.0.1-SNAPSHOT.jar"]
