$(function () {
    initModel();
    initGrid();
    initForm();
    initDialog();
    $('#description').cronGen();
    initNotification();
});

function loadDetail(id) {
   var rowData=$("#grid").jqGrid('getRowData',id);
    $('#configModel').modal('show');
    $('#detailForm').loadData(rowData);
}
function  initModel() {
    $('#configModel').modal({
        backdrop: 'static',
        keyboard: false,
        show:false
    });
    $('#configModel').on('hidden.bs.modal',function (e) {
        $('#detailForm').clearForm();
        $('#detailForm').resetForm();
     });
}
function runJobs(){
    openProgress();
    $.ajax({
        type: "GET",
        url: basePath+"/timer/runJobs.do",
        dataType: "json",
        success: function(data) {
            closeProgress();
            if(data.success){
                $("#grid").jqGrid().trigger('reloadGrid');
                $("#notification").data('kendoNotification').showText('开启成功！', 'success');
            }else{
                $("#notification").data('kendoNotification').showText('操作失败！'+json.msg, 'error');
            }
        },
        error:function (e) {
            closeProgress();
            $("#notification").data('kendoNotification').showText('操作失败！'+json.msg, 'error');
        }
    });
}
function freshJobs(){
    $("#grid").jqGrid().trigger('reloadGrid');
}
function shutdownJobs() {
    openProgress();
    $.ajax({
        type: "get",
        url: basePath+"/timer/shutdownJobs.do",
        dataType: "json",
        success: function(data) {
            closeProgress();
            if(data.success){
                $("#notification").data('kendoNotification').showText(data.msg, 'success');
                $("#grid").jqGrid().trigger('reloadGrid');
            }else{
                $("#notification").data('kendoNotification').showText('操作失败！'+data.msg, 'error');
            }
        },
        error:function (e) {
            closeProgress();
            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
        }
    });
}
function submitForm(){
    var options = {
        type:"POST",//请求方式：get或post
        dataType:"json",//数据返回类型：xml、json、script
        url: basePath+'/syn/basicConfiguration/update.do',
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
                $('#configModel').modal('hide');
                $("#grid").jqGrid().trigger('reloadGrid');
                $("#notification").data('kendoNotification').showText('成功！', 'success');
            }else{
                $("#notification").data('kendoNotification').showText('操作失败！'+json.msg, 'error');
            }
        },
        error:function(err){
            closeProgress();
            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
        }
    };
    $("#detailForm").ajaxSubmit(options);
}
function initForm() {
    var options = {
        type:"POST",//请求方式：get或post
        dataType:"json",//数据返回类型：xml、json、script
        url: basePath+'/basicConfiguration/update.do',
        beforeSerialize:function(){
            openProgress();
        },
        //data:{'txt':"JQuery"},//自定义提交的数据
        beforeSubmit:function(){
            //alert("表单提交前执行的操作！");
            //if($("#txt1").val()==""){return false;}//如：验证表单数据是否为空
        },
        success:function(json){//表单提交成功回调函数
            alert(data.msg);
            $("#grid").jqGrid().trigger('reloadGrid');
        },
        error:function(err){
            alert("表单提交异常！"+err.msg);
        }
    };
    $("#detailForm").ajaxForm(options);
}
function switchOn(id){
    var rowData=$("#grid").jqGrid('getRowData',id);
    var jobUrl=basePath;
      if(rowData.configState==0){
        jobUrl+="/timer/runSingleJob.do";
    }else{
        jobUrl+="/timer/shutdownSingleJob.do";
    }
    openProgress();
    $.ajax({
        type: "POST",
        url: jobUrl,
        data: {job:rowData.id},
        dataType: "json",
        success: function(data) {
            closeProgress();
            if(data.success) {
                $("#notification").data('kendoNotification').showText('成功！', 'success');
            }else{
                openWarmDialog('操作失败！'+data.msg);
                $("#notification").data('kendoNotification').showText('操作失败！'+data.msg, 'error');
            }
            $("#grid").jqGrid().trigger('reloadGrid');
        },
        error:function (e) {
            closeProgress();
            openWarmDialog("操作失败");
            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
        }
    });
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/syn/basicConfiguration/list.do?filter_EQI_type=1",
        datatype: "json",
         colModel: [
            {name: 'id', label: '任务编号', width: 200},
            {
                name: "", label: "操作", width: 100,
                formatter: function (cellvalue, options, rowObject) {
                    return  "<div class='btn-group btn-group-sm pull-center'>" +
                        "<button type='button' class='btn btn-xs btn-success' onclick=\"loadDetail('"+rowObject.id+"')\" data-toggle='tooltip'" +
                        "  data-placement='bottom' title='编辑'>"
                    + "<span class='glyphicon  glyphicon-edit' aria-hidden='true'></span>"
                    + " </button></div>";
                }
            },{name: 'configStateShow', label: '状态', width: 200,sortable:false,
                formatter: function (cellvalue, options, rowObject) {
                return  "<div class='btn-group btn-group-sm pull-left'> <span class='col-sm-10'>"+
                    "<input id='configState' onchange=\"switchOn('"+rowObject.id+"');\" value='"+ cellvalue+
                    "' type='checkbox' " +(rowObject.configState==1?"checked":"")+
                    " class='ace ace-switch ace-switch-5'/>"+
                    "<span class='lbl middle'></span></span></div>";
            }},
            {name: 'configState', label: 'configState', width: 200,hidden:true},
             {name: 'type', label: 'type', width: 200,hidden:true},
             
            {name: 'name', label: '名称', width: 200},
             {name: 'enable', label: 'enable', width: 200,hidden:true},
             {name: 'enableShow', label: '是否有效', width: 150,sortable:false,
                formatter: function (cellvalue, options, rowObject) {
                   return  "<div class='btn-group btn-group-sm pull-left'> <span class='col-sm-10'>"+
                    "<input id='configState' disabled "+
                    "type='checkbox' " +(rowObject.enable==true?"checked":"")+
                        " class='ace ace-switch ace-switch-5'/>"+
                     "<span class='lbl middle'></span></span></div>";
                }},
            {name: 'api', label: 'API', width: 400},
            {name: 'description', label: 'cron表达式', width: 300},
            {name: 'updateDate', label: '更新日期', width: 200},
            {name: 'remark', label: '备注', width: 200}

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
        autoScore:false,
        jsonReader:{
            id: "id",//设置返回参数中，表格ID的名字为blackId
            repeatitems : false
        },
        loadComplete: function (data) {
            //var records = data.total;
            //var total = Math.ceil(records / data.pageSize);
            //data.records = records;
            //data.total = total;
            //jQuery("#grid")[0].addJSONData(eval(data));
            //
            var table = this;
            setTimeout(function () {

                updatePagerIcons(table);
            }, 0);
        }

    });
    $(window).triggerHandler('resize.jqGrid');
    $("#grid").jqGrid("setFrozenColumns");
}


function initDialog() {
    $("#dialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "警告",
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
function openWarmDialog(content) {
    $("#dialog").data('kendoDialog').content("<center><h3>"+content+"</h3></center>").open();
}
function closeWarmDialog() {
    $("#dialog").data('kendoDialog').content("<center><h3>"+content+"</h3></center>").close();
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