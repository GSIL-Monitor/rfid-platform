  
   $(function(){
       initMultiSelect();
       initKendoUIGrid();
       $(".k-dropdown").css("width", "6em");
       $(".k-grid-toolbar").css("display","none");//隐藏toolbar
       $(".k-datepicker input").prop("readonly", true);

    });
   function initMultiSelect(){
       $("#filter_in_warehId").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4"
               }
           }
       })
   }
    function refresh(){
    	resetData();
    }
    function resetData(){
    	var gridData = $("#fittingGrid").data("kendoGrid");
        gridData.dataSource.filter({});
    }
    function exportExcel(){
    	$(".k-grid-excel").click();
    }
    function showAdvSearchPanel(){
    	 $("#searchPanel").slideToggle("fast");
    }
    function search(){
    	var gridData = $("#fittingGrid").data("kendoGrid");
    	var filters = serializeToFilter($("#searchForm"));
    	console.log(filters);
    	gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    	
    }
    function initKendoUIGrid(){
    	
    	$("#fittingGrid").kendoGrid({
    		toolbar: ["excel"],
            excel: {
                 allPages: true,
                 fileName: "试衣数据.xlsx"
            },

            dataSource: {
            	schema : {  
                    total : "total",  
                    model : {  
                      
                        fields: {

                            code: { type: "string" },
                            fittingDate: { type :"date"},
                            deviceId:{type:"string"},
                            storeName: { type: "string" },
                            styleId: { type: "string" },
                            styleName: { type: "string" },
                            clorId: { type: "string" },
                            clorName: { type: "string" },
                            sizeId: { type: "string" },
                            sizeName: { type: "string" },
                            price: { type: "number" }
                        }
                    }, 
                    data : "data",
                    groups : "data",
                    aggregates: "aggregates"
                },  
               
                transport: {
                    read: {
                        url: basePath + "/neoen/styleFitting/page.do",
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    },
                    parameterMap : function(options) {  
                        return JSON.stringify(options);  
                    }  
                },         
                pageSize: 500.0,
                serverSorting : true, 
                serverPaging : true,
                serverGrouping : true,
                serverFiltering : true

               
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
         //   selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,
            columns: [
                
                {
                    field:"storeName",
                    title:"仓店",
                    width:"180px"

                },
                {field: 'fittingDate', title: '试衣时间', editable: true, width: 180,
                    filterable: {
                        extra:true,
                        ui: function (element) {
                            element.kendoDatePicker({
                                format: "yyyy-MM-dd",
                                culture:"zh-CN"
                            });
                        }
                    },
                    format: "{0:yyyy-MM-dd HH:mm:ss}"},
                {
                    field:"code",
                    title:"EPC",
                    width:"180px"

                },
                {
                    field:"deviceId",
                    title:"设备号",
                    width:"180px"

                },
                { field:"styleId",title:"款号",width:"120px"},
                { field:"styleName",title:"款名",width:"120px"},
                { field:"colorId",title:"色号",width:"80px"},
                { field:"colorName",title:"颜色",width:"120px"},
                { field:"sizeId",title:"尺号",width:"80px"},
                { field:"sizeName",title:"尺码",width:"120px"},
                { field:"price",title:"吊牌价",width:"110px",groupable: false}
               
               
            ]
        });

    }

   