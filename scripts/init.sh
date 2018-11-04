#!/bin/bash
set -eu

echo 'Check install environment'

cmd="soar"

if ! command -v "${cmd}" &> /dev/null ; then
    echo -e "\033[91m${cmd} is not set.Eg:\nalias ${cmd}=/root/${cmd}\nexport ${cmd}=/root/${cmd}\033[0m"
    exit 1
else
    echo "${cmd} is ok"
fi