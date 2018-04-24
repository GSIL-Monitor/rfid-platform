
var url=basePath + "/location/Regional/page.do";
$(function () {
    initGrid();
    initdate();
    lodingownerid();
    /*if(roleId == '0'){
     $("#add_style_button").removeAttr("disabled");
     }else{
     $("#add_style_button").attr({"disabled": "disabled"});
     }*/

});
function refresh(){
    location.reload(true);
}
function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}
function initdate() {
    $.ajax({
        url : basePath + "/location/Regional/findprovince.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            debugger;
            var json= data.result;
            $("#form_province").append("<option >--请选择省--</option>");
            for(var i=0;i<json.length;i++){
                $("#form_province").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");

            }
            provinceChange();
        }
    })
}
function provinceChange() {

   var provinceid=$("#form_province").val();
   if(provinceid!=""&&provinceid!=undefined){
       $.ajax({
           url : basePath + "/location/Regional/findcity.do",
           cache : false,
           async : false,
           data:{provinceid:provinceid},
           type : "POST",
           success : function (data,textStatus){
               debugger;
               var json= data.result;
               $("#form_city").text("");
               $("#form_city").append("<option >--请选择市--</option>");
               for(var i=0;i<json.length;i++){
                   $("#form_city").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");

               }
               console.log($("#form_city").text());
               $('#form_city').selectpicker('refresh');
               cityChange();

           }
       })
   }
}
function cityChange() {
    var cityid=$("#form_city").val();
    if(cityid!=""&&cityid!=undefined){
        $.ajax({
            url : basePath + "/location/Regional/findarea.do",
            cache : false,
            async : false,
            data:{cityid:cityid},
            type : "POST",
            success : function (data,textStatus){

                var json= data.result;
                $("#form_area").text("");
                $("#form_area").append("<option >--请选择县--</option>");
                for(var i=0;i<json.length;i++){
                    $("#form_area").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");

                }
                $('#form_area').selectpicker('refresh');
            }
        })
    }

}
function initGrid() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid({
        height:  "auto",
        url: url,
        postData : params,
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'name', label:'名称',editable:true,width: 100},
            {name: 'province', label: '省', editable:true,width: 100},
            {name: 'city', label: '市', editable:true,width: 80},
            {name:'area',label:'县',editable:true,width:80},
            {name:'remark',label:'备注',eidtable:true,width:80}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'id',
        sortorder : "desc",
        autoScroll:false

    });


}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function save() {


}

function unSelected() {

}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid("setGridParam",{
        url: url,
        page : 1,
        postData : params
    }).trigger('reloadGrid');
}

function cleanSearch() {

}

function del() {

}
function locked() {


}
function lodingownerid() {
    $.ajax({
        url : basePath + "/location/Regional/findallUnit.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            debugger;
            var json= data.result;
            $("#form_ownerid").text("");
            $("#form_ownerid").append("<option >--请选择--</option>");
            for(var i=0;i<json.length;i++){
                $("#form_ownerid").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
                $("#form_ownerid").trigger('chosen:updated');
            }
            $('#form_ownerid').selectpicker('refresh');
        }
    })
    
}
function add() {
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');
    initdate();
}
function saveGuest() {
    $('#editForm').data('bootstrapValidator').validate();

    if (!$("#editForm").data("bootstrapValidator").isValid()) {
        return
    }

    cs.showProgressBar();
    $.post(basePath + "/location/Regional/save.do", $("#editForm")
        .serialize(), function (result) {
        cs.closeProgressBar();
        if (result.success == true || result.success == 'true') {
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            location.href = basePath + "/location/Regional/index.do";
        } else {
            cs.showAlertMsgBox(result.msg);

        }
    }, 'json');

}



	