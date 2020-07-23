const auth = require('./auth.js');

module.exports = {
    home: require('./home.js'),
    mostPopular: require('./mostPopular.js'),
    groups : require ('./groups/userGroups.js'),
    search : require ('./search.js'),
    account : require('./userAccount.js'),
    publicGroups : require('./groups/publicGroups'),
    group : require('./groups/groupDetails'),
    editGroup : require('./groups/editGroup'),
    editGroupSeries : require('./groups/addOrRemoveSeries'),
    signUp :auth.signUp,
    signIn  : auth.signIn,
    logout : auth.logout
};