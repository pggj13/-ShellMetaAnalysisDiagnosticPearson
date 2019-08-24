var eventsRouter = require('express').Router();
var metaD3 = require('../app/API');
//var _ = require('lodash');

eventsRouter.post('/draw_forest_plot/', function (req, res) {
    metaD3.draw_forest_plot(req.body, function (data) {
        res.status(201).json(data || {});
    });
});

eventsRouter.post('/draw_roc_plot/', function (req, res) {

    metaD3.draw_roc_plot(req.body, function (data) {
        res.status(201).json(data || {});
    });
});

module.exports = eventsRouter;