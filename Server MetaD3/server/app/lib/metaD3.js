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

//Função principal que desenha Forest Plot.
function DrawForest(pData, cb) {

    let data = pData["datas"];
    let pconfig = pData["plotConfig"];

    let config = {
        colorPrimary : '#fff',
        colorSecondary : '#d3d4ff',
        colorObjectPrimary : '#090a3a',
        colorObjectSecondary : '#228B22',
        colorText : '#000',
        colorCentralLine : '#444',
        type : pconfig.type || 'odds',
        mountNode: pconfig.mountNode || '#svg',
        width: pconfig.width || 800,
        margin: pconfig.margin || { left: 10, top: 15 },
        effectLabel: pconfig.effectLabel || 'Effect',
        fontSize: pconfig.fontSize || 12,
        fontFamily: pconfig.fontFamily || 'Helvetica',
        nTicks: pconfig.nTicks || 7,
        vCentralBar: (pconfig.vCentralBar !== undefined) ? pconfig.vCentralBar : null,
        risk: (pconfig.risk !== undefined) ? pconfig.risk : null,
        url: (pconfig.url !== undefined) ? pconfig.url : null,
        obs: (pconfig.obs !== undefined) ? pconfig.obs : '',
        maxPositive: (pconfig.maxPositive !== undefined) ? pconfig.maxPositive : 300,
        basicPlot: (pconfig.basicPlot !== undefined) ? pconfig.basicPlot : false
    };

    let layout = {
        tabWidth: 16,  // In pixels
        tableWidth: 0.15,
        plotWidth: 0.6,
        labelWidth: 0.25,
        rowHeight: 30,
        padding: { left: 5, top: 15 },
        squareFullSize: 40 //24
    };

    config.height = (data.length + 3) * layout.rowHeight;

    //const { window } = new JSDOM(`<!DOCTYPE html>`);
   
    return jsdom.env({
        url: config.url,
        src: [d3],
        done: function(err, window) {

            if (err) { throw err }

            global.window = window;
            global.document = window.document;
            const document = window.document;

            //Seta Plot Basico
            if (config.basicPlot) {
                
                config.colorSecondary = '#fff';
                config.colorObjectPrimary = '#000';
                config.colorObjectSecondary = '#000';
                config.colorText = '#000';
                config.colorCentralLine = '#000';
            }

            // Apaga o Antigo
            d3.select(document).select(config.mountNode).selectAll("*").remove();

            switch(config.type) {

                case 'odds' :
                case 'lrp' :
                case 'lrn' :
                    _draw_odds(config, layout, data, document);
                    break;

                case 'sen' :
                case 'spec' :
                    _draw_senSpec(config, layout, data, document);
                    break;    

                default :
                    _draw_odds(config, layout, data, document);
            }

            var lPlot = d3_save_svg.save(d3.select(document).select(config.mountNode).select('svg').node(), { filename: 'forestplot' });
            cb(lPlot);
            
            //this.window = 
            //d3_save_svg.save(d3.select('svg').node(), { filename: 'forestplot' })
            //window.close();
            //cb(this);
        }
    });
}

//
// Retorna a lista de efeito medio, baixo e alto.
//
function _get_effect(d) {

    if (d.effect.effect !== undefined &&
        d.effect.low !== undefined &&
        d.effect.high !== undefined) {
        return [
        d.effect.low, d.effect.effect, d.effect.high
        ];
    }

    try {
        return [
        d.effect.effect - d.effect.sd,
        d.effect.effect,
        d.effect.effect + d.effect.sd
        ];
    } catch(e) {}

    throw "Efeito inválido. Esperado 'effect' e " +
            "'sd' ou 'effect', 'low' e 'high'.";
}

//
// Monta function Translate
//
function translate(x, y) {
    return 'translate(' + x + ', ' + y + ')';
}

//
// Desenha plot de Odds
//
function _draw_odds(config, layout, data, document) {
    
    var svg = d3.select(document).select(config.mountNode).append('svg')
    .attr('width', config.width)
    .attr('height', config.height)
    .append('g')
    .attr('transform', translate(config.margin.left, config.margin.top));

    // Cria a tabela de descricao
    var table = svg.append('g')
        .attr('width', layout.tableWidth * config.width);

    var rows = table.selectAll('.row').data(data).enter()
        .append('g')
        .classed('row', true)
        .attr('transform', function(d, i) {
            return translate(
            0,
            layout.rowHeight * i
            );
        });

    rows
        .append('rect')
        .attr('height', layout.rowHeight)
        .attr('width', '100%')
        .attr('fill', (d, i) => { return (i % 2 === 0) ? config.colorSecondary : config.colorPrimary; })
        .attr('x', 0)
        .attr('y', 0);

    rows
        .append('text')
        .text(function(d) { return d.description; })
        .style('font-size', config.fontSize)
        .style('font-family', config.fontFamily)
        .attr('dy', (layout.rowHeight - config.fontSize) / 2 + 10)
        .attr('dx', function(d) {
            return (
            layout.padding.left +
            (d.descriptionOffset || 0) * layout.tabWidth
            );
        });

    // Cria o Gráfico        
    var plot = svg.append('g')
        .attr('width', layout.plotWidth * config.width)
        .attr('transform', translate(layout.tableWidth * config.width, 0))
        .classed('plot', true);

    // Desenha as linhas
    var lowX = Infinity;
    var highX = -Infinity;
    {
        let _, _low, _high;

        for (let e of data) {
        try {
            [_low, _, _high] = _get_effect(e);
        } catch(e) { continue; }

        if (_low < lowX) lowX = _low;
        if (_high > highX) highX = _high;
        }
    }

    lowX = 0.001;

    if(highX > config.maxPositive){
        highX = config.maxPositive; 
    }

    // monta escala
    if(config.type == 'lrp'){

        var x = d3.scaleLinear()
        .domain([0.01, 1, config.maxPositive])
        .range([0.01, (config.width * layout.plotWidth/9), config.width * layout.plotWidth]);

        if(config.maxPositive == 100){

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 1, 50, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }else if(config.maxPositive == 300){

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 1, 100, 200, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }
        else{

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 1, 100, 200, 300, 400, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }
    }
    else if(config.type == 'lrn'){

        var x = d3.scaleLinear()
        .domain([0.01, 1, config.maxPositive])
        .range([0.01, (config.width * layout.plotWidth/1.1), config.width * layout.plotWidth]);

        var xAxis = d3.axisBottom(x)
        .tickValues([0.01, 0.25, 0.5, 0.75, 1, config.maxPositive])
        .tickFormat(d3.format(".3"));
    }
    else{
        
        var x = d3.scaleLinear()
        .domain([0.01, 1, config.maxPositive])
        .range([0.01, (config.width * layout.plotWidth/3), config.width * layout.plotWidth]);

        if(config.maxPositive == 100){

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 0.25, 0.5, 0.75, 1, 25, 50, 75, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }else if(config.maxPositive == 300){

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 0.25, 0.5, 0.75, 1, 50, 100, 150, 200, 250, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }
        else{

            var xAxis = d3.axisBottom(x)
            .tickValues([0.01, 0.25, 0.5, 0.75, 1, 100, 200, 300, 400, config.maxPositive])
            .tickFormat(d3.format(".3"));
        }
    }

    // Desenha a descrição
    plot.append('g')
        .attr('transform', function() {
            return translate(0, data.length * layout.rowHeight + 5);
        })
        .call(xAxis)
        .append('g')
        .attr('transform', translate(0, layout.rowHeight + layout.padding.top))
        .append('text')
        .text(config.effectLabel)
        .attr('fill', config.colorText)
        .attr('text-anchor', 'middle')
        .attr('x', layout.plotWidth * config.width / 2)
        .style('font-weight', 'bold')
        .style('font-size', config.fontSize);

    // Desenha a Obs
    plot.append('g')
        .attr('transform', function() {
            return translate(0, data.length * layout.rowHeight + 5);
        })
        .call(xAxis)
        .append('g')
        .attr('transform', translate(0, layout.rowHeight + 2.3 * (layout.padding.top)))
        .append('text')
        .text(config.obs)
        .attr('fill', config.colorText)
        .attr('text-anchor', 'middle')
        .attr('x', layout.plotWidth * config.width / 2)
        .style('font-size', config.fontSize )
        .style('font-family', config.fontFamily);
        
    // Adiciona a barra dos efeitos
    var trees = plot.selectAll('.tree').data(data).enter()
        .append('g')
        .classed('tree', true)
        .attr('transform', function(d, i) {
        if (!d.effect) return;

        return translate(0, i * layout.rowHeight);
        });

    trees.append('line')
        .attr('x1', function(d) {
        try { return x(_get_effect(d)[0]); } catch(e) { return 0; }
        })
        .attr('x2', function(d) {
        try { 
                if(_get_effect(d)[2] > config.maxPositive) {
                    return x(config.maxPositive); 
                }
                return x(_get_effect(d)[2]); 
            }
            catch(e) { return 0; }
        })
        .attr('y1', layout.rowHeight / 2)
        .attr('y2', layout.rowHeight / 2)
        .attr('stroke-width', 1)
        .attr('stroke', config.colorText);

    // Seta    
    trees.append('polygon')
    .filter(function(d) {
        try {
            if(_get_effect(d)[2] < config.maxPositive){
                return false;
            }
            return true;
        } catch (e) { return false; }
        })
    .attr('points', function(d) {
        return x(config.maxPositive - 10) + "," + (layout.rowHeight - (layout.rowHeight / 3)) + " " + x(config.maxPositive - 10) + "," + (layout.rowHeight / 3) + " " + x(config.maxPositive) + "," + (layout.rowHeight / 2);
        })            
    .style("fill", config.colorObjectPrimary)
    .style("stroke", config.colorObjectPrimary);

    trees.append('rect')
        .filter(function(d) {
        try {
            _get_effect(d);
            if(d.Size == 0){
                return false;
            }
            if(_get_effect(d)[1] > config.maxPositive){
                return false;
            }
            return true;
        } catch (e) { return false; }
        })
        .datum(function(d) {            
        if(d.Size < 6) {
            d.r =  (6/100) * layout.squareFullSize;
        }
        else {
            d.r = (d.Size/100) * layout.squareFullSize;
        }
            return d;
        })
        .attr('x', function(d) {
            return x(_get_effect(d)[1]) - d.r / 2;
        })
        .attr('y', function(d) { return layout.rowHeight / 2 - d.r / 2; })
        .attr('width', function(d) { return d.r; })
        .attr('height', function(d) { return d.r; })
        .style('fill',  config.colorObjectPrimary);

    // Diamante    
    trees.append('polygon')
    .filter(function(d) {
    try {
        _get_effect(d);
        if(d.Size > 0){
            return false;
        }
        return true;
    } catch (e) { return false; }
    })
    .attr('points', function(d) {
        return x(_get_effect(d)[0]) + "," + (layout.rowHeight / 2) 
                + " " + x(_get_effect(d)[0] + ((_get_effect(d)[2] - _get_effect(d)[0]) / 2)) + "," + (layout.rowHeight / 4)
                + " " + x(_get_effect(d)[2]) + "," + (layout.rowHeight / 2) 
                + " " + x(_get_effect(d)[0] + ((_get_effect(d)[2] - _get_effect(d)[0]) / 2)) + "," + (layout.rowHeight - (layout.rowHeight / 4));
        })           
    .style("fill", config.colorObjectSecondary)
    .style("stroke", config.colorText);

    // Linha central
    if (config.vCentralBar !== null) {
        plot.append('line')
        .attr('x1', x(config.vCentralBar))
        .attr('x2', x(config.vCentralBar))
        .attr('y1', 0)
        .attr('y2', data.length * layout.rowHeight)
        .attr('stroke-width', 1)
        .attr('stroke', config.colorCentralLine)
        .attr('stroke-dasharray', '5, 5')
        .classed('vCentralBar', true);
    }

    // Adiciona a descrição dos efeitos.
    var effectLabels = svg.append('g')
        .attr('width', layout.labelWidth * config.width);

    var labels = table.selectAll('.label').data(data).enter()
        .append('g')
        .classed('label', true)
        .attr('transform', function(d, i) {
            return translate(
            (layout.tableWidth + layout.plotWidth) * config.width + 15,
            layout.rowHeight * i
            );
        });

    labels
        .filter(function(d) {
        try {
            if (d.overrideLabel) return true;

            _get_effect(d);
            return true;
        } catch (e) { return false; }
        })
        .append('text')
        .text(function(d) {
            if (d.overrideLabel) return d.overrideLabel;

            let [_low, _effect, _high] = _get_effect(d);
            if(d.Size > 0){
                return (`${_effect.toFixed(2)} ` +
                    `(${_low.toFixed(2)}, ${_high.toFixed(2)}) - ${d.Size}%`  );
            }
            return (`${_effect.toFixed(2)} ` +
                `(${_low.toFixed(2)}, ${_high.toFixed(2)})`  );
        })
        .style('font-size', config.fontSize - 2)
        .style('font-family', config.fontFamily)
        .attr('dy', (layout.rowHeight - config.fontSize) / 2 + 10)
        .attr('dx', layout.padding.left);
}

//
// Desenha plot de Sen e Spec
//
function _draw_senSpec(config, layout, data, document) {
    
    var svg = d3.select(document).select(config.mountNode).append('svg')
    .attr('width', config.width)
    .attr('height', config.height)
    .append('g')
    .attr('transform', translate(config.margin.left, config.margin.top));

    // Cria a tabela de descricao
    var table = svg.append('g')
        .attr('width', layout.tableWidth * config.width);

    var rows = table.selectAll('.row').data(data).enter()
        .append('g')
        .classed('row', true)
        .attr('transform', function(d, i) {
            return translate(
            0,
            layout.rowHeight * i
            );
        });

    rows
        .append('rect')
        .attr('height', layout.rowHeight)
        .attr('width', '100%')
        .attr('fill', (d, i) => { return (i % 2 === 0)? config.colorSecondary : config.colorPrimary; })
        .attr('x', 0)
        .attr('y', 0);

    rows
        .append('text')
        .text(function(d) { return d.description; })
        .style('font-size', config.fontSize)
        .style('font-family', config.fontFamily)
        .attr('dy', (layout.rowHeight - config.fontSize) / 2 + 10)
        .attr('dx', function(d) {
            return (
            layout.padding.left +
            (d.descriptionOffset || 0) * layout.tabWidth
            );
        });

    // Cria o Gráfico        
    var plot = svg.append('g')
        .attr('width', layout.plotWidth * config.width)
        .attr('transform', translate(layout.tableWidth * config.width, 0))
        .classed('plot', true);

    // Desenha as linhas
    var lowX = 0;
    var highX = 1;

    var x = d3.scaleLinear()
        .domain([lowX, highX])
        .range([0, config.width * layout.plotWidth]);

    var xAxis = d3.axisBottom(x)
        .ticks(config.nTicks);

    // Desenha a descrição
    plot.append('g')
        .attr('transform', function() {
        return translate(0, data.length * layout.rowHeight + 5);
        })
        .call(xAxis)
        .append('g')
        .attr('transform', translate(0, layout.rowHeight + layout.padding.top))
        .append('text')
        .text(config.effectLabel)
        .attr('fill', config.colorText)
        .attr('text-anchor', 'middle')
        .attr('x', layout.plotWidth * config.width / 2)
        .style('font-weight', 'bold')
        .style('font-size', config.fontSize);

    // Desenha a Obs
    plot.append('g')
    .attr('transform', function() {
        return translate(0, data.length * layout.rowHeight + 5);
    })
    .call(xAxis)
    .append('g')
    .attr('transform', translate(0, layout.rowHeight + 2.3 * (layout.padding.top)))
    .append('text')
    .text(config.obs)
    .attr('fill', config.colorText)
    .attr('text-anchor', 'middle')
    .attr('x', layout.plotWidth * config.width / 2)
    .style('font-size', config.fontSize )
    .style('font-family', config.fontFamily);

    // Adiciona a barra dos efeitos
    var trees = plot.selectAll('.tree').data(data).enter()
        .append('g')
        .classed('tree', true)
        .attr('transform', function(d, i) {
        if (!d.effect) return;

        return translate(0, i * layout.rowHeight);
        });

    trees.append('line')
        .attr('x1', function(d) {
        try { return x(_get_effect(d)[0]); } catch(e) { return 0; }
        })
        .attr('x2', function(d) {
        try { return x(_get_effect(d)[2]); } catch(e) { return 0; }
        })
        .attr('y1', layout.rowHeight / 2)
        .attr('y2', layout.rowHeight / 2)
        .attr('stroke-width', 1)
        .attr('stroke', config.colorText);

    trees.append('circle')
        .filter(function(d) {
        try {
            _get_effect(d);

            if(d.Size == 0){
                return false;
            }
            
            return true;
        } catch (e) { return false; }
        })
        .datum(function(d) {
            
        if(d.Size == 0) {
            d.r = 0;
        }
        else {
            d.r = (d.Size/100) * layout.squareFullSize;
        }
        return d;
        })
        .attr('cx', function(d) {
        return (x(_get_effect(d)[1]) - d.r / 2) + d.r;
        })
        .attr('cy', function(d) { return (layout.rowHeight / 2 - d.r / 2) + (d.r/2); })
        .attr('r', function(d) { return d.r; })
        .style("fill", config.colorObjectPrimary);

    // Diamante    
    trees.append('polygon')
        .filter(function(d) {
        try {
            _get_effect(d);
            if(d.Size > 0){
                return false;
            }
            return true;
        } catch (e) { return false; }
        })
        .attr('points', function(d) {
            return x(_get_effect(d)[0]) + "," + (layout.rowHeight / 2) 
                    + " " + x(_get_effect(d)[0] + ((_get_effect(d)[2] - _get_effect(d)[0]) / 2)) + "," + (layout.rowHeight / 4)
                    + " " + x(_get_effect(d)[2]) + "," + (layout.rowHeight / 2) 
                    + " " + x(_get_effect(d)[0] + ((_get_effect(d)[2] - _get_effect(d)[0]) / 2)) + "," + (layout.rowHeight - (layout.rowHeight / 4));
            })       
        .style("fill", config.colorObjectSecondary)
        .style("stroke", config.colorText);

    if (config.vCentralBar !== null) {
        plot.append('line')
        .attr('x1', x(config.vCentralBar))
        .attr('x2', x(config.vCentralBar))
        .attr('y1', 0)
        .attr('y2', data.length * layout.rowHeight)
        .attr('stroke-width', 1)
        .attr('stroke', config.colorCentralLine)
        .attr('stroke-dasharray', '5, 5')
        .classed('vCentralBar', true);
    }

    // Adiciona a descrição dos efeitos.
    var effectLabels = svg.append('g')
        .attr('width', layout.labelWidth * config.width);

    var labels = table.selectAll('.label').data(data).enter()
        .append('g')
        .classed('label', true)
        .attr('transform', function(d, i) {
            return translate(
            (layout.tableWidth + layout.plotWidth) * config.width + 15,
            layout.rowHeight * i
            );
        });

    labels
        .filter(function(d) {
        try {
            if (d.overrideLabel) return true;

            _get_effect(d);
            return true;
        } catch (e) { return false; }
        })
        .append('text')
        .text(function(d) {
            if (d.overrideLabel) return d.overrideLabel;

            let [_low, _effect, _high] = _get_effect(d);
            return (`${_effect.toFixed(2)} ` +
                `(${_low.toFixed(2)}, ${_high.toFixed(2)})`  );
        })
        .style('font-size', config.fontSize)
        .style('font-family', config.fontFamily)
        .attr('dy', (layout.rowHeight - config.fontSize) / 2 + 10)
        .attr('dx', layout.padding.left);
}

module.exports.DrawForest = DrawForest;