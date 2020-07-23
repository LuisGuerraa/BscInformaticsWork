const api = require('../cota-api.js');
const auth = require('../auth.js');
const global = require('../global.js');

const handlebars = global.handlebars;

const modListContentsTemplate =
    handlebars.compile(`
        <div class="m-3">
            {{#this}}
        <div class="card text-center">
        <div class="card-header bg-primary">
        {{name}}
        </div>
        <div class="card-body text-center">
                <p class="card-text"><em>{{desc}}</em></p>
                    <ul class="list-group list-group-flush">
                    {{#series}}
                    <li class="list-group-item">{{name}}<p><i class="far fa-star"></i>{{vote_average}}</p></li>             
                {{/series}}
                </ul>
                </div>
                <div class="card-footer text-muted">
                <i class="fas fa-user"></i> {{./owner}}
                </div>
                </div>
            {{/this}}
        
        </div>
`);

module.exports = {
    getView: () => {
        return `
		<h1>Public Groups</h1>

		<div id='allgroups'></div>	
	`
    },
    authenticationRequired: true,
    run: () => {
        const itemsContainer = document.querySelector('#allgroups');
        /*
                const alertContainer = document.querySelector('#alertContainer');
        */
        const user = auth.getCurrentUser();

        api.getAllPublicGroups()
            .then(publicGroups => {
                    if (publicGroups.success) {
                        const groups = publicGroups.success.data.filter((group) => group.owner !== user);
                        if (!groups.length) {
                            itemsContainer.innerHTML = global.noResultsTemplate();
                        } else {
                            itemsContainer.innerHTML = modListContentsTemplate(groups);
                        }
                    }
                }
            )
    }
}