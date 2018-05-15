<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>topbar</title>
</head>
<body>
<div style="border-bottom: 1px solid #eee;">
<div class="row" style="margin-left:0.8rem;margin-top:0.2rem;margin-bottom:0.2rem;">
  <div class="col-xs-6">
		<a style="color:#999;font-size:17px" href="/darklight/stocknews/list?userId=${sessionScope.user.userId}&newsSource=1">热点新闻</a>
  </div>
  <div class="col-xs-6">
		<a style="color:#999;font-size:17px" href="/darklight/stocknews/list?userId=${sessionScope.user.userId}&newsSource=0">利空新闻</a>
  </div>
</div>
</div>
</body>

</html>