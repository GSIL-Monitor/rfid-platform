$(function () {
    initGrid();
    initForm();
    initDialog();
    initProgressDialog();
    initNotification();

});

function initForm() {
    initType();
    var dataSource = new kendo.data.DataSource({
        type: "jsonp",
        transport: {
            read: basePath + "/unit/list.do"
        }
    });
    $("#search_storageId").kendoComboBox({
        dataTextField: "name",
        dataValueField: "id",
        height: 400,
        suggest: true,
        dataSource: dataSource
    }).data("kendoComboBox").input[0].name = "";
    $("#search_unit2Id").kendoComboBox({
        dataTextField: "name",
        dataValueField: "id",
        height: 400,
        suggest: true,
        dataSource: dataSource
    }).data("kendoComboBox").input[0].name = "";
}
function initType() {

    var dataSource = new kendo.data.DataSource({
        data: [
            {value: "8", name: "仓库入库"},
            {value: "26", name: "仓库退货"},
            {value: "9", name: "仓库盘点"},
            {value: "10", name: "仓库发货"},
            {value: "23", name: "仓库退货入库"},
            {value: "24", name: "仓库调拨出库"},
            {value: "25", name: "仓库调拨入库"},
            {value: "28", name: "仓库调整出库"},
            {value: "29", name: "仓库调整入库"},
            {value: "37", name: "仓库发代理商"},
            {value: "38", name: "仓库入库代理商"},

            {value: "32", name: "仓库其他出库"},
            {value: "14", name: "门店入库"},
            {value: "15", name: "门店销售"},
            {value: "16", name: "门店盘点"},
            {value: "18", name: "门店调拨出库"},
            {value: "19", name: "门店调拨入库"},
            {value: "17", name: "门店销售退货"},
            {value: "27", name: "门店退货出库"},
            {value: "30", name: "门店调整出库"},
            {value: "31", name: "门店调整入库"},
            {value: "33", name: "门店其他出库"},
            {value: "50", name: "调拨销售"}
        ]
    });
    $("#search_token").kendoComboBox({
        dataTextField: "value",
        dataValueField: "id",
        height: 400,
        suggest: true,
        dataSource: {
            type: "jsonp",
            transport: {
                read: basePath + "/sys/sysSetting/find.do?filter_EQS_codeType=TOKEN"
            }
        }
    }).data("kendoComboBox").input[0].name = "";
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/syn/task/page.do",
        datatype: "json",
//       sortable: true,
        colModel: [
            {name: 'id', label: '任务号', sortable: true, width: 200,frozen:true},
            {
                name: "", label: "操作", width: 100, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    var html;
                    html = "<a href='" + basePath + "/syn/task/detail/index.do?id=" + id + "' data-toggle='tooltip'" + "'><i class='ace-icon fa fa-list' title='明细'></i></a>";
                     html+= "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteTask()><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";

                    return html;

                }
            },
            {name: 'status', label: '对接状态', editable: true,align:"center", width: 100,
                formatter: function(value, options, rowObject){
                    html=''
                    if(value==1){
                        html+='<i class="fa fa-check green"></i>';
                    }else{
                        html+='<i class="fa fa-times red"></i>';
                    }
                    return html;
                }
            },
            
            {name: 'billNo', label: '单号', sortable: true, width: 200},
            {name: 'taskType', label: '类型', sortable: false, width: 150},
            {name: 'deviceId', label: '设备号', sortable: true, width: 100},
            {name: 'origName', label: '仓库名称', sortable: false, width: 150},
            {name: 'destName', label: '收货仓库', sortable: false, width: 150},
            {name: 'totEpc', label: '总数量', sortable: false, width: 100},
            {name: 'totStyle', label: '总款数', sortable: false, width: 100},
            {name: 'totSku', label: 'SKU数', sortable: false, width: 100},
            {name: 'beginTime', label: '开始时间', sortable: true, width: 200},
            {name: 'endTime', label: '结束时间', sortable: true, width: 200}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname: 'id',
        sortorder: "asc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

var is_batch = undefined;
function initDialog() {
    $("#alertDialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "错误",
        closable: false,
        modal: true,
        buttonLayout: "normal",
        actions: [
            {
                text: '<span class="glyphicon glyphicon-ok">确定</span>',
                primary: true
            },
            {text: '<span class="glyphicon glyphicon-remove">取消</span>'}
        ]
    }).data("kendoDialog").close();
    $("#dialog").kendoDialog({
        width: "400px",
        height: "280px",
        title: "警告",
        closable: false,
        modal: true,
        content: "<center><h3><div class='alert alert-danger' role='alert'>删除后将无法恢复！是否继续？</div></h3>" +
        "<input type='password' class='form-control' id='password' placeholder='输入密码'>" +
        "</center>",
        buttonLayout: "normal",
        actions: [
            {
                text: '<span class="glyphicon glyphicon-ok">删除</span>',
                primary: true,
                action: function (e) {
                    var rowKey = $("#grid").jqGrid('getGridParam', "selrow");
                    var id = "";
                    var serializeArray = $("#searchForm").serializeArray();
                    var params = undefined;
                    if (is_batch) {
                        params = array2obj(serializeArray);
                    } else {
                        id = rowKey;
                    }
                    openProgress();
                    $.ajax({
                        type: "POST",
                        url: basePath + "/syn/task/delete.do?password="+$('#password').val()+"&id=" + id,
                        dataType: "json",
                        data: params,
                        success: function (data) {
                            $("#grid").trigger("reloadGrid");
                            $("#dialog").data('kendoDialog').close();
                            closeProgress()
                            if (data.success) {
                                $("#notification").data('kendoNotification').showText('操作成功！', 'success');
                            } else {
                                openWarmDialog('操作失败！' + data.msg);

                                $("#notification").data('kendoNotification').showText('操作失败！' + data.msg, 'error');
                            }
                        },
                        error: function (msg) {
                            closeProgress();
                            $("#dialog").data('kendoDialog').close();
                            openWarmDialog('操作失败！');
                            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
                        }
                    });
                    return false;
                },
            },
            {text: '<span class="glyphicon glyphicon-remove">取消</span>'}
        ]
    }).data("kendoDialog").close();

    $("#uploadDialog").kendoDialog({
        width: "450px",
        height: "300px",
        title: "上传数据",
        closable: false,
        modal: true,
        buttonLayout: "normal",
        actions: [
            {
                text: '<span class="glyphicon glyphicon-ok">上传</span>',
                primary: true,
                action: function (e) {
                    submitFile();
                    return false;
                },
            },
            {text: '<span class="glyphicon glyphicon-remove">取消</span>'}
        ],
        close:function (e) {
            $('#taskFileForm').clearForm();
            $('#taskFileForm').resetForm();
            $('#result').hide();

        }
    }).data("kendoDialog").close();
    
    
    $("#files").kendoUpload({
        async: {
            batch: false,
            autoUpload: false
        },
        multiple: false,
        localization: {
            invalidFileExtension: "只能上传.zip,.rar",
            clearSelectedFiles: "清空选中文件"
        },
        validation: {
            allowedExtensions: [".zip", ".rar"],
        },
        success: function (e) {

        }, error: function (e) {
            $("#notification").data('kendoNotification').showText('上传失败！', 'error');
        }
    });

}

function submitFile() {
    var options = {
        type:"POST",//请求方式：get或post
        /*
         dataType:"json",//数据返回类型：xml、json、script
         */
        url: basePath+'/syn/task/uploadTaskFile.do',
        beforeSerialize:function(){
            //alert("表单数据序列化前执行的操作！");
            //$("#txt2").val("java");//如：改变元素的值
        },
        //data:{'txt':"JQuery"},//自定义提交的数据
        beforeSubmit:function(){
        openProgress();
        },
        success:function(json){//表单提交成功回调函数
            closeProgress();
            if(json.success){
                $("#uploadDialog").data("kendoDialog").close();
                $("#notification").data('kendoNotification').showText('上传成功！', 'success');
                $("#grid").trigger("reloadGrid");
            }else{
                $("#notification").data('kendoNotification').showText('上传失败！'+json.msg, 'error');
                $('#result').text("上传失败！"+json.msg).show();
                openWarmDialog("上传失败！"+json.msg);
            }
        },
        error:function(err){
            closeProgress();
            openWarmDialog("上传失败！");
            $("#notification").data('kendoNotification').showText('上传失败！', 'error');
        }
    };
    $("#taskFileForm").ajaxSubmit(options);
}
function deleteTask() {
    is_batch = false;
    $('#dialog').data('kendoDialog').open();
}
function batchDeleteTask() {
    is_batch = true;
    $("#grid").setSelection(0);   //第一行被选中，不存在$("#temptableid").setSelection(0);的写
    $('#dialog').data('kendoDialog').open();
}
function openUploadDialog() {
    $("#uploadDialog").data('kendoDialog').open();
}

function openWarmDialog(content) {
    $("#alertDialog").data('kendoDialog').content("<center><h3>"+content+"</h3></center>").open();
}
function initProgressDialog() {
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
