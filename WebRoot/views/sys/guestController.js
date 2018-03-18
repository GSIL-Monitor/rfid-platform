$(function () {
    iniGrid();

});

function refresh() {
    location.reload(true);
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url: basePath + "/sys/guest/page.do",
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function showSearchPannel() {
    $("#search-pannel").slideToggle("fast");
}

function addGuest() {
    location.href = basePath + "/sys/guest/add.do";
}

function editGuest(selrow) {
    // var selrow =$("#grid").jqGrid("getGridParam","selrow");
    // if(selrow){
    var row = $("#grid").jqGrid("getRowData", selrow);
    console.log("unitType="+row.unitType);
    location.href = basePath + "/sys/guest/edit.do?id=" + row.id+"&unitType="+row.unitType;
    // } else {
    //     bootbox.alert("请先选择一项");
    // }
}

function changeStatus(rowId) {
    var row = $("#grid").jqGrid('getRowData', rowId);
    var status = row.status;
    cs.showProgressBar("状态更改中");
    $.ajax({
        url: basePath + '/sys/guest/changeStatus.do',
        dataType: 'json',
        data: {
            id: row.id,
            status: status
        },
        success: function (result) {
            cs.closeProgressBar();
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });

}

function setDefaultCustomer(rowId){
    var row = $("#grid").jqGrid('getRowData', rowId);
    cs.showProgressBar("设置中");
    $.ajax({
        url: basePath + '/sys/guest/setDefaultGuest.do',
        dataType: 'json',
        data: {
            id: row.id,
            userId: userId
        },
        success: function (result) {
            cs.closeProgressBar();
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });

}
function iniGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/sys/guest/page.do",
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: '客户编号', width: 100, editable: true},
            {name: 'name', label: '姓名', editable: true, width: 100},
            {
                name: 'sex', label: '性别', editable: true, width: 50,
                formatter: function (cellValue, options, rowObject) {
                    if ("1" == cellValue)
                        return "<span class='ace-icon fa fa-male red' title='男'></span>";
                    else
                        return "<span class='ace-icon fa fa-female green' title='女'></span>";
                }
            },
            {
                name: '', label: '操作', editable: true, width: 150,align:"center",
                formatter: function (cellValue, option, rowObject) {
                    var html="<a style='margin-left: 20px;' href='" + basePath + "/sys/guest/edit.do?id=" + rowObject.id + "&unitType="+rowObject.unitType+"'>"
                             +    "<i class ='fa  fa-edit' aria-hidden='true' title='编辑'></i>"
                             +" </a>";
                    html +="<a style='margin-left: 20px' href='#' onclick=setDefaultCustomer('"+rowObject.id+"')><i class='ace-icon fa fa-cog' title='设置默认客户'></i></a>";
                        if(rowObject.status == 1){
                            html += "<a style='margin-left: 20px' href='#' onclick=changeStatus('"+rowObject.id+"')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                        } else{
                            html += "</i></a>";
                        }

                    return html;
                }
            },
            {name: 'status', label: '状态', width: 40, hidden: true},
            {name:'statusImg',label:'状态',editable:true,width:100,align:"center",
                formatter:function (cellValue, options, rowObject) {
                    switch(rowObject.status){
                        case 0:
                            return "<i class='ace-icon fa fa-lock blue' title='废除'></i>";
                        case 1:
                            return "<i class='ace-icon fa fa-check blue' title='启用'></i>";
                        default:
                            return '';
                    }
                }
            },
            {
                name: '', label: '客户类型', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    if ("CT-AT" == rowObject.unitType)
                        return "省代客户";
                    else if ("CT-ST" == rowObject.unitType)
                        return "门店客户";
                    else if("CT-LS"==rowObject.unitType)
                        return "零售客户";
                }
            },
            {name:'unitType',hidden:true}
            ,
            {
                name: 'tel', label: '电话', editable: true, width: 150
            }
            ,
            {
                name: 'unitName', label: '所属方', editable: true, width: 120
            }
            ,
           /* {
                name: 'bankCode', label: '银行账户', editable: true, width: 200
            }
            ,
            {
                name: 'bankAccount', label: '银行账号', editable: true, width: 200
            }
            ,
            {
                name: 'depositBank', label: '开户行', editable: true, width: 100
            }
            ,*/
            {
                name: 'birth', label: '生日', editable: true, width: 100
            }
            ,
            {
                name: 'storedValue', label: '储值金额', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == undefined) {
                        return "";
                    }
                    return "¥" + cellValue;
                }
            }
            ,
            {
                name: "storeDate", label: '储值日期', editable: true, width: 120
            }
            ,
            {
                name: 'discount', label: '折扣', editable: true, width: 80
            }
            ,
            {name:'linkman',label:'联系人',editable:true,width:100},
            {
                name: 'phone', label: '手机', editable: true, width: 120
            }
            ,
           /* {
                name: 'fax', label: '传真', editable: true, width: 100
            }
            ,
            {
                name: 'address', label: '地址', editable: true, width: 200
            }
            ,
            {
                name: 'province', label: '省份', editable: true, width: 100
            }
            ,
            {
                name: 'city', label: '城市', editable: true, width: 100
            }
            ,
            {
                name: 'areaId', label: '区县', editable: true, width: 100
            }
            ,
            {
                name: 'setUpdaterName', label: '建立人', editable: true, width: 100
            }
            ,*/
            {
                name: 'createTime', label: '建立时间', editable: true, width: 120
            }
            ,
            {
                name: 'remark', label: '备注', editable: true, width: 200
            }
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#gridPager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'createTime',
        sortorder: "desc",
        autoScroll: false
    });


}