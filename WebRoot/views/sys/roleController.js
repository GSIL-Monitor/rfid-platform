$(function () {
    initGrid();
    var type_parent_column = $("#roleGrid").closest('.widget-main');
    var property_parent_column = $("#resourceGrid").closest('.widget-main');
    $("#roleGrid").jqGrid( 'setGridWidth', type_parent_column.width() );
    $("#resourceGrid").jqGrid( 'setGridWidth', property_parent_column.width() );
    $(window).on('resize.jqGrid', function () {
        var type_parent_column = $("#roleGrid").closest('.widget-main');
        var property_parent_column = $("#resourceGrid").closest('.widget-main');
        $("#roleGrid").jqGrid( 'setGridWidth', type_parent_column.width() );
        $("#resourceGrid").jqGrid( 'setGridWidth', property_parent_column.width() );
    });
});

function refresh(){
    location.reload(true);
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initGrid() {
    $("#roleGrid").jqGrid({
        height: 500,
        url: basePath + "/sys/role/page.do",
        datatype: "json",
        mtype:"POST",
        colModel: [

            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'code', label: '角色编号',editable:true, width: 30},
            {name: 'name', label: '角色名称', editable:true,width: 40},
            {name: 'authIds', label: '权限ID', hidden:true,width: 40},
            {name: 'authNames', label: '拥有权限', sortable:false,editable:true,width: 40,hidden:true},
            {name: 'createTime', label: '创建时间',editable:true, width: 40},
            {name: 'remark', label: '备注', width: 40, sortable: false}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'createTime',
        sortorder : "desc",
        onSelectRow: function(id){
            $("input[type='checkbox']").prop('checked',false);
            var row = $("#roleGrid").jqGrid('getRowData',id);
            var authIdStr = row.authIds;
            var authIdArr = authIdStr.split(",");
            $.each(authIdArr,function(id,value) {
                $("#ckbox_" + value).prop("checked", 'true');
            });
        }
    });

    $("#resourceGrid").jqGrid({
        treeGrid: true,
        url: basePath + "/sys/role/findResource.do",
        datatype: "json",
        mtype:"POST",
        autowidth: true,
        viewrecords: true,
        altRows: true,
        height: 500,
        multiselect:false,
        shrinkToFit: true,
        colModel: [

            {name: 'code', label: '资源编号', width: 80,key:true,
                formatter: function (cellvalue, options, rowObject) {
                    return "    "+cellvalue;
                }
            },
            {name: 'name', label: '资源名称',width: 80},
            {name: 'ownerId', label: '父菜单ID',width: 80,
                formatter: function (cellvalue, options, rowObject) {
                    if(rowObject.leaf == false){
                        return "";
                    }else{
                        return rowObject.ownerId;
                    }
                }
            },
            //{name: 'seqNo', label: '序号',width: 80},
            {name:'locked', label:'选择', width: 60, align:'center', formatter: function (cellvalue, options, rowObject) {
                return '<input  id="ckbox_'+rowObject.code+'" name="'+rowObject.code+'" type="checkbox" />';
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
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#resourceGrid-pager",
        "treeIcons" : {
            "plus": "ace-icon fa fa-chevron-down",
            "minus": "ace-icon fa fa-chevron-up",
            "leaf" : ""
        },
        //ExpandColClick: true,
        jsonReader: {
            repeatitems: false
            //  root: "response"
        },
        onSelectRow: function(id){
            console.log(row.code);
            console.log(row.locked);
        },
        loadComplete: function(data) {
            //checkbox不可点击
            $("input[type='checkbox']").click(function () {return false});
        }
    });


}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#roleGrid").jqGrid('setGridParam', {
        page : 1,
        postData : params
    });
    $("#roleGrid").trigger("reloadGrid");
}


function refreshRole() {
    _search();
}

function add() {
    location.href = basePath+"/sys/role/addPage.do";
}
function edit() {
    var rowId = $("#roleGrid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#roleGrid").jqGrid('getRowData',rowId);
        location.href = basePath+"/sys/role/editPage.do?roleId="+row.id;

    } else {
        bootbox.alert("请选择一项进行修改！");
    }

}


