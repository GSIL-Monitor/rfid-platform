/**
 * Created by Wing li on 2014/6/5.
 */

var constant = {
    unitType: {
        Vender: 0,
        Headquarters: 1,
        Agent: 2,
        Factory: 3,
        Shop: 4,
        NetShop: 5,
        SampleRoom: 6,//
        Department: 7,//
        Franchisee: 8,
        Warehouse: 9,//
        Guest: 10,
        Organization: 11 //组织or公司
    },
    requestUrl: {}
};

$(function () {




});

var cs = $.extend({}, cs);
/* 定义全局对象，类似于命名空间或包的作用 */
cs.isBlank = function (v) {
    return v == undefined || v == null || v == "" || v == " " || v.length == 0;
};
var csProgerssDialog;
cs.showAlertMsgBox = function (msg) {
    bootbox.alert(msg);
};
cs.showProgressBar = function(msg) {
    /*var message = msg?msg:"服务器请求中...";
    csProgerssDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>'+message+'</p>'
    });*/
    $("#loadingModal").modal('show');
};
cs.closeProgressBar = function() {
    $("#loadingModal").modal('hide');
};
cs.showConfirmMsgBox = function (msg, callbackFunc) {
    bootbox.confirm({
        buttons: {
            confirm: {
                label: '确认',
                className: 'btn-success'
            },
            cancel: {
                label: '取消',
                className: 'btn-danger'
            }
        },
        message: msg,
        callback: callbackFunc
        //title: "bootbox confirm也可以添加标题哦",
    });
};

/*
* ltc
* 提取字符串中的汉字
* */
function GetChinese(strValue) {
    if(strValue!= null && strValue!= ""){
        var reg = /[\u4e00-\u9fa5\a-zA-Z]/g;
        return strValue.match(reg).join("");
    }else{
        return "";
    }
}

/*
 * ltc
 * 提取字符串中的数字
 * */
function GetNumber(strValue) {
    if(strValue!= null && strValue!= ""){
        var reg = /[^0-9\.]/ig;
        return strValue.replace(reg,"");
    }else{
        return "";
    }
}

/**
 * resourceButton 该页面所有权限
 * privliegeMap 权限的集合
 */
function ButtonAndDivPower(resourcePrivilege) {
    var tableList = [];
    var buttonList= [];
    var divList = [];
    var privliegeMap = {};
    $.each(resourcePrivilege,function (index,value) {
        if (value.type==="button") {
            if(value.isShow===0){
                if( $("#"+value.privilegeId).length>0){
                    $("#"+value.privilegeId).show();
                }
            }else {
                if( $("#"+value.privilegeId).length>0){
                    $("#"+value.privilegeId).hide();
                }
            }
            buttonList.push(value);
        }else if(value.type==="div"){
            if(value.isShow===0){
                if( $("#"+value.privilegeId).length>0){
                    $("#"+value.privilegeId).show();
                }
            }else {
                if( $("#"+value.privilegeId).length>0){
                    $("#"+value.privilegeId).hide();
                }
            }
            divList.push(value);
        }else{
            tableList.push(value);
        }
    });
    privliegeMap['table'] = tableList;
    privliegeMap['button'] = buttonList;
    privliegeMap['div'] = divList;
    return privliegeMap;
}