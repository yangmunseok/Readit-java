FROM ubuntu:latest
LABEL authors="alex2"

ENTRYPOINT ["top", "-b"]