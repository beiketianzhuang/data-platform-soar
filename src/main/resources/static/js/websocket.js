var ws = new WebSocket("ws://localhost:8080/myHandler");
ws.onmessage = function (message) {
    console.log(message.data);
    if (JSON.parse(message.data).verifyInfo !== null && JSON.parse(message.data).verifyInfo !== undefined) {
        showResponse(message.data);
    }
    if (JSON.parse(message.data).queryStatus !== null && JSON.parse(message.data).queryStatus !== undefined) {
        showQueryResult(message.data);
    }
};


ws.onclose = function (p1) {

};

ws.onopen = function (evnt) {
    console.log("onopen: ", evnt);
    heartCheck.start();
};

//发送心跳数据
var heartCheck = {
    timeout: 20000,//10s
    timeoutObj: null,
    reset: function () {
        clearInterval(this.timeoutObj);
        this.start();
    },
    start: function () {
        this.timeoutObj = setInterval(function () {
            if (ws.readyState == 1) {
                ws.send("HeartBeat");
            }
        }, this.timeout)
    }
};

function showResponse(message) {
    var response = $("#response");
    console.log(JSON.parse(message));
    response.html(JSON.parse(message).verifyInfo);
    var ele = layui.element;
    ele.tabChange('docDemoTabBrief', 'verfiyResult');
}


var cols = [];
var data = [];

function showQueryResult(message) {
    data = JSON.parse(message).result;
    var metas = [];

    for (var i = 0; i < JSON.parse(message).resultMeta.length; i++) {
        var meta = {field: JSON.parse(message).resultMeta[i], title: JSON.parse(message).resultMeta[i]};
        metas[i] = meta;
        console.log(meta);
    }
    console.log(metas);
    cols[0] = metas;
    console.log(cols);
    console.log(data);
    var table = layui.table;
    table.render({
        elem: '#queryResult'
        , data: data //数据接口
        , page: true //开启分页
        , cols: cols
    });
    var ele = layui.element;
    ele.tabChange('docDemoTabBrief', 'queryResult');
}
