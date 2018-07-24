$(function () {
    initTree();
    initSearchGridsku();
    initSearchGridcode();
    initSearchGridstyle();
});

//初始化树形结构
function initTree() {
    $('#jstree').on("changed.jstree", function (e, data) {
        if (data.selected.length) {
            // 点击节点，显示节点信息
            var nodeId = data.node.id;
            console.info(nodeId);
            console.info(data);
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
            initSearchGridsku(nodeId);
            initSearchGridcode(nodeId);
            initSearchGridstyle(nodeId);
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
        bootbox.alert("请先选择上级公司");
    }
}

function initSearchGridsku(nodeId) {
    $("#gridsku").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbysku.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'newRmId', label: 'sku'},
            {name: 'newRmId', label: '款号'},
            {name: 'newRmId', label: '款名'},
            {name: 'newRmId', label: '颜色'},
            {name: 'newRmId', label: '尺码'},
            {name: 'newRmId', label: '仓库'},
            {name: 'newRmId', label: '库位'},
            {name: 'totQty', label: '库存数量'},
            ],
        viewrecords: true,
        autowidth: true,
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
        sortorder: "desc"

    });
}
function initSearchGridcode(nodeId) {
    $("#gridcode").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbycode.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '唯一码', sortable: true, width: 45},
            {name: 'newRmId', label: 'sku'},
            {name: 'newRmId', label: '款号'},
            {name: 'newRmId', label: '款名'},
            {name: 'newRmId', label: '颜色'},
            {name: 'newRmId', label: '尺码'},
            {name: 'newRmId', label: '仓库'},
            {name: 'newRmId', label: '库位'},
            {name: 'totQty', label: '库存数量'}

        ],
        viewrecords: true,
        autowidth: true,
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
        sortorder: "desc"
    });
}
function initSearchGridstyle(nodeId) {
    $("#gridstyle").jqGrid({
        height: "auto",
        url: basePath + "/sys/repositoryController/findbystyle.do?rmId="+nodeId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '款号', sortable: true},

            {name: 'billNo', label: '款名', sortable: true},
            {name: 'billNo', label: '库存数量', sortable: true},
            {name: 'billNo', label: '仓库', sortable: true},
            {name: 'billNo', label: '库位', sortable: true}

        ],
        viewrecords: true,
        autowidth: true,
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
        sortorder: "desc"

    });
}

	
	