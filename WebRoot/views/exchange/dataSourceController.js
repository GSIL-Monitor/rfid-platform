var searchUrl = basePath + "/exchange/datasource/page.do";
$(function () {
    initGrid();
});
var pageType;

function refresh(){
    location.reload(true);
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'name', label: '描述',editable:true, width: 100},
            {name: 'dsIp', label: 'ip地址', editable:true,width: 200},
            {name: 'dsPort', label: '端口号', editable:true,width: 100},
            {name: 'dbName',label:'数据库名称',editable:true,width:100},
            {name: 'dbUser', label: '用户名',editable:true, width: 100},
            {name: 'dbPass', label: '密码',editable:true, width: 200},
            {name: 'type', label: '数据库类型',editable:true, width: 200,
                formatter: function (cellValue, options, rowObject) {
                    switch (cellValue){
                        case "M":
                            return "MYSQL";
                            break;
                        case "O":
                            return "ORACLE";
                            break;
                        case "S":
                            return "SQL_SERVER";
                            break;
                        default:
                            return cellValue;
                            break
                    }
                }},
            {name: 'status', label: '状态',editable:true, width: 200,align:"center",
                formatter: function (cellValue, options, rowObject) {
                      if (cellValue=="Y"){
                          return '<i class="fa fa-check green" title="正确"></i>';
                      }else{
                          return '<i class="fa fa-times red" title="错误"></i>';
                      }
                }
            }
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'id',
        sortorder : "desc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });

}



function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}



function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url:searchUrl,
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {
    $("#searchForm").resetForm();
}

function add() {
    window.location.href=basePath+"/views/exchange/dataSource_edit.jsp";
}


function reload(){
    location.reload(true);
}
function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/exchange/datasource/showEdit.do?id="+row.id;
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}