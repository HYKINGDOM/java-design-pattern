# 简述

本文的目的，是为了讲解super-portal项目整体的部署过程，与此同时在super-portal项目中，由于本文涉及到大量的脚本和命令，因此会在项目中携带一个名为re-deploy.sh的文件，此文件只需要执行就会完全重新部署项目，并且会把之前所有的数据全部清除。

在构建后端微服务程序的时候，需要连接MySQL和Redis，而因为MySQL和Redis都映射了本地端口，所以只需要手动在/etc/hosts中增加：

```
127.0.0.1  redis
127.0.0.1  mysql-server
```

同时要提前准备好Java、Maven、Node.js的相关环境！

```shell
wget https://nodejs.org/dist/v10.16.3/node-v10.16.3-linux-x64.tar.xz
tar -Jxf node-v10.16.3-linux-x64.tar.xz
cd node-v10.16.3-linux-x64
sudo bash
echo export NODE_HOME=`pwd` > /etc/profile.d/node.sh
echo PATH=`pwd`/bin:$PATH >> /etc/profile.d/node.sh
exit
source /etc/profile.d/node.sh
npm  config  set metrics-registry "https://registry.npm.taobao.org/"
npm  config  set  registry  "https://registry.npm.taobao.org/"
```



```shell
# 检查是否有JDK的文件，如果没有则自动下载一个
if [ ! -f "openjdk-11.0.2_linux-x64_bin.tar.gz" ]; then
    sudo wget https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz
fi

# 复制压缩包到/usr/local目录下并解压缩
sudo cp openjdk-11.0.2_linux-x64_bin.tar.gz /usr/local/
cd /usr/local/
if [ -e "jdk-11.0.2" ]; then
    sudo rm -rf jdk-11.0.2
fi
sudo tar -zxf openjdk-11.0.2_linux-x64_bin.tar.gz
sudo ln -s jdk-11.0.2 jdk

# 配置JAVA_HOME和PATH环境变量
sudo bash
echo "JAVA_HOME=/usr/local/jdk" > /etc/profile.d/jdk.sh
echo "PATH=\$JAVA_HOME/bin:\$PATH" >> /etc/profile.d/jdk.sh
exit

source /etc/profile.d/jdk.sh
```



```shell
sudo mkdir -p /data/maven_repository
sudo chmod 777 /data/maven_repository

if [ ! -f "apache-maven-3.6.2" ]; then
    echo "正在下载Maven..."
    #wget http://mirrors.shu.edu.cn/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
    # 在清华的镜像下载文件
    wget --connect-timeout 10 -t 1 http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz
    DOWNLOAD_STATUS=$?
    if [ "$DOWNLOAD_STATUS" -ne "0" ]; then
        # 如果下载失败，则在官方备份服务器下载文件
        wget --connect-timeout 10 -t 1 https://www-us.apache.org/dist/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz
    fi
    echo "Maven下载成功."
fi
if [ -e "apache-maven-3.6.0" ]; then
    rm -rf apache-maven-3.6.0
fi
tar -zxf apache-maven-3.6.0-bin.tar.gz

# 配置Maven从阿里云的镜像下载jar文件
echo '<?xml version="1.0" encoding="UTF-8"?>' > apache-maven-3.6.0/conf/settings.xml
echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> apache-maven-3.6.0/conf/settings.xml
echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> apache-maven-3.6.0/conf/settings.xml
echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">' >> apache-maven-3.6.0/conf/settings.xml
#    echo "    <localRepository>$SCRIPT_DIR/maven_repository</localRepository>" >> apache-maven-3.6.0/conf/settings.xml
echo "    <localRepository>/data/maven_repository</localRepository>" >> apache-maven-3.6.0/conf/settings.xml
echo '    <pluginGroups>' >> apache-maven-3.6.0/conf/settings.xml
echo '    </pluginGroups>' >> apache-maven-3.6.0/conf/settings.xml
echo '    <proxies>' >> apache-maven-3.6.0/conf/settings.xml
echo '    </proxies>' >> apache-maven-3.6.0/conf/settings.xml
echo '    <servers>' >> apache-maven-3.6.0/conf/settings.xml
echo '    </servers>' >> apache-maven-3.6.0/conf/settings.xml
echo '    <mirrors>' >> apache-maven-3.6.0/conf/settings.xml
echo '        <mirror>' >> apache-maven-3.6.0/conf/settings.xml
echo '            <id>all</id>' >> apache-maven-3.6.0/conf/settings.xml
echo '            <mirrorOf>*</mirrorOf>' >> apache-maven-3.6.0/conf/settings.xml
echo '            <url>https://maven.aliyun.com/repository/central</url>' >> apache-maven-3.6.0/conf/settings.xml
echo '        </mirror>' >> apache-maven-3.6.0/conf/settings.xml
echo '    </mirrors>' >> apache-maven-3.6.0/conf/settings.xml
echo '    <profiles>' >> apache-maven-3.6.0/conf/settings.xml
echo '    </profiles>' >> apache-maven-3.6.0/conf/settings.xml
echo '</settings>' >> apache-maven-3.6.0/conf/settings.xml

mkdir ~/.m2
cp apache-maven-3.6.0/conf/settings.xml ~/.m2/

# 配置Maven的环境变量，把mvn命令的路径加入PATH环境变量中
sudo bash
echo "M2_HOME=$SCRIPT_DIR/apache-maven-3.6.0" > /etc/profile.d/maven.sh
echo "PATH=\$M2_HOME/bin:\$PATH" >> /etc/profile.d/maven.sh
exit
source /etc/profile.d/maven.sh
```



# 网络配置

后端服务、前端程序，都放在一个网络环境下，通过Nginx反向代理统一对外提供服务。而Nginx将会映射到物理机的端口，用于域名解析、浏览器访问。

Docker中运行程序的时候，IP经常会发生变化，所以需要通过服务的名称来进行访问。如果不是在Docker中运行，则可以通过自己修改/etc/hosts文件的方式来实现名称和IP的转换。

```shell
# 此命令是在Docker创建一个网络配置，名称为micro-services，后面所有的服务都需要运行在此网络上面
docker network create micro-services
```



# Redis安装和配置

在Docker中运行Redis，主要是配置网络访问和本地存储。

```shell
# --network micro-services 指定前面创建的网络
# --name 指定服务名称
# --network-alias redis 网络名称的别名，多个同一个网络的容器之间可以通过此名称相互访问
# -p 6379:6379  此参数用于映射容器的端口到物理机，可选。这里主要是方便在物理机上面开发测试才映射的
docker run --restart always  \
	--network micro-services \
	--name redis \
	--network-alias redis \
	-p 6379:6379 
	-d \
	redis
```

如果需要数据持久化，需要增加卷映射

```shell
# 数据存储目录
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
```



# MySQL安装和配置

```shell
# 创建数据存储目录
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
	-p 3306:3306 \
	--restart always \
	-d \
	mysql:8.0.17

```



导入测试数据是每次重建以后都需要执行的，所以一并也写入脚本中

```shell
# 导入测试数据，需要MySQL客户端
mysql -h127.0.0.1 -uroot -p1234 < cloud.sql
```



# 构建后端服务镜像

通过mvn docker:build命令即可构建后端服务的镜像，但是在构建后端服务镜像之前，需要把整个项目install一下。并且此处的说明中，并没有把镜像push到仓库里面，所以运行的时候要记得携带版本号。

```shell
# 切换到项目中portal目录下再执行命令
mvn install

# 然后切换到每个服务的目录下，构建镜像。如：
cd api-gateway
mvn docker:build

# 其余服务都相同，一个个构建即可
```



# 启动后端服务

前面一步已经把镜像放入Docker中，现在启动后端服务的时候，只需要把容器开启来即可，记住：指定主机名，且镜像要指定版本号。

注意：必须等待注册中心启动成功以后，才能启动配置中心；等配置中心启动成功以后，才能启动其他服务！

第一次使用run命令，创建容器:

```shell
# 每个服务都差不多，这里仅以api-gateway作为示例

# 停止运行中的容器
docker stop api-gateway
# 删除已有容器，但并不是删除镜像！
docker rm api-gateway
docker run \
	--restart always \
	--network micro-services \
	--name api-gateway \
	--network-alias api-gateway \
	-d \
	api-gateway:1.0-SNAPSHOT
```

如果要重启容器:

```shell
# 停止容器
docker stop api-gateway
# 启动容器
docker start api-gateway

# 或者直接使用restart命令
docker restart api-gateway
```



# 构建前端程序

进入portal-ui目录中，可以构建前端程序。需要注意的是：构建出来的前端程序需要放到Nginx去运行，因为前端程序在构建出来以后，就没有Node.js环境了。

构建前端程序之前，需要先安装Node.js环境

```shell
# 构建程序
npm run build
npm install

# 复制构建后的文件到Nginx的目录中，用于后面的部署
mkdir -p /data/docker/nginx/web/default
cp -a ./dist/* /data/docker/nginx/web/default/
```



# 配置Nginx代理

```shell
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
docker stop nginx
docker rm nginx

docker run --name nginx \
    -v /data/docker/nginx/conf.d:/etc/nginx/conf.d \
    -v /data/docker/nginx/web/default/:/web/default/:ro \
    -v /data/docker/nginx/logs:/var/log/nginx \
    -d nginx
```



# 测试程序

测试的时候，在浏览器中输入服务器的物理IP即可，测试账号为13924120301，密码是1234