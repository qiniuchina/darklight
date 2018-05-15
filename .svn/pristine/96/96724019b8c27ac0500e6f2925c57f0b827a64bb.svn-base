
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>关注公众号朕以为</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css">
<link rel="stylesheet" href="../css/common.css" />
<link rel="stylesheet" href="../css/pullToRefresh.css?v=1.2" />
<script src="../js/jquery-1.11.3.min.js"></script>

</head>
<body>
<div class="base">
	<div class="content">
	<!--must content ul li,or shoupi-->
	<div id="wrapper">
		<ul>
		    <li id = "wxfollow">
				识别二维码关注公众号朕以为
			</li>
			<li id = "wxfollow">
				<img src="../images/zhen.jpg"></img>
			</li>		
		</ul>
	</div>
	</div>
	<div style="height: 60px;"></div>
<%@ include file="footer.jsp" %>
</div>
<script>

		(function(doc, win) {
			var docEl = doc.documentElement, resizeEvt = 'orientationchange' in window ? 'orientationchange'
					: 'resize', recalc = function() {
				var clientWidth = docEl.clientWidth;
				if (!clientWidth)
					return;
				docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
			};
			if (!doc.addEventListener)
				return;
			win.addEventListener(resizeEvt, recalc, false);
			doc.addEventListener('DOMContentLoaded', recalc, false);
		})(document, window);
</script>
</body>
</html>