$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});


function send_letter() {
	$("#sendModal").modal("hide");
	//从页面获取目标用户和私信内容
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		CONTEXT_PATH  + "/letter/send",
		{"toName": toName, "content":content},
		function (data) {
			data = $.parseJSON(data);
			if(data.code == 0){
				$("#hintBody").text("发送成功");
			}
			else{
				$("#hintBody").text(data.msg);
			}
			//显示提示框，过两秒然后自动隐藏掉,然后再刷新当前页面
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide"); //显示提示框
				location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}