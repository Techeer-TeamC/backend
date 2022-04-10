# https://inma.tistory.com/148 - docker 베포
FROM openjdk:11-jdk As builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build

#java 11 사용하므로 openjdk:11 image사용 (8은 error 발생)
FROM openjdk:11-jdk

#기본 작업 디렉토리 설정
WORKDIR /app

#https://iagreebut.tistory.com/171?category=887117
#jar파일 생성 후 복사
ADD build/libs/*.jar /app/app.jar

#컨테이너가 실행될 때 명령어 수행
ENTRYPOINT ["java","-jar","/app/app.jar"]

# 1. backend 폴더에서 ./gradlew build 실행
# 2. docker-compose가 존재하는 상위 폴더로 이동
# 3. docker-compose up --build