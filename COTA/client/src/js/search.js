const global = require('./global.js');
const api = require('./cota-api.js');

const handlebars = global.handlebars;

const modListContentsTemplate =
    handlebars.compile(global.seriesGroupTemplate());

module.exports = {
    getView: () => `
		<h1>Search Results</h1>

		<div id='results'></div>	
	`,
    authenticationRequired: false,
    run: (request) => {
        const itemsContainer = document.querySelector('#results');
        const seriesToSearch = request.args[0];

        api.getSeriesWithName(seriesToSearch)
            .then(seriesResult => {
                    if (seriesResult.success) {
                        api.getGroups()
                            .then((allGroups) => {
                                if (allGroups.success) {
                                    let groups = allGroups.success.data;
                                    groups = groups.sort((a, b) => (a.visibility > b.visibility) ? 1 : -1)
                                    itemsContainer.innerHTML = modListContentsTemplate({
                                        series : seriesResult.success.data,
                                        groups : groups
                                    });
                                } else {
                                    itemsContainer.innerHTML = modListContentsTemplate({
                                        series : seriesResult.success.data,
                                        groups : []
                                    });
                                }

                            })
                    } else {
                        itemsContainer.innerHTML = global.noResultsTemplate();
                    }
                }
            )
            .catch((error) => itemsContainer.innerHTML = global.errorTemplate(error.detail));
    }
}
