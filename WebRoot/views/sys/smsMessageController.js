var searchUrl = basePath + "/sys/SMSMessage/page.do";
$(function(){
    //初始化
    initGrid();
    //initadd();
});
function refresh(){
    location.reload(true);
}
/*function initadd(){
 $.ajax({
 url : basePath + "/sys/role/list.do",
 cache : false,
 async : false,
 type : "POST",
 success : function (data,textStatus){
 var json= data;
 for(var i=0;i<json.length;i++){
 $("#form_roleId").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
 $("#form_roleId").trigger('chosen:updated');
 }
 }
 })

 }*/
function initGrid(){
    $("#grid").jqGrid({
        height: "auto",
        url:basePath + "/sys/SMSMessage/page.do",
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'customer', label: '客户', width: 200},
            {name: 'iphone', label: '电话',width: 200},
            {name: 'message', label: '发送内容',width: 200},
            {name: 'sendTime', label: '发送时间',width: 200}


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
        sortname : 'sendTime',
        sortorder : "desc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
    function isAdmin(cellvalues,option,rowObject){
        if(cellvalues==1){
            return "是";
        }else{
            return "否";
        }
    }

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

function _clearSearch(){
    $("#searchForm").resetForm();
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}


function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}

