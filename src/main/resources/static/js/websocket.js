var ws = new WebSocket("ws://localhost:8080/myHandler");
ws.onmessage = function (message) {
    console.log(message.data);
    showResponse(message.data);
}


ws.onclose = function (p1) {

}

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
}