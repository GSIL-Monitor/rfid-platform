<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-code-show-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                唯一码明细
            </div>
            <div class="modal-content">
                <div class="hr hr4"></div>
                <table id="CodesShowDetailgrid"></table>
                <div id="Codes-Show-Detail-grid-pager"></div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        loadCodesShowDetailgridTable();
    });
    function loadCodesShowDetailgridTable() {
        $("#CodesShowDetailgrid").jqGrid({
            height: 400,
            datatype: "local",
            mtype: 'POST',
            colModel: [
                {name: 'uniqueCodes', label: '唯一码',width: 100},
                {name: 'styleId', label: '款号',width: 60},
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 60},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 60},
                {name: 'sku', label: 'SKU', width: 60},
                {name: 'sku', label: '仓库', width: 60},
                {name: 'outOrInQty', label: '出库数量', width: 60},
                {name: 'thisQty', label: '本次数量', width: 60}


            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#Codes-Show-Detail-grid-pager',
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc",
            footerrow: true,
            cellsubmit: 'clientArray',
            afterEditCell: function (rowid, celname, value, iRow, iCol) {
                /* addDetailgridiRow = iRow;
                 addDetailgridiCol = iCol;*/
            },
            gridComplete: function () {
                setFooterData();
            },
            loadComplete : function(){
                var table = this;
                setTimeout(function(){

                    //加载完成后，替换分页按钮图标
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0)
            }
        });
    }
    function updatePagerIcons(table) {
        //ui-icon ui-icon-circlesmall-minus
        var replacement =
            {
                'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
                'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
                'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
                'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
            };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
        });
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({
            container : 'body'
        });
        $(table).find('.ui-pg-div').tooltip({
            container : 'body'
        });
    }
</script>