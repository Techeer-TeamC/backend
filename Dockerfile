# https://inma.tistory.com/148 - docker 베포
# FROM openjdk:11-jdk As builder
# COPY gradlew .
# COPY gradle gradle
# COPY build.gradle .
# COPY settings.gradle .
# COPY src src
# COPY . .
# RUN chmod +x ./gradlew
# RUN ./gradlew build
# RUN ./gradlew assemble

#java 11 사용하므로 openjdk:11 image사용 (8은 error 발생)
FROM openjdk:11-jdk

# COPY --from=builder build/libs/*.jar app.jar
#https://iagreebut.tistory.com/171?category=887117
#jar파일 생성 후 복사
COPY build/libs/*.jar app.jar

#ENV DOCKERIZE_VERSION v0.6.1

## install dockerize
#RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
#    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz
#
## db가 setting 된 후 backend 시작을 위함
#ENTRYPOINT ["dockerize", "-wait", "tcp://db:3306", "-timeout", "20s"]

#컨테이너가 실행될 때 명령어 수행
CMD ["java","-jar","/app.jar"]

# 1. backend 폴더에서 ./gradlew build 실행
# 2. docker-compose가 존재하는 상위 폴더로 이동
# 3. docker-compose up --build
