$(function(){
	$("#publishBtn").click(publish);
});
function publish() {
	/*把弹框隐藏*/
	$("#publishModal").modal("hide");
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求
	$.post(
		//global.js中定义的
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function(data) {
			/*得到状态和提示消息*/
			data = $.parseJSON(data);
			//在提示框中返回消息
			$("#hintBody").text(data.msg);
			$("#hintModal").modal("show");
			/*显示提示框，2秒后自动隐藏提示框*/
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);
		}
	)
}