const metaD3 = require('./lib/metaD3');
const metaRoc = require('./lib/metaRoc');


function draw_forest(data, cb) {
  let plotSvg = metaD3.DrawForest(data, cb);
}
function draw_roc(data, cb) {
  let plotSvg = metaRoc.DrawRoc(data, cb);
}

module.exports = {
  draw_forest_plot: (data, cb) => {
    return draw_forest(data, cb);
  },
  draw_roc_plot: (data, cb) => {
    return draw_roc(data, cb);
  }
};