var globalModules = {
    materialize_css: "materialize-css",
    moment: "moment",
    chartjs_plugin_streaming: "chartjs-plugin-streaming"
};

const importRule = {
    // Force require global modules
    test: /.*-(fast|full)opt\.js$/,
    loader:
        "imports-loader?" +
        Object.keys(globalModules)
            .map(function(modName) {
                return modName + "=" + globalModules[modName];
            })
            .join(",")
};

const exposeRules = Object.keys(globalModules).map(function(modName) {
    // Expose global modules
    return {
        test: require.resolve(globalModules[modName]),
        loader: "expose-loader?" + globalModules[modName]
    };
});

const allRules = exposeRules.concat(importRule);

module.exports = {
    performance: { hints: false },
    module: {
        rules: allRules
    }
};