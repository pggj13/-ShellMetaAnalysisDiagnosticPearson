const d3 = require("d3");
const d3_save_svg = require("d3-save-svg");
global.xmls = require("xmlserializer");
global.blob = require('blob');
global.base64 = require('base-64');
global.utf8 = require('utf8');

var jsdom;
try {
    jsdom = require("jsdom/lib/old-api.js"); // jsdom >= 10.x
} catch (e) {
    jsdom = require("jsdom"); // jsdom <= 9.x
}

//Função principal que desenha Curva Roc.
function DrawRoc(pData, cb) {

    var pconfig = pData["plotConfig"];
    let descEstudos = pData["descEstudos"];
    var allstudy = _allStudy(pData['datas']);
   // let dataCurveRoc = pData["dataCurveRoc"];

    let config = {
        mountNode: pconfig.mountNode || '#svg',
        url: (pconfig.url !== undefined) ? pconfig.url : null,
        basicPlot: (pconfig.basicPlot !== undefined) ? pconfig.basicPlot : false
    };
    return jsdom.env({

        url: config.url,
        src: [d3],
        done: function (err, window) {

            if (err) { throw err }

            global.window = window;
            global.document = window.document;
            const document = window.document;

            // Apaga o Antigo
            d3.select(document).select(config.mountNode).selectAll("*").remove();

            var margin = { top: 20, right: 20, bottom: 30, left: 50 },
                width = 620 - margin.left - margin.right,
                height = 520 - margin.top - margin.bottom;

            var x = d3.scaleLinear()
                .domain([0, 1])
                .range([0, width]);

            var y = d3.scaleLinear()
                .domain([0, 1])
                .range([height, 0]);

            var xAxis = d3.axisBottom()
                .scale(x);

            var yAxis = d3.axisLeft()
                .scale(y);

            var line = d3.line()
                .x(function (d) { return x(d.fpr); })
                .y(function (d) { return y(d.tpr); })
                .curve(d3.curveBasis);

            var svg = d3.select(document).select(config.mountNode).append('svg')
                .attr("width", width + 305 + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .style('background-color', "#e9fffb")
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

            svg.append("g")
                .attr("class", "y axis")
                .call(yAxis);
            svg.append("text")
                .attr("class", "x label")
                .attr("text-anchor", "end")
                .attr("x", width)
                .attr("y", height - 6)
                .text("1 - Especificidade");

            svg.append("text")
                .attr("class", "y label")
                .attr("text-anchor", "end")
                .attr("y", 6)
                .attr("dy", ".75em")
                .attr("transform", "rotate(-90)")
                .text("Sensibilidade");

            //Descrição da curva
            descEstudos.forEach(function (daCurve, i) {
                svg.append("text")
                    .attr("x", width + 5)  // space legend
                    .attr("y", margin.top + 20 + (i * 20))
                    .attr("class", "legend")
                    .text(daCurve)
            });


            //draw(svg);

            // function draw(svg) {

            svg.selectAll("path.line").remove();


            //CURVA DA DOR
            svg.append("path").attr("class", "line")
                .style("stroke", "steelblue")
                .style("fill", "none")
                .style("stroke-width", "1.5px")
                .attr("d", line(_draw_dor(pconfig.dor)))

            //CURVA DOR INFERIOR
            svg.append("path").attr("class", "line")
                .style("stroke", "steelblue")
                .style("fill", "none")
                .style("stroke-width", "1.5px")
                .attr("d", line(_draw_dor(pconfig.dorInferior)));


            //CURVA DOR SUPERIOR
            svg.append("path").attr("class", "line")
                .style("stroke", "steelblue")
                .style("fill", "none")
                .style("stroke-width", "1.5px")
                .attr("d", line(_draw_dor(pconfig.dorSuperior)));



            //circle curve
            svg.selectAll("dot")
                .data(allstudy)
                .enter().append("circle")
                .attr('fill','red')
                .attr("r", function (d) { return d.amostra })
                .attr("cx", function (d) { return x(d.specy); })
                .attr("cy", function (d) { return y(d.sens); });

            svg.append("g").attr("class", "vors").selectAll("g");
            // }

            var lPlot = d3_save_svg.save(d3.select(document).select(config.mountNode).select('svg').node(), { filename: 'forestplot' });
            cb(lPlot);
        }
    })
};

function _draw_dor(dor) {

    var spec = 0;
    var sens = 0;
    var i = 0;
    var curveDor = [];

    while (spec <= 0.99) {

        curveDor[i] = { fpr: spec, tpr: sens };
        spec += 0.005;
        sens = 1 / (1 + (1 / dor * ((1 - spec) / spec)));
        i++;
    }

    //console.log(dor);
    return curveDor;
}


function _allStudy(datas) {

    var i = 0
    var study = []
    var dataAmostra = [];
    var somaAmostra = []

    for (i = 0; i <= datas.length - 1; i++) {
        if (datas[i] !== null) {
            somaAmostra[i] = datas[i].vp + datas[i].fp + datas[i].vn + datas[i].fn;
            dataAmostra[i] = datas[i]
        }
    }

    var numMaior = Math.max.apply(null, somaAmostra);
    var mediaAmostra
    var i = 0;
    var raio = 6;

    dataAmostra.forEach((datas) => {

        let value = datas.vp + datas.fp + datas.vn + datas.fn;

        if (value === numMaior) {
            mediaAmostra = raio
        } else {
            mediaAmostra = ((raio * value) / numMaior) + 1
        }

        let spec = 1 - datas.spec
        study[i] = { sens: datas.sen, specy: spec, amostra: mediaAmostra }
        i++

    })
    return study
}
module.exports.DrawRoc = DrawRoc;
