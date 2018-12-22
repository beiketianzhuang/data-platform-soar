var ws = new WebSocket("ws://localhost:8080/myHandler");
ws.onmessage = function (message) {
    console.log(message.data);
    if (JSON.parse(message.data).verifyInfo !== null && JSON.parse(message.data).verifyInfo !== undefined) {
        showResponse(message.data);
    }
    if (JSON.parse(message.data).queryStatus !== null && JSON.parse(message.data).queryStatus !== undefined) {
        showQueryResult(message.data);
    }

    if (JSON.parse(message.data).parserSql !== null && JSON.parse(message.data).parserSql !== undefined || JSON.parse(message.data).error != undefined) {
        showParserSql(message.data);
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
    var status = JSON.parse(message).queryStatus;
    if ("FAILURE" === status) {

        layer.prompt({
            formType: 2,
            value: JSON.parse(message).queryErrorMsg,
            title: '错误提示',
            area: ['600px', '200px'] //自定义文本域宽高
        }, function (value, index, elem) {
            layer.close(1);
            layer.close(2)
        });
        return;
    }
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

function showParserSql(message) {
    var error = JSON.parse(message).error;
    if (undefined !== error) {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.msg(JSON.parse(message).error);
        });
        return;
    }
    var response = $("#columnParser");
    console.log(JSON.parse(message));
    ;
    var msg = '';
    for (var i = 0; i < JSON.parse(message).parserSql.length; i++) {
        msg += JSON.parse(message).parserSql[i] + "<br>";
    }
    response.html(msg);
    var ele = layui.element;
    ele.tabChange('docDemoTabBrief', 'columnParser');
}