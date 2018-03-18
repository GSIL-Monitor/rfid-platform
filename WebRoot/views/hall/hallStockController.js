$(function () {
    inisearchHallRoom();
    inisearchHallFloor();
    iniStockGrid();
    console.log("用户:" + "<%=s.getAttribute('')%>")
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);
});

function refresh() {
    resetData();
}

function exportExcel() {
    $(".k-grid-excel").click();
}

function resetData() {
    var gridData = $("#stockGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}

function search() {
    var gridData = $("#stockGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}
function showphotoes() {
    var row = $("#stockGrid").data("kendoGrid").select();
    var data = $("#stockGrid").data("kendoGrid").dataItem(row);
    if (data) {
        iniPhotoes(data.styleId, data.colorId);

    } else {
        bootbox.alert("请选择一项以查看图片");
    }
}


function iniPhotoes(styleId, colorId) {
    $("#thumbs").innerHTML = "";

    $.ajax({
        url: basePath + "/prod/photo/list.do?filter_EQS_styleId=" + styleId + "&filter_EQS_colorId=" + colorId,
        dataType: "json",
        type: "POST",
        async: false,
        success: function (data) {
            var json = data;
            var html = ""
            if (json.length > 0) {
                $("#thumbs").empty();
                $("div#galleryOverlay").remove();
                $(".k-list-container").remove();
                placeholder=null;
                for (var i = 0; i < json.length; i++) {
                    html += '<a href="'+basePath+'/product/photo' + json[i].src + '" style="background-image:url('+basePath+'/product/photo' + json[i].src + ')"></a>';
                }
                $("#thumbs").append(html);
                $("#thumbs a").touchTouch();
                $("#photo_modal").modal("show");
            } else {
                bootbox.alert("该样衣无展示图片");
            }
        }
    });
}
function closeShowDialog() {
    $("#photo_modal").modal("hide");
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function inisearchHallRoom() {
    $.ajax({
        url: basePath + "/hall/room/list.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            if (json) {
                for (var i = 0; i < json.length; i++) {
                    $("#filter_eq_ownerId").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                    $("#filter_eq_ownerId").trigger("chosen:updated");
                }
            }
        }
    });
}

function inisearchHallFloor() {
    $.ajax({
        url: basePath + "/hall/floor/list.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            if (json) {
                for (var i = 0; i < json.length; i++) {
                    if (json[i].code.length == 8) {
                        $("#filter_eq_floor").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                        $("#filter_eq_floor").trigger("chosen:updated");
                    }
                }
            }
        }
    });
}

function iniStockGrid() {
    $("#stockGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true,
            fileName: "样衣库存查询.xlsx",
            proxyURL: basePath + "/hall/hallStock/export.do",
            filterable: true
        },
        // excelExport: function(e) {
        //     var sheet = e.workbook.sheets[0];
        //     var statusTemplate = kendo.template(this.columns[0].template);
        //
        //     var rowIndex =1;
        //     var groupNum = this.dataSource._group.length;
        //     for (var i = 1; i < sheet.rows.length; i++) {
        //         var row = sheet.rows[i];
        //         if(row.cells[1+groupNum]){
        //             var gridRow = $("#stockGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
        //             var dataItem = {
        //                 inDate:row.cells[9+groupNum].value,
        //                 status: row.cells[0+groupNum].value,
        //             };
        //             row.cells[0+groupNum].value = statusTemplate(dataItem);
        //             rowIndex++;
        //         }
        //     }
        // },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        status: {type: "number"},           // 状态
                        styleId: {type: "string"},          // 款号
                        code: {type: "string"},             // 吊牌码
                        styleName: {type: "string"},        // 款名
                        colorId: {type: "string"},          // 颜色
                        sizeId: {type: "string"},           // 尺码
                        tagPrice: {type: "number"},         // 价格
                        remark: {type: "string"},           // 备注
                        ownerId: {type: "string"},          // 样衣间编号
                        unitName: {type: "string"},         // 样衣间名称
                        floor: {type: "string"},            // 库位编号
                        floorName: {type: "string"},          // 库位名称
                        inDate: {type: "date"},             //入库时间
                        bussinessOwnerId: {type: "string"}, // 借用人
                        businessDate: {type: "date"},       // 借用时间
                    }
                },
                "data": "data",
                "groups": "data"
            },

            transport: {
                read: {
                    url: basePath + "/hall/hallStock/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            sort: [{field: "code", dir: "desc"}],
            pageSize: 500.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: true,
            serverFiltering: true,
            aggregate: [
                {field: "code", aggregate: "count"},
            ]
        },
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 500.0,
            pageSizes: [100, 500, 1000, 2000, 5000]
        },

        groupable: true,
        group: onGrouping,
        columnMenu: true,
        filterable: {
            extra: false
        },
        selectable: "row",
        reorderable: true,
        resizable: false,
        scrollable: true,
        columns: [
            {
                field: "status", title: "状态", width: 100,
                template: function (data) {
                    var status = data.status;
                    var text = "";
                    if (status == 0) {
                        text += "未入库";
                    } else if (status == 1) {
                        text += "在库";
                    } else if (status == 2) {
                        text += "外借";
                    } else if (status == 3) {
                        text += "在途";
                    } else if (status == 4) {
                        text += "报损出库";
                    } else if (status == 5) {
                        text += "丢失";
                    } else if (status == 6) {
                        text += "已报损";
                    } else if (status == 7) {
                        text += "报损补标";
                    }
                    return text;
                },
                groupHeaderTemplate: function (data) {
                    var status = data.value;
                    var text = "";
                    if (status == 0) {
                        text += "未入库";
                    } else if (status == 1) {
                        text += "在库";
                    } else if (status == 2) {
                        text += "外借";
                    } else if (status == 3) {
                        text += "在途";
                    } else if (status == 4) {
                        text += "报损出库";
                    } else if (status == 5) {
                        text += "丢失";
                    } else if (status == 6) {
                        text += "已报损";
                    } else if (status == 7) {
                        text += "报损补标";
                    }
                    if (status != undefined) {
                        return "状态:" + text
                    }
                }
            },
            {field: "styleId", title: "款号", width: 150},
            {field: "code", title: "吊牌码", width: 200},
            {field: "styleName", title: "款名", width: 150, sortable: false, groupable: false, filterable: false},
            {field: "colorId", title: "颜色", width: 80},
            {field: "sizeId", title: "尺码", width: 80},
            {field: "tagPrice", title: "价格", width: 80},
            {field: "remark", title: "备注", width: 200, filterable: false, sortable: false, groupable: false},
            {field: "ownerId", title: "样衣间编号", width: 150},
            {field: "unitName", title: "样衣间名称", width: 150, sortable: false, groupable: false, filterable: false},
            {field: "floor", title: "库位编号", width: 150},
            {field: "floorName", title: "库位名称", width: 150, sortable: false, groupable: false, filterable: false},
            {
                field: "inDate", title: "入库时间", width: 200
                , aggregates: ["count"],
                filterable: {
                    extra: true,
                    ui: function (element) {
                        element.kendoDatePicker({
                            format: "yyyy-MM-dd",
                            culture: "zh-CN"
                        });
                    }
                },
                format: "{0:yyyy-MM-dd HH:mm:ss}"
            },

            {
                field: "businessDate", title: "最近操作时间", width: 200,
                aggregates: ["count"],
                filterable: {
                    extra: true,
                    ui: function (element) {
                        element.kendoDatePicker({
                            format: "yyyy-MM-dd",
                            culture: "zh_CN"
                        });
                    }
                },
                format: "{0:yyyy-MM-dd HH:mm:ss}"
            },
            {field: "businessOwnerId", title: "操作方", width: 100},

        ]
    });
}

function confirm() {
    var row = $("#stockGrid").data("kendoGrid").select();
    var data = $("#stockGrid").data("kendoGrid").dataItem(row);
    if (data) {
        if (data.ownerId == userManage) {
            if (data.status == 4 || data.status == 2) {
                bootbox.confirm({
                    title: "申请报损",
                    message: "是否确认报损?",
                    buttons: {
                        confirm: {
                            label: "确认",
                            className: "btn-primary"
                        },
                        cancel: {
                            label: "取消",
                            // className: "btn-inverse"
                        },
                    },
                    callback: function (result) {
                        if (result) {
                            $.post(
                                basePath + "/hall/hallStock/confirmDamage.do?code=" + data.code,
                                function (result) {
                                    if (result.success == true) {
                                        $("#notification").data('kendoNotification').showText(result.msg, 'success');
                                        resetData();
                                    } else {
                                        bootbox.alert("申请失败");
                                    }
                                }
                            )
                        }
                    }
                });
            } else {
                bootbox.alert("该状态下不能确认报损");
            }
        } else {
            bootbox.alert("不能补报其他样衣间");
        }
    } else {
        bootbox.alert("请先选择一条记录确认报损");
    }
}
//补标报损
function badDebt() {
    console.log("usercode" + userCode + ",owner:" + userManage);
    var row = $("#stockGrid").data("kendoGrid").select();
    var data = $("#stockGrid").data("kendoGrid").dataItem(row);
    if (data) {
        if (data.ownerId == userManage) {
           /* if(rows[0].status==7){
                $.messager.alert('提示','已经报损补标');

            }else if(rows[0].status ===4 || rows[0].status === 2 || rows[0].status === 1){
                $.messager.confirm('警告', '确认报损补标?', function(r){
                    if (r){

                        $.ajax({
                            url : cs.baseUrl()+"/sampleAction!ComfirmDamage.action",
                            data : {
                                code:rows[0].code
                            },
                            dataType : "json",
                            success : function(response) {
                                _search();
                            }
                        });




                    }
                });


            }*/
           if(data.status===7){
               bootbox.alert("已经报损补标");
           }else if(data.status === 4||data.status === 2||data.status === 1) {
                bootbox.confirm({
                    title: "确认补标",
                    message: "是否确认补标?",
                    buttons: {
                        confirm: {
                            label: "确认",
                            className: "btn-primary"
                        },
                        cancel: {
                            label: "取消",
                            // className: "btn-inverse"
                        },
                    },
                    callback: function (result) {
                        if (result) {
                            $.post(
                                basePath + "/hall/hallStock/ApplyDamage.do?code=" + data.code,
                                function (result) {
                                    if (result.success == true) {
                                        $("#notification").data('kendoNotification').showText(result.msg, 'success');
                                        resetData();

                                    } else {
                                        bootbox.alert("申请失败");
                                    }
                                }
                            )
                        }
                    }
                });
            } else {
                bootbox.alert("该状态下不能报损补标");
            }
        } else {
            bootbox.alert("不能补报其他样衣间！");
        }
    } else {
        bootbox.alert("请先选择一条记录报损补标");
    }
}
function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}
