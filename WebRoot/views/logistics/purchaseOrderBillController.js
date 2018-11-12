var searchUrl = basePath + "/logistics/purchaseOrderBill/page.do?filter_GTI_status=-1";
var autoSelect = false;//是否自动选中
var showScanDialog = false;
var editDtailRowId = null;  //当前编辑行号
var editDtailColumn = null; //当前编辑列名
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allocationId = null;//货位
var levelId = null;//货层
var rackId = null;//货架
var cageId = null;//仓库
var deep = null;//深度
$(function () {
    load().then(function (data) {
        /*初始化左侧grig*/
        initSearchGrid();
    });
});
function load() {
    var promise = new Promise(function(resolve, reject){
        /*初始化右侧grig*/
        initAddGrid();
        /*初始化from表单*/
        initForm();
        /*初始化jstree*/
        $("#destId").empty();
        $("#destId").val("--请选择入库库位--");
        if(billNo){
            bootbox.alert("单据"+billNo+"正在编辑中");
        }else{
            sessionStorage.removeItem("billNopurchase");
        }
        initButtonGroup(0);
        loadingButtonDivTable();
        initEditFormValid();
        initSelectbuyahandIdForm();
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#search_destId").empty();
                $("#search_destId").append("<option value=''>--请选择入库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                }
                $(".selectpicker").selectpicker('refresh');
            }
        });
        resolve("success");
    });
    return promise;
}

//初始化树形结构
function initTree(id) {
    cageId = $("#edit_destId").val();
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
            if (deep == "3") {
                allocationId = data.node.id;
                levelId = data.node.parents[0];
                rackId = data.node.parents[1];
                allocationName = data.node.text;
                levelName = $("#" + allocationId + "_anchor").parent().parent().prev().text();
                rackName = $("#" + levelId + "_anchor").parent().parent().prev().text();
            }
            else if (deep == "2") {
                levelId = data.node.id;
                rackId = data.node.parents[0];
                levelName = data.node.text;
                rackName = $("#" + levelId + "_anchor").parent().parent().prev().text();
            }
            else {
                rackId = data.node.id;
                rackName = data.node.text;
            }
            console.info(cageId);
            console.info(rackId);
            console.info(levelId);
            console.info(allocationId);
            $("#destId").val(rackName + "-" + levelName + "-" + allocationName);

        }

    })
        .jstree({
            'core': {
                'animation': 0,
                'check_callback': true,
                'data': {
                    'url': basePath + "/sys/repositoryController/unitListById.do",
                    "data": function (node) {
                        if (node.id == "#") {//第一次加载
                            return {
                                "id": cageId
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
                    /*'dataFilter':function (data, type) {
                     // 对Ajax返回的原始数据进行预处理
                     var json = JSON.parse(data)
                     return JSON.stringify(json.result);// 返回处理后的数据
                     }*/
                }
            },
            'types': {
                "default": {
                    "icon": "fa fa-university"
                }
            },
            'plugins': ['search', 'wholerow', 'types']
        })
        //双击  确定jstree.js中已经添加双击事件
        .bind('dblclick.jstree', function (event) {
            if (deep != "3") {
                $.gritter.add({
                    text: "请选择具体货位！",
                    class_name: 'gritter-success  gritter-light'
                });
            }
            else {
                $("#tree").css("display", "none");
            }
        })
        .on("loaded.jstree", function (event, data) {
            var inst = data.instance;
            var obj = inst.get_node(id);
            inst.select_node(obj);

        })
}

//入库仓库改变事件
$("#edit_destId").on("change", function () {
    /*初始化jstree*/
    initTree();
    $("#destId").val("--请选择入库库位--");
});

//入库选择点击事件
$("#destId").click(function () {
    $("#tree").css("display", "block");
    initTree();
});

//确定搜索
function chooseCage() {
    if (deep != "3") {
        $.gritter.add({
            text: "请选择具体货位！",
            class_name: 'gritter-success  gritter-light'
        });
    }
    else {
        $("#tree").css("display", "none");
    }
}

//取消
function unChoose() {
    rackId = null;
    levelId = null;
    allocationId = null;
    $("#destId").val("--请选择入库库位--");
    $("#tree").css("display", "none");
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

function initForm() {
    initSelectDestForm();

}

function initSearchGrid() {
    var url="";
    if (cargoTrack=="cargoTracking"){
        url= basePath + "/logistics/purchaseOrderBill/findBill.do?billNo="+cTbillNo;
    }else {
        url = basePath + "/logistics/purchaseOrderBill/page.do?filter_GTI_status=-1";
    }
    $("#grid").jqGrid({
        height: "auto",
        url:url,
        datatype: "json",
        sortorder: 'desc',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},
            {
                name: "", label: "操作", width: 60, editable: false, align: "center", hidden: true,
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a href='" + basePath + "/logistics/purchaseOrderBill/copyAdd.do?billNo=" + billNo + "'><i class='ace-icon fa fa-files-o' title='复制新增'></i></a>";
                    html += "<a style='margin-left: 20px' href='" + basePath + "/logistics/purchaseOrderBill/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=check('" + billNo + "')><i class='ace-icon fa fa-check-square-o' title='审核'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                    /*  html += "<a style='margin-left: 20px' href='#' onclick=doPrint('" + billNo + "')><i class='ace-icon fa fa-print' title='打印'></i></a>";*/

                    html += "<a style='margin-left: 20px' href='#' onclick=quit('" + rowObject.billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";

                    return html;

                }
            },
            {name: 'status', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 1:
                            html = "<i class='fa fa-check-square-o blue' title='审核'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3 :
                            html = "<i class='fa fa-sign-out blue' title='未结束'></i>";
                            break;
                        default:
                            break;

                    }
                    return html;
                }
            },
            {
                name: 'inStatusImg', label: '入态', width: 20, align: 'center', hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-sign-out blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {name: 'origUnitId', label: '供应商ID', hidden: true},
            {name: "ownerId", hidden: true},
            {name: 'origUnitName', label: '供应商', width: 40},
            {name: 'origId', label: '出库仓ID', hidden: true},
            {name: 'origName', label: '出库仓', hidden: true, width: 40},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', hidden: true, width: 40},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 35},
            {name: 'totQty', label: '单据数量', sortable: false, width: 20,align:"center"},
            {name: 'totInQty', label: '已入库数', width: 30, hidden: true},
            {
                name: 'totInVal', label: '总入库金额', width: 30, hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    var totInVal = parseFloat(cellValue).toFixed(2);
                    return totInVal;
                }
            },
            {name: 'remark', label: '备注', sortable: false, width: 40, hidden: true},
            {name: 'cageId', hidden: true},
            {name: 'rackId', hidden: true},
            {name: 'levelId', hidden: true},
            {name: 'allocationId', hidden: true},
            {name: 'id', hidden: true},
            {name: 'buyahandId', hidden: true},
            {name: 'payPrice', hidden: true},
            {name: 'payType', hidden: true},
            {name: 'orderWarehouseId', hidden: true},
            {name: 'discount', hidden: true},
            {name: 'srcBillNo', hidden: true},
            {name: 'arrival', hidden: true},
            {name: 'remark', hidden: true},
            {name: 'returnBillNo', hidden: true}
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
        sortname: 'billNo',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
            if (autoSelect) {
                var rowIds = $("#grid").getDataIDs();
                $("#grid").setSelection(rowIds[0]);
                autoSelect = false;
            }
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
        },
        loadComplete:function () {
            if (cargoTrack=="cargoTracking"){
                initDetailData(cTbillNo);
                $("#search_billId").val(cTbillNo);
            }
        }
    });
}

function setFooterData() {
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty', false, 'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal
    });
}

function initDetailData(rowid) {
    $("#PIDtl_save").attr('disabled', false);
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    console.info(rowData);
    if (rowData.allocationId == "" || rowData.allocationId == undefined || rowData.allocationId == null) {
        $("#destId").empty();
        $("#destId").val("--请选择入库库位--");
    }
    else {
        initTree(rowData.allocationId);
    }
    slaeOrder_status = rowData.status;
    if (slaeOrder_status != "0" && userId != "admin") {
        $("#edit_origId").attr('disabled', true);
    }
    if (userId == "admin") {
        $("#edit_guest_button").removeAttr("disabled");
        $("#edit_origId").attr('disabled', true);
    }
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    initButtonGroup(slaeOrder_status);
    $("#addDetailgrid").trigger("reloadGrid");
}

function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'sku', label: 'sku', hidden: true},
            {
                name: 'status', hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    return 0;
                }
            },
            {name: 'inStatus', label: '入态', hidden: true},
            {
                name: 'printStatus', hidden: true, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    return 0;
                }
            },
            {
                name: 'statusImg', label: '状态', width: 13, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                }
            },
            {
                name: 'inStatusImg', label: '入态', width: 13, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-sign-out blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'printStatusImg', label: '打印状态', width: 13,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-times blue" title="未打印"></i>';
                }
            },
            {name: "inStockType", hidden: true},
            {
                name: 'inStockTypeName', label: '入库类型', width: 20, editable: true,
                editoptions: {
                    dataInit: function (e) {
                        $(e).kendoComboBox({
                            dataTextField: "name",
                            dataValueField: "id",
                            height: 200,
                            suggest: true,
                            change: function (e) {
                                if (this._initial != this._prev) {
                                    $('#addDetailgrid').saveRow(editDtailRowId);
                                    var value = $('#addDetailgrid').getRowData(editDtailRowId);
                                    value.inStockType = this.value();
                                    $("#addDetailgrid").setRowData(editDtailRowId, value);
                                }

                            },
                            dataSource: {
                                type: "jsonp",
                                transport: {
                                    read: basePath + "/sys/property/searchByType.do?type=C6"
                                }
                            }
                        });
                    }

                }, formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.inStockType) {
                        case 'XK':
                            return "新款";
                        case 'BH':
                            return "补货";
                        case 'PH':
                            return "供应商配货";
                        case 'JS':
                            return "寄售";
                        default :
                            return "";
                    }
                }
            },
            {name: 'styleId', label: '款号', width: 16,hidden: true},
            {name: 'colorId', label: '色码', width: 16,hidden: true},
            {name: 'sizeId', label: '尺码', width: 16,hidden: true},
            {name: 'styleName', label: '款名', width: 16},
            {name: 'colorName', label: '颜色', width: 16,hidden: true},
            {name: 'sizeName', label: '尺码', width: 16,hidden: true},
            {
                name: 'qty', label: '数量', editable: true, width: 16,
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
            {name: 'arrival', hidden: true},
            {name: 'actPrintQty', label: '已打数', width: 16},
            {name: 'inQty', label: '已入数', width: 16},
            {name: 'sku', label: 'SKU', width: 20},

            {
                name: 'price', label: '采购价格', editable: true, width: 20,
                editrules: {
                    number: true
                }
            },
            {
                name: 'discount', label: "折扣", width: 20, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {name: 'totPrice', label: '采购金额', width: 20},
            {name: 'actPrice', label: '实际价格', editable: true, width: 20},
            {name: 'totActPrice', label: '实际金额', width: 20},
            {
                name: '', label: '唯一码明细', width: 20, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.sku + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            }
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
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            editDtailRowIds = rowid;
            editDtailColumn = celname;
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;
        },
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price") / 100);
                var var_totActPrice = var_actPrice * $('#addDetailgrid').getCell(rowid, "qty");
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = value * $('#addDetailgrid').getCell(rowid, "qty");
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            }
            setEditFooterData();
            for (var i = 0; i < addDetailgridiRow.length; i++) {
                if (addDetailgridiRow[i] == iRow && addDetailgridiCol[i] == iCol) {
                    addDetailgridiRow.splice(i, 1);
                    addDetailgridiCol.splice(i, 1);
                }
            }
        },
        gridComplete: function () {
            setEditFooterData();
        }
    });
    var parent_column = $("#main-container");
    $("#addDetailgrid").setGridParam().showCol("operation");
    $("#addDetailgrid").jqGrid('navGrid', "#addDetailgrid-pager",
        {
            edit: false,
            add: true,
            addicon: "ace-icon fa fa-plus",
            addfunc: function () {
                addDetail();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        }
    );
    $("#addDetailgrid-pager_center").html("");
}

var arrival = 0;
var isfrist = true;
var editDtailiRow = null;
var editDtailiCol = null;

function initeditGrid(billNo) {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/purchaseOrderBill/findBillDtl.do?billNo=" + billNo,
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'sku', label: 'sku', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'printStatus', hidden: true},
            {
                name: '', label: '状态', width: 10, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.status == 1) {
                        return '<i class="fa fa-sign-in blue" title="入库状态"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-sign-out blue" title="出库状态"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: '', label: '入态', width: 10, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-sign-out blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: '', label: '打印态', width: 10, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.printStatus == 0) {
                        return '<i class="fa fa-times blue" title="未打印"></i>';
                    } else if (rowObject.printStatus == 1) {
                        return '<i class="fa fa-tags blue" title="部分打印"></i>';
                    } else if (rowObject.printStatus == 2) {
                        return '<i class="fa fa-print blue" title="已打印"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: '', label: '入型', width: 10,
                formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.inStockType) {
                        case 'XK':
                            return "新款";
                        case 'BH':
                            return "补货";
                        case 'PH':
                            return "供应商配货";
                        case 'JS':
                            return "寄售";
                        default :
                            return "";
                    }
                }
            },

            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 20},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
            {name: 'qty', label: '数量', width: 15,align:"center"},
            {name: 'arrival', label: '本次到货数', width: 15,align:"center", editable: true},
            {name: 'actPrintQty', label: '已打印数量', width: 15,align:"center"},
            {
                name: 'printQty', label: '待打印数量', width: 15,align:"center"
            },
            {name: 'inQty', label: '已入库数量', width: 15,align:"center"},
            {name: 'sku', label: 'SKU', width: 30},
            {
                name: 'price', label: '采购价格', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {
                name: 'totPrice', label: '采购金额', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {
                name: 'actPrice', label: '实际价格', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }
            },
            {
                name: 'totActPrice', label: '实际金额', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }
            },

            {name: 'actQty', hidden: true},
            {name: "billId", hidden: true},
            {name: "billNo", hidden: true},
            {name: "inStockType", hidden: true},
            {
                name: '', label: '唯一码明细', width: 20, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.sku + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            }

        ],

        cellEdit: true,
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
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            editDtailiCol = iCol;
            editDtailiRow = iRow;
        },
        beforeEditCell: function (rowid, celname, value, iRow, iCol) {
            if (isfrist) {
                arrival = $('#addDetailgrid').getCell(rowid, "arrival");
            }
            isfrist = false;
        },
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            var styleId = $('#addDetailgrid').getCell(rowid, "styleId");
            //数量
            var qty = $('#addDetailgrid').getCell(rowid, "qty");
            //已打印数
            var actPrintQty = $('#addDetailgrid').getCell(rowid, "actPrintQty");
            //打印状态
            var printStatus = $('#addDetailgrid').getCell(rowid, "printStatus");
            //待打印数量
            var printQty = $('#addDetailgrid').getCell(rowid, "printQty");

            if (parseInt(value) > parseInt(printQty)) {
                $.gritter.add({
                    text: "已超过" + styleId + "的待打印总数量",
                    class_name: 'gritter-success  gritter-light'
                });
                $('#addDetailgrid').setCell(rowid, "arrival", arrival);
            }
            if (parseInt(value) < 0) {
                $.gritter.add({
                    text: "请填写正确的到货数",
                    class_name: 'gritter-success  gritter-light'
                });
                $('#addDetailgrid').setCell(rowid, "arrival", arrival);
            }
        },
        gridComplete: function () {
            $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                var printStatus = $('#addDetailgrid').getCell(value, "printStatus");
                if (printStatus == 2) {
                    $("#addDetailgrid").jqGrid('setCell', value, 17, '', 'not-editable-cell');
                }
            });
            setAddFooterData();
        }
    });

    if (curOwnerId !== "1") {
        $("#addDetailgrid").setGridParam().hideCol("price");
        $("#addDetailgrid").setGridParam().hideCol("totPrice");
        $("#addDetailgrid").setGridParam().hideCol("actPrice");
        $("#addDetailgrid").setGridParam().hideCol("totActPrice");
        $("#addDetailgrid").setGridParam().hideCol("discount");
    } else {
        $("#addDetailgrid").setGridParam().showCol("price");
        $("#addDetailgrid").setGridParam().showCol("totPrice");
        $("#addDetailgrid").setGridParam().showCol("actPrice");
        $("#addDetailgrid").setGridParam().showCol("totActPrice");
        $("#addDetailgrid").setGridParam().showCol("discount");
    }
}

function setAddFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

var prefixId;
var dialogOpenPage;

function openSearchVendorDialog(preId) {
    dialogOpenPage = "purchaseOrder";
    prefixId = preId;
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    console.log(prefixId);
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseOrder(prefixId)'>确认</button>"
    );
}

function initEditFormValid() {
    $('#editForm').bootstrapValidator({
        message: '输入值无效',
        excluded: [':disabled', ':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitHandler: function (validator, form, submitButton) {
            $.post(form.attr('action'), form.serialize(), function (result) {
                if (result.success == true || result.success == 'true') {
                } else {
                    $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                }
            }, 'json');
        },
        fields: {
            billNo: {
                validators: {}
            },
            origUnitId: {
                validators: {
                    notEmpty: {
                        message: '请选择供应商'
                    }
                }
            },
            destId: {
                validators: {
                    notEmpty: {
                        message: '请选择收货仓库'
                    }
                }
            },
            orderWarehouseId: {
                validators: {
                    notEmpty: {
                        message: '请选择订货仓库'
                    }
                }
            },
            billDate: {
                validators: {
                    notEmpty: {
                        message: '请选择单据日期'
                    }
                }
            },
            payPrice: {
                validators: {
                    notEmpty: {
                        message: '实付金额不能为空'
                    }
                }
            },
            discount: {
                validators: {
                    numeric: {
                        message: '折扣只能只能为0-100之间的数字'
                    },
                    callback: {
                        message: '折扣只能只能为0-100之间的数字',
                        callback: function (value, validator) {
                            if (parseInt(value) < 0 || parseInt(value) > 100) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            },
            rmId: {
                validators: {
                    callback: {
                        message: '请选择入库库位',
                        callback: function (value, validator) {
                            if ($.trim(value) == $.trim("--请选择入库库位--")) {
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


function addDetail() {
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        $("#stylegrid").clearGridData();
        $("#color_size_grid").clearGridData();
    });
}

function showCodesDetail(sku) {
    var billNo = $("#edit_billNo").val();
    var uniqueCodes = "";
    $.ajax({
        async: false,
        dataType: "json",
        url: basePath + "/stock/warehStock/findCodesStr.do",
        data: {
            sku: sku,
            billNo: billNo
        },
        type: "POST",
        success: function (result) {
            if (result.success) {
                uniqueCodes = result.result;
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}

function initSelectbuyahandIdForm() {
    var url;
    url = basePath + "/sys/user/list.do?filter_EQS_roleId=BUYER";
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_buyahandId").empty();
            $("#search_buyahandId").append("<option value='' >--请选择买手--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_buyahandId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
            }
            $(".selectpicker").selectpicker('refresh');
            $("#search_buyahandId").val(saleOrder_buyahandId);
        }
    });
}

function initSelectDestForm() {
    if (userId === "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#edit_destId").empty();
                $("#edit_destId").append("<option value=''>--请选择入库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                }
                $(".selectpicker").selectpicker('refresh');
                $("#edit_destId").val(defaultWarehId);
                initTree();
            }
        });
    } else {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#edit_destId").empty();
                $("#edit_destId").append("<option value=''>--请选择入库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                }
                $(".selectpicker").selectpicker('refresh');
                $("#edit_destId").val(defaultWarehId);
            }
        });
    }
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_orderWarehouseId").empty();
            $("#search_orderWarehouseId").append("<option value=''>--请选择订货仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_orderWarehouseId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $(".selectpicker").selectpicker('refresh');
        }
    });
}

/**
 * 查询左侧表格内容
 * */
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

function _resetForm() {
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#select_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
}

function search_discount_onblur() {
    setDiscount();
}

function setDiscount() {
    var discount = $("#search_discount").val();
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        $('#addDetailgrid').setCell(index, "discount", discount);
        var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(index, "price") / 100);
        var var_totActPrice = var_actPrice * $('#addDetailgrid').getCell(index, "qty");
        $('#addDetailgrid').setCell(index, "actPrice", var_actPrice);
        $('#addDetailgrid').setCell(index, "totActPrice", var_totActPrice);
    });
    setEditFooterData();
}

function addProductInfo(status) {
    if (editDtailRowId != null) {
        $('#addDetailgrid').saveRow(editcolosizeRow);
    }
    var addProductInfo = [];
    if (editcolosizeRow != null) {
        $('#color_size_grid').saveRow(editcolosizeRow);
    }
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));

    $.each($("#color_size_grid").getDataIDs(), function (index, value) {
        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.price = styleRow.preCast;
            productInfo.actPrice = productInfo.price;
            productInfo.status = 0;
            productInfo.printStatus = 0;
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.sku = productInfo.code;
            productInfo.printQty = 0;
            productInfo.inStockType = styleRow.class6;
            addProductInfo.push(productInfo);
        }
    });
    var isAdd = true;
    $.each(addProductInfo, function (index, value) {
        isAdd = true;
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.code === dtlRow.sku) {
                dtlRow.qty = parseInt(dtlRow.qty) + parseInt(value.qty);
                dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                $("#addDetailgrid").setRowData(dtlndex, dtlRow);
                isAdd = false;
            }
        });
        if (isAdd) {
            $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
        }
    });
    if (status) {
        $("#modal-addDetail-table").modal('hide');
    }
    setEditFooterData();
}

function setEditFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = value.qty * value.actPrice;
    $("#addDetailgrid").setRowData(rowId, value);
    setEditFooterData();
}

function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setEditFooterData();
}

function addNew() {
    $("#editForm").clearForm();
    $("#search_status").val("");
    $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    $("#search_payPrice").val(0);
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    initSelectDestForm();
    initButtonGroup(0);
    $("#destId").empty();
    $("#destId").val("--请选择入库库位--");
}

function save() {
    cs.showProgressBar();
    $("#search_destId").removeAttr('disabled');
    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加采购商品");
        cs.closeProgressBar();
        return;
    }
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }

    var dtlArray = [];
    console.info($("#destId").val());
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $("#search_billDate").val(updateTime($("#search_billDate").val()));
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/purchaseOrderBill/save.do",
        data: {
            purchaseBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId,
            cageId: cageId,
            rackId: rackId,
            levelId: levelId,
            allocationId: allocationId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit_billNo").val(msg.result);
                quitback();
                addNew();
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#destId").empty();
    $("#destId").val("--请选择入库库位--");
}

function quitback() {
    $.ajax({
        url: basePath + "/logistics/purchaseOrderBill/quit.do?billNo=" + $("#edit_billNo").val(),
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            if (textStatus == "success") {
                $.gritter.add({
                    text: billNo + "可以修改",
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
}

function convertToTagBirth() {
    var sum = 0;
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        sum += parseInt(rowData.arrival);
        dtlArray.push(rowData);
    });
    if (sum == 0) {
        $.gritter.add({
            text: "无初始化标签的商品！",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchaseOrderBill/covertToTagBirth.do",
        data: {strDtlList: JSON.stringify(dtlArray)},
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#addDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function findBirthTags(){
    var billNo = $("#edit_billNo").val();
    if(billNo == "" || billNo == undefined){
        $.gritter.add({
            text: "无单据信息请选择要查看的单据！",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    $("#show-findBirthNo-list").modal('show');
    initBirthNoList(billNo);

}

function openAddEpcDialog() {
    var billNo = $("#edit_billNo").val();
    $("#modal-addEpc-table").modal('show').on('hidden.bs.modal', function () {
        $("#epcgrid").clearGridData();
    });
    $("#epcgrid").jqGrid('setGridParam', {
        page: 1,
        url: basePath + '/logistics/purchaseOrderBill/findNotInEpc.do',
        postData: {billNo: billNo}
    }).trigger("reloadGrid");
}

function saveCovert() {
    cs.showProgressBar();
    var ids = $("#epcgrid").jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        cs.closeProgressBar();
        bootbox.alert("请选择要入库的唯一码信息");
    } else {
        var epcArray = [];
        $.each(ids, function (index, value) {
            var rowData = $("#epcgrid").getRowData(value);
            epcArray.push(rowData);
        });
        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });

        $.ajax({
            dataType: "json",
            url: basePath + "/logistics/purchaseOrderBill/convert.do",
            data: {strDtlList: JSON.stringify(dtlArray), recordList: JSON.stringify(epcArray)},
            type: "POST",
            success: function (msg) {
                cs.closeProgressBar();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#modal-addEpc-table").modal('hide');
                    $("#addDetailgrid").trigger("reloadGrid");
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    }
}

function doPrint() {
    /*$("#editForm").resetForm();*/
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#edit_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type: "PI"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    if (billNo.indexOf(msg.result[i].type) >= 0) {
                        addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                            "<button class='btn btn-info'>" +
                            "<i class='cae-icon fa fa-refresh'></i>" +
                            "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                            "</button>" +
                            "</div>"
                    }
                }
                $("#addbutton").html(addcont);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function set(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessage.do",
        data: {"id": id, "billno": $("#billno").val()},
        type: "POST",
        success: function (msg) {

            if (msg.success) {
                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                var LODOP = getLodop();
                //var LODOP=getLodop(document.getElementById('LODOP2'),document.getElementById('LODOP_EM2'));
                eval(print.printCont);
                var printCode = print.printCode;
                var printCodes = printCode.split(",");
                for (var i = 0; i < printCodes.length; i++) {
                    var plp = printCodes[i];
                    var message = cont[plp];
                    if (message != "" && message != null && message != undefined) {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', message);
                    } else {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', "");
                    }

                }

                var recordmessage = "";
                var sum = 0;
                var allprice = 0;
                var alldiscount = 0;
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                        "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.actPrice.toFixed(1) + "</td>" +
                        "<td align='right' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + (conts.actPrice * conts.qty).toFixed(2) + "</td>" +
                        "</tr>";

                    sum = sum + parseInt(conts.qty);
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    alldiscount = alldiscount + parseFloat((conts.actPrice * conts.qty).toFixed(2));
                }
                alldiscount = alldiscount.toFixed(0);
                recordmessage += " <tr style='border-top:1px dashed black;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td>" +
                    "<td align='left'style='border-top:1px dashed black;padding-top:5px;'>" + sum + "</td>" +
                    "<td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    " <td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    "<td align='right' style='border-top:1px dashed black;padding-top:5px;'>" + alldiscount + "</td>" +
                    " </tr>";

                $("#loadtab").html(recordmessage);
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialog2").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function cancel() {
    var billId = $("#edit_billNo").val();
    if ($("#search_status").val() != 0) {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "撤销确定",
        callback: function (result) {
            /* $("#PIDtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billId);
                addNew()
            } else {
            }
        }
    });
}

function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchaseOrderBill/cancel.do",
        data: {billNo: billId},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function end() {
    $("#return_origId").val($("#edit_destId").val());
    $("#return_destUnitId").val($("#edit_origUnitId").val());
    $("#return_destUnitName").val($("#edit_origUnitName").val());
    $("#return_billDate").val(getToDay("yyyy-MM-dd"));
    $("#return_billDate").val(updateTime($("#return_billDate").val()));
    $("#search_remark").val("该单由采购单据转换而来，具体请查看采购单据。");
    var PbillNo = $("#edit_billNo").val();
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        if (parseInt(rowData.qty) != parseInt(rowData.actPrintQty)) {
            rowData.qty = rowData.printQty;
            rowData.actPrice = rowData.price;
            rowData.totPrice = -rowData.price * rowData.qty;
            rowData.totactprice = -rowData.actPrice * rowData.qty;
            dtlArray.push(rowData);
        }
    });
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/purchaseOrderBill/endNb.do",
        data: {
            purchaseBillStr: JSON.stringify(array2obj($("#ReturnEditForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId,
            PbillNo: PbillNo
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                addNew();
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function initButtonGroup(billStatus) {
    $("#buttonGroup").html("" +
        "<button id='PIDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='PIDtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>" +
        "<button id='PIDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>" +
        "<button id='PIDtl_doPrint' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>打印</span>" +
        "</button>" +
        "<button id='PIDtl_doPrintSanLian' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintSanLian()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>三联打印</span>" +
        "</button>" +
        "<button id='PIDtl_doPrintA4' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>A4打印</span>" +
        "</button>" +
        "<button id='PIDtl_findRetrunno' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='convertToTagBirth()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>标签初始化</span>" +
        "</button>" +
        "<button id='PIDtl_birthTag' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findBirthTags()'>" +
        "    <i class='ace-icon fa fa-tags'></i>" +
        "    <span class='bigger-110'>查看标签初始化</span>" +
        "</button>"+
        "<button id='PIDtl_findshopMessage' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='openAddEpcDialog()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>采购入库单</span>" +
        "</button>" +
        "<button id='PIDtl_end' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='end()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>结束订单</span>" +
        "</button>"
    );
    $("#addDetail").show();
    loadingButtonDivTable(billStatus);
}

function loadingButtonDivTable(billStatus) {
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'], function (index, value) {
        if (value.isShow != 0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var disableButtonIds = "";
    switch (billStatus) {
        case "-1" :
            disableButtonIds = ["PIDtl_save", "PIDtl_cancel", "PIDtl_findRetrunno", "PIDtl_findshopMessage", "PIDtl_end"];
            break;
        case "0" :
            disableButtonIds = [];
            break;
        case "1":
            disableButtonIds = [];
            break;
        case "2" :
            disableButtonIds = ["PIDtl_save", "PIDtl_cancel", "PIDtl_findRetrunno", "PIDtl_findshopMessage", "PIDtl_end"];
            break;
        case "3":
            disableButtonIds = [];
            break;
        default:
            disableButtonIds = [];
    }
    //根据单据状态disable按钮
    $.each(privilegeMap['button'], function (index, value) {
        if ($.inArray(value.privilegeId, disableButtonIds) != -1) {
            $("#" + value.privilegeId).attr({"disabled": "disabled"});
        } else {
            $("#" + value.privilegeId).removeAttr("disabled");
        }
    });
}

function doPrintSanLian() {
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type: "PI",
            ruleReceipt: "SanLian"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setSanLian('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function setSanLian(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageSanLian.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogSanLian").html(print.printTableTh);
                var printCode = print.printCode;
                var printCodeArray = printCode.split(",");
                for (var i = 0; i < printCodeArray.length; i++) {
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#" + plp).text(message);
                }
                var tbodyCont = "";
                for (var a = 0; a < contDel.length; a++) {
                    var del = contDel[a];
                    var printTableCode = print.printTableCode.split(",");
                    tbodyCont += " <tr style='border-top:1px ;padding-top:5px;'>";
                    for (var b = 0; b < printTableCode.length; b++) {
                        if (printTableCode[b] == "styleId" || printTableCode[b] == "styleName" || printTableCode[b] == "colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else if (printParameter.sizeArrySanLian.indexOf(printTableCode[b]) != -1) {
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else {
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont += "</tr>"
                }
                $("#loadtabSanLian").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100, 1, printParameter.receiptWidthSanLian, printParameter.receiptheightSanLian, $("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

/**
 * A4打印
 * */
function doPrintA4() {

    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type: "SR",
            ruleReceipt: "A4"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setA4('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function setA4(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageA4.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogSanLian").html(print.printTableTh);
                var printCode = print.printCode;
                var printCodeArray = printCode.split(",");
                for (var i = 0; i < printCodeArray.length; i++) {
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#" + plp).text(message);
                }
                var tbodyCont = "";
                for (var a = 0; a < contDel.length; a++) {
                    var del = contDel[a];
                    var printTableCode = print.printTableCode.split(",");
                    tbodyCont += " <tr style='border-top:1px ;padding-top:5px;'>";
                    for (var b = 0; b < printTableCode.length; b++) {
                        if (printTableCode[b] == "styleId" || printTableCode[b] == "styleName" || printTableCode[b] == "colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else if (printParameter.sizeArry.indexOf(printTableCode[b]) != -1) {
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else if (printTableCode[b] == "other") {
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else {
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont += "</tr>"
                }
                $("#loadtabA4").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100, 1, printParameter.receiptWidthA4, printParameter.receiptheightSanLian, $("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
