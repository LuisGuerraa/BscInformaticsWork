const express = require('express')
const session = require('express-session')
const request = require('request')
const passport = require('passport')

const utils = require('./ciborg-utils')(request)

const api = require('./board-games-data')(request, utils)
const db = require('./ciborg-db.js')(request, utils)
const services = require('./ciborg-services')(api, db, utils)
const gamesApi = require('./ciborg-web-api')(services)

const app = express()

app.use(session({
	resave: false,
	saveUninitialized: false,
	secret: 'iselleic'
}));

app.use(express.json())
app.use('/', express.static(__dirname + '/public'))

app.use(passport.initialize()); 
app.use(passport.session()); 

passport.serializeUser(gamesApi.serializeUser)
passport.deserializeUser(gamesApi.deserializeUser)

app.post('/login', gamesApi.validateLogin)
app.get('/auth', gamesApi.verifyAuthenticated)
app.post('/register', gamesApi.registerUser)
app.put('/logout', gamesApi.logout)

app.get('/games/popular', gamesApi.getPopularGames)
app.get('/games/:gameName', gamesApi.getGamesWithName)
app.get('/groups', gamesApi.getGroups)
app.get('/groups/:groupName', gamesApi.getGroup)
app.get('/groups/:groupName/games/:minPlaytime&:maxPlaytime', gamesApi.getGamesBetweenPlaytime)
app.post('/groups', gamesApi.createGroup)
app.put('/groups/:groupName', gamesApi.updateGroup)
app.put('/groups/:groupName/games/:gameName', gamesApi.addGameToGroup)
app.delete('/groups/:groupName/games/:gameName', gamesApi.deleteGameFromGroup)
app.delete('/groups/:groupName', gamesApi.deleteGroup)

app.listen(utils.SERVER_PORT, () => console.log(`Server listening on port ${utils.SERVER_PORT}.`))