$(function () {
    initTree();
    initSearchGridsku();
    initSearchGridcode();
    initSearchGridstyle();
    var parent_column = $("#gridcode").closest('#wid');
    //resize to fit page size
    $("#gridcode").jqGrid( 'setGridWidth', parent_column.width()-52);
    $("#gridstyle").jqGrid( 'setGridWidth', parent_column.width()-52);
});

//初始化树形结构
function initTree() {
    $('#jstree').on("changed.jstree", function (e, data) {
        if (data.selected.length) {
            // 点击节点，显示节点信息
            var nodeId = data.node.id;
            console.info(nodeId);
            $("#form_ownerId").val(nodeId);
            var deep = data.node.original.deep;
            var unitName = data.node.original.text;
            $("#form_unitName").val(unitName);
            console.info(deep);
            $("#form_deep").val(deep)
            if(deep == '0'){
                $(".rack").show();
                $(".level").hide();
                $(".allocation").hide();
            }
            else if(deep == '1'){
                $(".rack").hide();
                $(".level").show();
                $(".allocation").hide();
            }
            else{
                $(".rack").hide();
                $(".level").hide();
                $(".allocation").show();
            }
            $('#gridsku').jqGrid("clearGridData");
            $('#gridsku').jqGrid('GridUnload');
            initSearchGridsku(nodeId);
            $("#gridsku").trigger("reloadGrid");

            $('#gridcode').jqGrid("clearGridData");
            $('#gridcode').jqGrid('GridUnload');
            initSearchGridcode(nodeId);
            $("#gridcode").trigger("reloadGrid");

            $('#gridstyle').jqGrid("clearGridData");
            $('#gridstyle').jqGrid('GridUnload');
             initSearchGridstyle(nodeId);
            $("#gridstyle").trigger("reloadGrid");

        }
    }).jstree({
        "themes": {
            "stripes": true,
        },
        'core': {
            'animation': 0,
            'check_callback': true,
            'data': {
                'url': basePath + "/sys/repositoryController/unitList.do",
                "data": function (node) {
                    if(node.id === "#") {
                        return {
                            "filter_EQI_type": 9,
                            "id": node.id
                        }
                    }
                    else {
                        return {
                            "id": node.id
                        }
                    }
                },
                success: function (res) {
                }
            }
        },
        'types': {
            "default" : {
                "icon": "fa fa-university"
            }
        },
        'plugins': ['search', 'wholerow', 'types']
    })
}
function saveOrganization() {
    var that = this;
    $("#addForm").data('bootstrapValidator').validate();
    if (!$("#addForm").data('bootstrapValidator').isValid()) {
        return;
    }
    //进度条
    cs.showProgressBar();
    $.post(basePath + "/sys/repositoryController/save.do",
        $("#addForm").serialize(),
        function (result) {
            cs.closeProgressBar();
            if (result.success == true || result.success == 'true') {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit-dialog").modal('hide');
                refresh();
            }
        }, 'json');
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast", function () {
        var searchFormHeight = $(".widget-main").height();
    });
}

function refresh() {
    location.reload(true);
}


function _clearSearch() {

}
$("#search_organizationName").keydown(function(e) {
    if (e.keyCode == 13) {
        _search();
    }
});
function _search() {
    var searchResult = $("#jstree").jstree('search', $("#search_organizationName").val());
    $(searchResult).find('.jstree-search').focus();
}

function add() {
    var ownerId = $("#form_ownerId").val();
    var unitName = $("#form_unitName").val();
    if (ownerId && ownerId !== null) {
        $("#addForm").resetForm();
        $("#form_ownerId").val(ownerId);
        $("#form_unitName").val(unitName);
        $("#edit-dialog").modal('show');
    } else {
        bootbox.alert("请先选择上级仓库");
    }
}

function initSearchGridsku(nodeId) {
    console.log("saber"+nodeId);
    $("#gridsku").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbysku.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'sku', label: 'sku',width: 100},
            {name: 'styleId', label: '款号',width: 100},
            {name: 'colorId', label: '颜色',width: 100},
            {name: 'sizeId', label: '尺码',width: 100},
            {name: 'warehouseId', label: '仓库',width: 100},
            {name: 'rackName', label: '货架',width: 100},
            {name: 'areaName', label: '货层',width: 100},
            {name: 'allocationName', label: '货位',width: 97},
            {name: 'floorRack', label: '货架',hidden: true},
            {name: 'floorArea', label: '货层',hidden: true},
            {name: 'floorAllocation', label: '货位',hidden: true},
            {name: 'qty', label: '库存数量',width: 68},
            ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pagersku",
        multiselect: false,
        shrinkToFit: true,
        autoScroll: false,
        footerrow: true,
        sortname: 'billNo',
        sortorder: "desc",
        gridComplete: function () {
            setFooterData("gridsku");
        }

    });
}
function initSearchGridcode(nodeId) {
    $("#gridcode").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbycode.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'code', label: '唯一码',width: 90},
            {name: 'sku', label: 'sku',width: 90},
            {name: 'styleId', label: '款号',width: 90},
            {name: 'colorId', label: '颜色',width: 90},
            {name: 'sizeId', label: '尺码',width: 90},
            {name: 'warehouseId', label: '仓库',width: 90},
            {name: 'rackName', label: '货架',width: 90},
            {name: 'areaName', label: '货层',width: 90},
            {name: 'allocationName', label: '货位',width: 85},
            {name: 'floorRack', label: '货架',hidden: true},
            {name: 'floorArea', label: '货层',hidden: true},
            {name: 'floorAllocation', label: '货位',hidden: true},
            {name: 'qty', label: '库存数量',width: 60},

        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pagercode",
        multiselect: false,
        shrinkToFit: true,
        autoScroll: false,
        footerrow: true,
        sortname: 'billNo',
        sortorder: "desc",
        gridComplete: function () {
            setFooterData("gridcode");
        }
    });
}
function initSearchGridstyle(nodeId) {
    $("#gridstyle").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbystyle.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'styleId', label: '款号',width: 150},
            {name: 'warehouseId', label: '仓库',width: 150},
            {name: 'rackName', label: '货架',width: 150},
            {name: 'areaName', label: '货层',width: 150},
            {name: 'allocationName', label: '货位',width: 147},
            {name: 'floorRack', label: '货架',hidden: true},
            {name: 'floorArea', label: '货层',hidden: true},
            {name: 'floorAllocation', label: '货位',hidden: true},
            {name: 'qty', label: '库存数量',width: 118}


        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pagerstyle",
        multiselect: false,
        shrinkToFit: true,
        autoScroll: false,
        footerrow: true,
        sortname: 'billNo',
        sortorder: "desc",
        gridComplete: function () {
            setFooterData("gridstyle");

        }
    });
}
function setFooterData(id) {

    var sum_totQty = $("#"+id+"").getCol('qty', false, 'sum');
    $("#"+id+"").footerData('set', {
        styleId: "合计",
        qty: sum_totQty
    });
}

	
	