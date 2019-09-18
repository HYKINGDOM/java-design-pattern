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

start() {
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
    echo "启动${service}服务"
    cd "${service}" || exit
    mvn spring-boot:start
    RETVAL=$?
    cd .. || exit
    if [ -$RETVAL -eq 0 ]; then
      echo "服务${service}启动成功"
    else
      echo "服务${service}启动失败，程序退出"
      break
    fi
  done
  unset i
  if [ -$RETVAL -eq 0 ]; then
    echo "所有服务启动成功"
  else
    echo "部分服务启动失败，请检查日志！"
  fi
  return $RETVAL
}

stop() {
  echo "正在停止所有服务..."
  i=${#services[@]}
  i=${i}-1
  while [[ i -ge 0 ]]; do
    service="${services[i]}"
    echo "停止${service}服务"
    cd "${service}" || exit
    mvn spring-boot:stop
    RETVAL=$?
    cd .. || exit
    if [ -$RETVAL -eq 0 ]; then
      echo "服务${service}停止成功"
    else
      echo "服务${service}停止失败，程序退出"
      break
    fi
    i=${i}-1
  done
  unset i
  if [ -$RETVAL -eq 0 ]; then
    echo "所有服务停止成功"
  else
    echo "部分服务停止失败，请检查日志！"
  fi
  return $RETVAL
}

case "$1" in
start)
  start
  ;;
stop)
  stop
  ;;
*)
  echo $"Usage: $0 {start|stop}"
  RETVAL=1
  ;;
esac
exit $RETVAL
