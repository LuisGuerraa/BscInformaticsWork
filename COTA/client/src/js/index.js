require('../css/style.css');
require ('../css/bootstrap.css');
import '@fortawesome/fontawesome-free/js/fontawesome'
import '@fortawesome/fontawesome-free/js/solid'
import '@fortawesome/fontawesome-free/js/regular'
import '@fortawesome/fontawesome-free/js/brands'
import 'bootstrap';


const global = require('./global.js');
const routes = require('./routes.js');
const auth = require('./auth.js');

window.onload = () => {

    const [mainContainer, userinfo] = setBaseTemplate();

    auth.initialize(userinfo)
        .then(() => {
                window.onhashchange = onHashChange;
                if (location.hash) {
                    onHashChange();
                } else {
                    location.hash = '#home';
                }
            }
        );

    function onHashChange() {
        let [modName, ...args] = location.hash.substring(1).split('/');

        const module = getModule(modName);

        const request = {'name': modName, 'args': args};

        const currentUser = auth.getCurrentUser();
        const authReq = module.authenticationRequired;

        if (authReq && (currentUser === null)) {
            alert("You Must Be Logged In To Access This Feature");
            location.hash = '#home';
        } else {
            module.getView && (mainContainer.innerHTML = module.getView());
            module.run && module.run(request);
        }
    }

    function setBaseTemplate() {
        document.body.innerHTML =
            `
            <nav>
               <div class='navbar navbar-expand-lg navbar-dark bg-dark'>
                  <a class="navbar-brand" href="#home">
                  <img src='${global.logo}' width="75" height="30" alt="">
                  </a>
                  <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                     <div class="navbar-nav" id="navBarLinks">
                        <a href='#mostPopular' class="nav-item nav-link" >MostPopular</a>		
                        <a href='#publicGroups' class="nav-item nav-link" >Public Groups</a>	
                        <div class="form-inline">
                           <input  id="searchForm" class="form-control mr-sm-2" type="search" placeholder="Search">
                           <button id="searchButton" class="btn btn-primary"><i class="fas fa-search"></i></button>
                        </div>
                     </div>
                     <div id='userInfo'>
                     </div>
                  </div>
               </div>
            </nav>
            <hr>
            <div id='mainContainer'></div>
            <div class="w-25 alertDiv" id="alertContainer"></div>
		    `;

        const mainContainer = document.querySelector('#mainContainer');
        const userInfo = document.querySelector('#userInfo');

        document.getElementById("searchButton").addEventListener("click",redirectSearchResult);


        return [mainContainer, userInfo];
    }

    function redirectSearchResult() {
        const query = document.getElementById("searchForm").value;
        if (query)
            location.hash = `#search/${query}`;
    }

    function getModule(name) {

        const modDefault = {
            getView: (req) => '<h1>' + req.name + '</h1>',
            authenticationRequired: false,
            run: () => {
            }
        };

        return routes[name] || modDefault;
    }


}
