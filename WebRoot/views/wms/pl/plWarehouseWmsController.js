$(function () {
    initKendoUITreeList();
    initMutiSelect();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
})
function initKendoUITreeList(){
    var dataSource = new kendo.data.TreeListDataSource({
        transport: {
            read: {
                url: basePath + "/wms/pl/warehouse/list.do",
                type: "POST",
                dataType: "json",
                contentType: 'application/json'
            },
            parameterMap: function (options) {
                return JSON.stringify(options);
            }
        },
        serverFiltering: true,
        schema: {
            model: {
                id: "id",
                parentId: "parentId",
                fields: {
                    id: { type: "string",field: "id"},
                    name:{ field: "name"},
                    parentId: { field: "parentId" },
                    barcode:{field:"barcode"},
                    remark:{field:"remark"}
                },
                expanded: true
            },
            data:function(e){
                return e;
            }
        }
    });
    $("#wms_floor").kendoTreeList({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: dataSource,
        columns: [
            { field: "id", title: "Id",
                template:function(data) {
                    switch (data.type){
                        case 0:
                            return "[仓库]"+data.id;
                            break;
                        case 1:
                            return "[区]"+data.id;
                            break;
                        case 2:
                            return "[库位]"+data.id;
                            break;
                    }

                }},
            { field: "image",title: "图片" , template:function(data) {
                if(data.image){
                    return "<img src='"+basePath+data.image+"' style='height:60px' />";
                }
            }},
            { field: "barcode",title: "条码" },
            { field: "name", title: "名称"},
            { field: "deviceId",title: "设备号" },
            { field: "remark",title: "备注" },

        ],
        selectable: "row"
    });
}
function initMutiSelect(){
    $("#filter_IN_floorAreaParentCode").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/warehouse/list.do?filter_INI_type=9"
            }
        }
    })
}
function exportExcel(){
    $(".k-grid-excel").click();
}

function refresh(){
    resetData();
}
function resetData(){
    var gridData = $("#wms_floor").data("kendoTreeList");
    gridData.dataSource.filter({});
}

function del(){
    //获取选中项数据
    var treeList = $("#wms_floor").data("kendoTreeList");
    var data = treeList.dataItem(treeList.select());

    $.ajax({
        url: basePath+"/wms/pl/warehouse/del.do?id="+data.id+"&type="+data.type,
        type: "post",
        dataType: "json",
        success: function(result) {
            if(result.success == true || result.success == 'true') {
                bootbox.alert("删除成功");
            }else{
                bootbox.alert("删除失败");
            }
        }
    });
}

function add(){
    var treeList = $("#wms_floor").data("kendoTreeList");
    var data = treeList.dataItem(treeList.select());
    if(data){
        switch (data.type){
            case 0:
                window.location.href=basePath+"/wms/pl/warehouse/addFa.do?parentId="+data.id;
                break;
            case 1:
                window.location.href=basePath+"/wms/pl/warehouse/addFl.do?parentId="+data.id;
                break;
            case 2:
            case 3:
                bootbox.alert("无法添加下一级");
                break;
        }
    }else{
        window.location.href=basePath+"/wms/pl/warehouse/addFa.do?parentId=#";
    }
}


function edit(){
    var treeList = $("#wms_floor").data("kendoTreeList");
    var data = treeList.dataItem(treeList.select());
    if(data){
        switch (data.type){
            case 0:
                bootbox.alert("无法编辑仓店");
                break;
            case 1:
                window.location.href=basePath+"/wms/pl/warehouse/editFa.do?id="+data.id;
                break;
            case 2:
                window.location.href=basePath+"/wms/pl/warehouse/editFl.do?id="+data.id;
                break;
        }
    }else{
        bootbox.alert("请选择一项编辑");
    }

}


function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#wms_floor").data("kendoTreeList");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}
