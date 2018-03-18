$(function () {
    initEditFormValid();
    if ("add" == pageType) {
        var date = new Date();
        $("#edit_createTime").val(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate());
        $("#edit_sex input:radio[value='1']").attr("checked", "checked");
    }
    if ("edit" == pageType) {
        initData();
    }
});

function saveGuest() {
    $('#editForm').data('bootstrapValidator').validate();
    if ($("#edit_ownerId").val() == "") {
        bootbox.alert("请选择所属方");
        return
    }
    if (!$("#editForm").data("bootstrapValidator").isValid()) {
        return
    }
    cs.showProgressBar();
    $.post(basePath + "/shop/customer/save.do", $("#editForm")
        .serialize(), function (result) {
        cs.closeProgressBar();
        if (result.success == true || result.success == 'true') {
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            location.href = basePath + "/shop/customer/index.do";
        } else {
            cs.showAlertMsgBox(result.msg);

        }
    }, 'json');

}

function initEditFormValid() {
    $('#editForm').bootstrapValidator({
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
                    $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                }
            }, 'json');
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    } ,
                }
            },
            tel: {
                validators: {
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入11位电话号码'
                    },
                    regexp: {
                        regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                        message: '请输入正确的电话号码'
                    }
                }
            },
            email: {
                validators: {
                    regexp: {
                        regexp: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
                        message: '邮箱格式有误'
                    }
                }
            },
            phone: {
                validators: {
                    notEmpty: {
                        message: '手机号码不能为空'
                    },
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
            },
            saveMon: {
                validators:{
                    regexp:{
                        regexp:/^\d+(.\d*)?$/,
                        message:'请输入正确的金额'
                    }
                }
            },
            zk: {
                validators: {
                    regexp: {
                        regexp: /^([1-9]\d?|100)$/,
                        message: "请输入1-100之间的整数"
                    },
                    notEmpty: {
                        message: '折扣不能为空'
                    }
                }
            },
        }
    });
}
