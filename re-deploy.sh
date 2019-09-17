#!/bin/bash

sudo mkdir /data
sudo chown $USER:root /data

########### 创建网络
docker network create micro-services

########### 配置和运行Redis
# 数据存储目录
docker stop redis
docker rm redis
sudo rm -rf /data/docker/redis
mkdir -p /data/docker/redis

# 生成配置文件
echo -e "
protected-mode no
daemonize no
pidfile \"/data/pid\"
logfile \"log\"
dbfilename \"dump.rdb\"
dir \"/data\"
" > /data/docker/redis/redis.conf

# 加了-v参数，并且在redis后面通过redis-server启动服务时指定配置文件
docker run --restart always  \
	--name redis \
	-v "/data/docker/redis":/data \
	--network-alias redis \
	--network micro-services \
	-d redis redis-server /data/redis.conf

########### 配置和运行MySQL
# 创建数据存储目录
docker stop mysql
docker rm mysql
sudo rm -rf /data/docker/mysql
mkdir -p /data/docker/mysql/conf.d
mkdir -p /data/docker/mysql/data

# 生成配置文件
echo -e "
[mysqld_safe]
timezone=HKT

[mysqld]
lower-case-table-names=1
character-set-server=utf8mb4
default-time-zone='+8:00'
" > /data/docker/mysql/conf.d/my.cnf

# -e参数用于指定容器的环境变量
# -e MYSQL_ROOT_PASSWORD=1234用于设置root用户的密码，此密码可以在登录后修改
docker run \
	--network micro-services \
	--name mysql-server \
	--network-alias mysql-server \
	-e MYSQL_ROOT_PASSWORD=1234 \
	-v /data/docker/mysql/conf.d:/etc/mysql/conf.d \
	-v /data/docker/mysql/data:/var/lib/mysql \
	--restart always \
	-d \
	mysql:8.0.17

ROOT=${PWD}
########### 创建Docker镜像
services=("registry-center" "config-center" "user-center" "verify-code" "oauth-server" "storage" "we-chat" "content" "daily-sign-in" "monitor" "api-gateway")
version="1.0-SNAPSHOT"

echo "本程序用于构建所有服务的镜像，首先会先在本地Maven仓库安装所有程序，然后把构建的镜像放在本地Docker容器里面。"
cd "${ROOT}/portal" || exit
mvn install

for service in ${services[@]}; do
    echo "构建${service}服务的镜像"
    docker stop ${service}
    docker rm ${service}
    cd "${ROOT}/portal/${service}" || exit
    mvn docker:build
done


########### 启动应用容器
echo "如果已经把镜像构建放入Docker容器中，则可以使用此脚本在容器中启动一个实例，实例的名称跟服务名称相同。"
for service in ${services[@]}; do
    echo "启动${service}服务"
    docker run --network micro-services --name ${service} --network-alias ${service} \
        -P \
        -d ${service}:${version}
    sleep 10
done
echo "所有服务的服务实例启动成功"


########### 构建前端程序
# 构建程序
cd "${ROOT}/portal-ui"
npm run build

# 复制构建后的文件到Nginx的目录中，用于后面的部署
docker stop nginx
docker rm nginx

sudo rm -rf /data/docker/nginx
mkdir -p /data/docker/nginx/web/default
cp -a ./dist/* /data/docker/nginx/web/default/


########### 配置和运行Nginx
mkdir -p /data/docker/nginx/conf.d
mkdir -p /data/docker/nginx/logs
echo -e "server {
    listen       80;
    server_name  _;
    location / {
        root   /web/default;
        index  index.html index.htm;
    }
    location /api/{
        proxy_pass http://api-gateway:20001/api/;
    }

    location /oauth2/{
        proxy_pass http://api-gateway:20001/oauth2/;
    }

    location /login/oauth2{
        proxy_pass http://api-gateway:20001/login/oauth2;
    }
}
 "\
    > /data/docker/nginx/conf.d/default.conf

# 重新配置基于Docker运行的Nginx

docker run --name nginx \
    -v /data/docker/nginx/conf.d:/etc/nginx/conf.d \
    -v /data/docker/nginx/web/default/:/web/default/:ro \
    -v /data/docker/nginx/logs:/var/log/nginx \
    -d nginx








