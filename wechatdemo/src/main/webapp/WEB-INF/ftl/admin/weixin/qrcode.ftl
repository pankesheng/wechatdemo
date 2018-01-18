<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="${contextPath}/ext/jquery/jquery-1.8.1.min.js"></script>
	<script type="text/javascript" src="${contextPath}/admin/javascripts/qrcode.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
</head>
<body>
<script>

	//微信api配置
	wx.config({
	    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: '${map.appId}', // 必填，公众号的唯一标识
	    timestamp: ${map.timestamp}, // 必填，生成签名的时间戳
	    nonceStr: '${map.nonceStr}', // 必填，生成签名的随机串
	    signature: '${map.signature}',// 必填，签名，见附录1
	    jsApiList: [
	    	'scanQRCode'
	    ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	
	wx.scanQRCode({
	    needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	   		var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		}
	});

</script>
</body>
</html>