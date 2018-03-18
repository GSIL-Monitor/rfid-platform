<%@ page language="java" pageEncoding="UTF-8" %>

<script>

    function initDialog() {
        initNotification();

        //提交对话框
        $("#attribute_dialog").kendoDialog({
            closable: false,
            height: 450,
            width: 500,
            title: "信息明细",
            hide: function () {
                $('#attributeForm').clearForm();
                $('#attributeForm').resetForm();
            },
            close: function (e) {
                $('#attributeForm').clearForm();
                $('#attributeForm').resetForm();
            },
            actions: [{
                text: "确定",
                action: submitForm,
                primary: true
            }, {
                text: "关闭"
            }],
            close: function (e) {
                return true;
            }
        }).data("kendoDialog").close();

        $("#progressDialog").kendoDialog({
            width: "400px",
            height: "250px",
            title: "提示",
            closable: false,
            animation: true,
            modal: true,
            content: '<center><h3>正在处理中...</h3></center>' +
            '<div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 45%">' +
            '<span class="sr-only">100%</span></div></div>',
            buttonLayout: "normal"
        }).data("kendoDialog").close();
        $("#attr_type").kendoComboBox({
            dataSource: [
                {name: "操作人", id: 1},
                {name: "产权所有者", id: 2}
            ],
            dataTextField: "name",
            dataValueField: "id",
            height: 400,
            index: 1
        }).data("kendoComboBox").input[0].name = "";

        $("#attributeForm").kendoValidator({
            messages: {
                required: "必须填写"
            }
        });
    }
    function submitForm() {
        var options = {
            type: "POST",//请求方式：get或post
            dataType: "json",//数据返回类型：xml、json、script
            url: basePath + '/cclean/saveAddedAttribute.do',
            beforeSerialize: function () {
                var validator = $("#attributeForm").data("kendoValidator");
                if (!validator.validate()) {
                    return false;
                } else {
                    return true;
                }
            },
            //data:{'txt':"JQuery"},//自定义提交的数据
            beforeSubmit: function () {
                openProgress();
            },
            success: function (json) {//表单提交成功回调函数
                closeProgress();
                if (json.success) {
                    search();
                    $("#notification").data('kendoNotification').showText('成功！', 'success');
                    $("#attribute_dialog").data("kendoDialog").close();
                } else {
                    $("#notification").data('kendoNotification').showText('操作失败！' + json.msg, 'error');
                }
            },
            error: function (err) {
                closeProgress();
                $("#notification").data('kendoNotification').showText('操作失败！', 'error');
            }
        };
        $("#attributeForm").ajaxSubmit(options);
        return false;
    }
    function openProgress() {
        $("#progressDialog").data('kendoDialog').open();
    }
    function closeProgress() {
        $("#progressDialog").data('kendoDialog').close();
    }

    function initNotification() {
        $("#notification").kendoNotification({
            position: {
                top: 50
            },
            stacking: "left"
        }).data("kendoNotification").hide();
    }
</script>
<div id="attribute_dialog">

    <form id="attributeForm">
        <input type="text" name="id" hidden>
        <div class="form-group">
            <label for="attr_type">类型</label>
            <label for="attr_type" id="typeName" style="font-size: larger"></label>
            <input type="text" class="form-control" id="attr_type" name="type" required>

        </div>
        <div class="form-group">
            <label for="code">编号</label>
            <input type="text" class="form-control" id="code" name="code" required>
        </div>
        <div class="form-group">
            <label for="name">名称</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="remark">备注</label>
            <textarea class="form-control" rows="3" id="remark" name="remark"></textarea>
        </div>
    </form>
</div>

<div id="progressDialog"></div>
<span id="notification"></span>