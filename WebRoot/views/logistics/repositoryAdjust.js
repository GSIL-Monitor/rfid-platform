var searchUrl = basePath + "/logistics/repositoryAdjust/page.do?filter_GTI_status=-1&userId=" + userId;
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
var billNo;
var autoSelect =false;//是否自动选中
var showScanDialog = false;
var isCheckWareHouse=false;//是否检测出库仓库
var allocationId =null;//货位
var levelId = null;//货层
var rackId = null;//货架
var cageId = null;//仓库
var deep = null;//深度
var SallocationId =null;//货位
var SlevelId = null;//货层
var SrackId = null;//货架
var ScageId = null;//仓库
var Sdeep = null;//深度
$(function () {

    /*初始化左侧grig*/
    initSearchGrid();
    /*初始化右侧grig*/
    initAddGrid();
    /*初始化from表单*/
    initSearchAndEditForm();
    /*初始化jstree*/
    $("#destId").empty();
    $("#destId").val("--请选择入库库位--");
    $("#SdestId").empty();
    $("#SdestId").val("--请选择入库库位--");
    //选择用户默认仓库
    $("#edit_origId").selectpicker('val', defaultWarehId);
    $("#search_origId").selectpicker('val', defaultWarehId);
    console.info($('#edit_origId option:selected').text());
    initTree();
    SinitTree();
    if (billNo) {
        bootbox.alert("单据" + billNo + "正在编辑中");
    } else {
        sessionStorage.removeItem("billNosale");
    }
    /*回车监事件*/
    keydown();
    addProduct_keydown();
    input_keydown();
    /*初始化按钮*/
    pageType="add";
    initButtonGroup(pageType);
    loadingButtonDivTable(0);
    /*初始化右侧表单验证*/
    initEditFormValid();
    //动态加载按钮
    loadingButtonDivTable();

});
//初始化搜索树形结构
function SinitTree(id) {
    ScageId = $("#search_origId").val();
    $("#Sjstree").jstree("destroy");
    $('#Sjstree').on("select_node.jstree", function (e, data) {
        //变化事件
        if (data.selected.length) {
            // 点击节点，显示节点信息
            Sdeep = data.node.original.deep;
            console.info(Sdeep);
            var allocationName = "请选择";
            var levelName = "请选择";
            var rackName = "请选择";
            if(Sdeep == "3"){
                SallocationId = data.node.id;
                SlevelId = data.node.parents[0];
                SrackId = data.node.parents[1];
                allocationName = data.node.text;
                levelName = $("#"+SallocationId+"_anchor").parent().parent().prev().text();
                rackName = $("#"+SlevelId+"_anchor").parent().parent().prev().text();
            }
            else if(Sdeep == "2"){
                SlevelId = data.node.id;
                SrackId = data.node.parents[0];
                levelName = data.node.text;
                rackName = $("#"+SlevelId+"_anchor").parent().parent().prev().text();
            }
            else {
                SrackId = data.node.id;
                rackName = data.node.text;
            }
            console.info(ScageId);
            console.info(SrackId);
            console.info(SlevelId);
            console.info(SallocationId);
            $("#SdestId").val(rackName+"-"+levelName+"-"+allocationName);

        }

    })
        .jstree({
            'core': {
                'animation': 0,
                'check_callback': true,
                'data': {
                    'url': basePath + "/sys/repositoryController/unitListById.do",
                    "data": function (node) {
                        return {
                            "id": ScageId
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
        //双击  确定jstree.js中已经添加双击事件
        .bind('dblclick.jstree',function(event){
            if(Sdeep != "3"){
                $.gritter.add({
                    text: "请选择具体货位！",
                    class_name: 'gritter-success  gritter-light'
                });
            }
            else{
                $("#Stree").css("display","none");
            }
        })
        .on("loaded.jstree", function (event, data) {
            var inst = data.instance;
            var obj = inst.get_node(id);
            inst.select_node(obj);

        })
}
//入库仓库改变事件
$("#search_origId").on("change",function () {
    /*初始化jstree*/
    SinitTree();
    $("#SdestId").val("--请选择入库库位--");
});

//入库选择点击事件
$("#SdestId").click(function () {
    $("#Stree").css("display","block");
});
//确定
function SchooseCage() {
    if(Sdeep != "3"){
        $.gritter.add({
            text: "请选择具体货位！",
            class_name: 'gritter-success  gritter-light'
        });
    }
    else{
        $("#Stree").css("display","none");
    }
}
//取消
function SunChoose() {
    SrackId = null;
    SlevelId = null;
    SallocationId = null;
    $("#SdestId").val("--请选择入库库位--");
    $("#Stree").css("display","none");
}
//jstree搜索
$("#Ssearch_organizationName").keydown(function (event) {
    if (event.keyCode == 13) {
        SsearchTree();
    }
});
function SsearchTree() {
    var searchResult = $("#Sjstree").jstree('search', $("#Ssearch_organizationName").val());
    //var searchResult = $('#jstree').jstree(true).search($("#search_organizationName"));
    $(searchResult).find('.jstree-search').focus();

}




//初始化树形结构
function initTree(id) {
    cageId = $("#edit_origId").val();
    $("#jstree").jstree("destroy");
    $('#jstree').on("select_node.jstree", function (e, data) {
        //变化事件
        if (data.selected.length) {
            // 点击节点，显示节点信息
            deep = data.node.original.deep;
            console.info(deep);
            var allocationName = "请选择";
            var levelName = "请选择";
            var rackName = "请选择";
            if(deep == "3"){
                allocationId = data.node.id;
                levelId = data.node.parents[0];
                rackId = data.node.parents[1];
                allocationName = data.node.text;
                levelName = $("#"+allocationId+"_anchor").parent().parent().prev().text();
                rackName = $("#"+levelId+"_anchor").parent().parent().prev().text();
            }
            else if(deep == "2"){
                levelId = data.node.id;
                rackId = data.node.parents[0];
                levelName = data.node.text;
                rackName = $("#"+levelId+"_anchor").parent().parent().prev().text();
            }
            else {
                rackId = data.node.id;
                rackName = data.node.text;
            }
            console.info(cageId);
            console.info(rackId);
            console.info(levelId);
            console.info(allocationId);
            $("#destId").val(rackName+"-"+levelName+"-"+allocationName);

        }

    })
        .jstree({
            'core': {
                'animation': 0,
                'check_callback': true,
                'data': {
                    'url': basePath + "/sys/repositoryController/unitListById.do",
                    "data": function (node) {
                        return {
                            "id": cageId
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
        //双击  确定jstree.js中已经添加双击事件
        .bind('dblclick.jstree',function(event){
            if(deep != "3"){
                $.gritter.add({
                    text: "请选择具体货位！",
                    class_name: 'gritter-success  gritter-light'
                });
            }
            else{
                $("#tree").css("display","none");
            }
        })
        .on("loaded.jstree", function (event, data) {
            var inst = data.instance;
            var obj = inst.get_node(id);
            inst.select_node(obj);

        })
}
//入库仓库改变事件
$("#edit_origId").on("change",function () {
    /*初始化jstree*/
    initTree();
    $("#destId").val("--请选择入库库位--");
});

//入库选择点击事件
$("#destId").click(function () {
    $("#tree").css("display","block");
});
//确定搜索
function chooseCage() {
    if(deep != "3"){
        $.gritter.add({
            text: "请选择具体货位！",
            class_name: 'gritter-success  gritter-light'
        });
    }
    else{
        $("#tree").css("display","none");
    }
}
//取消
function unChoose() {
    rackId = null;
    levelId = null;
    allocationId = null;
    $("#destId").val("--请选择入库库位--");
    $("#tree").css("display","none");
}
//jstree搜索
$("#search_organizationName").keydown(function (event) {
    if (event.keyCode == 13) {
        searchTree();
    }
});
function searchTree() {
    var searchResult = $("#jstree").jstree('search', $("#search_organizationName").val());
    //var searchResult = $('#jstree').jstree(true).search($("#search_organizationName"));
    $(searchResult).find('.jstree-search').focus();

}

function initSearchGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/repositoryAdjust/page.do?filter_GTI_status=-1&userId=" + userId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},

            {name: 'status', hidden: true},
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {name: 'origId', label: '仓库ID', hidden: true},
            {name: 'origName', label: '发货仓库', hidden: true},
            {name: 'ownerId',  hidden: true},
            {name:'nrackId',label:'新货架',hidden:true},
            {name:'nlevelId',label:'新货层',hidden:true},
            {name:'nallocationId',label:'新货位',hidden:true},
            {name: 'busnissId', hidden: true},
            {name: 'totQty', label: '单据数量', width: 20},
            {name: 'oprId', label: '操作人', width: 20},
            {name: 'remark', label: '备注', hidden: true}
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
        autoScroll: false,
        footerrow: true,
        sortname: 'billNo',
        sortorder: "desc",
        gridComplete: function () {
            setFooterData();
            if(autoSelect){
                var rowIds = $("#grid").getDataIDs();
                $("#grid").setSelection(rowIds[0]);
                autoSelect =false;
            }
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
        }
    });
}

function initDetailData(rowid){
    $("#myTab li").eq(0).find("a").click();
    var rowData = $("#grid").getRowData(rowid);
    if(rowData.nallocationId != null){
        initTree(rowData.nallocationId);
    }
    $("#editForm").setFromData(rowData);
    $("#edit_Id").val(rowData.billNo);
    console.info(rowData);
    rm_status = rowData.status;
    pageType="edit";
    initButtonGroup(pageType);
    $(".selectpicker").selectpicker('refresh');

    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    $('#codeDetailgrid').jqGrid("clearGridData");
    $('#codeDetailgrid').jqGrid('GridUnload');

    initeditGrid(rowData.billNo);
    initcodeDetail(rowData.billNo);

    $("#codeDetailgrid").trigger("reloadGrid");
    $("#addDetailgrid").trigger("reloadGrid");
    if (rm_status == "0") {
        //录入状态可以所有操作
        $("#SODtl_rmIdAdjust").removeAttr("disabled");
        $("#SODtl_cancel").removeAttr("disabled");
        $("#SODtl_addUniqCode").removeAttr("disabled");
        //可编辑表单
        $("#edit_origId").removeAttr("disabled");
        //入库选择点击事件
        $("#destId").click(function () {
            $("#tree").css("display","block");
        });
    }
    if(rm_status == "-1"){
        //撤销状态按钮全禁用，页面不可编辑
        $("#SODtl_rmIdAdjust").attr({"disabled": "disabled"});
        $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
        $("#SODtl_cancel").attr({"disabled": "disabled"});
        $("#SODtl_save").attr({"disabled": "disabled"});
        //不可编辑表单
        $("#edit_origId").attr('disabled', true);
        //取消点击事件
        $("#destId").unbind("click");
        //隐藏操作
        $("#addDetailgrid").setGridParam().hideCol("operation");
    }
    if(rm_status == "2"){
        //结束和撤销一样
        $("#SODtl_rmIdAdjust").attr({"disabled": "disabled"});
        $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
        $("#SODtl_cancel").attr({"disabled": "disabled"});
        $("#SODtl_save").attr({"disabled": "disabled"});
        //不可编辑表单
        $("#edit_origId").attr('disabled', true);
        //取消点击事件
        $("#destId").unbind("click");
        //隐藏操作
        $("#addDetailgrid").setGridParam().hideCol("operation");
    }
    loadingButtonDivTable(rm_status);
}
/**
 * 新增单据调用
 * isScan 是否调用扫码框
 * */
function addNew(isScan){
    $("#myTab li").eq(0).find("a").click();
    showScanDialog = isScan;
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    $('#codeDetailgrid').jqGrid("clearGridData");
    $('#codeDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    $("#editForm").clearForm();
    setEditFormVal();
    $("#addDetailgrid").trigger("reloadGrid");
    $("#codedetaillgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');
    pageType="add";
    initButtonGroup(pageType);
}
function setFooterData() {

    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty
    });
}

function initAddGrid() {

    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {
                name: "operation", label: "操作", width: 30, align: "center", sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return "<a style='margin: 8px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="录入状态"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-sign-out blue" title="结束状态"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'styleId', label: '款号', width: 40,sortable: false},
            {name: 'styleName', label: '款名', width: 40,sortable: false},
            {name: 'colorId', label: '色码', width: 40,sortable: false},
            {name: 'colorName', label: '颜色', width: 30,sortable: false},
            {name: 'sizeId', label: '尺码', width: 30,sortable: false},
            {name: 'sizeName', label: '尺码', width: 40,sortable: false},
            {
                name: 'qty', label: '数量', editable: true, width: 40,sortable: false,
                editrules: {
                    number: true,
                    minValue: 1
                },
                editoptions: {
                    dataInit: function (e) {
                        $(e).spinner();
                    }
                }
            },
            {name: 'sku', label: 'SKU', width: 50,sortable: false},
            {
                name: 'price', label: '销售价格', width: 40,sortable: false,
                editrules: {
                    number: true
                }
            },
            {name: 'totPrice', label: '销售金额', width: 40,sortable: false},
            {
                name: 'discount', label: "折扣", width: 40,sortable: false,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {
                name: 'actPrice', label: '实际价格',sortable: false, width: 40,
                editrules: {
                    number: true,
                    minValue: 0
                }
            },
            {name: 'totActPrice', label: '实际金额', width: 40,sortable: false},
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name:'orackId',label:'旧货架',hidden:true},
            {name:'olevelId',label:'旧货层',hidden:true},
            {name:'oallocationId',label:'旧货位',hidden:true},
            {name: 'warehouseId', label: '仓库号', width: 40, hidden: true},
            {name: 'uniqueCodes', label: '唯一码', width: 40, hidden: true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#addDetailgrid-pager',
        multiselect: false,
        shrinkToFit: true,
        footerrow: true,
        cellEdit: false,
        cellsubmit: 'clientArray',
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });
    $("#addDetailgrid").setGridParam().showCol("operation");

    $("#addDetailgrid-pager_center").html("");
}

function initeditGrid(billId) {
    billNo = billId;
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/repositoryAdjust/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {
                name: "operation", label: "操作", width: 30, align: "center", sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return "<a style='margin: 8px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="录入状态"></i>';
                    } else if (rowObject.status == 1) {
                        return '<i class="fa fa-sign-in blue" title="调整状态"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-sign-out blue" title="结束状态"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'styleId', label: '款号', width: 40,sortable: false},
            {name: 'styleName', label: '款名', width: 40,sortable: false},
            {name: 'colorId', label: '色码', width: 40,sortable: false},
            {name: 'colorName', label: '颜色', width: 30,sortable: false},
            {name: 'sizeId', label: '尺码', width: 30,sortable: false},
            {name: 'sizeName', label: '尺码', width: 40,sortable: false},
            {
                name: 'qty', label: '数量', editable: true, width: 40,sortable: false,
                editrules: {
                    number: true,
                    minValue: 1
                },
                editoptions: {
                    dataInit: function (e) {
                        $(e).spinner();
                    }
                }
            },
            {name: 'sku', label: 'SKU', width: 50,sortable: false},
            {
                name: 'price', label: '销售价格', width: 40,sortable: false,
                editrules: {
                    number: true
                }
            },
            {name: 'totPrice', label: '销售金额', width: 40,sortable: false},
            {
                name: 'discount', label: "折扣", width: 40,sortable: false,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {
                name: 'actPrice', label: '实际价格',sortable: false, width: 40,
                editrules: {
                    number: true,
                    minValue: 0
                }
            },
            {name: 'totActPrice', label: '实际金额', width: 40,sortable: false},
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name:'orackId',label:'旧货架',hidden:true},
            {name:'olevelId',label:'旧货层',hidden:true},
            {name:'oallocationId',label:'旧货位',hidden:true},
            {name: 'warehouseId', label: '仓库号', width: 40, hidden: true},
            {name: 'uniqueCodes', label: '唯一码', width: 40, hidden: true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#addDetailgrid-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellEdit: false,
        cellsubmit: 'clientArray',
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });

}
function setAddFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_returnQty = $("#addDetailgrid").getCol('returnQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#edit_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        returnQty: sum_returnQty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl !== "") {
        if (allCodeStrInDtl.substr(0, 1) === ",") {
            allCodeStrInDtl = allCodeStrInDtl.substr(1);
        }
    }
    console.info(allCodeStrInDtl);
}


function initSearchAndEditForm(){
    initSelectDestForm();
    initSelectOrigForm();
    setEditFormVal();
    $(".selectpicker").selectpicker('refresh');
}

function initSelectDestForm() {

    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择--</option>");
            $("#edit_destId").empty();
            $("#edit_destId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {

                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });


}

function initSelectOrigForm() {
    var searchOrigIdUrl="";
    if (userId == "admin") {
        searchOrigIdUrl=basePath + "/unit/list.do?filter_EQI_type=9";
    } else {
        searchOrigIdUrl=basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId;
    }
    $.ajax({
        url: searchOrigIdUrl,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            $("#search_origId").append("<option value=''>--请选择仓库--</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });

}

function input_keydown() {
    $("#edit_discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
}

function initEditFormValid() {
    $('#editForm').bootstrapValidator({
        message: '输入值无效',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitHandler: function (validator, form, submitButton) {
            $.post(form.attr('action'), form.serialize(), function (result) {
                if (result.success == true || result.success == 'true') {
                } else {
                    // Enable the submit buttons
                    $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                }
            }, 'json');
        },
        fields: {
            billNo: {
                validators: {}
            },
            billDate: {
                validators: {
                    notEmpty: {
                        message: '请选择单据日期'
                    }
                }
            },
            origId: {
                validators: {
                    notEmpty: {
                        message: '请选择入库仓库'
                    }
                }
            },
            newRmId: {
                validators: {
                    callback: {
                        message: '请选择入库库位',
                        callback: function (value, validator) {
                            if ($.trim(value) === $.trim("--请选择入库库位--")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        }
    });
}

/*根据权限初始化按钮*/
function initButtonGroup(type){
    console.info(type);
    $("#buttonGroup").html("" +
        "<button id='SODtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew(true)'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='SODtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>" +
        "<button id='SODtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>" +
        "<button id='SODtl_rmIdAdjust' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='rmIdChange()'>" +
        "    <i class='ace-icon fa fa-sign-out'></i>" +
        "    <span class='bigger-110'>调整</span>" +
        "</button>" +
        "<button id='SODtl_addUniqCode' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
        "    <i class='ace-icon fa fa-barcode'></i>" +
        "    <span class='bigger-110'>扫码</span>" +
        "</button>"
    );

    //未保存禁用撤销，调整，结束
    $("#SODtl_finishBill").attr({"disabled": "disabled"});
    $("#SODtl_rmIdAdjust").attr({"disabled": "disabled"});
    $("#SODtl_cancel").attr({"disabled": "disabled"});
    $("#addDetail").show();
    //入库选择点击事件
    $("#destId").click(function () {
        $("#tree").css("display","block");
    });
    loadingButton();

}
function loadingButton() {
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/repositoryAdjust/findResourceButton.do",
        type: "POST",
        success: function (msg) {

            if (msg.success) {

                var result=msg.result;
                for(var i=0;i<result.length;i++){
                    if(result[i].ishow===0){
                        if( $("#"+result[i].buttonId).length>0){
                            $("#"+result[i].buttonId).show();
                        }
                    }else {
                        if( $("#"+result[i].buttonId).length>0){
                            $("#"+result[i].buttonId).hide();
                        }

                    }

                }

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function updateBillDetailData(){
    var ct = $("#edit_customerType").val();
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var dtlRow = $("#addDetailgrid").getRowData(value);
        var stylePriceMap = JSON.parse(dtlRow.stylePriceMap);
        if (ct == "CT-AT") {//省代价格
            dtlRow.price = stylePriceMap['puPrice'];
        } else if (ct == "CT-ST") {//门店价格
            dtlRow.price = stylePriceMap['wsPrice'];
        } else if (ct == "CT-LS") {//吊牌价格
            dtlRow.price = stylePriceMap['price'];
        }
        dtlRow.totPrice = dtlRow.qty * dtlRow.price;
        dtlRow.totActPrice = (dtlRow.qty * dtlRow.actPrice).toFixed(2);
        if(dtlRow.id){
            $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
        }else{
            $("#addDetailgrid").setRowData(value, dtlRow);
        }
    });

}


function deleteItem(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setAddFooterData();
    var totActPrice = value.totActPrice;
    cs.showProgressBar();
    saveAjax();
}

/**
 * 查询左侧表格内容
 * */
function _search() {
    if($.trim($("#SdestId").val()) == "--请选择入库库位--") {
        $("#SdestId").val("");
    }
    else {
        $("#SdestId").val(SallocationId);
    }
    var serializeArray = $("#searchForm").serializeArray();
    console.info(serializeArray);
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params,
         loadComplete: function () {
             $("#SdestId").val("--请选择入库库位--");
         }
    });
    $("#grid").trigger("reloadGrid");

}

function _resetForm(){
    $("#searchForm").clearForm();
    $("#SdestId").empty();
    $("#SdestId").val("--请选择入库库位--");
    $(".selectpicker").selectpicker('refresh');
    $("#search_origId").val();
}


function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#edit_billNo").val();
    var ct = "CT-ST";
    if (origId && origId != null) {
        $("#dialog_buttonGroup").html("" +
            "<button  type='button' id = 'so_savecode_button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("请选择入库仓库!")
    }
    allCodes = "";
}

function rmIdChange() {
    cs.showProgressBar();

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return false;
    }
    var billNo = $("#edit_billNo").val();
    $.ajax({
     dataType: "json",
     async: true,
     url: basePath + "/logistics/repositoryAdjust/rmIdChange.do",
     data: {
         billNo : billNo
     },
     type: "POST",
        success:function (res) {
            cs.closeProgressBar();
            $.gritter.add({
                text: res.msg,
                class_name: 'gritter-success  gritter-light'
            });

            initcodeDetail(billNo);
            $("#grid").trigger("reloadGrid");
            $("#codeDetailgrid").trigger("reloadGrid");
        }
     });
    //调整后不能撤销,库位不能改变
    //结束和撤销一样
    $("#SODtl_rmIdAdjust").attr({"disabled": "disabled"});
    $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
    $("#SODtl_cancel").attr({"disabled": "disabled"});
    $("#SODtl_save").attr({"disabled": "disabled"});
    //不可编辑表单
    $("#edit_origId").attr('disabled', true);
    //取消点击事件
    $("#destId").unbind("click");
}

function cancel() {

    var billId= $("#edit_billNo").val();
    var status = $("#edit_status").val();
    if (status != "0") {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    if(billId == "" || billId == undefined){
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "撤销确定",
        callback: function (result) {
            /* $("#SODtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billId);
                addNew(false)
            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/repositoryAdjust/cancel.do",
        data: {billNo: billId},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
                //撤销状态按钮全禁用，页面不可编辑
                $("#SODtl_rmIdAdjust").attr({"disabled": "disabled"});
                $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
                $("#SODtl_cancel").attr({"disabled": "disabled"});
                $("#SODtl_save").attr({"disabled": "disabled"});
                //不可编辑表单
                $("#edit_origId").attr('disabled', true);
                //取消点击事件
                $("#destId").unbind("click");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}


function addProductsOnCode() {
    var productListInfo = [];
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var ct = $("#edit_customerType").val();
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            if(productInfo.code!=""&&productInfo.code!=undefined){
                productInfo.qty = 1;
                if (ct == "CT-AT") {//省代价格
                    productInfo.price = productInfo.puPrice;
                } else if (ct == "CT-ST") {//门店价格
                    productInfo.price = productInfo.wsPrice;
                } else if (ct == "CT-LS") {//吊牌价格
                    productInfo.price = productInfo.price;
                }
                productInfo.status = 0;
                if ($("#search_discount").val() && $("#search_discount").val() !== null) {
                    productInfo.discount = $("#search_discount").val();
                } else {
                    productInfo.discount = 100;
                }
                productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                productInfo.totPrice = productInfo.price;
                productInfo.totActPrice = productInfo.actPrice;
                var stylePriceMap={};
                stylePriceMap['price']=productInfo.price;
                stylePriceMap['wsPrice']=productInfo.wsPrice;
                stylePriceMap['puPrice']=productInfo.puPrice;
                productInfo.uniqueCodes = productInfo.code;
                productInfo.stylePriceMap=JSON.stringify(stylePriceMap);
                //记录商品原库位信息
                productInfo.inStock = productInfo.inStock;
                productInfo.orackId = productInfo.floorRack;
                productInfo.olevelId = productInfo.floorArea;
                productInfo.oallocationId = productInfo.floorAllocation;
                productInfo.warehouseId = productInfo.warehouseId;

                productListInfo.push(productInfo);
            }
        });
        if (productListInfo.length == 0) {
            bootbox.alert("请添加唯一码");
            $("#so_savecode_button").removeAttr("disabled");
            return;
        }
        var isAdd = true;
        var alltotActPrice = 0;
        $.each(productListInfo, function (index, value) {
            console.info(value);
            isAdd = true;
            $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                if (value.sku === dtlRow.sku) {
                    if (dtlRow.uniqueCodes.indexOf(value.code) != -1) {
                        isAdd = false;
                        $.gritter.add({
                            text: value.code + "不能重复添加",
                            class_name: 'gritter-success  gritter-light'
                        });
                        return true;
                    }
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                    dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                    alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                    dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                    console.info(dtlRow);
                    if (dtlRow.id) {
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                    } else {
                        $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                    }
                    isAdd = false;
                }
            });
            if (isAdd) {
                $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
            }
        });
        $("#so_savecode_button").removeAttr("disabled");
        $("#add-uniqCode-dialog").modal('hide');
        setFooterData();
    }
}


function saveAjax() {
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
    var origName = $('#edit_origId option:selected').text();
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/repositoryAdjust/save.do",
        data: {
            rmAdjustBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId,
            origName:origName,
            rackId: rackId,
            levelId: levelId,
            allocationId: allocationId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#SODtl_wareHouseIn").removeAttr("disabled");
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#editForm").setFromData(msg.result);
                if(msg.result.newRmId != null){
                    var rmId = msg.result.newRmId.split("-");
                    var id = $("#edit_origId").val()+"-"+rmId[0].replace(/[^0-9]/ig,"")+"-"+rmId[1].replace(/[^0-9]/ig,"")+"-"+rmId[2].replace(/[^0-9]/ig,"");
                    initTree(id);
                }

                $("#addDetailgrid").jqGrid('setGridParam', {
                    datatype: "json",
                    page: 1,
                    url: basePath + "/logistics/repositoryAdjust/findBillDtl.do?billNo=" + msg.result.billNo,
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#grid").trigger("reloadGrid");
                initcodeDetail(msg.result.billNo);
                $("#codeDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
            //成功启用所有按钮
            $("#SODtl_finishBill").removeAttr("disabled");
            $("#SODtl_rmIdAdjust").removeAttr("disabled");
            $("#SODtl_cancel").removeAttr("disabled");
        }
    });
    window.isTrue = true;
}

function setEditFormVal(){
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $("#edit_userName").val(userId);
    $("#edit_origId").selectpicker('val', defaultWarehId);
    $("#destId").empty();
    $("#destId").val("--请选择入库库位--");
    initTree();
}

/**
 * 保存按钮方法
 * */
function save() {
    cs.showProgressBar();

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return false;
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加调整商品！");
        cs.closeProgressBar();
        return false;
    }
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    $("#edit_status").val("0");
    saveAjax();
}
function end() {
    var billNo = $("#edit_billNo").val();
    $.ajax({
        dataType: "json",
        async:true,
        url: basePath + "/logistics/repositoryAdjust/end.do",
        data: {
            billNo:billNo
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function initcodeDetail(billNo) {
    $("#codeDetailgrid").jqGrid({
        height: 'auto',
        url: basePath + "/logistics/repositoryAdjust/codeDetail.do?billNo=" + billNo,
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'uniqueCode', label: 'code',width :120},
            {name: 'sku', label: 'SKU',width :170,editable :true},
            {name: 'billNo', label: '单据号',width :170,editable :true},
            {name: 'userId', label: '操作人',width :60,editable :true},
            {name: 'updateTime', label: '操作时间',width :130,editable :true},
            {name: 'warehouseId', label: '所在仓库',width :130,editable :true},
            {name: 'oldRm', label: '原库位',width :180,editable :true},
            {name: 'newRm', label: '新库位',width :180,editable :true}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc"
    });
}

/**
 * billStatus 单据状态新增为0
 * 动态配置按钮,div,表格列字段
 * */
function loadingButtonDivTable(billStatus) {
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!=0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['div'],function(index,value){
        if(value.isShow!=0) {
            debugger
            $("#"+value.privilegeId).hide();
        }
    });
    $.each(privilegeMap['button'],function(index,value){
        if(value.isShow!=0) {
            $("#"+value.privilegeId).hide();
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_rmIdAdjust","SODtl_addUniqCode"];
            break;
        case "0" :
            disableButtonIds = [];
            break;
        case "1":
            disableButtonIds = ["TRDtl_cancel","TRDtl_save,TRDtl_addUniqCode"];
            break;
        case "2" :
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_rmIdAdjust","SODtl_addUniqCode"];
            break;
        case "3":
            disableButtonIds = ["TRDtl_cancel","TRDtl_save","TRDtl_addUniqCode"];
            break;
        default:
            disableButtonIds = ["TRDtl_wareHouseIn"];
    }
    //根据单据状态disable按钮
    $.each(privilegeMap['button'],function(index,value){
        //找对应的按钮
        if($.inArray(value.privilegeId,disableButtonIds)!= -1){
            $("#"+value.privilegeId).attr({"disabled": "disabled"});
        }else{
            $("#"+value.privilegeId).removeAttr("disabled");
        }
    });

}

