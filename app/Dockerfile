FROM openjdk:8-jre

WORKDIR /opt

# configure the "inativ" user
RUN /usr/sbin/useradd --create-home --home-dir /home/anonymous --shell /bin/bash anonymous

ENV TZ Europe/Paris

COPY build/install/app/bin/ /opt/bin
COPY build/install/app/lib/ /opt/lib

USER anonymous

ENTRYPOINT ["/opt/bin/app"]
