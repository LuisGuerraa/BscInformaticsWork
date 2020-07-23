const api = require('./cota-api.js');
const auth = require('./auth.js');

module.exports = {
    getView: () => {
        let currentUser = auth.getCurrentUser();
        return `
            <h1>${currentUser}'s Account</h1>
            <div class="container">
            <div class="row h-100 justify-content-center align-items-center"">
            <div class="col-sm">
               <div class="card">
                  <h5 class="card-header text-center bg-primary ">Account Statistics</h5>
                  <div class="card-body">
                     <h5 class="card-title"><i class="fas fa-lock-open"></i> Public</h5>
                     <p class="card-text" id="publicGroupsCount"></p>
                     <h5 class="card-title"><i class="fas fa-lock"></i> Private</h5>
                     <p class="card-text" id="privateGroupsCount"></p>
                     <h5 class="card-title"><i class="fas fa-tv"></i> Series</h5>
                     <p class="card-text" id="seriesCount"></p>
                  </div>
               </div>
            </div>
            <div class="col-sm">
               <h5 class="text-center mb-4">Not Enough? Create A New Group!</h5>
                  <div class="form-group row">
                     <label for="groupName" class="col-sm-2 col-form-label">Name</label>
                     <div class="col-sm-10">
                        <input type="text" class="form-control" id="groupName" placeholder="Enter Group Name" required>
                     </div>
                  </div>
                  <div class="form-group row">
                     <label for="groupDesc" class="col-sm-2 col-form-label">Description</label>
                     <div class="col-sm-10">
                        <input type="text" class="form-control" id="groupDesc" placeholder="Enter Group Description" required>
                     </div>
                  </div>
                  <fieldset class="form-group">
                     <div class="row">
                        <legend class="col-form-label col-sm-2 pt-0">Visibility</legend>
                        <div class="col-sm-10">
                           <div class="form-check">
                              <input class="form-check-input" type="radio" name="gridRadios" id="publicRadio" value="public" checked>
                              <label class="form-check-label" for="publicRadio">
                              Public
                              </label>
                           </div>
                           <div class="form-check">
                              <input class="form-check-input" type="radio" name="gridRadios" id="privateRadio" value="private">
                              <label class="form-check-label" for="privateRadios">
                              Private
                              </label>
                           </div>
                        </div>
                  </fieldset>
                  <div class="form-group row">
                  <div class="col text-center">
                  <button id="createButton" class="btn btn-primary">Create</button>
                  </div>
                  </div>
               </div>
            </div>
            <div class="row">
               <div class="col-sm">
                  <div class = "mx-auto">
                     <a href="#groups" class="btn btn-primary btn-lg btn-block mt-5">My Groups</a>
                  </div>
               </div>
            </div>
	        `
    },
    authenticationRequired: true,
    run: () => {
        api.getGroups()
            .then(allGroups => {
                    if (allGroups.success) {
                        let publicField = document.getElementById("publicGroupsCount");
                        let privateField = document.getElementById("privateGroupsCount");
                        let seriesCountField = document.getElementById("seriesCount");
                        let publicCounter = allGroups.success.data.filter((group) => group.visibility === "public");
                        let privateCounter = allGroups.success.data.filter((group) => group.visibility === "private");
                        let seriesCounter = 0;
                        allGroups.success.data.forEach((group) => {
                            seriesCounter += group.series.length;
                        })
                        publicField.innerHTML = `<b>${publicCounter.length}</b> <u>Public</u> Groups Registered`
                        privateField.innerHTML = `<b>${privateCounter.length}</b> <u>Private</u> Groups Registered`
                        seriesCountField.innerHTML = `Watched <b>${seriesCounter}</b> Series`
                    }
                }
            ).catch(err => {
            alert(err)
        });

        const txtName = document.querySelector('#groupName');
        const txtDescription = document.querySelector('#groupDesc');
        const radioPrivate = document.querySelector('#privateRadio');
        const createButton = document.querySelector('#createButton');

        createButton.onclick = () => {

            let visibility = 'public';
            const groupName = txtName.value;
            const groupDesc = txtDescription.value;

            if (radioPrivate.checked) {
                visibility = 'private';
            }

            const name = txtName.value;
            if (name.length === 0 || name.length >= 10) {
                alert('Group Name invalid');
                return;
            }
            const desc = txtDescription.value;
            if (desc.length === 0) {
                alert('Group Description is empty.');
                return;
            }

            return api.createGroup(groupName, groupDesc, visibility)
                .then(createResponse => {
                    if (createResponse.success) {
                        alert(`New Group ${groupName} Created`)
                        run();
                    } else {
                        return Promise.reject(createResponse);
                    }
                }).catch(rejected => {
                    alert(rejected.error.detail);
                })

        }

    }
}
