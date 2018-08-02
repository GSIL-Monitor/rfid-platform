<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var pageType="${pageType}"
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container" style="">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">

            <div class="page-header">
                <h1>
                    <a href="#" onclick="toIndex()">角色信息</a>
                    <small>
                        <i class="ace-icon fa fa-angle-double-right"></i>
                        <c:if test="${pageType == 'add'}">
                            增加
                        </c:if>
                        <c:if test="${pageType == 'edit'}">
                            编辑
                        </c:if>
                    </small>
                </h1>
            </div><!-- /.page-header -->


            <div class="page-content">
                <!-- /.page-header -->
                <div class="row">
                    <div class="col-xs-12 col-sm-4 widget-container-col">
                        <div class="widget-box widget-color-blue light-border">
                            <div class="widget-header">
                                <h5 class="widget-title">角色信息</h5>
                                <div class="widget-toolbar no-border">
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main padding-5">
                                    <form class="form-horizontal" role="form" id="editForm">
                                        <input type="hidden" id="editForm-id" name="id" value="${role.id}"/>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right" for="editForm-code"> 角色编号 </label>

                                            <div class="col-sm-9">
                                                <input type="text" id="editForm-code" name="code" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="" class="col-xs-10 col-sm-5" value="${role.code}" />
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right" for="editForm-code"> 角色名称 </label>

                                            <div class="col-sm-9">
                                                <input type="text" id="editForm-name" name="name" placeholder="" class="col-xs-10 col-sm-5"
                                                       value="${role.name}"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right" for="editForm-remark"> 备注 </label>

                                            <div class="col-sm-9">
                                                <textarea maxlength="50" id="editForm-remark" name="remark" placeholder="" class="col-xs-10 col-sm-5">${role.remark}</textarea>
                                            </div>
                                        </div>

                                    </form>
                                </div>
                                <div class="widget-toolbox padding-8 clearfix">
                                    <div class="col-sm-offset-5 col-sm-6">
                                        <button class="btn btn-xs btn-success" type="button" id="saveRoleBtn" onclick="saveRole()">
                                            <i class="ace-icon fa fa-save"></i>
                                            <span class="bigger-110">保存</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-8">
                        <div class="widget-box widget-color-blue light-border" id="authBox">
                            <div class="widget-header">
                                <h5 class="widget-title">角色权限</h5>
                                <div class="widget-toolbar">
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <form>
                                        <!-- <legend>Form</legend> -->
                                        <table id="authGrid"></table>


                                    </form>
                                </div>
                            </div>
                            <div class="widget-toolbox padding-8 clearfix">
                                <span>* 勾选权限后系统立即保存</span>
                            </div>
                        </div>
                    </div>



                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->


            </div>
        </div>

        <!--/.fluid-container#main-container-->
    </div>

    <jsp:include page="../layout/footer_js.jsp"></jsp:include>

    <script type="text/javascript">

        function toIndex() {
            location.href = basePath+"/sys/role/index.do";
        }

        function saveRole() {
            $('#editForm').data('bootstrapValidator').validate();
            if(!$('#editForm').data('bootstrapValidator').isValid()){
                return ;
            }
            cs.showProgressBar();
            $.post(basePath+"/sys/role/save.do",
                $("#editForm").serialize(),
                function(result) {
                    cs.closeProgressBar();
                    if(result.success == true || result.success == 'true') {
                        $.gritter.add({
                            text: result.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                        $("#editForm-id").val(result.result.id);
                        $("#editForm-code").attr("readonly",true);
                        pageType="edit";
                        initAuthGrid();
                    } else {
                        cs.showAlertMsgBox(result.msg);
                    }
                }, 'json');
        }

        $(function() {
            initAuthGrid();
            initEditFormValid();
            if( '${pageType}'=='edit') {
                $("#editForm-code").attr("readonly",true);
            }
        });

        function initEditFormValid() {
            $('#editForm').bootstrapValidator({
                message: '输入值无效',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    code: {
                        validators: {
                            stringLength: {
                                min: 5,
                                max: 20,
                                message: '用户名长度必须在5到20之间'
                            },
                            threshold :  5 , //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                            remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                                url: basePath+"/sys/role/checkCode.do?pageType=${pageType}",//验证地址
                                message: '编号已存在',//提示消息
                                delay : 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                                type: 'POST',//请求方式
                                data: function(validator) {
                                    return {
                                        //  password: $('[name="passwordNameAttributeInYourForm"]').val(),
                                        //  whatever: $('[name="whateverNameAttributeInYourForm"]').val()
                                    };
                                }

                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9_]+$/,
                                message: '编号由数字字母下划线和.组成'
                            }
                        }
                    },
                    name: {
                        validators: {
                            notEmpty: {
                                message: '名称不能为空'
                            }
                        }
                    },
                    groupId: {
                        validators: {
                        }
                    },
                    ownerId: {
                        validators: {
                            notEmpty: {
                                message: '所属方不能为空'
                            }
                        }
                    },
                    tel: {
                        validators: {
                            stringLength: {
                                min: 11,
                                max: 11,
                                message: '请输入11位手机号码'
                            },
                            regexp: {
                                regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                                message: '请输入正确的手机号码'
                            }
                        }
                    } ,
                    linkman: {
                        validators: {
                            notEmpty: {
                                message: '联系人不能为空'
                            }
                        }
                    }
                }
            });
        }

        function initAuthGrid() {
            $("#authGrid").jqGrid({
                treeGrid: true,
                url: basePath + "/sys/role/findResource.do?roleId=${role.code}&pageType="+pageType,
                datatype: "json",
                autowidth: true,
                height: 300,
                multiselect:false,
                shrinkToFit: true,
                colModel: [

                    {name: 'code', label: '资源编号', width: 10,key:true,
                        formatter: function (cellvalue, options, rowObject) {
                            return "    "+cellvalue;
                        }
                    },
                    {name: 'name', label: '资源名称',width: 20},
                    {name: 'ownerId', label: '父菜单ID',hidden:true},
                    {name: 'seqNo', label: '序号',width: 5},
                    {name:'checked', label:'选择', width: 5, align:'center', formatter: function (cellvalue, options, rowObject) {
                        if(cellvalue) {
                            return '<input id="ckbox_' + rowObject.code + '" name="' + rowObject.code + '" class="inputcheckbox" type="checkbox" checked="checked" />';
                        } else {
                            return '<input id="ckbox_' + rowObject.code + '" name="' + rowObject.code + '" class="inputcheckbox" type="checkbox" />';
                        }
                    }
                    },
                    {
                        name: '',
                        label: '按钮配置细则',
                        width: 40,
                        align: 'center',
                        formatter: function (cellvalue, options, rowObject) {
                           console.log(rowObject.resourcePrivilegeList);
                           var html="";
                           if(rowObject.resourcePrivilegeList!=""&&rowObject.resourcePrivilegeList!=undefined){
                               for(var i=0;i<rowObject.resourcePrivilegeList.length;i++){
                                   if(pageType=="add"){
                                       html+='<input id="ckbox_' + rowObject.resourcePrivilegeList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcePrivilegeList[i].id + '",this) name="' + rowObject.resourcePrivilegeList[i].privilegeId + '" value="' + rowObject.resourcePrivilegeList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourcePrivilegeList[i].privilegeName+"&nbsp;";
                                       if((i%3)==0){
                                           html+="<br>"
                                       }
                                   }else{
                                       if(rowObject.resourcePrivilegeList[i].isShow===0){
                                           html+='<input id="ckbox_' + rowObject.resourcePrivilegeList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcePrivilegeList[i].id + '",this) name="' + rowObject.resourcePrivilegeList[i].privilegeId + '" value="' + rowObject.resourcePrivilegeList[i].privilegeId + '" type="checkbox" checked="checked"/> '+ rowObject.resourcePrivilegeList[i].privilegeName+"&nbsp;"
                                       }else{
                                           html+='<input id="ckbox_' + rowObject.resourcePrivilegeList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcePrivilegeList[i].id + '",this) name="' + rowObject.resourcePrivilegeList[i].privilegeId + '" value="' + rowObject.resourcePrivilegeList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourcePrivilegeList[i].privilegeName+"&nbsp;"
                                       }
                                       if((i%3)==0){
                                           html+="<br>"
                                       }
                                   }

                               }
                               return html;
                           }else{
                               return html;
                           }

                        }
                    },
                    {
                        name: '',
                        label: '表格配置细则',
                        width: 40,
                        align: 'center',
                        formatter: function (cellvalue, options, rowObject) {
                            console.log(rowObject.resourcetableList);
                            var html="";
                            if(rowObject.resourcetableList!=""&&rowObject.resourcetableList!=undefined){
                                for(var i=0;i<rowObject.resourcetableList.length;i++){
                                    if(pageType=="add"){
                                        html+='<input id="ckbox_' + rowObject.resourcetableList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcetableList[i].id + '",this) name="' + rowObject.resourcetableList[i].privilegeId + '" value="' + rowObject.resourcetableList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourcetableList[i].privilegeName+"&nbsp;";
                                        if((i%3)==0){
                                            html+="<br>"
                                        }
                                    }else{
                                        if(rowObject.resourcetableList[i].isShow===0){
                                            html+='<input id="ckbox_' + rowObject.resourcetableList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcetableList[i].id + '",this) name="' + rowObject.resourcetableList[i].privilegeId + '" value="' + rowObject.resourcetableList[i].privilegeId + '" type="checkbox" checked="checked"/> '+ rowObject.resourcetableList[i].privilegeName+"&nbsp;"
                                        }else{
                                            html+='<input id="ckbox_' + rowObject.resourcetableList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourcetableList[i].id + '",this) name="' + rowObject.resourcetableList[i].privilegeId + '" value="' + rowObject.resourcetableList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourcetableList[i].privilegeName+"&nbsp;"
                                        }
                                        if((i%3)==0){
                                            html+="<br>"
                                        }
                                    }

                                }
                                return html;
                            }else{
                                return html;
                            }

                        }
                    },
                    {
                        name: '',
                        label: '表单配置细则',
                        width: 40,
                        align: 'center',
                        formatter: function (cellvalue, options, rowObject) {
                            console.log(rowObject.resourceDivList);
                            var html="";
                            if(rowObject.resourceDivList!=""&&rowObject.resourceDivList!=undefined){
                                for(var i=0;i<rowObject.resourceDivList.length;i++){
                                    if(pageType=="add"){
                                        html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourceDivList[i].id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + rowObject.resourceDivList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourceDivList[i].privilegeName+"&nbsp;";
                                        if((i%3)==0){
                                            html+="<br>"
                                        }
                                    }else{
                                        if(rowObject.resourceDivList[i].isShow===0){
                                            html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourceDivList[i].id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + rowObject.resourceDivList[i].privilegeId + '" type="checkbox" checked="checked"/> '+ rowObject.resourceDivList[i].privilegeName+"&nbsp;"
                                        }else{
                                            html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + rowObject.resourceDivList[i].id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + rowObject.resourceDivList[i].privilegeId + '" type="checkbox" /> '+ rowObject.resourceDivList[i].privilegeName+"&nbsp;"
                                        }
                                        if((i%3)==0){
                                            html+="<br>"
                                        }
                                    }

                                }
                                return html;
                            }else{
                                return html;
                            }

                        }
                    }

                ],
                treeReader : {
                    level_field: "level",
                    parent_id_field: "ownerId",
                    leaf_field: "leaf",
                    expanded_field: "expand"
                },
                treeGridModel: "adjacency",
                ExpandColumn: "code",
                rowNum: -1,
                pager: "false",
                "treeIcons" : {
                    "plus": "ace-icon fa fa-chevron-down",
                    "minus": "ace-icon fa fa-chevron-up",
                    "leaf" : ""
                },
                jsonReader: {
                    repeatitems: false
                },
                loadComplete: function(data) {
                    //绑定选择事件
                    $(".inputcheckbox").click(function () {
                        if(cs.isBlank($("#editForm-id").val() )) {
                            cs.showAlertMsgBox("角色信息未保存，不能编辑权限");
                            return;
                        }
                        var resId = $(this).attr("id").split("_")[1];
                        if ($(this).prop("checked") == true) {//选中
                            cs.showProgressBar();
                            console.log(($(this).prop("checked")));
                            console.log(($(this).attr("id")));

                            $.post(basePath+"/sys/role/addAuth.do",
                                {roleId:$("#editForm-id").val(),resId:resId},
                                function(result) {
                                    cs.closeProgressBar();
                                    if(result.success == true || result.success == 'true') {
                                        $.gritter.add({
                                            text: result.msg,
                                            class_name: 'gritter-success  gritter-light'
                                        });
                                    } else {
                                        cs.showAlertMsgBox(result.msg);
                                    }
                                }, 'json');
                        } else { //取消
                            $.post(basePath+"/sys/role/deleteAuth.do",
                                {roleId:$("#editForm-id").val(),resId:resId},
                                function(result) {
                                    cs.closeProgressBar();
                                    if(result.success == true || result.success == 'true') {
                                        $.gritter.add({
                                            text: result.msg,
                                            class_name: 'gritter-success  gritter-light'
                                        });
                                    } else {
                                        cs.showAlertMsgBox(result.msg);
                                    }
                                }, 'json');
                        }
                    });
                }
            });


        }
        function selectresourcePrivilege(id, t) {
             var state=0;
            if($(t).prop('checked')){
                state=0;
            }else{
                state=1;
            }
            if(cs.isBlank($("#editForm-id").val() )) {
                cs.showAlertMsgBox("角色信息未保存，不能编辑权限");
                return;
            }
            cs.showProgressBar();
            $.post(basePath+"/sys/role/updateResourceButtonIsShow.do",
                {id:id,isShow:state},
                function(result) {
                    cs.closeProgressBar();
                    if(result.success == true || result.success == 'true') {
                        $.gritter.add({
                            text: result.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    } else {
                        cs.showAlertMsgBox(result.msg);
                    }
                }, 'json');
        }
        function initPrivilegeCheckbox(privilegeList){
            $.each(privilegeList,function(index,value){
                if(pageType=="add"){
                    html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + value.id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + value.privilegeId + '" type="checkbox" /> '+ value.privilegeName+"&nbsp;";
                    if((i%3)==0){
                        html+="<br>"
                    }
                }else{
                    if(rowObject.resourceDivList[i].isShow===0){
                        html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + value.id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + value.privilegeId + '" type="checkbox" checked="checked"/> '+ value.privilegeName+"&nbsp;"
                    }else{
                        html+='<input id="ckbox_' + rowObject.resourceDivList[i].privilegeId + '" onclick=selectresourcePrivilege("' + value.id + '",this) name="' + rowObject.resourceDivList[i].privilegeId + '" value="' + value.privilegeId + '" type="checkbox" /> '+ value.privilegeName+"&nbsp;"
                    }
                    if((i%3)==0){
                        html+="<br>"
                    }
                }
            })
        }
    </script>
</body>
</html>