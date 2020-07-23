const global = require('./global.js');

module.exports = {
    getView: () =>

        `
            <div class="homeItems">
               <div class="jumbotron m-3">
                  <div class="row">
                     <div class="col-md-8">
                        <div class="intro">
                           <h2>Chelas Open Tv Application</h2>
                           <p>Never miss any detail of your favorite tv show and don&#39;t forget those you&#39;ve already seen.<br />
                              <br />
                              <em>Made in Chelas.</em>
                           </p>
                        </div>
                     </div>
                     <div class="col-10 col-sm-4 offset-0">
                        <img class="align-items-center device" src='${global.logo}' style="width: 300px;" />
                        <div class="d-none d-md-block"></div>
                     </div>
                  </div>
               </div>
               <div class="jumbotron features m-3">
                  <div class="row">
                     <div class="col mb-5">
                        <h2 class="text-center">Features</h2>
                        <p class="text-center">Take a peek at all that COTa has to offer</p>
                     </div>
                     </div>
                     <div class="row">
                        <div class="col">
                           <div class="card text-white bg-primary mb-3 h-100 p-3">
                              <i class="fas fa-video featuresIcon"></i>
                              <h3 class="name">MovieDatabase</h3>
                              <p class="description">Based on The Movie Database API with over 1M entries.</p>
                           </div>
                        </div>
                        <div class="col">
                           <div class="card text-white bg-primary mb-3 h-100 p-3">
                              <i class="fas fa-money-bill-alt icon featuresIcon"></i>
                              <h3 class="name">Always Free</h3>
                              <p class="description">COTa is, and always will be, completely free.</p>
                           </div>
                        </div>
                        <div class="col">
                           <div class="card text-white bg-primary mb-3 h-100 p-3">
                              <i class="far fa-user-circle icon featuresIcon"></i>
                              <h3 class="name">Customizable </h3>
                              <p class="description">Create groups with your favorite tv shows.</p>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
               <div class="footer-basic text-center">
                  <footer>
                     <div class="container">
                        <a class="btn btn-primary btn-circle btn-sm footerItem" href="https://github.com/TiagoPereira06"><i class="fab fa-github footerIcon"></i></a>
                        <a class="btn btn-primary btn-circle btn-sm footerItem" href="https://www.linkedin.com/in/tiagomrpereira/"><i class="fab fa-linkedin footerIcon"></i></a>
                        <a class="btn btn-primary btn-circle btn-sm footerItem" href="https://github.com/LuisGuerraa"><i class="fab fa-github footerIcon"></i></a>
                        <a class="btn btn-primary btn-circle btn-sm footerItem" href="https://www.linkedin.com/in/luis-guerra-2319121a1/"><i class="fab fa-linkedin footerIcon"></i></a>
                     </div>
                     <p class="copyright">ISEL-PI-G10 © 2020</p>
                  </footer>
               </div>
            </div>
        `,
    authenticationRequired : false,
    run: () => {
    }
};
