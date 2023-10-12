FROM mysql:5.7

MAINTAINER Zsolt

ARG MYSQL_DATABASE
ARG MYSQL_USER
ARG MYSQL_PASSWORD
ARG MYSQL_ROOT_PASSWORD

# alapertelmezett adatbazis, amit letrehoz inicializalaskor
# altalanos felhasznalo neve
# a fentebbi felhasznalo jelszava
# root felhasznalo jelszava
ENV MYSQL_DATABASE=${MYSQL_DATABASE} \
    MYSQL_USER=${MYSQL_USER} \
    MYSQL_PASSWORD=${MYSQL_PASSWORD} \
    MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}

ADD scripts/bibliospring-schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306
