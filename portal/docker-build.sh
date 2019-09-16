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

# listing all services
services=("registry-center" "config-center" "user-center" "verify-code" "oauth-server" "storage" "test" "we-chat" "content" "daily-sign-in" "monitor" "api-gateway")

build() {
  echo "本程序用于构建所有服务的镜像，首先会先在本地Maven仓库安装所有程序，然后把构建的镜像放在本地Docker容器里面。"
  echo "重新构建并安装所有服务"
  mvn install
  RETVAL=$?
  if [ -$RETVAL -eq 0 ]; then
    echo "服务构建成功，开始启动服务..."
  else
    echo "服务构建失败，程序退出"
    return $RETVAL
  fi

  for i in $(seq 0 ${#services[@]}); do
    service=${services[$i]}
    if [ -z "$service" ]; then
      break
    fi
    echo "构建${service}服务的镜像"
    docker stop ${service}
    cd "${service}" || exit
    mvn docker:build
    RETVAL=$?
    cd .. || exit
    if [ -$RETVAL -eq 0 ]; then
      echo "构建${service}服务的镜像完成"
    else
      echo "构建${service}服务的镜像失败，程序退出"
      break
    fi
  done
  unset i
  if [ -$RETVAL -eq 0 ]; then
    echo "所有服务的镜像构建成功"
  else
    echo "部分服务的镜像构建失败，请检查日志！"
  fi
  return $RETVAL
}


case "$1" in
build)
  build
  ;;
*)
  echo $"Usage: $0 {build}"
  RETVAL=1
  ;;
esac
exit $RETVAL
