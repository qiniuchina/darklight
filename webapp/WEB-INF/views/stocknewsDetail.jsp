<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>黑天鹅</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css">
<link rel="stylesheet" href="../css/common.css?v=1.4" />
<link rel="stylesheet" href="../css/pullToRefresh.css" />
<script src="../js/jquery-1.11.3.min.js"></script>
<script src="../js/iscroll.js"></script>
<script src="../js/pullToRefresh.js"></script>

</head>
<body>
	<div class="base">
		<div class="content">
			<!--must content ul li,or shoupi-->
			<div id="stockDetail" style="margin-bottom: 10px;">
				<div  class="row" style="     margin-left: 0px;">
						
						<div class="col-xs-6 " id="stockName"></div>	
						<div class="col-xs-6 " style=" font-size: large;" >${stockNewsDetail.pricePhase}</div>
						
				</div>
				<div  class="row" style="     margin-left: 0px;">
						
						<div class="col-xs-3 ">当前价格:</div>
						<div class="col-xs-2 " id="stockCurrPrice"></div>
						<div class="col-xs-3 ">涨跌幅:</div>
						<div class="col-xs-2 " id="stockUpDown"></div>
				</div>
					
					
				</div>
				<div id="indiKLine" class="indiKLine">
					<div id="kMenu" style="padding-left: 30px;">
						<div style="cursor: pointer; display: inline;"
							onclick="changeNewchart('min')">分时线</div>
						<div style="cursor: pointer; display: inline;"
							onclick="changeNewchart('daily')">日k线</div>
						<div style="cursor: pointer; display: inline;"
							onclick="changeNewchart('weekly')">周k线</div>
						<div style="cursor: pointer; display: inline;"
							onclick="changeNewchart('monthly')">月k线</div>
					</div>
					<div id="min">
						<img style="width: 360px;"
							src="http://image.sinajs.cn/newchart/min/n/${stockNewsDetail.stockCode}.gif">
					</div>
					<div id="daily">
						<img style="width: 360px;"
							src="http://image.sinajs.cn/newchart/daily/n/${stockNewsDetail.stockCode}.gif">
					</div>
					<div id="weekly">
						<img style="width: 360px;"
							src="http://image.sinajs.cn/newchart/weekly/n/${stockNewsDetail.stockCode}.gif">
					</div>
					<div id="monthly">
						<img style="width: 360px;"
							src="http://image.sinajs.cn/newchart/monthly/n/${stockNewsDetail.stockCode}.gif">
					</div>
				</div>
				<div class="container-fluid">
					<div id="indiNews" class="bg-yellow">
						<div
							style="display: inline; font-size: medium; padding-left: 5px;">相关新闻:</div>
						<div id="wrapper"
							style="position: absolute; z-index: 1; width: 100%;">
							<ul>
								<c:forEach items="${stockNewsList}" var="stockNews">
									<li class="row-fluid" style="height: 35px; margin: 15px"
										id="<fmt:formatDate value="${stockNews.pubDate}" pattern="yyyy-MM-dd hh:mm" />,${stockNews.id},${stockNews.stockCode}">
										<a href="${stockNews.newsLink}">${stockNews.title}</a> 
										( <fmt:formatDate
											value="${stockNews.pubDate}" pattern="yyyy-MM-dd hh:mm" /> )
											
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="height: 60px;"></div>
		<%@ include file="footer.jsp"%>
	</div>

	<script type="text/javascript">
		$("#daily").hide();
		$("#min").show();
		$("#weekly").hide();
		$("#monthly").hide();
		var stockCode = "${stockNewsDetail.stockCode}";
		$.ajax({
			url : "http://hq.sinajs.cn/list=s_" + stockCode,
			dataType : "script",
			cache : "false",
			type : "GET",
			success : function(a) {
				var data = eval("hq_str_s_" + stockCode);
				var elements = data.split(",");
				$("#stockName").html(elements[0] + "(" + stockCode + ")");
				$("#stockCurrPrice").html(elements[1]);
				$("#stockUpDown").html(elements[3] + "%");
			}
		});
		var currUserId = "${sessionScope.user.userId}"
		//下拉下拉初始化
		refresher.init({
			id : "wrapper",//<------------------------------------------------------------------------------------┐
			pullDownAction : refresh,
			pullUpAction : load
		});

		//上拉刷新新闻，加载所有最新更新的新闻
		function refresh() {

			// <-- Simulate network congestion, remove setTimeout from production!
			var el, li, newsId;
			var allLi = $("#wrapper ul").children();
			if (allLi != null && allLi.length > 0) {
				newsId = allLi[0].getAttribute("id");
			}
			el = document.querySelector("#wrapper ul");
			//这里写你的刷新代码
			document.getElementById("wrapper").querySelector(".pullDownIcon").style.display = "none";
			$.ajax({
				url : "/darklight/indistocknews/refresh",
				type : "get",
				data : {
					id : newsId
				},
				datatype : "json",
				success : function(data) {
					var li;
					var jsonData = eval(data);
					var refreshNewsList = jsonData.refreshNewsList;
					$.each(refreshNewsList, function(index, item) {
						//循环获取数据
						var id = refreshNewsList[index].id;
						var newsLink = refreshNewsList[index].newsLink;
						var title = refreshNewsList[index].title;
						var pubDate = refreshNewsList[index].pubDate;
						var stockCode = refreshNewsList[index].stockCode;
						var keywords = refreshNewsList[index].keywords;
						li = document.createElement("li");
						//为li标签设置id属性并赋值
						li.setAttribute("id", timeStamp2String(pubDate) + ","
								+ id + "," + stockCode);
						li.setAttribute("class", "row-fluid");
						li.setAttribute("style", "height:35px;margin:15px");
						//拼接HTML代码
						/* var favorNews = "&nbsp&nbsp" */
						li.innerHTML =  "<a href = "+newsLink+">"
								+ title + "(" + stockCode + ")" + "</a>&nbsp("
								+ timeStamp2String(pubDate) + ")<br>"
						if (newsId != null) {
							el.insertBefore(li, el.childNodes[0]);
						} else {
							$("#wrapper ul").append(li);
						}

					});
				}
			});
			wrapper.refresh();
			document.getElementById("wrapper").querySelector(".pullDownLabel").innerHTML = "";

			/****remember to refresh after  action completed！ ---yourId.refresh(); ----| ****/

		}

		//下拉加载更多新闻，每次10条
		function load() {
			var newsId;
			var allLi = $("#wrapper ul").children();
			if (allLi != null && allLi.length > 0) {
				newsId = allLi[allLi.length - 1].getAttribute("id");
			}
			// <-- Simulate network congestion, remove setTimeout from production!
			$.ajax({
				url : "/darklight/indistocknews/more",
				type : "get",
				data : {
					id : newsId
				},
				datatype : "json",
				success : function(data) {
					var li;
					var jsonData = eval(data);
					var moreNewsList = jsonData.moreNewsList;
					$.each(moreNewsList, function(index, item) {
						//循环获取数据
						var id = moreNewsList[index].id;
						var newsLink = moreNewsList[index].newsLink;
						var title = moreNewsList[index].title;
						var pubDate = moreNewsList[index].pubDate;
						var stockCode = moreNewsList[index].stockCode;
						var keywords = moreNewsList[index].keywords;
						var userId = moreNewsList[index].userId;
						li = document.createElement("li");
						//为li标签设置id属性并赋值
						li.setAttribute("id", timeStamp2String(pubDate) + ","
								+ id + "," + stockCode);
						li.setAttribute("class", "row-fluid");
						li.setAttribute("style", "height:35px;margin:15px");
						//拼接HTML代码
						var favorNews = "&nbsp&nbsp"
						if (userId != null && userId == currUserId) {
							favorNews = "&nbsp&nbsp"
						}
						li.innerHTML = "<a href = "+newsLink+">"
								+ title + "(" + stockCode + ")" + "</a>&nbsp("
								+ timeStamp2String(pubDate) + ")"
						$("#wrapper ul").append(li);
					});
				}
			});
			wrapper.refresh();
			/****remember to refresh after action completed！！！   ---id.refresh(); --- ****/

		}

		//将timestap转换为date格式的string
		function timeStamp2String(time) {
			var datetime = new Date();
			datetime.setTime(time);
			var year = datetime.getFullYear();
			var month = datetime.getMonth() + 1;
			if (month < 10) {
				month = "0" + month;
			}
			var date = datetime.getDate();
			if (date < 10) {
				date = "0" + date;
			}
			var hour = datetime.getHours();
			if (hour < 10) {
				hour = "0" + hour;
			}
			var minute = datetime.getMinutes();
			if (minute < 10) {
				minute = "0" + minute;
			}
			return year + "-" + month + "-" + date + " " + hour + ":" + minute;
		};

		function changeNewchart(kmenu) {
			if (kmenu == 'min') {
				$("#min").show();
				$("#daily").hide();
				$("#weekly").hide();
				$("#monthly").hide();
			} else if (kmenu == 'daily') {
				$("#daily").show();
				$("#min").hide();
				$("#weekly").hide();
				$("#monthly").hide();
			} else if (kmenu == 'weekly') {
				$("#weekly").show();
				$("#daily").hide();
				$("#min").hide();
				$("#monthly").hide();
			} else if (kmenu == 'monthly') {
				$("#monthly").show();
				$("#daily").hide();
				$("#weekly").hide();
				$("#min").hide();
			}

		}
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