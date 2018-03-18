var searchUrl=basePath+"/smart/newProduct/page.do";
$(function() {
   initGrid();

})
function initGrid(){
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
            {
                name: "", label: "操作", width: 50,editable:false,align:"center",
                formatter: function (cellvalue, options, rowObject) {
                    var styleId=rowObject.styleId;
                    return "<a href='"+basePath+"/smart/newProduct/detail.do?styleId="+styleId+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name:'url',label:'图片',editable:true,width:120,
                formatter:function(cellValue, options, rowObject){
                    if(cellValue!=null){
                        var images=cellValue;
                        var n=images.split(',');
                        var  html="<img width=80px height=100px src='"+basePath+"/mirror/newProduct/"+n[0]+"' />";
                        return html;
                    }else{
                        return "暂无图片";
                    }
                }
            },
            {name: 'styleId', label: '款号',editable:true, width: 100},
            {name: 'name', label: '款名', editable:true,width: 250},
            {name: 'brandCode', label: '品牌', editable:true,width: 150},
            {name: 'colorIds', label: '颜色', editable:true,width: 100},
            {name: 'sizeIds', label: '尺码',editable:true, width: 120},
            {name: 'price', label: '价格',editable:true, width: 150},
            {name: 'seqNo', label: '序号',editable:true, width: 100},
            {name: 'isDet', label: '是否推荐',editable:true, width: 100},
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 10,
        rowList: [10, 20, 50],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'styleId',
        sortorder : "asc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function add(){
    window.location.href=basePath+'/views/smart/newProduct_edit.jsp';
}

function edit(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/smart/newProduct/edit.do?styleId="+row.styleId;
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}

function setDet(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        $.ajax({
            type:"POST",
            url:basePath+"/smart/newProduct/setDet.do",
            data: {"styleId":row.styleId},
            dataType:"json",
            success:function(result){
                $("#grid").trigger("reloadGrid");//刷新grid
            }
        });
    } else {
        bootbox.alert("请选择一项进行修改！");
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

function _clearSearch() {
    $("#searchForm").resetForm();
}
function preview(){

}