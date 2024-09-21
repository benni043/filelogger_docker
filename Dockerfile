FROM openjdk:17-jdk-slim

COPY src/FileSizeLogger.java .

RUN javac FileSizeLogger.java

CMD ["java", "FileSizeLogger", "/bind/fileToLog.txt", "/log/log.txt"]

# docker build -t logger:v1 .

# (PC) docker run --mount type=bind,source=/c/Users/benni/Documents/bind,target=/bind  --mount type=volume,source=log,target=/log logger:v1

# (LAPTOP) docker run --mount type=bind,source=/c/school/wmc_bind,target=/bind  --mount type=volume,source=log,target=/log logger:v1
# (LAPTOP-SHORTHAND) docker run -v /c/school/wmc_bind:/bind -v log:/log logger:v1

# docker ps
# docker exec -it [id] /bin/bash

# print file -> cat log/log.txt
# write to file -> echo "huff" > bind/fileToLog.txt