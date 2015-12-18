<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link href="../lib/nv.d3.css" rel="stylesheet" type="text/css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.2/d3.min.js" charset="utf-8"></script>
    <script src="../lib/nv.d3.js"></script>
    <script src="../lib/stream_layers.js"></script>

    <style>
        text {
            font: 12px sans-serif;
        }
        svg {
            display: block;
        }
        html, body, #chart, svg {
            margin: 0px;
            padding: 0px;
            height: 100%;
            width: 100%;
        }
    </style>
</head>
<body>

<div id="chart" class='with-3d-shadow with-transitions'>
    <svg></svg>
</div>

<script>

    var some_data = {};
    some_data.values = JSON.parse('[{"x":1,"y":17},{"x":2,"y":67},{"x":3,"y":58},{"x":4,"y":58},{"x":5,"y":282},{"x":6,"y":277},{"x":7,"y":365},{"x":8,"y":387},{"x":9,"y":338},{"x":10,"y":335},{"x":11,"y":335},{"x":12,"y":111},{"x":13,"y":114},{"x":14,"y":26},{"x":15,"y":4},{"x":16,"y":3},{"x":17,"y":3},{"x":18,"y":3},{"x":19,"y":3},{"x":20,"y":0},{"x":21,"y":0},{"x":22,"y":0},{"x":23,"y":0},{"x":24,"y":0},{"x":25,"y":0},{"x":26,"y":35},{"x":27,"y":38},{"x":28,"y":224},{"x":29,"y":225},{"x":30,"y":225},{"x":31,"y":278}]');
    some_data.key = 'test-glory';
    some_data.area = true;

    nv.addGraph(function() {
        // console.log(some_data);
        // console.log(stream_layers(3,31,.1));
        // console.log(testData());
    
        var chart = nv.models.lineWithFocusChart();
        chart.xAxis.tickFormat(d3.format(',f'));
        chart.x2Axis.tickFormat(d3.format(',f'));
        chart.yAxis.tickFormat(d3.format(',.2f'));
        chart.y2Axis.tickFormat(d3.format(',.2f'));
        
        var array = [some_data];
        
        d3.select('#chart svg')
            .datum(testData())
            .call(chart);
        nv.utils.windowResize(chart.update);
        return chart;
    });

    function testData() {
        return stream_layers(3,31,.1).map(function(data, i) {
            // console.log(data);
            return {
                key: 'Stream' + i,
                area: i === 1,
                values: data
            };
        });
    }

</script>
</body>
</html>