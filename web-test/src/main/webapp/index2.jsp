<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/assets/echarts-lib/js/jquery-1.8.3.min.js"></script>
</head>
<body>
<div id="main"></div>

<!-- 必须要先引入 ECharts 主文件 -->
<script src="${pageContext.request.contextPath}/assets/echarts-lib/lib/echarts/echarts.js"></script>
<script src="${pageContext.request.contextPath}/assets/echarts-lib/lib/echarts/chart/map.js"></script>
<!-- 引入 ECharts-X 主文件 -->
<script src="${pageContext.request.contextPath}/assets/echarts-lib/lib/echarts-x/echarts-x.js"></script>

<script type="text/javascript">

    <%--var cityDataMap = '${ipLocationMap}';--%>
    var data1 = "${ipLocationMap}";
    var data2 = "${cityKeys}";
    var data3 = "${linkMap}";
    console.info(data1);
    console.info(data2);
    console.info(data3);


    var cityData = [
        {
            name: '北京',
            geoCoord: [116.387778,39.7825]
        },
        {
            name: '武汉',
            geoCoord: [114.2081,30.783758]
        },
        {
            name: '东京',
            geoCoord: [139.779694,35.552258]
        },
        {
            name: '兰州',
            geoCoord: [103.73,36.03]
        },
        {
            name: '纽约',
            geoCoord: [-73.89852,40.867539]
        },
        {
            name: '广州',
            geoCoord: [113.23,23.16]
        },
        {
            name: '奥地利',
            geoCoord: [15.37,47.13]
        },
        {
            name: '新德里',
            geoCoord: [77.09899,28.741933]
        }

    ];

    function geyCityGeoCoord(cityName,cityData) {
        var cityGeoCoord = [];
        cityData.map(function (value) {
            if (value.name === cityName) {
                cityGeoCoord = value.geoCoord;
                return false;
            }
        });
        return cityGeoCoord;
    }

    function getLink(src,dst) {
        var srcGeoCoord = geyCityGeoCoord(src,cityData);
        var desGeoCoord = geyCityGeoCoord(dst,cityData);

        return [{geoCoord:srcGeoCoord}, {geoCoord:desGeoCoord}];

    }


    try {
        var canvas = document.createElement('canvas');
        var gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl');
        if (!gl) {
            console.warn("浏览器不支持");
        }
    } catch (e) {
        // 浏览器不支持 WebGL
        console.warn(e);
    }

    function resize() {
        $('#main').height($(window).height() - $('#header').height());
    }
    $(window).resize(resize);
    resize();

    // 配置后续加载的各种 chart 配置 config
    require.config({
        paths: {
            "lib": '${pageContext.request.contextPath}/assets/echarts-lib/lib',
            'echarts': '${pageContext.request.contextPath}/assets/echarts-lib/lib/echarts',
            'echarts-x': '${pageContext.request.contextPath}/assets/echarts-lib/lib/echarts-x'
        }
    });

    // 然后就可以动态加载图表进行绘制啦
    require([
        'echarts',
        'echarts-x',
        // ECharts-X 中 map3d 的地图绘制基于 ECharts 中的 map。
        'echarts/chart/map',
        'echarts-x/chart/map3d'
    ], function (ec) {
        // 跟 ECharts 一样的方式初始化
        var myChart = ec.init(document.getElementById('main'));
        var option = {
            title: {
                show: true,
                text: '远程控制',
                subtext: '远程控制',
                x:'center',
                y:'top'
            },
            tooltip: {
                formatter: '{b}'
            },
            series: [{
                nameMap:{'China' : '中国'},
                name: 'globe',
                type: 'map3d',
                // 底图配置
                baseLayer: {
                    backgroundColor: '',
                    backgroundImage: '${pageContext.request.contextPath}/assets/echarts-lib/img/earth2.jpg', // 地球贴图
                    quality: 'high',  // 贴图质量
                    heightImage: '${pageContext.request.contextPath}/assets/echarts-lib/img/elev_bump_4k.jpg'  // 高地图
                },
                // 地图绘制配置成只绘制轮廓线和标签
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        borderWidth: 0.5,
                        borderColor: 'yellow',
                        areaStyle: {
                            color: 'rgba(0, 0, 0, 0)'
                        }
                    }
                },
                // 表层（比如云层）配置
                // surfaceLayers: [{
                //     type: 'texture',
                //     image: 'echarts-lib/img/clouds.png'
                // }],
                // echarts-lib/img/background.jpg
                background:"${pageContext.request.contextPath}/assets/echarts-lib/img/b/b4.jpg",

                light: {
                    show: true,
                    // Use the system time
                    // time: '2013-08-07 18:09:09',
                    sunIntensity: 0.8  //日照强度
                },

                // 鼠标漫游配置
                roam: {
                    autoRotate: false,           // 是否开启自转
                    autoRotateAfterStill: 3,    // 鼠标离开地球后多少秒开始自转
                    focus: '中国' ,            // 初始化镜头聚焦的区域名称，比如需要停留到中国上空时
                    zoom: 1 ,                   // 初始化的缩放大小。
                    minZoom: 0.8 ,              // 最小缩放值
                    maxZoom: 1.5 ,              // 最大缩放值
                    preserve: true              // 是否在每次 setOption 后保留之前的鼠标操作状态。
                },

                markPoint: {
                    // circle 圆点, rectangle 方块, triangle 三角, diamond 菱形, emptyCircle, emptyRectangle, emptyTriangle, emptyDiamond
                    symbol: 'circle',
                    symbolSize: 2,
                    distance: -1,
                    // orientation: 'normal',
                    orientationAngle: 90,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                formatter: '{b}',
                                position: 'bottom',
                                textStyle: {
                                    fontSize: 100,
                                    color: 'white'
                                }
                            }
                        }
                    },
                    data:cityData
                },
                markLine: {
                    itemStyle: {
                        normal: {
                            // 线的颜色默认是取 legend 的颜色
                            // color: null
                            // 线宽，这里线宽是屏幕的像素大小
                            width: 10,
                            // 线的透明度
                            opacity: 1
                        }
                    },
                    effect: {
                        show: true,
                        scaleSize: 5
                    },
                    data: [
                        getLink('北京', '兰州'),
                        getLink('北京', '北京'),
                        getLink('北京', '北京'),
                        getLink('北京', '北京'),
                        getLink('北京', '北京'),
                        getLink('东京', '兰州'),
                        getLink('奥地利', '兰州'),
                        getLink('广州', '兰州'),
                        getLink('纽约', '兰州'),
                        getLink('兰州', '北京'),
                        getLink('新德里', '北京'),
                        getLink('武汉', '广州')
                    ]


                },
                // Empty data
                data: [{}]
            }]
        };

        myChart.setOption(option);
    })
</script>
</body>
</html>
