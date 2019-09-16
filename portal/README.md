# 开发环境运行说明

配置文件分两个不同的管理方式，一种是通过Git仓库管理的，用于存储正式环境的配置文件。而在在开发环境中，所有的服务都使用dev配置，因此必须要激活dev配置文件才能正常使用，比如：

```shell
cd api-gateway
mvn  -Dspring.profiles.active=dev  spring-boot:start
```

需要注意的是：配置中心同时还需要激活native配置，才能读取本地配置文件，否则将使用Git仓库里面的配置文件。命令：

```shell
cd config-center
mvn -Dspring.profiles.active=dev,native spring-boot:start
```

除此之外，必须要保证Redis和MySQL正常运行，否则许多服务没法正常启动。

```shell
# 如果需要持久化数据，请建立目录
# Redis配置文件名目录
mkdir -p /data/docker/redis/conf.d
# Redis数据存储目录
mkdir -p /data/docker/redis/data
# 这里放MySQL数据库的配置文件，请放一个名为my.cnf的文件
mkdir -p /data/docker/mysql/conf.d
# MySQL数据存储目录
mkdir -p /data/docker/mysql/data

# 如果没有网络，需要先创建网络，并让所有服务都在这个网络上面运行
docker network create micro-services

# 使用Docker运行一个Redis实例，也可以考虑部署为一个Redis集群
docker run --network micro-services --name redis --network-alias redis -p 6379:6379 --restart always -d redis

# 使用Docker运行一个MySQL实例，运行后需要在创建应用需要的数据库，目前需要名为cloud的数据库
# 数据库root用户的登录名密码通过-e参数指定
docker run --network micro-services \
	--name mysql-server \
	--network-alias mysql-server \
	-e MYSQL_ROOT_PASSWORD=1234 \
	-v /data/docker/mysql/conf.d:/etc/mysql/conf.d \
	-v /data/docker/mysql/data:/var/lib/mysql \
	--restart always \
	-d \
	mysql:8.0.17
```



my.cnf

```shell
[mysqld_safe]
timezone=HKT

[mysqld]
lower-case-table-names=1
character-set-server=utf8mb4
default-time-zone='+8:00'
```



在服务启动以后，导入测试数据到MySQL，登录名是13800138000，密码是1234

```mysql
INSERT INTO `user` VALUES 
	('678bf851-786c-40e4-9f82-e7bb40e9002d','2020-02-29 14:01:24.704000'
     ,_binary '',_binary '','13800138000','疯狂软件',
     'oVfatuAD7ifHGACn5ClJ-9bkFRbM',
     '{bcrypt}$2a$10$t7bhcHuMss4D8DguTAMb7.WGywBn5eVV545EMgTrurkhSYl4atQli',
     '2019-12-01 14:01:24.704000','13800138000','2019-09-02 14:01:24.694000',NULL);

INSERT INTO `user_info` VALUES 
	('4901a5b0-8caf-47a0-9d29-481983074ee0','gh_f21f1485101e','广州','中国',_binary '',103,
     'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN9APr4clu4j00USgtLh3vHX4c1pPfVElhgPpVagx6nWPLYXMticQicXUM8NuiaDhHSgWhg3Itv7ma9XHgRibtYtcMDa/132',
     'zh_CN','疯狂软件','oVfatuAD7ifHGACn5ClJ-9bkFRbM','广东','0','','',1,1,'2019-08-26 11:50:41.000000',
     'ADD_SCENE_QR_CODE',1566791441,NULL,'678bf851-786c-40e4-9f82-e7bb40e9002d');

INSERT INTO `user_roles`( USER_ID, ROLES_ID ) (SELECT U.ID, R.ID FROM `user` U, `role` R);
```



# Docker发布运行说明

在Docker中运行的时候，为了在IP变化时也能够正常找到服务，所有的服务均通过--network-alias参数指定了主机名，Git仓库中的配置文件都是使用主机名来访问服务的。

为了简化起见，可以通过【./docker-build.sh build】命令构建镜像，这种方式构建的镜像只是把镜像放在本地Docker中。构建了镜像以后，则可以通过【./docker.sh run】命令创建所有服务的实例。实例创建以后，下次再运行的时候则应该使用【./docker.sh start】命令来启动所有的服务。docker.sh同时还有stop、rm命令。

在Docker中运行的时候，使用默认profile运行，所以为了能够访问Git仓库中的配置文件，需要在配置中心的bootstrap.yaml文件中配置Git仓库的访问私钥，密钥生成命令：

```shell
ssh-keygen -t rsa
```



# 非Docker发布运行说明

在非Docker中运行时，则需要修改/etc/hosts文件，手动解析主机名，每个服务的名称就是主机名。Windows里面/etc/hosts文件在C:\Windows\System32\drivers\目录下。记录的内容大概如下：

```
127.0.0.1      registry-center
127.0.0.1      config-center
...
```

