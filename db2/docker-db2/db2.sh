#https://hub.docker.com/r/ibmcom/db2express-c/
#docker pull ibmcom/db2express-c

WORKINGDIR=/root/projects

docker run -it -v "$(pwd)":${WORKINGDIR}/ \
    -w ${WORKINGDIR} \
    -p 50000:50000 \
    -e DB2INST1_PASSWORD=pwd-sample \
    -e LICENSE=accept \
    ibmcom/db2express-c:latest \
    /bin/bash

# https://vladmihalcea.com/how-to-install-db2-express-c-on-docker-and-set-up-the-jdbc-connection-properties/
# su - db2inst1
# db2start
# db2 create db hibern8
# db2sampl
# docker run -it -p 50000:50000 -e DB2INST1_PASSWORD=db2inst1-pwd -e LICENSE=accept ibmcom/db2express-c:latest bash

# docker run -d -p 50000:50000 -e DB2INST1_PASSWORD=db2inst1-pwd -e LICENSE=accept  ibmcom/db2express-c:latest db2start
