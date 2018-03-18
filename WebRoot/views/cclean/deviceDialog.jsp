<%@ page language="java" pageEncoding="UTF-8" %>

<script>

    function initDialog() {
        initNotification();

        //提交对话框
        $("#device_dialog").kendoDialog({
            closable: false,
            height: 500,
            width: 600,
            title: "信息明细",
            hide: function () {
                $('#deviceForm').clearForm();
                $('#deviceForm').resetForm();
            },
            close: function (e) {
                $('#deviceForm').clearForm();
                $('#deviceForm').resetForm();
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
        $("#deviceId").kendoComboBox({
            dataSource: {
                transport: {
                    read: basePath + "/data/device/list.do"
                }
            },
            dataTextField: "code",
            dataValueField: "code",
            height: 400
         }).data("kendoComboBox").input[0].name = "";
        $("#warehouseCode").kendoComboBox({
            dataSource: {
                transport: {
                    read: basePath + "/api/cclean/downloadWarehouseWS.do?deviceId=KE201601"
                },
                schema: {
                    data: "result"
                }
            },
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            change: function (e) {
                $('#warehouse').val(this.text());
            }
        }).data("kendoComboBox").input[0].name = "";
        $("#factoryCode").kendoComboBox({
            dataSource: {
                transport: {
                    read: basePath + "/api/cclean/downloadFactoryWS.do?deviceId=KE201601"
                },
                schema: {
                    data: "result"
                }
            },
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            change: function (e) {
                $('#factory').val(this.text());
            }
        }).data("kendoComboBox").input[0].name = "";
        $("#locationCode").kendoComboBox({
            dataSource: {
                transport: {
                    read: basePath + "/api/cclean/downloadAddressWS.do?deviceId=KE201601"
                },
                schema: {
                    data: "result"
                }
            },
            dataTextField: "name",
            dataValueField: "no",
            height: 400,
            change: function (e) {
                $('#location').val(this.text());
             }
        }).data("kendoComboBox").input[0].name = "";
        $("#deviceForm").kendoValidator({
            messages: {
                required: "必须填写"
            }
        });
    }
    function submitForm() {
        var options = {
            type: "POST",//请求方式：get或post
            dataType: "json",//数据返回类型：xml、json、script
            url: basePath + '/cclean/updateLinenDevice.do',
            beforeSerialize: function () {
                var validator = $("#deviceForm").data("kendoValidator");
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
                    $("#device_dialog").data("kendoDialog").close();
                } else {
                    $("#notification").data('kendoNotification').showText('操作失败！' + json.msg, 'error');
                }
            },
            error: function (err) {
                closeProgress();
                $("#notification").data('kendoNotification').showText('操作失败！', 'error');
            }
        };
        $("#deviceForm").ajaxSubmit(options);
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
<div id="device_dialog">


    <form id="deviceForm">
        <input id="location" name="location" hidden>
        <input id="warehouse" name="warehouse" hidden>
        <input id="factory" name="factory" hidden>
        <table>
            <tr>
                <tb>
                    <div class="form-group">
                        <label for="deviceId">设备号</label>
                        <input type="text" class="form-control" id="deviceId" name="deviceId" required>
                    </div>

                </tb>
            </tr>
            <div class="form-group">
                <label for="warehouseCode">仓储</label>
                <input type="text" class="form-control" id="warehouseCode" name="warehouseCode" required>
            </div>
            <div class="form-group">
                <label for="factoryCode">工厂</label>
                <input type="text" class="form-control" id="factoryCode" name="factoryCode" required>
            </div>
            <div class="form-group">
                <label for="locationCode">所在地</label>
                <input type="text" class="form-control" id="locationCode" name="locationCode" required>
            </div>
            <div class="form-group">
                <label for="remark">备注</label>
                <textarea class="form-control" rows="3" id="remark" name="remark"></textarea>
            </div>
        </table>
    </form>
</div>

<div id="progressDialog"></div>
<span id="notification"></span>