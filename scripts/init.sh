#!/bin/bash
set -eu

echo 'Check install environment'

cmd="soar"

if ! command -v "${cmd}" &> /dev/null ; then
    echo -e "\033[91m${cmd} is not set.Eg:\nalias ${cmd}=/root/${cmd}\nexport ${cmd}=/root/${cmd}\033[0m"
    wget https://github.com/XiaoMi/soar/releases/download/v0.8.1/soar.linux-amd64 -O soar
    chmod a+x soar
    go get -d github.com/XiaoMi/soar
    cd ${GOPATH}/src/github.com/XiaoMi/soar && make
    exit 1
else
    echo "${cmd} is ok"
fi