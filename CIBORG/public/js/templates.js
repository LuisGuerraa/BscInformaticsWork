const navbarTemplate = Handlebars.compile(
    `
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand mb-0 h1">CIBORG</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item font-weight-bold">
            <a class="nav-link" href="#groups">Groups<span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item font-weight-bold">
            <a class="nav-link" href="#games/popular">Popular Games<span class="sr-only">(current)</span></a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input id="searchGameInput" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
            <button id="searchGameBtn" class="btn btn-outline-success my-2 my-sm-0" type="button">Search</button>
            <button id="logoutBtn" class="btn btn-outline-danger ml-2 my-2 my-sm-0" type="button">Logout</button>
        </form>
        </div>
    </nav>
    `
)

const loginTemplate = Handlebars.compile(
    `
    <div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100">
				<form class="login100-form validate-form flex-sb flex-w">
					<span class="login100-form-title p-b-20">
                        CIBORG
                    </span>
                    
					<div class="wrap-input100 validate-input m-b-16" data-validate = "Username is required">
						<input id="usernameInput" class="input100" type="text" name="username" placeholder="Username">
						<span class="focus-input100"></span>
					</div>
					
					<div class="wrap-input100 validate-input m-b-16" data-validate = "Password is required">
						<input id="passwordInput" class="input100" type="password" name="pass" placeholder="Password">
						<span class="focus-input100"></span>
					</div>
					
					<div class="container-login100-form-btn m-t-5">
                        <button type="button" class="login100-form-btn">Login</button>
					</div>
                    
					<div class="flex-sb-m p-t-8">
                        <span id="bottom-text">Don't have an account? Register</span><span class="helper">here</span>.
                    </div>                    
                </form>
			</div>
		</div>
	</div>
    `
)

const gameTemplate = Handlebars.compile(
    `
    <div class="content-box">
        <div class="mt-4 mb-2 text-center" style="max-height: 450px; width: 100%; height: 40%;">
            <img src="{{image}}" style="max-height: 100%; max-width: 100%;"/><br>
        </div>
        <div class="clearfix" style="position:relative">
            <h1 class="float-left">{{name}}</h1>
        </div>
        <p>{{{desc}}}</p>
        <div id="gameInfoFooter" class="clearfix">
            <p class="lead float-left text-muted">{{rating}}</p>
            <img class="float-left text-muted" src="../img/star.png" style="margin: 6px 4px 0 4px; width: 18px; height: 18px;"/>
            <p class="lead float-left text-muted">{{minPlaytime}}-{{maxPlaytime}} mins</p>
        </div>
    </div>
    `
)

const gamesTemplate = Handlebars.compile(
    `
    <div class="content-box">
        <table class="table">
            <thead>
                <tr>
                <th scope="col">Name</th>
                <th scope="col">Minimum Playtime</th>
                <th scope="col">Maximum Playtime</th>
                <th scope="col">Rating</th>
                </tr>
            </thead>
            <tbody>
            {{#each this}}
            <tr>
                <td scope="row">
                    <img src="{{image}}" style="border-radius: 50%; width: 40px; height: 40px; margin-right:10px;"/>
                    <a href="#games/{{name}}">{{name}}</a>
                </td>
                <td>{{minPlaytime}}</td>
                <td>{{maxPlaytime}}</td>
                <td>{{rating}}</td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
    `
)

const popularGamesTemplate = Handlebars.compile(
    `
    <div class="content-box">
        <table class="table">
            <thead>
                <tr>
                <th scope="col">Name</th>
                <th scope="col">Minimum Playtime</th>
                <th scope="col">Maximum Playtime</th>
                <th scope="col">Rating</th>
                </tr>
            </thead>
            <tbody>
            {{#each this}}
            <tr>
                <td scope="row">
                    <img src="{{image}}" style="border-radius: 50%; width: 40px; height: 40px; margin-right:10px;"/>
                    <a href="#games/{{name}}">{{name}}</a>
                </td>
                <td>{{minPlaytime}}</td>
                <td>{{maxPlaytime}}</td>
                <td>{{rating}}</td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
    `
)

const groupsTemplate = Handlebars.compile(
    `
    <div class="content-box">
        <h2>{{username}}'s groups</h2>
        <table class="table">
            <thead>
                <tr>
                <th scope="col">Name</th>
                <th scope="col">Description</th>
                <th scope="col">Games</th>
                <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
            {{#each groups}}
            <tr>
                <td id="groupName"><a id="groupNameHref" href="#groups/{{name}}">{{name}}</a></td>
                <td>{{desc}}</td>
                <td>{{games.length}}</td>
                <td><i id="deleteGroup" class="fas fa-trash" style="color:red; cursor:pointer;"></i></td>
            </tr>
            {{/each}}
            </tbody>
        </table>

        <div class="text-center">
            <div id="loading" class="spinner-border text-primary" role="status" style="visibility: hidden;">
                <span class="sr-only">Loading...</span>
            </div>
        </div>
    </div>
    `
)

const groupTemplate = Handlebars.compile(
    `
    <div class="content-box">
        <div class="mt-3">
            <h2 id="groupNameLabel">{{name}}</h2>
            <i id="groupDescLabel" class="float-left">{{desc}}</i>
            <div id="groupBtns" class="clearfix" style="position:relative">
                <button data-toggle="modal" data-target="#editGroupModal" style="margin-left: 5px" type="button" class="btn btn-primary btn-sm float-right"><i class="fas fa-users-cog"></i></button>
            </div>
            <table class="table mt-3">
                <thead>
                    <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Minimum Playtime</th>
                    <th scope="col">Maximum Playtime</th>
                    <th scope="col">Rating</th>
                    <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                {{#each games}}
                <tr>
                    <td id="gameName" scope="row">
                        <img src="{{image}}" style="border-radius: 50%; width: 40px; height: 40px; margin-right:10px;"/>
                        <a id="gameNameHref" href="#games/{{name}}">{{name}}</a>
                        <td>{{minPlaytime}}</td>
                    </td>
                    <td>{{maxPlaytime}}</td>
                    <td>{{rating}}</td>
                <td><i id="deleteGame" class="fas fa-trash" style="color:red; cursor:pointer;"></i></td>
                </tr>
                {{/each}}
                </tbody>
            </table>
            
            <div class="text-center">
                <div id="loading" class="spinner-border text-primary" role="status" style="visibility: hidden;">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
        </div>
    </div>
    `
)

const addGroupTemplate = Handlebars.compile(
    `
    <h3>Add new group</h3>
    <form style="width:75%">
        <div class="form-group">
            <label>Name</label>
            <input id="groupName" class="form-control" placeholder="Enter group name">
            <small id="groupNameHelp" class="form-text text-muted">Max. {{maxGroupNameLength}} characters.</small>
        </div>
        <div class="form-group">
            <label>Description</label>
            <input id="groupDescription" class="form-control" placeholder="Description">
            <small id="groupDescriptionHelp" class="form-text text-muted">Max. {{maxGroupDescLength}} characters.</small>
        </div>
        <button id="createGroupBtn" type="button" class="btn btn-primary">Submit</button>
    </form>
    `
)

const alertSuccessTemplate = Handlebars.compile(
    `
    <div class="alert alert-success" role="alert" style="position: absolute; right: 30px; top: 30px; z-index: 9999;">
        <div id="alert-message" style="font-weight: bold"></div>
    </div>
    `
)

const alertDangerTemplate = Handlebars.compile(
    `
    <div class="alert alert-danger" role="alert" style="position: absolute; right: 30px; top: 30px; z-index: 9999;">
        <div id="alert-message" style="font-weight: bold"></div>
    </div>
    `
)

const listGroupsTemplate = Handlebars.compile(
    `
    <div id="listGroupsModal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">Choose a group</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
        {{#each this}}
            <p id="groupName" style="cursor: pointer">{{name}}</p>
        {{/each}}
        </div>
        </div>
    </div>
    </div>
    `
)

const filterBetweenPlaytimeModalTemplate = Handlebars.compile(
    `
    <div id="filterBetweenPlaytimeModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Minute range filter</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="minPlaytimeInput">Minimum</label>
                        <input type="number" class="form-control" id="minPlaytimeInput" placeholder="Lower limit">
                    </div>
                    <div class="form-group">
                        <label for="maxPlaytimeInput">Maximum</label>
                        <input type="number" class="form-control" id="maxPlaytimeInput" placeholder="Upper limit">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="applyPlaytimeFilterBtn" type="button" class="btn btn-primary">Apply</button>
            </div>
        </div>
    </div>
    `
)

const editGroupModalTemplate = Handlebars.compile(
    `
    <div id="editGroupModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Editing '{{name}}'</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="editGroupNameInput">Name</label>
                        <input class="form-control" id="editGroupNameInput" placeholder="{{name}}">
                    </div>
                    <div class="form-group">
                        <label for="editGroupDescInput">Description</label>
                        <input class="form-control" id="editGroupDescInput" placeholder="{{desc}}">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="applyEditGroupBtn" type="button" class="btn btn-primary">Apply</button>
            </div>
        </div>
    </div>
    `
)

const addGameToGroupBtnTemplate = Handlebars.compile(
    `
    <p id="addGameToGroupBtn" data-toggle="modal" data-target="#listGroupsModal" class="lead float-right text-muted" style="cursor: pointer"><u>Add game to group</u></p>
    `
)

const filterBetweenPlaytimeBtnTemplate = Handlebars.compile(
    `
    <button id="filterBetweenPlaytimeBtn" type="submit" data-toggle="modal" data-target="#filterBetweenPlaytimeModal" class="lead float-right btn btn-primary btn-sm" style="cursor: pointer"><i class="fas fa-sliders-h"></i></button>
    `
)