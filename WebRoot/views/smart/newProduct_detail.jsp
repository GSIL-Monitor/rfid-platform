<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
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
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">

                <div class="widget-header">
                    <h5 class="widget-title">基本信息</h5>
                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" onclick="javascript:history.back(-1);">
                            <i class="ace-icon fa fa-arrow-left"></i>
                            返回
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <form class="form-horizontal" role="form" id="editForm">
                <div class="form-group">
                <label class="col-xs-1 control-label" for="search_styleId">款号</label>
                <div class="col-xs-2">
                    <input class="form-control" id="search_styleId" name="filter_LIKES_styleId"
                           type="text" disabled value=${newProduct.styleId} />
                </div>

                    <label class="col-xs-1 control-label" for="search_name">款名</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_name" name="filter_LIKES_name"
                               type="text" disabled value=${newProduct.name} />
                    </div>

                    <label class="col-xs-1 control-label" for="search_colorIds">颜色</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_colorIds" name="filter_LIKES_colorIds"
                               type="text" disabled value=${newProduct.colorIds} />
                    </div>

                    <label class="col-xs-1 control-label" for="search_sizeIds">尺码</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_sizeIds" name="filter_LIKES_sizeIds"
                               type="text" disabled value=${newProduct.sizeIds} />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-1 control-label" for="search_price">价格</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_price" name="filter_LIKES_price"
                               type="text" disabled value=${newProduct.price} />
                    </div>

                    <label class="col-xs-1 control-label" for="search_seqNo">序号</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_seqNo" name="filter_LIKES_seqNo"
                               type="text" disabled value=${newProduct.seqNo} />
                    </div>

                    <label class="col-xs-1 control-label" for="search_isDet">是否推荐</label>
                    <div class="col-xs-2">
                        <input class="form-control" id="search_isDet" name="filter_LIKES_isDet"
                               type="text" disabled value=${newProduct.isDet} />
                    </div>
                </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">
                <div class="widget-header">
                    <h5 class="widget-title">图片</h5>
                </div>
            </div>
                <ul class="ace-thumbnails clearfix" id="gallery">
                </ul>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-body" align="center">
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>

<script type="text/javascript">
    $(function() {
        initImg();
    })
    function initImg(){
        var images="${newProduct.url}";
        if(images!=""){
            var n=images.split(',');
            var ul=document.getElementById("gallery");
            for(var i=0;i< n.length;i++){
                var li=document.createElement("li");
                var img=document.createElement("img");
                ul.appendChild(li);
                li.appendChild(img);
                img.setAttribute("class","img-responsive");
                img.setAttribute("width","150px");
              //  img.setAttribute("height","250px");
                img.setAttribute("src","<%=basePath%>/mirror/newProduct/"+n[i]);
            }
        }

    }
</script>
<script src="<%=basePath%>/Olive/plugin/photo-gallery.js"></script>
</html>