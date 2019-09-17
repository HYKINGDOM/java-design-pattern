#!/bin/bash

#
### BEGIN INIT INFO
# Provides:          portal
# Required-Start:    $remote_fs $syslog $mysql
# Required-Stop:     $remote_fs $syslog
# Should-Start:      $network $time
# Should-Stop:       $network $time
# Default-Start:     3 4 5
# Default-Stop:      0 2 1 6
# Short-Description: Start and stop the Tomcat Server
### END INIT INFO
#

. /lib/lsb/init-functions

## 在启动服务之前，必须先启动一个Redis，并且服务网络别名要指定为redis，因为配置文件中使用主机名的方式访问数据库。
## 最简单的启动命令如下：
#
#  docker network create micro-services
#  docker run --network micro-services --name redis --network-alias redis -p 6379:6379 -d redis
#
##

# listing all services
services=("registry-center" "config-center" "user-center" "verify-code" "oauth-server" "storage" "we-chat" "content" "daily-sign-in" "monitor" "api-gateway")
version="1.0-SNAPSHOT"

run() {
  echo "如果已经把镜像构建放入Docker容器中，则可以使用此脚本在容器中启动一个实例，实例的名称跟服务名称相同。"
  for service in ${services[@]}; do
    if [ -z "$service" ]; then
      break
    fi
    echo "启动${service}服务"
    docker stop ${service}
    docker rm ${service}
    docker run --network micro-services --name ${service} --network-alias ${service} \
    -P \
    -d ${service}:${version}
  done
  unset i
  echo "所有服务的服务实例启动成功，下次请使用【$0 start】启动服务"
  return $RETVAL
}

start() {
  echo "启动在Docker中运行的所有服务"
  for service in ${services[@]}; do
    if [ -z "$service" ]; then
      break
    fi
    echo "启动${service}服务"
    docker start ${service} 
  done
  unset i
  echo "所有服务的服务实例启动成功"
  return $RETVAL
}

stop() {
  echo "停止在Docker中运行的所有服务"
  for service in ${services[@]}; do
    if [ -z "$service" ]; then
      break
    fi
    echo "停止${service}服务"
    docker stop ${service} 
  done
  unset i
  echo "所有服务的服务实例停止成功"
  return $RETVAL
}

rm() {
  echo "删除所有在Docker中运行的所有服务"
  for service in ${services[@]}; do
    if [ -z "$service" ]; then
      break
    fi
    echo "停止${service}服务"
    docker stop ${service} \
    -P \
    -d ${service}
    
    echo "删除${service}服务"
    docker stop ${service}
  done
  unset i
  echo "所有服务的服务实例删除成功"
  return $RETVAL
}

case "$1" in
    run)
      run
      ;;
    start)
      start
      ;;
    stop)
      stop
      ;;
    rm)
      rm
      ;;
    *)
      echo $"Usage: $0 {run|start|stop|rm}"
      echo "第一次的时候，应该使用run方式启动，这样会根据镜像创建容器，后面则请使用start方式！"
      RETVAL=1
    ;;
esac
exit $RETVAL
