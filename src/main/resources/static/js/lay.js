layui.use(['form', 'layer'], function () { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
    var form = layui.form
    //触发事件
    var active = {
        notice: function () {
            //示范一个公告层
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , closeBtn: false
                , area: '600px;'
                , shade: 0.8
                , id: 'LAY_layuipro' //设定一个id，防止重复弹出
                , btn: ['添加', '取消']
                , btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: $("#form")
                , yes: function (index, layero) {
                    $.ajax({
                        url: "/schema",
                        type: "post",
                        data: JSON.stringify($('#sub').serializeObject()),
                        contentType: "application/json",
                        dataType: "JSON",
                        success: function (data) {
                            layer.msg(data.message, {
                                time: 2000,
                            });
                            layer.close(index)
                        },
                        error: function (e, data) {
                            console.log(e.responseJSON.message)
                            console.log(data)
                            layer.msg(e.responseJSON.message, {
                                time: 3000,
                            })
                        }
                    });
                }
            });
        }

    };

    $('#layerDemo .layui-btn').on('click', function () {
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });

    //定义form表单规则
    form.verify({
        title: function (value) {
            if (value.length < 5) {
                return '标题至少得5个字符啊';
            }
        }
        , pass: [/(.+){6,12}$/, '密码必须6到12位']
        , content: function (value) {
            layedit.sync(editIndex);
        }
    });

});