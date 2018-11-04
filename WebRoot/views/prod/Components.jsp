<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<style>
    .icon {
        width: 1em; height: 1em;
        vertical-align: -0.15em;
        fill: currentColor;
        overflow: hidden;
    }
    .componentsList{
        margin-left: 0px;
        list-style: none;
        padding: 0;
        overflow: hidden;
        position: relative;
        background: #fff;
        margin-bottom: 0;
    }
    .componentsList li{
        height: 40px;
        position: relative;
        line-height: 40px;
        background: #fff;
        text-align: center;
        width: 20%;
        float: left;
        color: #666;
        font-size: 1em;
        cursor: pointer;
        border: 1px solid #ececec;
    }
    .componentsList li svg{
        font-size: 1.8em;
        margin-right: 0.5em;
    }
    .top-content{
        padding: 2%;
        border: 1px solid #eeeeee;
    }
    #componentsTable{
        width: 100%;
        border: 1px solid #eeeeee;
        margin-top: 1%;
        text-align: center;
    }
    #componentsTable tr th{
        text-align: center;
        color: #666;
    }
    #componentsTable tr td input{
        width:100%;
    }
    #componentsTable tr td button{
        margin: 2%;
        border: 0;
    }
    #searchresult
    {
        width: 25%;
        position: absolute;
        z-index: 1;
        overflow: hidden;
        left: 30%;
        top: 10%;
        background: #E0E0E0;
        border-top: none;

    }
    .line
    {
        font-size: 15px;
        background: #E0E0E0;
        width: 100%;
        padding:0px;
    }
    .hover
    {
        background: #007ab8;
        width: 100%;
        color: #fff;
    }
    .std
    {
        width: 30%;
    }
    #aa{
        width: 100%;
    }
</style>
<div id="edit_components_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" style="width: 40%;">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close"  onclick="closeComponentsDialog()" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                成分编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <div class="top-content">
                    <div id="components_div">
                        <ul class="componentsList">

                        </ul>
                    </div>
                </div>
                <form class="form-horizontal" role="form" id="ComponentsForm">
                    <div class="form-group">
                        <table id="componentsTable">
                            <tr>
                                <th>成分</th>
                                <th>含量</th>
                                <th>操作</th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" id="components0" name="components" width="40%"/>
                                    <div id="searchresult" style="display: none;">
                                    </div>
                                </td>
                                <td>
                                    <input type="text" id="componentsRemark0" width="40%"/>
                                </td>

                            </tr>
                        </table>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer" style="text-align: left">
            <button type="button"  class="btn btn-primary" onclick="addComponent()">添加</button>
            <div style="text-align: right;float: right;">
                <a href="#" class="btn" onclick="closeComponentsDialog()">关闭</a>

                <button type="button"  class="btn btn-primary" onclick="saveComponents()">保存</button>
            </div>
        </div>
    </div>
</div>
<script>
    var componenthtml = "";//成分大类
    var count = 0;//成分个数计数，方便取值
    var index = 0;//只拉取一次原成分
    var operate = 0;//判断用户是否进行操作
    var componentArray = new Array();//成分数据
    $(function(){
        //拉取原成分

        $("#edit_components_dialog").on('show.bs.modal', function () {
            initeditComponentFormValid();
            if(index ==0){
                $.ajax({
                    url: basePath + "/prod/components/findByStyleId.do?filter_EQS_styleId="+$("#form_styleId").val(),
                    dataType: 'json',
                    async: false,//同步
                    success: function (result) {
                        $.each(result,function(index,value){
                            $("#components0").val(value.componentsName);
                            $("#componentsRemark0").val(value.cremark);
                            addComponent();
                        });
                        index++;
                    }
                });
            }
        });
        $.ajax({
            url: basePath + "/prod/components/searchByDeep.do?deep=0",
            dataType: 'json',
            async: false,//同步
            success: function (result) {
                $.each(result,function(index,value){
                    componenthtml  +="<li id="+result[index].code+">"
                        /*+"<svg class='icon' aria-hidden='true'>"
                        +"<use xlink:href='#icon-"+result[index].iconCode+"'></use>"
                        +"</svg>"*/
                        +"<span>"+result[index].name+"</span>"
                        +"</li>";
                });
                $(".componentsList").html(componenthtml);
                //默认第一个成分
                $(".componentsList").find("li").eq(0).addClass("active");
                //赋值唯一name做校验
                $("#componentsRemark0").attr("name",$(".componentsList").find("li").eq(0).attr("id"));
                //绑定事件
                $(".componentsList").find("li").each(function () {
                    //有active变色
                    if ($(this).hasClass("active")) {
                        $(this).css("background", "#307ecc");
                        $(this).css("color","#fff");
                    }
                    //点击方法
                    $(this).bind("click",function () {
                        //点当前选择无效果
                        if($(this).hasClass("active")){
                            return;
                        }
                        else {
                            removeActive();
                            $(this).css("background","#307ecc");
                            $(this).css("color","#fff");
                            $(this).addClass("active");
                            $("#componentsRemark0").attr("name",$(this).attr("id"));
                        }
                        //重置校验器
                        $("#ComponentsForm").data('bootstrapValidator').destroy();
                        $('#ComponentsForm').data('bootstrapValidator', null);
                        initeditComponentFormValid();
                    });
                });
            }
        });
        //绑定模糊查询
        $("#components0").keyup(function (evt) {
            var parentCode = $("#componentsRemark0").attr("name");
            var term = $("#components0").val();
            ChangeCoords(); //控制查询结果div坐标
            var k = window.event ? evt.keyCode : evt.which;
            //不为空 && 不为上箭头或下箭头或回车
            if ($("#components0").val() != "" && k != 38 && k != 40 && k != 13) {
                $.ajax({
                    url: basePath + "/prod/components/findByRemark.do",
                    dataType: 'json',
                    data: {term:term,parentCode:parentCode},
                    success: function (data) {
                        console.log(data);
                        if (data.length > 0) {
                            var layer = "";
                            layer = "<table id='aa'>";
                            $.each(data, function (idx, item) {
                                layer += "<tr class='line'><td class='std'>" + item.name + "</td></tr>";
                            });
                            layer += "</table>";

                            //将结果添加到div中
                            $("#searchresult").empty();
                            $("#searchresult").append(layer);
                            $(".line:first").addClass("hover");
                            $("#searchresult").css("display", "");
                            //鼠标移动事件

                            $(".line").hover(function () {
                                $(".line").removeClass("hover");
                                $(this).addClass("hover");
                            }, function () {
                                $(this).removeClass("hover");
                            });
                            //鼠标点击事件
                            $(".line").click(function () {
                                $("#components0").val($(this).text());
                                $("#searchresult").css("display", "none");
                            });
                        } else {
                            $("#searchresult").empty();
                            $("#searchresult").css("display", "none");
                        }
                    }
                });
            } else if (k == 38) {//上箭头
                $('#aa tr.hover').prev().addClass("hover");
                $('#aa tr.hover').next().removeClass("hover");
                $('#components0').val($('#aa tr.hover').text());
            } else if (k == 40) {//下箭头
                $('#aa tr.hover').next().addClass("hover");
                $('#aa tr.hover').prev().removeClass("hover");
                $('#components0').val($('#aa tr.hover').text());
            } else if (k == 13) {//回车
                $('#components0').val($('#aa tr.hover').text());
                $("#searchresult").empty();
                $("#searchresult").css("display", "none");
            }
            else {
                $("#searchresult").empty();
                $("#searchresult").css("display", "none");
            }
        });
        $("#searchresult").bind("mouseleave", function () {
            $("#searchresult").empty();
            $("#searchresult").css("display", "none");
        });

    });
    function ChangeCoords() {
        var left = $("#components0").position().left; //获取距离最左端的距离，像素，整型
        var top = $("#components0").position().top + 35; ; //获取距离最顶端的距离，像素，整型（20为搜索输入框的高度）
        $("#searchresult").css("left", left + "px"); //重新定义CSS属性
        $("#searchresult").css("top", top + "px"); //同上
    }
    function removeActive() {
        $(".componentsList").find("li").each(function () {
            if ($(this).hasClass("active")) {
                $(this).css("background","#fff");
                $(this).css("color","#666");
                $(this).removeClass("active");//移除active
            }
        });
    }
    function addComponent() {
        $('#ComponentsForm').data('bootstrapValidator').validate();
        if(!$('#ComponentsForm').data('bootstrapValidator').isValid()){
            return ;
        }
        $("#componentsTable").append("<tr id='flag"+count+"'>"
            +"<td>"
            +"<input type='text' id='components"+count+"' width='50%' value='"+$("#components0").val()+"' readonly/>"
            +"</td>"
            +"<td>"
            +"<input type='text' id='componentsRemark"+count+"' width='50%' value='"+$("#componentsRemark0").val()+"' readonly/>"
            +"</td>"
            +"<td>"
            +"<button type='button'  class='btn btn-primary' onclick='removeComponent("+count+")'>删除</button>"
            +"</td>"
            +"</tr>");
        //放进缓存
        componentArray.push($.trim($("#components0").val()+":"+$("#componentsRemark0").val())+"\n");
        //清空数据
        $("#components0").val("");
        $("#componentsRemark0").val("");
        count++;
    }
    function removeComponent(count) {
        $("#flag"+count).remove();
        componentArray.splice(count,1);
        operate++;
        //重置校验器
        $("#ComponentsForm").data('bootstrapValidator').destroy();
        $('#ComponentsForm').data('bootstrapValidator', null);
        initeditComponentFormValid();
    }
    function saveComponents() {
        $('#ComponentsForm').data('bootstrapValidator').validate();
        if(!$('#ComponentsForm').data('bootstrapValidator').isValid()){
            return ;
        }
        //去掉array的，
        $("#components").val(componentArray);
        var data = $("#components").val().replace(/[\,]/g,'');
        console.info(data);
        $("#components").val(data);
        operate = 0;//重置操作
        closeComponentsDialog();

    }
    function closeComponentsDialog(){
        if(operate != 0){
            bootbox.alert("删除成分请保存!");
        }
        else {
            $("#edit_components_dialog").modal('hide');
        }
    }
    $('#edit_components_dialog').on('hidden.bs.modal', function() {
        $("#ComponentsForm").data('bootstrapValidator').destroy();
        $('#ComponentsForm').data('bootstrapValidator', null);
        initeditComponentFormValid();
    });
    function initeditComponentFormValid() {
        $('#ComponentsForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function(validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function(result) {
                    if (result.success == true || result.success == 'true') {

                    } else {
                        $('#ComponentsForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                /*components: {
                    validators: {
                        notEmpty: {
                            message: '成分名称不能为空'
                        }
                    }
                },*/
                mianliao: {
                    validators: {
                        regexp: {
                            regexp: /^([1-9]\d?)$/,
                            message: '面料不允许百分之百'
                        }
                    }
                }
                /*liliao: {
                    validators: {
                        notEmpty: {
                            message: '成分含量不能为空'
                        }
                    }
                },
                tianchongwu: {
                    validators: {
                        notEmpty: {
                            message: '成分含量不能为空'
                        }
                    }
                },
                fuliao: {
                    validators: {
                        notEmpty: {
                            message: '成分含量不能为空'
                        }
                    }
                }*/
            }
        });
    }

</script>
