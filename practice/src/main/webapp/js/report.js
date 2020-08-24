$(function(){
    // 初始化柱状图
    $('#container').highcharts(initMcRete(data));
    // 初始化饼状图
    initPieChart();
    console.log(contextPath);
});

function initMcRete(data){
    $('#container').highcharts({
        chart: {
            type: 'column'
        },
        credits: {
            enabled: false
        },
        title: {
            text: "大标题"
        },
        subtitle: {
            text: "小标题"
        },
        xAxis: {
            type: 'category'
        },
        yAxis: {
            min: 0,
            title: {
                text: '人数 (个)'
            }
        },
        plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: true,
                    format: '{y}'
                }
            }
        },
        series:
            [{
                name: '各个职业人数统计',
                colorByPoint: true,
                data: [{
                    name: 'CLERK',
                    y: data[0]
                }, {
                    name: 'SALESMAN',
                    y: data[1]
                }, {
                    name: 'MANAGER',
                    y: data[2]
                } ,{
                    name: 'ANALYST',
                    y:data[3]
                } ,{
                    name: 'PRESIDENT',
                    y: data[4]
                }]
            }]
    });
}

function initPieChart() {
    var clickFunc = function(e) {
        var param = e.point.name;
        queryPieChartDetail(param);
    };
    // 查询饼状图数据
    queryData(function(response){
            if (response.total <= 0) {
                $('#container1').html("<div class='nodata'>暂无数据！</div>");
            } else {
            var data = [
                ["800~2000", response['A1']],
                ["2001~4000", response['A2']],
                ["4001~6000", response['A3']]
            ];
            //var total = data[0]+ data[1] + data[2];
            $('#container1').highcharts(getOptions(data, clickFunc, "薪资分布饼状图"));
        }
    });
}

function queryData(successCallback) {
    $.ajax({
        type :"POST",
        dataType : "json",
        url : contextPath + "/employee/getPieChart",
        success : function(data) {
            successCallback(data);
        },
        error : function(data) {
            alert("数据获取异常！" + data);
            return;
        }
    });
}

function queryPieChartDetail(param) {
    $.ajax({
        type :"POST",
        data : {"param" : param},
        dataType : "json",
        async : false,
        url : contextPath + "/employee/getPieDetail",
        success : function(data) {
            var div = document.getElementById("detail");
            var str = "<table border='1px'>";
            str += "<th>empno</th><th>ename</th><th>job</th><th>sal</th>";
            $.each(data, function (index, item) {
                str += "<tr>";
                var empno = "<td>" + item.empno + "</td>";
                var ename = "<td>" + item.ename + "</td>";
                var job = "<td>" + item.job + "</td>";
                var sal = "<td>" + item.sal + "</td>";
                str += empno + ename + job + sal;
                str += "</tr>";
            });
            str += "</table>";
            console.log(str);
            div.innerHTML = str;
        },
        error : function(data) {
            alert("数据获取异常！" + data);
            return;
        }
    });
}

function getOptions(data, clickFunc, title) {
    var noop = function(e) {};
    clickFunc = clickFunc || noop;
    return {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        credits: {
            enabled: false
        },
        title :{
            text:title
        },
        legend:{
            verticalAlign:'middle',
            align:'right',
            layout: "vertical",
            labelFormat: "{name}:{percentage:.1f}%"
        },
        colors: ['#7cb5ec', '#f7a35c', '#90ed7d', '#8085e9', '#f15c80',
            '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        tooltip: {
            headerFormat: '',
            pointFormat: '<b>{point.name} : {point.percentage:.1f}% ({point.y})</b>',
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '{point.percentage:.1f}%',
                    connectorWidth:0,
                    connectorPadding:0,
                    distance:-30
                },
                showInLegend: true,
                events: {
                    click: clickFunc
                }
            }
        },
        series: [{
            type: 'pie',
            data: data
        }]
    };
}