
   $(function(){
        initMultiSelect();
    	initKendoUIGrid();
    	$(".k-dropdown").css("width", "6em");
    	$(".k-grid-toolbar").css("display","none");//隐藏toolbar

    });
       function initMultiSelect(){
           $("#filter_in_unit2Id").kendoMultiSelect({
               dataTextField: "name",
               dataValueField: "code",
               height: 400,
               suggest: true,
               dataSource: {
                   transport: {
                       read:  basePath + "/sys/warehouse/list.do?filter_INI_type=0,1,2"
                   }
               }
           });
           $("#filter_in_destId").kendoMultiSelect({
               dataTextField: "name",
               dataValueField: "code",
               height: 400,
               suggest: true,
               dataSource: {
                   transport: {
                       read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
                   }
               }
           })

       }
    function refresh(){
    	resetData();
    }
    function resetData(){
    	var gridData = $("#searchGrid").data("kendoGrid");
        gridData.dataSource.filter({});
    }
    function exportExcel(){
    	$(".k-grid-excel").click();
    }
    function showAdvSearchPanel(){
    	 $("#searchPanel").slideToggle("fast");
    }
    function search(){
    	var gridData = $("#searchGrid").data("kendoGrid");
    	var filters = serializeToFilter($("#searchForm"));
    	console.log(filters);

    	gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    	
    }

   function expandGroup(){
       var grid = $("#searchGrid").data("kendoGrid");
      grid.expandRow(".k-grouping-row");
   }

   function collapseGroup(){
       var grid = $("#searchGrid").data("kendoGrid");
       grid.collapseRow(".k-grouping-row");
   }
    function initKendoUIGrid(){
        $("#searchGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                allPages: true
            },
            dataSource: {
                schema : {
                    total : "total",
                    model : {
                        fields: {
                            id:{ type:"string"},
                            cartonId:{ type:"string"},
                            scanDate:{ type:"string"},
                            scanTime:{ type:"string"},
                            deviceId: { type: "string" },
                            qty: { type :"number"},
                            preQty: { type :"number"},
                            sizeId:{type:"string"},
                            sku: { type: "string" },
                            styleId: { type: "string" },
                            styleName: { type: "string" },
                            colorId: { type: "string" },
                            destId: { type: "string" },
                            origId: { type: "string" },
                            token: { type :"number"},
                            taskId: { type: "string" },
                            billNo:{type:"string"}
                        }
                    },
                    data : "data"
                },
                group: {
                    field: "scanDate"
                },
                transport: {
                    read: {
                        url: basePath + "/search/boxDtlSearch/list.do",
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    },
                    parameterMap : function(options) {
                        return JSON.stringify(options);
                    }
                },
                sort:[{field: "scanTime", dir: "desc"}],
                pageSize: 500.0,
                serverSorting : true,
                serverPaging : true,
                serverGrouping : false ,
                serverFiltering : true,
                aggregate: [

                    { field: "qty", aggregate: "sum" },
                    { field: "styleId", aggregate: "count" },

                ]
            },
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            pageable: {
                input : true,
                buttonCount: 5,
                pageSize: 500.0,
                pageSizes : [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            columnMenu: true,
            filterable: {
                extra:false
            },
            reorderable: true,
            resizable: false,
            scrollable: true,
            columns: [

                { field:"scanDate",title:"装箱日期",width:"120px"},
                { field:"scanTime",title:"装箱时间",width:"220px"},
                { field:"cartonId",title:"箱号",width:"200px"},
                { field:"sku",title:"SKU",width:"200px"},
                { field:"styleId",title:"款号",width:"150px"},
                { field:"styleName",title:"款名",width:"120px"},
                { field:"colorId",title:"色号",width:"80px"},
                { field:"sizeId",title:"尺号",width:"80px"},
                { field:"qty",title:"数量",width:"80px",groupable: false, aggregates: ["sum"], footerTemplate: "#=sum#"},
                { field:"preQty",title:"预计数量",width:"120px"},
                { field:"taskId",title:"任务号",width:"250px"},
                { field:"billNo",title:"ERP单号",width:"200px"},
                {
                    field: "token",
                    title: "入库方式",
                    width: "150px",
                    template: function (data) {
                        var tokenName = "";
                        switch (data.token) {
                            case 8:
                                tokenName = "仓库入库";break;
                            case 10:
                                tokenName = "仓库出库";break;
                            case 23:
                                tokenName = "退货入库";break;
                            case 24:
                                tokenName = "调拨出库";break;
                            case 25:
                                tokenName = "调拨入库";break;
                            case 26:
                                tokenName = "退货出库";break;
                            default :
                                tokenName = "";
                                break;
                        }
                        return kendo.htmlEncode(tokenName);
                    }
                },
                { field:"origId",title:"发货仓店",width:"180px",
                    template:function(data) {
                        if(data.destName){
                            return "["+data.origId+"]"+data.destName;
                        }else{
                            if(data.origId){
                                return "["+data.origId+"]";
                            }else{
                                return "";
                            }

                        }
                    }
                },
                { field:"destId",title:"收货仓店",width:"80px",width:"180px",
                    template:function(data) {
                        if(data.destName){
                            return "["+data.destId+"]"+data.destName;
                        }else{
                            if(data.destId){
                                return "["+data.destId+"]";
                            }else{
                                return "";
                            }

                        }
                    }
                }

            ]
        });
    }


   