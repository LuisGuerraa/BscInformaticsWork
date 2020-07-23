const api = require('../cota-api.js');
const global = require('../global.js');

const handlebars = global.handlebars;

const modListContentsTemplate =
    handlebars.compile(`
    <div class="card-columns card-userGroup m-3 text-center">
       {{#each series}}
       <div class="card">
          <img class="card-img-top img-fluid" src="{{poster_path}}" alt="Card image cap">
          <div class="card-body text-center">
             <h5 class="card-title">{{name}}</h5>
             <a href="#editGroupSeries/remove/{{path ../groupName}}/{{path name}}"
                <button type="button" class="btn btn-danger mt-4 mb-2" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-trash-alt"></i> Remove
                </button>
                </a>
            </div>
          </div>
       {{/each}}
       </div>
`);

module.exports = {
    getView: () => {
        return `
		<h1 id="title"></h1>

		<div id='groups'></div>	
	`
    },
    authenticationRequired: true,

    run: (request) => {
        const title = document.querySelector('#title');
        const itemsContainer = document.querySelector('#groups');
        const name = global.formatName(request.args[0]); 
        title.innerHTML = name;
    
        api.getGroupByName(name)
            .then(group => {
                    if (group.success) {
                        if (group.success.data.series.length) {
                            itemsContainer.innerHTML = modListContentsTemplate({
                                series : group.success.data.series,
                                groupName : name
                            });
                        } else {
                            itemsContainer.innerHTML = global.noResultsTemplate("This Group is Empty")
                        }
                    } else {
                        itemsContainer.innerHTML = global.errorTemplate;
                    }
                }
            );
    }
}
