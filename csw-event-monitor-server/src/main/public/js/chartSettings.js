// Chart.js global settings

// // Support for dragging a line to show chart value:
// // https://stackoverflow.com/questions/45159895/moving-vertical-line-when-hovering-over-the-chart-using-chart-js
// Chart.defaults.LineWithLine = Chart.defaults.line;
// Chart.controllers.LineWithLine = Chart.controllers.line.extend({
//     draw: function(ease) {
//         Chart.controllers.line.prototype.draw.call(this, ease);
//
//         if (this.chart.tooltip._active && this.chart.tooltip._active.length) {
//             var activePoint = this.chart.tooltip._active[0],
//                 ctx = this.chart.ctx,
//                 x = activePoint.tooltipPosition().x,
//                 topY = this.chart.scales['y-axis-0'].top,
//                 bottomY = this.chart.scales['y-axis-0'].bottom;
//
//             // draw line
//             ctx.save();
//             ctx.beginPath();
//             ctx.moveTo(x, topY);
//             ctx.lineTo(x, bottomY);
//             ctx.lineWidth = 2;
//             ctx.strokeStyle = '#07C';
//             ctx.stroke();
//             ctx.restore();
//         }
//     }
// });

Chart.defaults.global.hover.animationDuration = 10000;

// // Global chart.js tooltip formatting (easier to do it in JS)
// Chart.defaults.global.tooltips.callbacks.label = function(tooltipItem, data) {
//     var dataset = data.datasets[tooltipItem.datasetIndex];
//     var n = dataset.data[tooltipItem.index];
//     if (Number.isInteger(n))
//         return n;
//     return n.toFixed(3);
// };
