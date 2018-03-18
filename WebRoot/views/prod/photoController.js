$(function () {
    iniGrid();
});
var status = true;
var movetocenter=true;
function showAdvSearchPanel() {
    $("#searchPannel").slideToggle("fast");
}


function addphoto() {
    location.href = basePath + "/views/prod/photo_add.jsp";
}

function deletephoto() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            url: basePath + "/prod/photo/delPhoto.do",
            type: "POST",
            async: false,
            data: {
                src: row.src
            },
            dataType: "json",
            success: function (result) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid');
            }
        });
    } else {
        bootbox.alert("请选择一项进行删除");
    }

}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    console.log(serializeArray);
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    }).trigger("reloadGrid");

}

function refresh() {
    location.reload(true);
}


function showAlbum() {
    if (status=="true") {
        $.ajax({
            url: basePath + "/prod/photo/list.do",
            async: false,
            dataType: "json",
            type: "POST",
            success: function (data) {
                var json = data;
                if (json.length>0) {
                    status = !status;
                    $("#showDetail").slideUp("slow");
                    $("#showAlbum").slideDown("slow");
                    for (var i = 0; i < json.length; i++) {
                        $("#albumn").append("<img id='img"+i+"' src='" + basePath + "/product/photo" + json[i].src + "' alt='" + json[i].styleId + "<br />" + json[i].colorId + "<br />¥"+json[i].price+"' data-largesrc='" + basePath + "/product/photo" + json[i].src + "'/>");
                    }
                    inimyGallary();
                    if(movetocenter){
                        movetocenter=false;
                    setTimeout(function () {
                        console.log($("div.chrg-item"));
                        $(".chrg-grid > .chrg-item,.chrg-no-select").unbind("click");
                    },500);
                    }
                }
            }
        });
    } else {
        $("#showDetail").slideUp("slow");
        $("#showAlbum").slideDown("slow");
    }
}



function inimyGallary(){
    $("div.mygallery").innerHTML="";
    $(".mygallery").chromaGallery({
        color: '#000',
        gridMargin: 5,
        maxColumns: 5,
        dof: false,
        fullscreen:true,
        screenOpacity: 0.2
    });
}
//用于已移除的放大效果
function adaption(){
    setTimeout(function () {
        var scale= $("div.chrg-description").width()/ $("div.chrg-description").height();
        $("div.chrg-description,button.chrgi-next,button.chrgi-previous").bind("click", function () {
            console.log(scale);
            getELement();
            setTimeout(function () {
                var img="#"+$(".chrg-content .chrg-imgwrap img").attr('id');
                var img1=$(img);
                var img2=$(img1).next('img');
                $(img2).hide();
                console.log(img1);
                console.log("另一节点:"+img2);
                var distance=($(".chrg-content").height()- $("div.chrg-imgwrap").height())/2
                // $(".chrg-content .chrg-imgwrap img").css("height", $(".chrg-content").height()-50);
                img1.css("height", $(".chrg-content").height()-50);
                var width=$(".chrg-content").height()*scale;
                // $(".chrg-content .chrg-imgwrap img").css("width",width);
                img1.css("width",width);

                $("div.chrg-imgwrap").css("width",$(".chrg-content .chrg-imgwrap>img").width()+2    );
                $("div.chrg-imgwrap").css("height",$(".chrg-content .chrg-imgwrap>img").height()+2);
                if(movetocenter){
                    movetocenter=false;
                    console.log($("div.chrg-imgwrap").offset().left+"and"+width);
                    $("div.chrg-imgwrap").css("left", $("div.chrg-imgwrap").offset().left*2/3);
                }
            },200);

        });
    }, 100);
}
//用于已移除的放大效果
function getELement(){
    var img1="#"+$(".chrg-content .chrg-imgwrap img").attr('id');
    console.log(img1);
    $(img1)
}
function showDetails() {
    $("#showAlbum").slideUp("slow");
    $("#showDetail").slideDown("slow");
}

function uploadStyles() {
    $("#modal-Upload-batch").modal("show");

}
function uploadWithStyles(){
     $("#modal-single-batch").modal("show");
}
function initUploadStylePlugin(){
    $(".table-header").text("款式导入");
    $('#upload_fileS_inputs').fileinput({
        language: 'zh',
        uploadUrl: basePath + "/prod/photo/uplaodBatch2Photo.do",
        allowedPreviewTypes: [],
        allowedFileExtensions: ["zip"]

    }).on("fileuploaded", function (event, result) {
        cs.closeProgressBar();
        if (result.response.success) {
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });

        } else {
            $.gritter.add({
                text: result.response.msg + "!" ,
                class_name: 'gritter-false  gritter-light'
            });
        }
        console.log(result.response.msg);
        $("#modal-single-batch").modal("hide");
        $("#grid").trigger('reloadGrid');
    });
    $(".fileinput-upload-button").bind("click",function () {
        $("#modal-single-batch").modal("hide");
        cs.showProgressBar();
    });
}
function initUploadPlugin() {
    $(".table-header").text("批量导入");
    $('#upload_file_inputs').fileinput({
        language: 'zh',
        uploadUrl: basePath + "/prod/photo/uploadBatchPhoto.do",
        allowedPreviewTypes: [],
        allowedFileExtensions: ["zip"]

    }).on("fileuploaded", function (event, result) {
        cs.closeProgressBar();
        if (result.response.success) {
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });
        } else {
            $.gritter.add({
                text: result.response.msg + "!" ,
                class_name: 'gritter-false  gritter-light'
            });
        }
        console.log(result.response.msg);
        $("#modal-Upload-batch").modal("hide");
        $("#grid").trigger('reloadGrid');
    });
}

function iniGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/prod/photo/page.do",
        datatype: "json",
        mtype: "POST",
        colModel: [
            {
                name: "", label: "图片", width: 100, editable: true, formatter: function (cellValue, option, rowObject) {
                var pic = rowObject.src;
                if (pic == null) {
                    return "无图片";
                } else {
                    return "<img width=80 height=100 src='" + basePath + "/product/photo" + pic + "' alt='" + rowObject.styleId + "'/>";
                }
            }
            },
            {name: "src", label: "src", width: 20, editable: true, hidden: true},
            {name: "id", label: "id", width: 20, editable: true, hidden: true},
            {name: "styleId", label: "款号", width: 100, editable: true},
            {name: "styleName", label: "款名", wdith: 100, editable: true, sortable: false},
            {name: "colorId", label: "颜色", width: 100, editable: true},
            {name: "seqNo", label: "序号", width: 50, editable: true, sortable: false},
            {name: "price",label:"吊牌价",width:100,editable:true,hidden:true},
            {name: "",label:"吊牌价",width:100,editable:true,formatter:function (rowValue,options,rowObject) {
                return "¥"+rowObject.price;
            }},
            {name: "creator", label: "添加人", width: 100, editable: true},
            {name: "creatorName", label: "添加人名称", width: 150, editable: true, sortable: false},
            {name: "createTime", label: "添加时间", width: 150, editable: true}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#gridPager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'seqNo',
        sortorder: "asc",
        autoScroll: false
    });
}