FROM openjdk:17-jdk-slim

COPY src/FileSizeLogger.java .

RUN javac FileSizeLogger.java

CMD ["java", "FileSizeLogger", "/bind/contentToLog.txt", "/log/log.txt"]

# docker build -t logger:v2 .

# docker run --mount type=bind,source=/c/Users/benni/Documents/bind,target=/bind  --mount type=volume,source=log,target=/log logger:v2

# docker ps
# docker exec -it [id] /bin/bash

# print file -> cat log/log.txt
# write to file -> echo "huff" > bind/contentToLog.txt