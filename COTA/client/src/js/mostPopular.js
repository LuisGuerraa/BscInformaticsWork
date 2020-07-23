const api = require('./cota-api.js');
const global = require('./global.js');

const handlebars = global.handlebars;


let modListContentsTemplate =
    handlebars.compile(`
   ${global.seriesGroupTemplate()}
    <br>
    <div class='navButtons mx-auto'>
       <button id="prevButton" class="navitem btn btn-primary btn-lg">Prev</button>
       <h5 id="pageNumber" class="navitem">{{pageCount}}</h5>
       <button id="nextButton" class="navitem btn btn-primary btn-lg">Next</button> 
    </div>
`), nextButton, prevButton, page = 1, itemsContainer;

function getSeries(pageCounter, itemsContainer) {
    return api.getPopularSeries(pageCounter)
        .then(popularSeries => {
                if (popularSeries.success)
                    return api.getGroups()
                        .then((allGroups) => {
                                if (allGroups.success) {
                                    let groups = allGroups.success.data;
                                    groups = groups.sort((a, b) => (a.visibility > b.visibility) ? 1 : -1)
                                    itemsContainer.innerHTML = modListContentsTemplate({
                                        series: popularSeries.success.data,
                                        groups : groups,
                                        pageCount: pageCounter
                                    });
                                } else {
                                    itemsContainer.innerHTML = modListContentsTemplate({
                                        series: popularSeries.success.data,
                                        groups : [],
                                        pageCount: pageCounter,
                                    });
                                }
                            }
                        )
            }
        )
        .catch(() => itemsContainer.innerHTML = global.errorTemplate);
}

function fetchNextPage() {
    page++;
    getSeries(page, itemsContainer).then(() => {
        setupListeners()
    });
}

function fetchPrevPage() {
    if (page > 1) {
        page--;
        getSeries(page, itemsContainer).then(() => {
            setupListeners()
        });
    }
}

function setupListeners() {
    nextButton = document.getElementById("nextButton");
    prevButton = document.getElementById("prevButton");
    nextButton.addEventListener("click", fetchNextPage);
    prevButton.addEventListener("click", fetchPrevPage);
}

module.exports = {
    getView: () => `
		<h1>Most Popular TV Shows</h1>

		<div id='mostpopular'>
		${global.spinnerTemplate}
		</div>	
	`,
    authenticationRequired: false,
    run: () => {
        itemsContainer = document.querySelector('#mostpopular');
        getSeries(page, itemsContainer)
            .then(() => setupListeners());
    }

}

