FROM openjdk:8-alpine
ENV TIME_ZONE=Asia/Shanghai
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone;\
    apk --no-cache add bash tzdata ttf-dejavu fontconfig;
ARG JAR_FILE=purchase-admin/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app.jar"]
CMD ["--spring.profiles.active=local","--server.port=9090"]