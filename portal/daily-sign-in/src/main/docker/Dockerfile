# ${base}表示引用pom.xml文件中配置的属性
FROM ${base}
WORKDIR /boot
# maven/目录是一个特别的目录，在使用mvn docker:build的时候会自动把相关文件复制到名为maven的子目录中
COPY maven/ /boot
# 执行启动命令
CMD ["java", "-jar", "${project.build.finalName}.jar"]

# 使用哪些端口号
EXPOSE 20011 30011