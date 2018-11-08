#!/bin/sh
wget https://github.com/XiaoMi/soar/releases/download/v0.8.1/soar.linux-amd64 -O soar
chmod a+x soar
go get -d github.com/XiaoMi/soar
cd ${GOPATH}/src/github.com/XiaoMi/soar && make