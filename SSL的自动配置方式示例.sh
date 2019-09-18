#!/bin/bash

renew="$1"
# 自行管理阿里云的权限，必须授予完全的HTTPDNS权限（AliyunHTTPDNSFullAccess），建议使用RAM方式管理授权。
# 可以把变量设置为系统的环境变量，方便后面使用。
#export Ali_Key=xx
#export Ali_Secret=yy

# 基于Namecheap网站注册的域名，则需要有下面三个环境变量
#export NAMECHEAP_USERNAME="..."
#export NAMECHEAP_API_KEY="..."
#export NAMECHEAP_SOURCEIP="..."

# 准备Nginx的相关目录和文件
#/var/log/nginx/access.log

domains=("weidoc.cn")

# 更新证书
case "$renew" in
    renew)
        echo "更新证书"
        for domain in ${domains[@]}; do
            echo "~/.acme.sh/acme.sh --issue --force -d ${domain} -d *.${domain} --dns dns_ali --reloadcmd \"docker restart nginx\""
            ~/.acme.sh/acme.sh --issue --force -d ${domain} -d *.${domain} --dns dns_ali --reloadcmd "docker restart nginx"
            #~/.acme.sh/acme.sh --issue --force -d ${domain} -d *.${domain} --dns dns_namecheap --reloadcmd "docker restart nginx"
        done
    ;;
esac


# 配置和启动Nginx
volumes="-v /data/docker/nginx/web/default:/web/default:ro"

mkdir -p /data/docker/nginx/logs

# 默认站点
mkdir -p /data/docker/nginx/web/default
touch /data/docker/nginx/web/default/index.html



mkdir -p /data/docker/nginx/conf.d
rm /data/docker/nginx/conf.d/*

# 配置Nginx，默认站点
echo -e "server {
    listen       80;
    server_name  localhost;
    location / {
        root   /web/default;
        index  index.html index.htm;
    }
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /web/default;
    }
}
 "\
    > /data/docker/nginx/conf.d/default.conf

if [ ! -f "${HOME}/.acme.sh/acme.sh" ]; then
    echo "安装acme.sh"
    sudo apt update
    sudo apt install socat
    curl  https://get.acme.sh >> get.acme.sh
    sh ./get.acme.sh
fi
for domain in ${domains[@]}; do
    volumes="${volumes} -v /data/docker/nginx/web/${domain}:/web/${domain}:ro "

    mkdir -p /data/docker/nginx/web/${domain}
    echo "${domain} ok" > /data/docker/nginx/web/${domain}/index.html
    echo "${domain} error 500" > /data/docker/nginx/web/${domain}/50x.html
    echo -e "server {
        listen       80;
        listen       443 ssl;
        server_name  ${domain};
        ssl_certificate  /keys/${domain}/fullchain.cer;
        ssl_certificate_key /keys/${domain}/${domain}.key;
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;
        location / {
            root   /web/${domain};
            index  index.html index.htm;
            if (\$scheme = http) {
                return 301 https://\$server_name\$request_uri;
                #rewrite ^(.*)\$  https://\$host\$1 permanent; 
            }
        }
    }
     "\
        > /data/docker/nginx/conf.d/${domain}.conf

done

# 启动nexus
mkdir -p /data/docker/nexus
sudo chown -R 200 /data/docker/nexus
sudo ufw allow 8081
docker stop nexus
docker rm nexus
# --cpus 4
docker run -d -p 8081:8081 \
    --name nexus \
    -v /data/docker/nexus:/nexus-data \
    -e INSTALL4J_ADD_VM_PARAMS="-Xmx2g -XX:MaxDirectMemorySize=3g" \
    sonatype/nexus3

echo -e "server {
    listen       80;
    listen       443 ssl;
    server_name  nexus.weidoc.cn;
    ssl_certificate  /keys/weidoc.cn/fullchain.cer;
    ssl_certificate_key /keys/weidoc.cn/weidoc.cn.key;
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    location / {

        proxy_pass http://172.17.0.1:8081/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \"https\";

        if (\$scheme = http) {
            return 301 https://\$server_name\$request_uri;
            #rewrite ^(.*)\$  https://\$host\$1 permanent; 
        }
    }
}
server {
    listen       80;
    listen       443 ssl;
    server_name  nexus.wedoc.io;
    ssl_certificate  /keys/wedoc.io/fullchain.cer;
    ssl_certificate_key /keys/wedoc.io/wedoc.io.key;
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    location / {

        proxy_pass http://172.17.0.1:8081/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \"https\";

        if (\$scheme = http) {
            return 301 https://\$server_name\$request_uri;
            #rewrite ^(.*)\$  https://\$host\$1 permanent; 
        }
    }
}
 "\
    > /data/docker/nginx/conf.d/nexus.conf

# 重新配置基于Docker运行的Nginx
docker stop nginx
docker rm nginx

echo "卷映射: ${volumes}"
docker run --name nginx \
    -v /data/docker/nginx/conf.d:/etc/nginx/conf.d \
    ${volumes} \
    -v ${HOME}/.acme.sh/:/keys/:ro \
    -v /data/docker/nginx/logs:/var/log/nginx \
    -p 80:80 \
    -p 443:443 \
    -d nginx






