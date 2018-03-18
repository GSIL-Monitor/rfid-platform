$(function () {
    initKendoUI();
    initShopIdSelect();
 //   initFormValid();
})

function initFormValid() {
    $('#editForm').bootstrapValidator({
        message: '输入值无效',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            styleId: {
                validators: {
                    notEmpty: {
                        message: '款式不能为空'
                    },
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                        url: basePath + "/wms/pl/binding/checkStyle.do?rackId="+$("#form_rackId").val(),//验证地址
                        message: '款式已在其他货架绑定',//提示消息
                        delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        data: function (validator) {
                            return {
                            };
                        }

                    }
                }
            },
            colorId: {
                validators: {
                    notEmpty: {
                        message: '颜色不能为空'
                    }
                }
            }
        }
    });
}

function initShopIdSelect(){
    //初始化仓店下拉框
    $.ajax({
        type: "POST",
        url: basePath +"/sys/warehouse/list.do?filter_INI_type=4,9",
        dataType: "json",
        success: function (result) {
            var json = result;
            for (var i = 0; i < json.length; i++) {
                $("#form_shopId").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                $("#form_shopId").trigger('chosen:updated');
            }
        }
    });
}

function selectShopId(){
    //清空下拉框
    var obj=document.getElementById("form_floorAreaId");
    obj.options.length=0;

    var form_shopId=document.getElementById("form_shopId");
    var shopId=form_shopId.options[form_shopId.selectedIndex].value;

    initFaSelect(shopId);
}
function initFaSelect(shopId) {
//根据 选择的仓店 初始化区下拉框
    $.ajax({
        type: "POST",
        url: basePath + "/wms/pl/findFaByShopId.do?shopId=" + shopId,
        dataType: "json",
        success: function (result) {
            var json = result.result;
            for (var i = 0; i < json.length; i++) {
                $("#form_floorAreaId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                $("#form_floorAreaId").trigger('chosen:updated');
            }
        }
    });
}
function selectFloorAreaId(){
    var obj=document.getElementById("form_floorId");
    obj.options.length=0;

    var form_floorAreaId=document.getElementById("form_floorAreaId");
    var floorAreaId=form_floorAreaId.options[form_floorAreaId.selectedIndex].value;

    initFlSelect(floorAreaId);
}
function initFlSelect(floorAreaId) {
//根据 选择的区 初始化库位下拉框
    $.ajax({
        type: "POST",
        url: basePath + "/wms/pl/findFlByFaId.do?floorAreaId=" + floorAreaId,
        dataType: "json",
        success: function (result) {
            var json = result.result;
            for (var i = 0; i < json.length; i++) {
                $("#form_floorId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                $("#form_floorId").trigger('chosen:updated');
            }
        }
    });
}

function selectFloorId(){
    var obj=document.getElementById("form_rackId");
    obj.options.length=0;

    var form_floorId=document.getElementById("form_floorId");
    var floorId=form_floorId.options[form_floorId.selectedIndex].value;

    initRaSelect(floorId);
}
function initRaSelect(floorId) {
//根据 选择的库位 初始化货架下拉框
    $.ajax({
        type: "POST",
        url: basePath + "/wms/pl/findRaByFlId.do?floorId=" + floorId,
        dataType: "json",
        success: function (result) {
            var json = result.result;
            for (var i = 0; i < json.length; i++) {
                $("#form_rackId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                $("#form_rackId").trigger('chosen:updated');
            }
        }
    });
}

function search() {
    var gridData = $("#wms_floor").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}
function initKendoUI(){
    var dataSource = new kendo.data.TreeListDataSource({
        transport: {
            read: {
                url: basePath + "/wms/pl/binding/list.do",
                type: "POST",
                dataType: "json",
                contentType: 'application/json'
            },
            parameterMap: function (options) {
                return JSON.stringify(options);
            }
        },

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
            { field: "rackBarcode", title: "条码",width:"130px" },
            { field: "id", title: "Id", width: "130px",hidden:true },
            { field: "rackName", title: "货架",width:"130px" ,hidden:true },
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
            //设置选中项的值到form
            $.ajax({
                type: "POST",
                url: basePath +"/wms/pl/binding/findViewById.do?id="+data.id+"&type="+data.type,
                dataType: "json",
                success: function (result) {

                }});

            $.ajax({
                type: "POST",
                url:basePath+"/wms/pl/binding/detail.do?id="+data.id+"&type="+data.type,
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
function imgShow(img){
    var bigImg=document.getElementById("bigImg");
    bigImg.src=img.src;
    bigImg.setAttribute("width","400");
    $("#imgModel").modal("show");

}

function unbind(){
    var grid = $("#grid").data("kendoGrid");
    var data = grid.dataItem(grid.select());
    if(data){
      console.log(data.id);
        $.ajax({
            url: basePath+"/wms/pl/binding/unbind.do?id="+data.id,
            type: "post",
            dataType: "json",
            success: function(result) {
                if(result.success == true || result.success == 'true') {
                    grid.refresh();
                    bootbox.alert("解绑成功");
                }
            }
        })
    }else{
     bootbox.alert("请选择一项信息");
    }
}

function del(){
    //获取选中项数据
    var treeList = $("#treelist").data("kendoTreeList");
    var data = treeList.dataItem(treeList.select());

    $.ajax({
        url: basePath+"/wms/pl/binding/unbind.do?id="+data.id+"&type="+data.type,
        type: "post",
        dataType: "json",
        success: function(result) {
            if(result.success == true || result.success == 'true') {
                  bootbox.alert("删除成功");
              }
        }
    });
}

function  bind(){

    var formData= $("#editForm").serialize();
    $.ajax({
        type: "POST",
        data:formData,
        url: basePath + "/wms/pl/binding/bindRack.do",
        dataType: "json",
        success: function (result) {
            if(result.success == true || result.success == 'true') {
                bootbox.alert("保存成功");
            }else{
                bootbox.alert(result.msg);
            }
        }
    })

}

function  initSelect(){
    //初始化颜色下拉框
    var obj=document.getElementById("form_colorId");
    obj.options.length=0;
    $.ajax({
        type: "POST",
        url: basePath + "/prod/product/list.do?filter_EQS_styleId="+$("#form_styleId").val(),
        dataType: "json",
        success: function (result) {
            var json = result;
            for (var i = 0; i < json.length; i++) {
                var backColor = "#ffffff";
                if (json[i].hex != undefined) {
                    backColor = json[i].hex;
                }
                $("#form_colorId").append("<option value='" + json[i].colorId + "' style='background-color: " + backColor + "'>" + json[i].colorName + "</option>");
                $("#form_colorId").trigger('chosen:updated');
            }
        }
    });
}