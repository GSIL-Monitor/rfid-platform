
var url=basePath + "/prod/style/page.do";
$(function () {
    initGrid();
    if(roleId == '0'){
        $("#add_style_button").removeAttr("disabled");
    }else{
        $("#add_style_button").attr({"disabled": "disabled"});
    }

});
function refresh(){
    location.reload(true);
}

function initGrid() {
    $("#grid").jqGrid({
        height:  "auto",
        url: url,
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'id', label:'编号',editable:true,width: 100},
            {name: 'styleId', label: '款号',editable:true, width: 100,sortable: true,frozen:true},
            {name: 'styleName', label: '款名', editable:true,width: 100},
            {name: 'price', label: '吊牌价', editable:true,width: 80},
            {name:'brandCode',label:'品牌',editable:true,width:80},
            {name:'sizeSortId',label:'尺寸组',eidtable:true,width:80},
           	{name:'styleEname',label:'英文名',editable:true,width:80},
            {name:'oprId',label:'创建人',editable:true,width:80},
            {
                name: '', label: '操作', editable: true, width: 50, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html;
                    if (rowObject.isUse == "Y") {
                        html = "<a href='#' onclick=changeStyleStatus('" + rowObject.id + "','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    } else {
                        html = "<a href='#' onclick=changeStyleStatus('" + rowObject.id + "','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'isUse', label: '启用状态', hidden: true},
            {name:'updateTime',label:'更新时间',editable:true,width:160},
            {name: 'remark', label: '款描述', width: 120,sortable:false}

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
        sortname : 'id',
        sortorder : "desc",
        autoScroll:false

    });
   

}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function save() {


}

function unSelected() {

}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid("setGridParam",{
    	url: url,
        page : 1,
        postData : params
    }).trigger('reloadGrid');
}

function cleanSearch() {

}

function del() {

}
function locked() {


}
function add() {
    location.href=basePath + "/prod/style/add.do"
}
function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        location.href=basePath + "/prod/style/edit.do?styleId="+row.styleId;
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}
function initUploadSet(){
    $(".table-header").append("上传款信息");
	var file = $('#upload_file_input');	
	$('#upload_file_input').fileinput({
	    language: 'zh',
	    uploadUrl: basePath + "/prod/style/importExcel.do",
	    allowedPreviewTypes : ['image', 'html', 'text', 'video', 'audio', 'flash'],
	    allowedFileExtensions:['xls','xlsx']

	}).on("fileuploaded", function(event, result) {
		if(result.response.success){
			$.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
           });
		}else{
			$.gritter.add({
                text: result.response.msg+"!"+result.response.result,
                class_name: 'gritter-false  gritter-light'
           });
		}
		
    });

	$('#upload_file_input').on('fileuploaderror', function(event, data, previewId, index) {
	    var form = data.form, files = data.files, extra = data.extra,
	            response = data.response, reader = data.reader;
	    console.log(data);
	    console.log('File upload error');
	});

	$('#upload_file_input').on('fileerror', function(event, data) {
	    console.log(data.id);
	    console.log(data.index);
	    console.log(data.file);
	    console.log(data.reader);
	    console.log(data.files);
	});

	$('#upload_file_input').on('fileuploaded', function(event, data, previewId, index) {
	    var form = data.form, files = data.files, extra = data.extra,
	            response = data.response, reader = data.reader;
	    console.log('File uploaded triggered');
	});
	$("#upload_img_info").attr("src",basePath + "/images/uploadSample/style.jpg");
}
function uploadStyle(){

	$("#modal-tableUpload").on("hidden.bs.modal", function() {
		$("#uploadForm").resetForm();
	}).modal("show");
	
}
function changeStyleStatus(rowId, status) {
    $.ajax({
        url: basePath + '/prod/style/changeStyleStatus.do',
        datatype: 'json',
        data: {
            styleId: rowId,
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
	