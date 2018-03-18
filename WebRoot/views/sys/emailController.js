var searchUrl = basePath + "/email/page.do";
$(function () {
    initGrid();

});

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
            {
                name: "", label: "操作", width: 80,editable:false,align:"center",
                formatter: function (cellValue, options, rowObject) {
                    var id=rowObject.id;
                    var html="<a href='"+basePath+"/email/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                    if(rowObject.status!=true){
                        html+="<a style='margin-left:10px' href='javascript:sendMail("+id+")'><i class='ace-icon fa fa-undo'></i></a>"
                    }
                    return html;
                }
            },
            {name: 'status', label: '状态',editable:true, width: 50,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue==true){
                        return "成功";
                    }else{
                        return "失败";
                    }
                }
            },
            {name: 'type', label: '类型',editable:true, sortable:false,width: 100},
            {name: 'recipients', label: '收件人',editable:true, width: 150},
            {name: 'addresser', label: '发件人', editable:true,width: 150},
            {name: 'title', label: '主题', editable:true,width: 250},
            {name: 'sendTime', label: '发送时间',editable:true, width: 150},
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'sendTime',
        sortorder : "desc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}
function sendMail(id){
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 邮件发送中...</p>'
    });
    $.ajax({
        url:basePath+"/email/sendMail.do?id="+id,
        type:'post',
        dataType : 'json',
        success:function(result){
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                _search();
            }else{
                bootbox.alert("保存失败");
            }
        }
    });
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}