#data-platform-soar

##介绍
基于小米开源工具soar的java应用（sql审核平台），本项目是一个简单的spring boot应用，前端是采用前端开源框架layui，前端与后端的通信采用的是websocket方式

### 运行

####1. docker运行

``` shell
mvn package dependency:copy
docker build -f dev/Dockerfile -t boot-soar:latest --rm=true .
docker run <container id>
```
``` html
通过 http://localhost:8080 访问
```
####2. 本地直接运行

```text
 请参考soar的官方教程安装soar，并将soar的路径加入环境变量
```