layui.use(['form', 'layer','element','table'], function () { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
    var form = layui.form;
    var element = layui.element;
    var table = layui.table;
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
                        url: "/schemas",
                        type: "post",
                        data: JSON.stringify($('#sub').serializeObject()),
                        contentType: "application/json",
                        dataType: "JSON",
                        success: function (data) {
                            layer.msg(data.message, {
                                time: 2000,
                            });
                            layer.close(index);
                            // console.log(JSON.parse(JSON.stringify($('#sub').serializeObject())).schema);
                            // $('.layui-anim.layui-anim-upbit').append('<dd lay-value="'+JSON.parse(JSON.stringify($('#sub').serializeObject())).schema+'" class>'+JSON.parse(JSON.stringify($('#sub').serializeObject())).schema+'</dd>')
                            // $('#schema_refresh').append('<option value="'+JSON.parse(JSON.stringify($('#sub').serializeObject())).schema+'">'+JSON.parse(JSON.stringify($('#sub').serializeObject())).schema+'</option>');
                            window.location.reload()
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

    form.on('select(get_tables)', function (data) {
        if (data.value == '') {
            return;
        }
        localStorage.setItem("schema",data.value);
        $('#table_refresh').load("/schemas/" + data.value);
    });

    //获取hash来切换选项卡，假设当前地址的hash为lay-id对应的值
    var layid = location.hash.replace(/^#test1=/, '');
    element.tabChange('test1', layid); //假设当前地址为：http://a.com#test1=222，那么选项卡会自动切换到“发送消息”这一项

    //监听Tab切换，以改变地址hash值
    element.on('tab(test1)', function(){
        location.hash = 'test1='+ this.getAttribute('lay-id');
    });

    table.render({
        elem: '#queryResult'
        , data: [] //数据接口
        , page: true //开启分页
        , cols: []
    });
});