$(function () {
    initGrid();
    initForm();
    initProgressDialog();
    initNotification();
});

function initForm() {
    initType();
    var dataSource = new kendo.data.DataSource({
        type: "jsonp",
        transport: {
            read: basePath + "/unit/list.do?filter_INI_type=4,9"
        }
    });
    $("#search_storageId").kendoComboBox({
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
    $("#search_type").kendoComboBox({
        dataTextField: "name",
        dataValueField: "value",
        height: 400,
        suggest: true,
        dataSource: dataSource
    }).data("kendoComboBox").input[0].name = "";
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
/*
        url: basePath + "/syn/third/findBill.do",
*/
        datatype: "json",
//        sortable: true,
        sortorder: 'desc',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 200,frozen:true},
            {
                name: "", label: "操作", width: 100, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    return "<a href='" + basePath + "/syn/third/bill/detail/index.do?type="+rowObject.type+"&billId=" + id + "&billNo="+rowObject.billNo+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            
            {name: 'billDate', label: '单据日期', sortable: true, width: 200},
            {name: 'billType', label: '单据类型', sortable: true, width: 150},
            {name: 'origId', label: '发货方编号', sortable: false, width: 150},
            {name: 'origName', label: '发货方', sortable: false, width: 150},
            {name: 'destId', label: '收货方编号', sortable: false, width: 150},
            {name: 'destName', label: '收货方', sortable: false, width: 150},
            {name: 'totQty', label: '单据数量', sortable: false, width: 100},
            {name: 'logistical', label: '物流', sortable: false, width: 100},
            {name: 'trackNo', label: '运单号', sortable: true, width: 200},
            {name: 'type', hidden:true},
            {name: 'id', hidden:true}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
      /*  rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",*/
        multiselect: false,
        shrinkToFit: false,
        sortname: 'billNo',
        sortorder: "asc",
        autoScroll:false
    });
    $("#grid").jqGrid("setFrozenColumns");
}

function _search() {
  /*  var serializeArray = $("#searchForm").serializeArray();
     var params = array2obj(serializeArray);
     $("#grid").jqGrid('setGridParam', {
     page: 1,
     postData: params
     });
     $("#grid").trigger("reloadGrid");*/
    var options = {
        type:"POST",//请求方式：get或post
        dataType:"json",//数据返回类型：xml、json、script
        url: basePath+'/syn/third/bill/findBill.do',
        beforeSerialize:function(){
            //alert("表单数据序列化前执行的操作！");
            //$("#txt2").val("java");//如：改变元素的值
        },
        //data:{'txt':"JQuery"},//自定义提交的数据
        beforeSubmit:function(){
            var validator = $("#searchForm").kendoValidator().data("kendoValidator");
            if(validator.validate()){
                openProgress();
                return true;
            }
            return false;
        },
        success:function(json){//表单提交成功回调函数
            if(json.success){
                $("#notification").data('kendoNotification').showText('成功！', 'success');
               var thegrid = jQuery("#grid")[0];
               thegrid.addJSONData(json.result);
            }else{
                $("#notification").data('kendoNotification').showText('操作失败！'+json.msg, 'error');
            }
            closeProgress();
        },
        error:function(err){
            closeProgress();
            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
        }
    };
    $("#searchForm").ajaxSubmit(options);
}


function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");
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