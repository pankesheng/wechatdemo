<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="${contextPath}/ext/jquery/jquery-1.8.1.min.js"></script>
	<script type="text/javascript" src="${contextPath}/admin/javascripts/qrcode.min.js"></script>
	
</head>
<body>
	<div class="qr-code" id="qrcode"></div>
<script>
    //二维码
    var qrcodeurl = '${(qrcodeurl)!}';
	var qrcode = new QRCode(document.getElementById("qrcode"), {
		width: 134,
		height: 134
	});
	var dn = new Date().getTime() + '';
	qrcode.makeCode(qrcodeurl);
	
	var timer = setInterval(function() {
		$.post("${contextPath}/qrcode/checkSession.ajax",{},function(data){
			if(1==data.s){
				window.location.href=data.d;
			}
		},"json");
  	}, 1500);
	
</script>
</body>
</html>