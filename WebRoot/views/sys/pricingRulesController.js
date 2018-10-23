var searchUrl = basePath + "/sys/pricingRules/page.do";
$(function () {
    initGrid();
    initadd();
    initClass3();
});
/*刷新*/
function refresh() {
    location.reload(true);
}
/*初始化*/
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        datatype: "json",
        mtype: "POST",
        colModel: [

            {name: 'id', label: 'id', hidden: true, width: 20},
            {name: 'name', label: '定价规则名', editable: true, width: 40},
            {name: 'rule1', label: '吊牌价与采购价的关系', editable: true, width: 40},
            {name: 'rule2', label: '吊牌价与门店价的关系', editable: true, width: 40},
            {name: 'rule3', label: '吊牌价与代理价的关系', editable: true, width: 40},
            {name: 'series', hidden:true},
            {name: 'tagPrice',label: '吊牌价', editable: true, width: 40},
            {name: 'priceRule', hidden:true},
            {name: 'priceRuleImg',label: '吊牌价格规则', editable: true, width: 40,
                formatter: function (cellValue, option, rowObject) {
                    var value="";
                    if (rowObject.priceRule == "GT") {
                        value="高于";
                    } else if(rowObject.priceRule == "LT"){
                        value="低于";
                    }
                    return value;
                }
            },
            {name: 'isAllUse', hidden:true},
            {name: 'isAllUseImg', label:'适用所有',width:20,
                formatter: function (cellValue, option, rowObject) {
                    if (rowObject.isAllUse == "Y") {
                        return "是";
                    }else {
                        return "否";
                    }
                }
            },
            {name: 'prefix', label:'款前缀',width:20},
            {name:'suffix',label:'款后缀',width:20},
            {name: 'seriesName', label: '系列', editable: true, width: 40},
            {name: 'updateTime', label: '更新时间', editable: true, width: 40},
            {name: 'class3',label:'大类',hidden:true},
            {name: 'class3Name',label: '大类', editable: true, width: 40},
            {name: 'userId', label: '创建人', editable: true, width: 40},
            {name: 'state', hidden:true},
            {
                name: 'stateImg', label: '操作', editable: true, width: 50, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html;
                    if (rowObject.state == "Y") {
                        html = "<a href='#' onclick=changePricingRulesStatus('" + rowObject.id + "','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    } else {
                        html = "<a href='#' onclick=changePricingRulesStatus('" + rowObject.id + "','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            }
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "desc"
    });
    $("#grid").jqGrid('setFrozenColumns');
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

/*查询*/
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
/*清空*/
function _clearSearch(){
    $("#searchForm").resetForm();
}
/*关闭*/
function closeEditDialog() {
    $("#edit_pricingRules_dialog").modal('hide');
}
/*添加*/
function add() {
    $("#form_series").attr("disabled",false);
    pageType="add";
    $("#editPricingRulesForm").resetForm();
    $("#edit_pricingRules_dialog").modal('show');

}
/*编辑*/
function edit() {
    pageType="edit";
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit_pricingRules_dialog").modal("show");
        $("#editPricingRulesForm").loadData(row);
        $(".selectpicker").selectpicker('refresh');
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
    $("#form_series").attr("disabled","disabled");
}
function changePricingRulesStatus(id, state) {
    $.ajax({
        url: basePath + '/sys/pricingRules/changePricingRulesStatus.do',
        datatype: 'json',
        data: {
            id: id,
            state: state
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
function initadd() {
    $.ajax({
        url:basePath+"/sys/property/searchByType.do?type=C9",
        cache:false,
        async:false,
        inheritClass: true,
        type:"POST",
        success:function (data,textStatus) {
            var json=data;
            for (var i = 0; i < json.length; i++) {
                $("#form_series").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
                $("#form_series").trigger('chosen:updated');
                $("#search_series").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
                $("#search_series").trigger('chosen:updated');
            }
        }
    });
}
function initClass3() {
    $.ajax({
        url:basePath+"/sys/property/listMultiLevel.do?type=C3",
        cache:false,
        async:false,
        inheritClass: true,
        type:"POST",
        success:function (data,textStatus) {
            var json=data;
            for (var i = 0; i < json.length; i++) {
                var subLevelHeader = "";
                var depth = json[i].depth;
                if(depth > 1){
                    while (depth > 1){
                        subLevelHeader += "&nbsp&nbsp&nbsp&nbsp";
                        depth = depth - 1;
                    }
                }
                $("#form_class3").append("<option value='" + json[i].id + "'>" + subLevelHeader+ json[i].name+"</option>");
                $("#form_class3").trigger('chosen:updated');
                $("#search_class3").append("<option value='" + json[i].id + "'>" + subLevelHeader + json[i].name+"</option>");
                $("#search_class3").trigger('chosen:updated');
            }
        }
    });
    $(".selectpicker").selectpicker('refresh');
}

$("#form_class3").change(function () {
    var a = $("#form_class3").find("option:selected").text();
    a = $.trim(a);
    $("#form_class3").find("option:selected").text(a);
});
$("#search_class3").change(function () {
    var a = $("#search_class3").find("option:selected").text();
    a = $.trim(a);
    $("#search_class3").find("option:selected").text(a);
});
$("#form_class3").click(function () {
    initClass3();
});
$("#search_class3").click(function () {
    initClass3();
});
	