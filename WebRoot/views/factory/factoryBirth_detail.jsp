<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="row">
            <div class="col-xs-12">

                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">基本信息</h5>
                        <div class="widget-toolbar no-border">
                            <button class="btn btn-xs bigger btn-yellow dropdown-toggle" onclick="javascript:history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main padding-12">

                            <form class="form-horizontal" role="form" >
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">批次号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                               type="text"disabled value='${init.billNo}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billDate">导入时间</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billDate" name="filter_LIKES_billDate"
                                               type="text"disabled value="${init.billDate}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_fileName">导入文件名</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_fileName" name="filter_LIKES_fileName"
                                               type="text" disabled value='${init.fileName}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totSku">SKU数</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="filter_LIKES_totSku"
                                               type="text" disabled value='${init.totSku}' />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value='${init.totQty}' />
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<div class="hr hr4"></div>

<div class="row">
    <div class="col-sm-12">
        <table id="detailgrid"></table>
    </div>

</div>
</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initGrid();
    });
    function initGrid(){
        var billNo="${init.billNo}";
        var factoryBillNo="${init.factoryBillNo}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/factory/birth/detailList.do?billNo="+billNo+"&factory="+factoryBillNo,
            datatype: "json",
            colModel: [
                {name: 'groupId', label: '组别', editable: true, width: 40},
                {name: 'billNo', label: '办单号', editable: true, width: 40},
                {name: 'operator', label: '开单人', editable: true, width: 40},
                {name: 'billDate', label: '接单时间', editable: true, width: 40},
                {name: 'endDate', label: '办期', editable: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 40},
                {name: 'styleName', label: '客户款型', editable: true, width: 40},
                {name: 'colorId', label: '颜色', editable: true, width: 40},
                {name: 'colorName', label: '客户颜色', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 30},
                {name: 'qty', label: '数量', editable: true, width: 30},
                {name: 'customerId', label: '客户', editable: true, width: 40},
                {name: 'type', label: '办类', editable: true, width: 40},
                {name: 'washType', label: '洗水类型', editable: true, width: 40},
                {name: 'shirtType', label: '衫型', editable: true, width: 40},
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'styleId',
            sortorder: "asc",

        });


    }

</script>
</body>
</html>