const handlebars = require('handlebars');
handlebars.registerHelper('path', (string) => {
    return string.replaceAll(" ", "%20");
});

handlebars.registerHelper('groupVis', (visibility) => {
    if (visibility === 'public') {
        return '<i class="fas fa-lock-open float-right"></i>'
    } else {
        return '<i class="fas fa-lock float-right text-danger"></i>'
    }
});

handlebars.registerHelper('count', (array) => {
    return `${array.length}`;
});

handlebars.registerHelper('checkDup', (group, name) => {
    const duplicate = (series) => series.name === name;
    if (group.series.some(duplicate)) {
        return 'disabled'
    }
});

handlebars.registerHelper('checkEmpty', (array) => {
    if (!array.length) {
        return 'disabled'
    }
});


function formatName(name) {
    return name.replaceAll("%20", " ");
}

function seriesGroupTemplate() {
    return `<div class="card-columns card-popular m-3">
       {{#each series}}
       <div class="card">
          <img class="card-img-top img-fluid" src="{{poster_path}}" alt="Card image cap">
          <div class="card-body text-center">
             <h5 class="card-title">{{name}}</h5>
             <p class="card-text">{{overview}}</p>
             <div class="progress">
                <div class="progress-bar bg-primary" role="progressbar" style="width: {{votes_average}}%">{{votes_average}}</div>
             </div>
             {{#if ../groups}}
             <div class="dropdown mt-4 mb-2">
                <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-plus"></i> Add
                </button>
                <div class="dropdown-menu">
                <h6 class="dropdown-header">Groups</h6>
                   {{#each ../groups}}
                   <a class="dropdown-item {{checkDup this ../name}}" href=#editGroupSeries/add/{{path name}}/{{path ../name}}>{{{groupVis visibility}}} {{name}}</a>
                   {{/each}}
                </div>
             </div>
             {{/if}}
          </div>
       </div>
       {{/each}}
    </div>`;
}

module.exports = {
    logo: require('../images/logo.png').default,
    errorTemplate: (msg) => {
        if (msg) {
            return `
                <div class="alert alert-danger m-5 text-center" role="alert">
                ${msg}
                </div>
                	`
        } else {
            return `
                <div class="alert alert-danger m-5 text-center" role="alert">
                Something Went Wrong - Check Your Connection
                </div>
                	`
        }
    },
    successTemplate: (msg) => {
        if (msg) {
            return `
                <div class="alert alert-success m-5" role="alert">
                ${msg}
                </div>
                	`
        } else {
            return `
                <div class="alert alert-success m-5" role="alert">
                Operation Completed Successfully
                </div>
                	`
        }
    },
    noResultsTemplate: (msg) => {
        if (msg) {
            return `
                <div class="alert alert-warning m-5" role="alert">
                ${msg}
                </div>
                	`
        } else {
            return `
                <div class="alert alert-warning m-5" role="alert">
                No Results Found
                </div>
                	`
        }
    },
    handlebars: handlebars,
    formatName: formatName,
    spinnerTemplate: `
		    <div class="text-center">
		    <div class="spinner-border text-primary m-5" style="width: 3rem; height: 3rem;" role="status">
            <span class="sr-only">Loading...</span>
            </div>
            </div>
            `,
    seriesGroupTemplate : seriesGroupTemplate
}
