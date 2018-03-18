<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="modal-tableUpload" class="modal fade"  tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>

                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="searchPanel">
                        <form class="form-horizontal" role="form" id="uploadForm">
                            <input id="upload_file_input" class="file" name="file" type="file" multiple="multiple" >
                        </form>

                    </div>
                </div>
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    initUploadSet()
</script>
