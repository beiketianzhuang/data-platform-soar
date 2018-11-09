#data-platform-soar

##介绍
基于小米开源工具soar的java应用（sql审核平台），本项目是一个简单的spring boot应用，前端是采用前端开源框架layui，前端与后端的通信采用的是websocket方式

### 运行

####1. 推荐方式
``` shell
1.请参考soar的官方教程安装soar，并将soar的路径加入环境变量

2.进入项目主目录

3.mvn package dependency:copy

4.cd dev

5.nohup  java  -jar  data-soar.jar &

```
``` html
通过 http://localhost:8080 访问
```

####2. docker运行

``` shell
1.进入项目主目录
2.mvn package dependency:copy
3.wget http://golangtc.com/static/go/go1.10.3.linux-amd64.tar.gz
4.docker build -f dev/Dockerfile -t boot-soar:latest --rm=true .
5.docker run <container id>
```
``` html
通过 http://localhost:8080 访问
```
