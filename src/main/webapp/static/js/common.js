/**
 * Created by lee on 4/6/16.
 */

var ajaxWrapper = function (url, type, data, success) {
    $.ajax({
        url: url,
        type: type,
        dataType: "json",
        data: data,
        beforeSend: function(xhr) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        success: success
    });
};
