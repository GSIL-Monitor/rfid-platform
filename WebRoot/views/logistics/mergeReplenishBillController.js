var searchUrl = basePath + "/logistics/mergeReplenishBillController/page.do?filter_GTI_status=-1";
$(function () {
    initGrid();
    initForm();
    /* initProgressDialog();
     initNotification();*/


});

function initForm() {
    initSelectBusinessIdForm();
}

function initSelectBusinessIdForm() {
    var url;
    if (curOwnerId == "1") {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4";
    } else {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + curOwnerId;
    }
    $.ajax({
        url: url,
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_busnissId").empty();
            $("#search_busnissId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url:searchUrl,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},
            {
                name: "", label: "操作", width: 55, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {

                    var billNo = rowObject.billNo;
                    var html="";
                    /*  html = "<a href='" + basePath + "/logistics/saleOrder/copyAdd.do?billNo=" + billNo + "'><i class='ace-icon fa fa-files-o' title='复制新增'></i></a>";*/
                    html += "<a style='margin-left: 20px' href='" + basePath + "/logistics/mergeReplenishBillController/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    if( rowObject.status==0){
                        html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                    }
                    /*  html += "<a style='margin-left: 20px' href='#' onclick=check('" + billNo + "')><i class='ace-icon fa fa-check-square-o' title='审核'></i></a>";*/

                    /* if(curOwnerId == "1"){
                     html += "<a style='margin-left: 20px' href='#' onclick=quit('" + billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";
                     }*/

                    /*html += "<a style='margin-left: 20px' href='#' onclick=doPrint('" + billNo + "')><i class='ace-icon fa fa-print' title='打印'></i></a>";*/
                    return html;
                }
            },
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 1:
                            html = "<i class='fa fa-check-square-o blue' title='审核'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3:
                            html = "<i class='fa fa-wrench blue' title='操作中'></i>";
                            break;
                        case 4:
                            html = "<i class='fa fa-paper-plane blue' title='申请撤销'></i>";
                            break;
                        case 6:
                            html = "<i class='fa fa-bars blue' title='商城订单暂存'></i>";
                            break;
                        case 7:
                            html = "<i class='fa fa-caret-square-o-down blue' title='商城订单录入'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {name: 'status', hidden: true},
            {name: 'billDate', label: '单据日期', sortable: true, width: 35},
            {name: 'totQty', label: '单据数量', width: 30}


        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}
function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");
}

function refresh() {
    location.reload(true);
}

function cancel(billNo) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/mergeReplenishBillController/cancelbill.do",
        data: {billNo: billNo},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function merge() {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/relenishBill/mergeReplenishBill.do",
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
