<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="internationalization.MessageBundle" />
<!DOCTYPE html>
<c:choose>
    <c:when test="${user.credentials == 'admin'}">

        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

                <title><fmt:message key="title" /></title>
                <link rel="shortcut icon" href="http://static.vop.tiwi.be/team03/catan_icon.ico">

                <link type="text/css" href="page.css" rel="stylesheet" />
                <link href="bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
                <link rel="stylesheet" type="text/css" href="DataTables/DT_bootstrap.css">
                <link type="text/css" href="statistics.css" rel="stylesheet" />

                <script src="http://code.jquery.com/jquery.js"></script>

                <script src="RGraph/libraries/RGraph.common.core.js"></script>
                <script src="RGraph/libraries/RGraph.common.dynamic.js"></script>   <!-- Just needed for dynamic features (eg tooltips) -->

                <script src="RGraph/libraries/RGraph.common.annotate.js"></script>  <!-- Just needed for annotating -->
                <script src="RGraph/libraries/RGraph.common.context.js"></script>   <!-- Just needed for context menus -->
                <script src="RGraph/libraries/RGraph.common.effects.js"></script>   <!-- Just needed for visual effects -->
                <script src="RGraph/libraries/RGraph.common.key.js"></script>       <!-- Just needed for keys -->
                <script src="RGraph/libraries/RGraph.common.resizing.js"></script>  <!-- Just needed for resizing -->
                <script src="RGraph/libraries/RGraph.common.tooltips.js"></script>  <!-- Just needed for tooltips -->
                <script src="RGraph/libraries/RGraph.common.zoom.js"></script>      <!-- Just needed for zoom -->
                <script src="RGraph/libraries/RGraph.line.js"></script>
            </head>
            <body>

                <%-- menu --%>
                <jsp:include page="menu.jsp" flush="true"/>

                <div class="row-fluid">
                    <div class="span8 offset2" style="min-width: 750px">
                        <div class="hero-unit">

                            <h2><fmt:message key="statistics" /></h2>
                            <c:set var="period" value="${param.period}" />
                            <c:if test="${period != '7' && period != '30'}">
                                <c:set var="period" value="7" />
                            </c:if>
                            <div>
                                <label id="lblPeriod"><fmt:message key="lblPeriod" />: </label>
                                <select id="optPeriod" onchange="window.location.replace('statistics.jsp?period=${period == 7 ? 30 : 7}')">
                                    <option value="last week" ${period == '7' ? 'selected' : ''}><fmt:message key="lastweek" /></option>
                                    <option value="last month" ${period == '30' ? 'selected' : ''}><fmt:message key="lastmonth" /></option>
                                </select>
                            </div>                            
                            <canvas id="line1" width="700" height="350" ></canvas>

                            <div id="numbers" hidden=""><jsp:include page="Statistics.get?number=${period}&content=numbers" flush="true"/></div>
                            <div id="dates" hidden=""><jsp:include page="Statistics.get?number=${period}&content=dates" flush="true" /></div>
                            <script>
                                $('#numbers').hide();
                                $('#dates').hide();
                                CreateCharts = function ()
                                {
                                    var dates = $.parseJSON($('#dates').text());
                                    for (var i = 0; i < dates.length; i++){
                                        dates[i] = new String(dates[i]);
                                        if (dates[i].length == 3){
                                            dates[i] = '0' + dates[i];
                                        }
                                        dates[i] = dates[i].charAt(0) + dates[i].charAt(1) + '/' + dates[i].charAt(2) + dates[i].charAt(3);
                                    }
                                    var line1 = new RGraph.Line('line1', $.parseJSON($('#numbers').text()));
                                    line1.Set('chart.filled', true);
                                    line1.Set('chart.fillstyle', ['Gradient(pink:rgba(255,255,255,0))']);
                                    line1.Set('chart.colors', ['red', 'green']);
                                    // Add the shadow back - was removed as an example
                                    //line1.Set('chart.shadow', true);
                                    line1.Set('chart.linewidth', 5);
                                    line1.Set('chart.hmargin', 0.0005);
                                    line1.Set('chart.text.angle', 45);
                                    line1.Set('chart.labels', dates);
                                    line1.Set('chart.tooltips', dates);
                                    line1.Set('chart.background.grid.autofit', true);
                                    line1.Set('chart.background.grid.autofit.align', true);
                                    line1.Set('chart.gutter.bottom', 50);
                                    line1.Set('chart.marker', true);
                                        !RGraph.isOld() ? RGraph.Effects.Line.jQuery.Trace(line1) : line1.Draw();
                                }
        
                                if(!RGraph.isOld()) {
                                    CreateCharts();
                                }else {
                                    window.onload = CreateCharts;
                                }
                            </script>

                        </div>
                    </div>
                </div>

            </body>
        </html>
    </c:when>
    <c:otherwise>
        <jsp:forward page="login.jsp"/>
    </c:otherwise>
</c:choose>