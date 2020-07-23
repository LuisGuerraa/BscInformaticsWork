const api = require('../cota-api.js');
const global = require('../global.js');

module.exports = {
    getView: () => {
        return `
		<h1 id="title"></h1>
		
		<div id="alert" class="m-4"></div>

		<div class="col-lg-6 offset-lg-3" id="formDiv">
			    <div class="form-group">
				    <label for='txtName'>Group Name: </label>
				    <input type='text' class="form-control" id='txtName' placeholder="Enter New Name" required><br>
				    <label for='txtDesc'>Group Description : </label>
				    <input type='text' class="form-control" id='txtDesc' placeholder="Enter New Description" required><br>
				    <button type="submit" class="btn btn-success" id='butUpdate'>Update</button>
			    </div>
			</div>
	`
    },
    authenticationRequired: true,

    run: (request) => {
        const title = document.querySelector('#title');
        const alertDiv = document.querySelector('#alert');
        const formDiv = document.querySelector('#formDiv');
        const oldGroupName = global.formatName(request.args[0]);
        title.innerHTML = `${oldGroupName} Group Editor`;

        const txtNameField = document.querySelector('#txtName');
        const txtDescField = document.querySelector('#txtDesc');
        const butLogin = document.querySelector('#butUpdate');

        butLogin.onclick = () => {
            const name = txtNameField.value;
            if (name.length === 0) {
                alert('Username is empty.');
                return;
            }
            const desc = txtDescField.value;
            if (desc.length === 0) {
                alert('Password is empty.');
                return;
            }

            alertDiv.innerHTML = global.spinnerTemplate;

            return api.editGroup(oldGroupName, name, desc)
                .then(response => {
                    formDiv.innerHTML = `
                            <div class="text-center">
		                    <button type="button" id="backButton" class="btn btn-primary text-center">Go Back</button>
		                    </div>`;
                    const backButton = document.querySelector('#backButton')

                    backButton.onclick = () => {
                        window.history.back();
                    }
                    if (response.success) {
                        alertDiv.innerHTML = global.successTemplate(`${oldGroupName} group is now ${name}`);
                    } else {
                        alertDiv.innerHTML = global.errorTemplate(response.error.detail)
                    }
                })
        }
    }
}