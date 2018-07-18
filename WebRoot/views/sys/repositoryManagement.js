$(function () {
    initTree();
    initCompanyInfoValid();
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
            showCompanyInfo(nodeId);
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
        'plugins': ['dnd', 'search', 'wholerow', 'types']
    }).on('move_node.jstree', function (event, data) {
        moveTree(event, data);
    })
}

function showCompanyInfo(nodeId) {
    $.ajax({
        url: basePath + "/sys/organizationController/findOrganization.do",
        type: "POST",
        data: {
            unitId: nodeId
        },
        success: function (data) {
            if (data.success) {
                var company = data.result;
                $("#info_creatorId").val(company.creatorId);
                $("#info_createTime").val(company.createTime);
                $("#info_code").val(company.code);
                $("#info_name").val(company.name);
                $("#info_ownerId").val(company.ownerId);
                $("#info_unitName").val(company.unitName);
                $("#from_tel").val(company.tel);
                $("#info_linkman").val(company.linkman);
                $("#info_email").val(company.email);
                $("#info_provinceId").val(company.provinceId);
                $("#info_cityId").val(company.cityId);
                $("#info_address").val(company.address);
                $("#info_remark").val(company.remark);
            } else {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
}

function saveOrganization() {
    var that = this;
    $("#addForm").data('bootstrapValidator').validate();
    if (!$("#addForm").data('bootstrapValidator').isValid()) {
        return;
    }
    //进度条
    cs.showProgressBar();
    $.post(basePath + "/sys/repositoryController/save.do",
        $("#addForm").serialize(),
        function (result) {
            cs.closeProgressBar();
            if (result.success == true || result.success == 'true') {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit-dialog").modal('hide');
                refresh();
            }
        }, 'json');
}

function saveEdit() {
    var companyInfo = $("#companyInfo");
    companyInfo.data('bootstrapValidator').destroy();
    companyInfo.data('bootstrapValidator', null);
    initCompanyInfoValid();
    companyInfo.data('bootstrapValidator').validate();
    if (!companyInfo.data('bootstrapValidator').isValid()) {
        return;
    }

    cs.showProgressBar();
    $.post(basePath + "/sys/repositoryController/save.do",
        companyInfo.serialize(),
        function (result) {
            cs.closeProgressBar();
            if (result.success == true || result.success == 'true') {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success gritter-light'
                });
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail gritter-light'
                });
            }
        }, 'json');

}

function moveTree(event, data) {
    var id = data.node.id;
    var parentId = data.parent;
    var position = data.position;
    var sourcePosition = data.old_position;
    var sourceParentId = data.old_parent;
    var params = {
        "id": id,
        "parentId": parentId,
        "position": position,
        "sourceParentId": sourceParentId,
        "sourcePosition": sourcePosition
    };
    $.ajax({
        url: basePath + "/sys/organizationController/move.do",
        type: 'post',
        dataType: 'json',
        data: params,
        timeout: 1000 * 10,
        success: function (data) {
            if (data.success == true || data.success == 'true') {
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail gritter-light'
                });
            }
        }
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

function _search() {
    $('#jstree').jstree(true).search($("#search_organizationName").val());
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

function del() {

}

function initCompanyInfoValid() {
    $('#companyInfo').bootstrapValidator({
        message: '输入值无效',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitHandler: function (validator, form, submitButton) {
            $.post(form.attr('action'), form.serialize(), function (result) {
                if (result.success == true || result.success == 'true') {
                } else {
                    // Enable the submit buttons
                    $('#companyInfo').bootstrapValidator('disableSubmitButtons', false);
                }
            }, 'json');
        },
        fields: {
            name: {
                validators: {
                    notEmpty: '名称不能为空'
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
                    },
                    notEmpty: {
                        message: '联系电话人不能为空'
                    }
                }
            },
            linkman: {
                validators: {
                    notEmpty: {
                        message: '联系人不能为空'
                    }
                }
            },
            address: {
                validators: {
                    notEmpty: {
                        message: '街道地址不能为空'
                    }
                }
            }

        }
    });
}

	
	