
   $(function(){
       $(".selectpicker").selectpicker({
           noneSelectedText : '--请选择--'//默认显示内容
       });
       initMultiSelect();
    	initKendoUIGrid();
    	$(".k-dropdown").css("width", "6em");
    	$(".k-grid-toolbar").css("display","none");//隐藏toolbar

    });
   function initMultiSelect(){
        /*$("#filter_in_destid").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
               }
           }
        });*/
       $.ajax({
           url:basePath + "/sys/warehouse/list.do?filter_INI_type=4,9",
           cache: false,
           async: false,
           type: "POST",
           success: function (data, textStatus) {
               $("#filter_in_destid").empty();
               var json = data;
               for (var i = 0; i < json.length; i++) {
                   $("#filter_in_destid").append("<option value='" + json[i].code + "'>"  + json[i].name + "</option>");
               }
           }
       });
        /*$("#filter_in_origid").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
               }
           }
        });*/
       $.ajax({
           url:basePath + "/sys/warehouse/list.do?filter_INI_type=4,9",
           cache: false,
           async: false,
           type: "POST",
           success: function (data, textStatus) {
               $("#filter_in_origid").empty();
               var json = data;
               for (var i = 0; i < json.length; i++) {
                   $("#filter_in_origid").append("<option value='" + json[i].code + "'>"  + json[i].name + "</option>");
               }
           }
       });
        /*$("#filter_in_destUnitId").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_INI_type=0,1,2"
               }
           }
        });*/
       $.ajax({
           url:basePath + "/sys/warehouse/list.do?filter_INI_type=0,1,2",
           cache: false,
           async: false,
           type: "POST",
           success: function (data, textStatus) {
               $("#filter_in_destUnitId").empty();
               var json = data;
               for (var i = 0; i < json.length; i++) {
                   $("#filter_in_destUnitId").append("<option value='" + json[i].code + "'>"  + json[i].name + "</option>");
               }
           }
       });
       $('.selectpicker').selectpicker('refresh');
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
    function initKendoUIGrid(){
    	
    	$("#searchGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                 fileName: "入库明细统计.xlsx",
                 proxyURL: basePath + "/search/detailInboundViewSearch/export.do",
                 filterable: true
            },
            excelExport: function(e) {
                debugger;
                var sheet = e.workbook.sheets[0];
                var tokenTemplate = kendo.template(this.columns[4].template);
                var destTemplate = kendo.template(this.columns[6].template);
                var destUnitTemplate = kendo.template(this.columns[7].template);
                var origTemplate = kendo.template(this.columns[8].template);
                var diffTemplate = kendo.template(this.columns[15].template);
                var rowIndex =1;
                var groupNum = this.dataSource._group.length;
                for (var i = 1; i < sheet.rows.length; i++) {
                    var row = sheet.rows[i];
                    if(row.cells[3+groupNum] && row.cells[5+groupNum] && row.cells[6+groupNum] && row.cells[7+groupNum]){
                        var gridRow = $("#searchGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
                        var dataItem = {
                            token: row.cells[4+groupNum].value,
                            destid:row.cells[6+groupNum].value,
                            destUnitId:row.cells[7+groupNum].value,
                            origid:row.cells[8+groupNum].value,
                            destName:gridRow.destName,
                            destUnitName:gridRow.destUnitName,
                            origName:gridRow.origName,
                            billNo:gridRow.billNo,
                            qty:gridRow.qty,
                            billQty:gridRow.billQty
                        };
                        row.cells[4+groupNum].value = tokenTemplate(dataItem);
                        row.cells[6+groupNum].value = destTemplate(dataItem);
                        row.cells[7+groupNum].value = destUnitTemplate(dataItem);
                        row.cells[8+groupNum].value = origTemplate(dataItem);
                        row.cells[15+groupNum].value = diffTemplate(dataItem);
                        rowIndex++;
                    }

                }
            },
            dataSource: {
            	schema : {  
                    total : "total",  
                    model : {  
                      
                        fields: {
                        	billDate: { type: "date" },
                            billNo: { type: "string" },
                            taskId:{type:"string"},
                            token:{ type: "number"},
                            deviceId: { type: "string" },
                            origid: { type: "string" },
                            destid:{ type: "string" },
                            destUnitId:{ type: "string" },
                            origName:{type:"string"},
                            destName:{ type : "string" },
                            destUnitName:{ type: "string" },
                            styleId: { type: "string" },                           
                            styleName: { type: "string" },
                            colorId: { type: "string" },
                            sizeId: { type: "string" },
                            qty: { type: "number" },
                            billQty: { type: "number" },
                            diffQty: { type: "number" },
                            price: { type: "number" }
                        }
                    }, 
                    data : "data",  
                    groups : "data"  
                },  
               
                transport: {
                    read: {
                        url: basePath + "/search/detailInboundViewSearch/list.do",
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    },
                    parameterMap : function(options) {  
                        return JSON.stringify(options);  
                    }  
                },
                sort:[{field: "billDate", dir: "desc"}],
                pageSize: 100.0,
                serverSorting : true, 
                serverPaging : true,  
                serverGrouping : false ,
                serverFiltering : true,  
                aggregate: [
                           
                            { field: "qty", aggregate: "sum" },   
                            { field: "styleId", aggregate: "count" },   
                            { field: "price", aggregate: "average" },
                            
                        ]

               
            },
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            rowNumber: true,
            pageable: {
            	input : true, 
            	buttonCount: 5,
            	pageSize: 100.0,
                pageSizes : [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            group: onGrouping,
            columnMenu: true,
            filterable: {
                extra:false 
            },
            //selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,

            columns: [
                {
                    title: "日期",
                    field: "billDate",
                    width: "200px",
                    aggregates: [ "count"],
                    filterable: {
                    	extra:true,
                    	ui: function (element) {
                            element.kendoDatePicker({
                                format: "yyyy-MM-dd",
                                culture:"zh-CN"
                            });
                        }
                       
                    },
                    
                    format: "{0:yyyy-MM-dd HH:mm:ss}",
                    groupHeaderTemplate: function(data) {
                   
                      var totQty = data.aggregates.qty.sum;
                        var value = kendo.toString(data.value, 'yyyy-MM-dd HH:mm:ss');
                        var avgPrice = data.aggregates.price.average;
                      return "日期:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }
                },
                {
                    title: "任务号",
                    field: "taskId",
                    width: "250px",
                    aggregates: [ "count"],
                    groupHeaderTemplate: function(data) {
                        
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "任务号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                      }
                },
                {
                    title: "单号",
                    field: "billNo",
                    width: "250px",
                    aggregates: [ "count"],
                    groupHeaderTemplate: function(data) {
                        debugger;
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "单号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                      }
                },
                {
                    field: "sku",
                    title: "SKU",
                    width:"150px"
                },
                {
                    field: "token",
                    title: "入库方式",
                    width:"150px",
                   template:function(data){
                        var tokenName="";
                        switch (data.token){
                            case 8: tokenName=  "仓库入库"; break;
                            case 23: tokenName=  "退货入库"; break;
                            case 25: tokenName=  "调拨入库"; break;
                            case 29: tokenName=  "仓库调整入库"; break;
                            case 14: tokenName=  "门店入库"; break;
                            case 17: tokenName=  "门店销售退货入库"; break;
                            case 19: tokenName=  "门店调拨入库"; break;
                            case 31: tokenName=  "门店调整入库"; break;
                            default :tokenName= ""; break;
                        }
                        return kendo.htmlEncode(tokenName);
                    },

                    filterable:false,
                    groupHeaderTemplate: function(data) {
                        var tokenName="";
                        switch (data.value){
                            case 8: tokenName=  "仓库入库"; break;
                            case 23: tokenName=  "退货入库"; break;
                            case 25: tokenName=  "调拨入库"; break;
                            case 29: tokenName=  "仓库调整入库"; break;
                            case 14: tokenName=  "门店入库"; break;
                            case 17: tokenName=  "门店销售退货"; break;
                            case 19: tokenName=  "门店调拨入库"; break;
                            case 31: tokenName=  "门店调整入库"; break;
                            default :tokenName= ""; break;
                        }
                        var totQty = data.aggregates.qty.sum;
                        var avgPrice = data.aggregates.price.average;
                        return "入库方式:"+tokenName +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }
                },
               
                {
                    field: "deviceId",
                    title: "设备号",
                    width:"110px",
                    width:"110px"
                },

                {
                    field:"destId",
                    title:"收货仓店",
                    width:"180px",
                    template:function(data) {
                        if(data.destName){
                            return "["+data.destid+"]"+data.destName;
                        }else{
                            if(data.destid){
                                return "["+data.destid+"]";
                            }else{
                                return "";
                            }

                        }
                    }
                },
                {
                    field:"origUnitId",
                    title:"发货方",
                    width:"180px",
                    template:function(data) {
                        if(data.destUnitName){
                            return "["+data.destUnitId+"]"+data.destUnitName;
                        }else{
                            if(data.destUnitId){
                                return "["+data.destUnitId+"]";
                            }else{
                                return "";
                            }

                        }
                    }
                },
                {
                    field:"origId",
                    title:"发货仓店",
                    width:"180px",
                    template:function(data) {
                        if(data.origName){
                            return "["+data.origid+"]"+data.origName;
                        }else{
                            if(data.origid){
                                return "["+data.origid+"]";
                            }else{
                                return "";
                            }
                        }
                    }
                },


                { field:"styleId",title:"款号",width:"140px",
                	aggregates: ["count"],
                	 groupHeaderTemplate: function(data) {
                         
                         var totQty = data.aggregates.qty.sum;
                         var value = data.value;
                         var avgPrice = data.aggregates.price.average;
                         return "款号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                       }},
                { field:"styleName",title:"款名",width:"140px"},
                { field:"colorId",title:"色号",width:"80px"},
                { field:"sizeId",title:"尺号",width:"80px"},
                { field:"qty",title:"数量",width:"80px",groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                { field:"billQty",title:"单据数量",width:"110px",groupable: false,groupable: false},
                { field:"diffQty",title:"差异数量",width:"110px",groupable: false,groupable: false,
                    template:function(data) {
                        if(data.billNo){
                            return data.billQty-data.qty;
                        }else{
                            return 0;
                        }
                    }
                },
                { field:"price",title:"吊牌价",width:"110px",groupable: false,aggregates: ["average"]}
               
               
            ]

        });

    }

    function onGrouping(arg) {
/*
        kendoConsole.log("Group on " + kendo.stringify(arg.groups));
*/
    }
   