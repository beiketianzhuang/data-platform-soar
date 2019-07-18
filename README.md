# data-platform-soar

## 介绍
基于小米开源工具soar的java应用（sql审核平台），本项目是一个简单的spring boot应用，前端是采用前端开源框架layui，前端与后端的通信采用的是websocket方式
```txt
1.可以动态添加数据库，对指定表进行数据查询(没有对查询数据量没有限制)
2.集成soar，可以对平日的sql进行审核分析，规范sql的编写
3.通过druid对sql进行解析，可以对查询表做屏蔽敏感字段（需要完善）
```

### 运行

#### 1. 推荐方式
``` shell
1.请参考soar的官方教程安装soar，并将soar的路径加入环境变量

2.进入项目主目录

3.mvn package -Dmaven.test.skip=true  dependency:copy

4.cd dev

5.nohup  java  -jar  data-soar.jar &

```
``` html
通过 http://localhost:8080 访问
```

#### 2. docker运行

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

### DEMO
![Aaron Swartz](https://github.com/beiketianzhuang/data-platform-verfiy/blob/master/demo1.jpg)
![Aaron Swartz](https://github.com/beiketianzhuang/data-platform-verfiy/blob/master/demo2.jpg)
![Aaron Swartz](https://github.com/beiketianzhuang/data-platform-verfiy/blob/master/demo3.jpg)
![Aaron Swartz](https://github.com/beiketianzhuang/data-platform-verfiy/blob/master/demo4.png)
