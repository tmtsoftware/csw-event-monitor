// Chart.js global settings

// Support for dragging a line to show chart value:
// https://stackoverflow.com/questions/45159895/moving-vertical-line-when-hovering-over-the-chart-using-chart-js
Chart.defaults.LineWithLine = Chart.defaults.line;
Chart.controllers.LineWithLine = Chart.controllers.line.extend({
    draw: function(ease) {
        Chart.controllers.line.prototype.draw.call(this, ease);

        if (this.chart.tooltip._active && this.chart.tooltip._active.length) {
            var activePoint = this.chart.tooltip._active[0],
                ctx = this.chart.ctx,
                x = activePoint.tooltipPosition().x,
                // topY = this.chart.scales['y-axis-0'].top,
                // bottomY = this.chart.scales['y-axis-0'].bottom;
                topY = 0,
                bottomY = this.chart.scales['y-axis-0'].bottom + 40;

            // draw line
            ctx.save();
            ctx.beginPath();
            ctx.moveTo(x, topY);
            ctx.lineTo(x, bottomY);
            ctx.lineWidth = 2;
            // ctx.strokeStyle = '#07C';
            ctx.strokeStyle = "#333";
            ctx.setLineDash([10, 10]);
            ctx.stroke();
            ctx.restore();
        }
    }
});

Chart.defaults.global.hover.animationDuration = 0;
Chart.defaults.global.hover.intersect = false;
Chart.defaults.global.hover.mode = 'nearest';

// Global chart.js tooltip formatting (easier to do it in JS)
Chart.defaults.global.tooltips.callbacks.label = function(tooltipItem, data) {
    var n = tooltipItem.yLabel;
    if (Number.isInteger(n))
        return n;
    return n.toFixed(3);
};

Chart.defaults.global.tooltips.callbacks.title = function(tooltipItem, data) {
    var d = data.datasets[0].data[tooltipItem[0].index].x;
    return ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2) + ":" + ("0" + d.getSeconds()).slice(-2);
};
