FROM openjdk:17.0.2-slim AS build-dist

#RUN sed -i 's@deb.debian.org@nexus.prd.estargo.com.cn:8081/repository/ustc-debian-proxy-repo@g' /etc/apt/sources.list
ADD sources.list /etc/apt/
RUN apt-get update && apt-get install -y maven fontconfig libfreetype6

WORKDIR /usr/src/project
ADD . /usr/src/project/
RUN mvn clean compile package -DskipTests=true

FROM openjdk:17.0.2-slim AS prod
ENV TZ=Asia/Shanghai DEBIAN_FRONTEND=noninteractive
RUN ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone && dpkg-reconfigure --frontend noninteractive tzdata && rm -rf /var/lib/apt/lists/*
COPY --from=build-dist /usr/src/project/order-service-main/target/order-service-main-0.0.1-SNAPSHOT.jar /bin/order-service-main-0.0.1-SNAPSHOT.jar
EXPOSE 10100
EXPOSE 10110
ENTRYPOINT ["java", "-Xms2g", "-Xmx2g", "-XX:MetaspaceSize=256m", "-XX:MaxMetaspaceSize=256m", "-XX:ReservedCodeCacheSize=64m", "-XX:InitialCodeCacheSize=64m", "-XX:CompressedClassSpaceSize=64m", "-XX:StringTableSize=1000003", "-XX:+UseZGC", "-XX:ConcGCThreads=1", "-XX:ParallelGCThreads=2", "-XX:ZCollectionInterval=120", "-XX:ZAllocationSpikeTolerance=5", "-XX:+UnlockDiagnosticVMOptions", "-XX:-ZProactive", "-jar", "/bin/order-service-main-0.0.1-SNAPSHOT.jar"]