#FROM maven:latest as builder
#
#COPY . /project
#
#WORKDIR /project
#
#RUN mvn clean package -Dmaven.test.skip=true

# 拉取运行环境，这个镜像打包出的镜像比较小，如需要可换成oracle的jre
# 记得先在本地编译了再打包上去，在容器里编译太慢了
FROM fabric8/java-alpine-openjdk11-jre

COPY  ./target/online_blackboard-1.0.jar /

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/online_blackboard-1.0.jar"]

EXPOSE 18888