const api = require('../cota-api.js');
const global = require('../global');

module.exports = {
    getView: () => {
        return `
		<div id='alert'>
		${global.spinnerTemplate}
        </div>
		<div class="text-center">
		<button type="button" id="backButton" class="btn btn-primary text-center">Go Back</button>
		</div>	
	`
    },
    authenticationRequired: true,
    run: (request) => {
        const operation = global.formatName(request.args[0]);
        const groupName = global.formatName(request.args[1]);
        const seriesName = global.formatName(request.args[2]);
        const alertContainer = document.querySelector('#alert');
        const backButton = document.querySelector('#backButton')

        backButton.onclick = () => {
            window.history.back();
        }

        if (operation === 'add') {
            return api.addSeriesToGroup(groupName, seriesName)
                .then((response) => {
                    if (response.success) {
                        alertContainer.innerHTML = global.successTemplate(`${seriesName} added to ${groupName}`);
                    } else {
                        alertContainer.innerHTML = global.errorTemplate(`${response.error.detail}`);
                    }
                })
        } else {
            return api.deleteSeriesFromGroup(groupName, seriesName)
                .then((response) => {
                    if (response.success) {
                        alertContainer.innerHTML = global.successTemplate(`${seriesName} deleted from ${groupName}`);
                    } else {
                        alertContainer.innerHTML = global.errorTemplate(`${response.error.detail}`);
                    }
                })
        }
    }
}