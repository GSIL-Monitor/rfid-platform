$(function () {
    initGrid();
    initDialog();
});
function refresh() {
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/prod/product/page.do",
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'code', label: 'SKU', editable: true, width: 200, frozen: true},
            {name: 'id', label: 'id', hidden: true, width: 20},
            {name: 'styleId', label: '款号', editable: true, width: 100},
            {name: 'styleName', label: '款名', editable: true, width: 100},
            {name: 'colorId', label: '色码', editable: true, width: 60},
            {name: 'colorName', label: '颜色名', editable: true, width: 60},
            {name: 'sizeId', label: '尺码', editable: true, width: 60},
            {name: 'sizeName', label: '尺码名', editable: true, width: 180},
            {name:"price",label:"吊牌价",editable:true,width:100,hidden:true},
            {name:"",label:"吊牌价",editable:true,width:100,formatter:function (rowValue, option, rowObject) {
                return "¥"+rowObject.price;
            }},
            {
                name: '', label: '操作', editable: true, width: 40, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html;
                    if (rowObject.isUse == "Y") {
                        html = "<a style='margin-left: 0px' href='#' onclick=changeProductStatus('" + rowObject.code + "','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    } else {
                        html = "<a style='margin-left: 0px' href='#' onclick=changeProductStatus('" + rowObject.code + "','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'remark', label: '描述', editable: true, width: 100},
            {name:'oprId',label:'创建人',editable:true,width:100},
            {name:'updateTime',label:'更新时间',editable:true,width:150},
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
        sortname: 'code',
        sortorder: "desc",
        autoScroll: false
    });
    $("#grid").jqGrid("setFrozenColumns")
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    
    $("#grid").jqGrid('setGridParam', {
    	url: basePath + "/prod/product/page.do",
        page: 1,
        postData: params
    }).trigger("reloadGrid");
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}


var is_batch = undefined;
function initDialog() {
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
            invalidFileExtension: "只能上传.xls",
            clearSelectedFiles: "清空选中文件"
        },
        validation: {
            allowedExtensions: [".xls"],
        },
        success: function (e) {

        }, error: function (e) {
            $("#notification").data('kendoNotification').showText('上传失败！', 'error');
        }
    });
    $("#notification").kendoNotification({
        position: {
            top: 50
        },
        stacking: "left"
    }).data("kendoNotification").hide();
}
function openUploadDialog() {
    $("#uploadDialog").data('kendoDialog').open();
}

function submitFile() {
    var options = {
        type:"POST",//请求方式：get或post
		/*
		 dataType:"json",//数据返回类型：xml、json、script
		 */
        url: basePath+'//prod/product/importExcel.do',
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
function openProgress() {
    $("#progressDialog").data('kendoDialog').open();
}
function closeProgress() {
    $("#progressDialog").data('kendoDialog').close();
}

function changeProductStatus(code, status) {
    $.ajax({
        url: basePath + '/prod/product/changeProductStatus.do',
        datatype: 'json',
        data: {
            code: code,
            status: status
        },
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid')
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail gritter-light'
                });
            }
        }

    });
}
function getTemp() {
    window.location.href=basePath+"/prod/product/templet.do";
}
