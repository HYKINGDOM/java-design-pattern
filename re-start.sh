#!/bin/bash

########### 重启微服务
services=("registry-center" "config-center" "user-center" "verify-code" "oauth-server" "storage" "we-chat" "content" "daily-sign-in" "monitor" "api-gateway")
version="1.0-SNAPSHOT"

echo "本程序用于构建所有服务的镜像，首先会先在本地Maven仓库安装所有程序，然后把构建的镜像放在本地Docker容器里面。"
mvn install

for service in ${services[@]}; do
    echo "重启${service}服务"
    docker stop ${service}
    docker start ${service}
    sleep 10
done

# 重启Nginx
docker restart nginx







