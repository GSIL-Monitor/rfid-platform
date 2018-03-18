var searchUrl = basePath + "/sys/company/page.do?filter_EQI_type=1";
$(function () {
    initGrid();
    initModuleGrid();
});
function refresh(){
    location.reload(true);
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype:"POST",
        url: searchUrl,
        datatype: "json",
        colModel: [
			{
			    name: "src", label: "来源", width: 80,editable:false,
			    formatter: function (cellValue, options, rowObject) {
			        var html = "";
			        switch(cellValue) {
			            case "01":
			                html = '<span class="label label-sm label-success">系统</span>';
			                break;
			            case "02":
			                html = '<span class="label label-sm label-inverse">同步</span>';
			                break;
			            case "03":
			                html = '<span class="label label-sm label-warning">导入</span>';
			                break;
			            default:
			                html = '<span class="label label-sm label-inverse">系统</span>';
			        }
			        return html;
			    }
			},
            {name: 'code', label: '编号',editable:true, width: 100},
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'name', label: '名称', editable:true,width: 100},
            {name: 'tel', label: '联系电话', editable:true,width: 150},
            {name: 'linkman', label: '联系人', editable:true,width: 100},
            {name: 'email', label: '邮箱', editable:true,width: 150},
            {name: 'provinceId', label: '所在省份', editable:true,width: 100},
            {name: 'cityId', label: '所在城市',editable:true, width: 150},
            {name: 'address', label: '街道地址',sortable:false,editable:true, width: 200},
            {name: 'createTime', label: '创建时间', width: 200},
            {name: 'remark', label: '备注', sortable:false,width: 400}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname : 'createTime',
        sortorder : "desc",
        autoScroll:false

    });
}

function initModuleGrid() {
    $("#moduleGrid").jqGrid({
        treeGrid: true,
        url: basePath + "/sys/company/findAllModule.do",
        datatype: "json",
        autowidth: true,
        height: "auto",
        multiselect:false,
        shrinkToFit: true,
        colModel: [

            {name: 'code', label: '模块编号', width: 80,key:true,
                formatter: function (cellvalue, options, rowObject) {
                    return "    "+cellvalue;
                }
            },
            {name: 'name', label: '模块名称',width: 80},
            {name: 'ownerId', label: '父模块ID',hidden:true},
            {name: 'seqNo', label: '序号',width: 80},
            {name:'status', label:'选择', width: 60, align:'center', formatter: function (cellvalue, options, rowObject) {
                if(cellvalue===1) {
                    return '<input id="ckbox_' + rowObject.code + '" name="' + rowObject.code + '" type="checkbox" checked="checked" />';
                } else {
                    return '<input id="ckbox_' + rowObject.code + '" name="' + rowObject.code + '" type="checkbox" />';
                }
            }
            }

        ],
        treeReader : {
            level_field: "level",
            parent_id_field: "ownerId",
            leaf_field: "leaf",
            expanded_field: "expand"
        },
        treeGridModel: "adjacency",
        ExpandColumn: "code",
        rowNum: -1,
        pager: "false",
        "treeIcons" : {
            "plus": "ace-icon fa fa-chevron-down",
            "minus": "ace-icon fa fa-chevron-up",
            "leaf" : ""
        },
        jsonReader: {
            repeatitems: false
        },
        loadComplete: function(data) {
            //绑定选择事件
            $("input[type='checkbox']").click(function () {
                var resId = $(this).attr("id").split("_")[1];
                if ($(this).prop("checked") == true) {//选中
                    cs.showProgressBar();
                    console.log(($(this).prop("checked")));
                    console.log(($(this).attr("id")));

                    $.post(basePath+"/sys/company/selectModule.do",
                        {resId:resId},
                        function(result) {
                            cs.closeProgressBar();
                            if(result.success == true || result.success == 'true') {
                                $.gritter.add({
                                    text: result.msg,
                                    class_name: 'gritter-success  gritter-light'
                                });
                            } else {
                                cs.showAlertMsgBox(result.msg);
                            }
                        }, 'json');
                } else { //取消
                    $.post(basePath+"/sys/company/cancelModule.do",
                        {resId:resId},
                        function(result) {
                            cs.closeProgressBar();
                            if(result.success == true || result.success == 'true') {
                                $.gritter.add({
                                    text: result.msg,
                                    class_name: 'gritter-success  gritter-light'
                                });
                            } else {
                                cs.showAlertMsgBox(result.msg);
                            }
                        }, 'json');
                }
            });
        }
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

function _clearSearch(){
    $("#searchForm").resetForm();
}