$(function () {
    initKendoUI();
    initSelect();
});
function initSelect(){
    $("#filter_IN_floorAreaParentCode").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
            }
        }
    })
}
function refresh(){
    resetData();
}
function resetData(){
    var gridData = $("#treelist").data("kendoTreeList");
    gridData.dataSource.filter({});
}
function search() {
    var gridData = $("#treelist").data("kendoTreeList");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initKendoUI(){
    var dataSource = new kendo.data.TreeListDataSource({
        transport: {
            read: {
                url: basePath + "/wms/pl/warehouseBind/list.do",
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
                expanded: true,
                fields: {
                    id: { type: "string",field: "id"},
                    name:{ field: "name"},
                    parentId: { field: "parentId" },
                    barcode:{field:"barcode"},
                    remark:{field:"remark"}
                }
            },
            data:function(e){
                return e;
            }
        }
    });


    $("#grid").kendoGrid({
        height: 550,
        detailTemplate: kendo.template($("#template").html()),
        detailInit: detailInit,
        columns: [
            { field: "floorBarcode", title: "条码",width:"130px" },
            { field: "id", title: "Id", width: "130px",hidden:true },
            { field: "floorName", title: "库位",width:"130px" ,hidden:true },
            { field: "floorAreaName", title: "区",width:"130px" ,hidden:true },
            { field: "styleId", title: "款号", width: "130px" },
            { field: "styleName", title: "款式", width: "130px" },
            { field: "colorId", title: "色码",width:"130px" },
            { field: "colorName", title: "颜色",width:"130px" },
        ],
        selectable: "row"
    });

    $("#treelist").kendoTreeList({
        height: 550,
        dataSource: dataSource,
        columns: [
            { field: "id", title: "Id",width:"130px"},
            { field: "image", title: "图片",width:"130px",
                template:function(data) {
                    if(data.image){
                        return "<img src='"+basePath+data.image+"'style='height:60px' />"
                    }
                }
            },
            { field: "name", title: "名称",width:"130px"},
            { field: "barcode",title: "条码",width:"130px" },
            { field: "remark",title: "备注",width:"130px" },
        ],
        selectable: "row",
        //TreeList的选中事件
        change: function() {
            //获取选中项的值
            var treeList = $("#treelist").data("kendoTreeList");
            var data = treeList.dataItem(treeList.select());
            var progressDialog = bootbox.dialog({
                message: '<p><i class="fa fa-spin fa-spinner"></i> 数据查询中...</p>'
            });


            $.ajax({
                type: "POST",
                url:basePath+"/wms/pl/warehouseBind/detail.do?id="+data.id+"&type="+data.type,
                dataType: "json",
                success: function (data) {
                    var dataSource=new kendo.data.DataSource({
                        data: data.result,
                        schema: {
                            model: {
                                fields: {
                                    colorId: { type: "string" },
                                    colorName:{type:"string"},
                                    styleId: { type: "string" },
                                    styleName: { type: "string" },
                                    id: { type: "string" },
                                    floorAreaName: { type: "string" },
                                    floorName: { type: "string" },
                                    rackName: { type: "string" },
                                    rackBarcode: { type: "string" }
                                }
                            }
                        }
                    })

                    var grid = $("#grid").data("kendoGrid");
                    grid.setDataSource(dataSource);

                    progressDialog.modal('hide');
                }
            })

        }
    });

}

function  detailInit(e){
    var detailRow = e.detailRow;
    detailRow.find(".tabstrip").kendoTabStrip({
        animation: {
            open: { effects: "fadeIn" }
        }
    });


    $.ajax({
        url: basePath+"/third/playlounge/analysis/plFitting_detail.do?filter_EQS_styleId="+ e.data.styleId+"&filter_EQS_colorId="+ e.data.colorId,
        type: "post",
        success: function(data) {
            if(data.result.styleName!=null){
                var html="";
                html+='<div class="row">';
                html+='<div class="col-lg-12 " id="productShow">';

                html+='<div class="col-lg-2 col-xs-2">';
                html+='<img src="#= image #"  onclick="imgShow(this)" width="100%"/>';
                html+='</div>';

                html+='<div class="col-lg-4 col-xs-4">';
                html+='<div class="profile-user-info profile-user-info-striped">';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 款号 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= styleId #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 款式 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= styleName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 尺码 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= sizeName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 颜色 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= colorName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 品牌 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class1 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 年份 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class2 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 大类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class3 #</span>';
                html+='</div>';
                html+='</div>';
                html+='</div>';

                html+='</div>';
                html+='<div class="col-lg-4 col-xs-4">';
                html+='<div class="profile-user-info profile-user-info-striped">';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 小类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class4 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 性别 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class5 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 厂商 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class6 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 库位码 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class7 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 材质 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class8 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 库类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class9 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 四季分类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class10 #</span>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                var template = kendo.template(html);
                var result=template(data.result);
                $("."+data.result.styleId).html(result);
            }
        }
    })
}
function bind(){
    window.location.href=basePath+"/views/wms/pl/plWmsWarehouseBindingRelation_edit.jsp";
}
function unbind(){
    var grid = $("#grid").data("kendoGrid");
    var data = grid.dataItem(grid.select());
    if(data){
        console.log(data.id);
        $.ajax({
            url: basePath+"/wms/pl/warehouseBind/unbind.do?id="+data.id,
            type: "post",
            dataType: "json",
            success: function(result) {
                if(result.success == true || result.success == 'true') {
                    grid.refresh();
                    //获取选中项的值
                    var treeList = $("#treelist").data("kendoTreeList");
                    var data = treeList.dataItem(treeList.select());
                    var progressDialog = bootbox.dialog({
                        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据查询中...</p>'
                    });


                    $.ajax({
                        type: "POST",
                        url:basePath+"/wms/pl/warehouseBind/detail.do?id="+data.id+"&type="+data.type,
                        dataType: "json",
                        success: function (data) {
                            var dataSource=new kendo.data.DataSource({
                                data: data.result,
                                schema: {
                                    model: {
                                        fields: {
                                            colorId: { type: "string" },
                                            colorName:{type:"string"},
                                            styleId: { type: "string" },
                                            styleName: { type: "string" },
                                            id: { type: "string" },
                                            floorAreaName: { type: "string" },
                                            floorName: { type: "string" },
                                            rackName: { type: "string" },
                                            rackBarcode: { type: "string" }
                                        }
                                    }
                                }
                            })

                            var grid = $("#grid").data("kendoGrid");
                            grid.setDataSource(dataSource);
                            progressDialog.modal('hide');
                            bootbox.alert("解绑成功");
                        }
                    })

                }else{
                    bootbox.alert("解绑失败");
                }
            }
        })
    }else{
        bootbox.alert("请选择一项商品信息");
    }
}