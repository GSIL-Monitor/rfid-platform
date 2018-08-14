
$(function () {
    initEditFormValid();
    initSelectBusinessIdForm();
    if ("add" == pageType) {
        var date = new Date();
        $("#edit_storedValue").val("0");
        $("#edit_owingValue").val("0");
        $("#edit_createTime").val(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate());
        $("#edit_sex input:radio[value='1']").attr("checked", "checked");
    }
    if ("edit" == pageType) {
        initData();

    }
    initButtonGroup();

    $(".selectpicker").selectpicker('refresh');
});

function initButtonGroup(){
    if ("add" == pageType) {
        $("#guest_edit_buttongroup").html(""+
            "<button id='save_guest_button' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveGuest()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>"
        );
    }
    if ("edit" == pageType) {
        $("#guest_edit_buttongroup").html(""+
            "<button id='update_guest_button' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='updateGuest()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>修改</span>" +
            "</button>"
        );
    }
}
function updateGuest(){
    if($('#edit_discount').is(':hidden')){
        $('#edit_discount').val(100);
    }
    $("#edit_type").attr("disabled",false);
    $('#editForm').data('bootstrapValidator').validate();
    if ($("#edit_ownerId").val() == "") {
        bootbox.alert("请选择所属方");
        return
    }
    if (!$("#editForm").data("bootstrapValidator").isValid()) {
        return
    }
    $("#edit_type").removeAttr("disabled");
    cs.showProgressBar();
    $.post(basePath + "/sys/guest/update.do", $("#editForm")
        .serialize(), function (result) {
        cs.closeProgressBar();
        if (result.success == true || result.success == 'true') {
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            location.href = basePath + "/sys/guest/index.do";
        } else {
            cs.showAlertMsgBox(result.msg);

        }
    }, 'json');
}
function initSelectBusinessIdForm() {
    debugger;
    var url;
    if (curOwnerId == "1") {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4";
    } else {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + curOwnerId;
    }
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_linkman").empty();
            $("#edit_linkman").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_linkman").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
        }
    });
}


function saveGuest() {
    if($('#edit_discount').is(':hidden')){
        $('#edit_discount').val(100);
    }
    $("#edit_type").attr("disabled",false);
    $('#editForm').data('bootstrapValidator').validate();
    if ($("#edit_ownerId").val() == "") {
        bootbox.alert("请选择所属方");
        return
    }
    if (!$("#editForm").data("bootstrapValidator").isValid()) {
        return
    }
    $("#edit_type").removeAttr("disabled");
    cs.showProgressBar();
    $.post(basePath + "/sys/guest/save.do", $("#editForm")
        .serialize(), function (result) {
        cs.closeProgressBar();
        if (result.success == true || result.success == 'true') {
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            location.href = basePath + "/sys/guest/index.do";
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
                    }
                }
            },
            discount: {
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
            linkman:{
              validators:{
                  notEmpty:{
                      message:'联系人不能为空'
                  }
              }
            },
            tel: {
                validators: {
                    notEmpty:{
                        message:'电话不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入11位电话号码'
                    },
                    regexp: {
                        regexp: /^1[2|3|4|5|6|7|8]{1}[0-9]{9}$/,
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
            storedValue:{
                validators:{
                    regexp:{
                        regexp:/^\d+(.\d*)?$/,
                        message:'请输入正确的金额'
                    }
                }
            },
            fax:{
                validators:{
                    regexp:{
                        regexp:/^\d*$/,
                        message:'请输入正确的传真号码'
                    }
                }
            }
        }
    });
}