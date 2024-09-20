FROM debian:latest

RUN apt update -y && apt install default-jdk -y

COPY src/FileSizeLogger.java .

RUN javac FileSizeLogger.java

CMD ["java", "FileSizeLogger", "/bind/contentToLog.txt", "/log/log.txt"]

#docker build -t logger:v2 .
#docker run -d --mount type=bind,source=/c/Users/benni/Documents/bind,target=/bind  --mount type=volume,source=log,target=/log logger:v2
#docker exec -it 712500b442ee89b413a0b74f546199d6bc6d409ce1655b3ff36e6c1cf2bbf7bd tail -f /log/log.txt