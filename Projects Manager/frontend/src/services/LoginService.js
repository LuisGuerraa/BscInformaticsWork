import {NotificationManager} from 'react-notifications';

/**
 * Function used to obtain the service associated to the login
 */
export function getLoginService(apiHost) {
  function getAuthorizationToken(username, password) {
      let credentials = `${username}:${password}`
      return `Basic ${btoa(credentials)}`
  }

  const AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
  const USERNAME = "USERNAME"
  const service = apiService(apiHost)

  return {
      /**
       * Method used to perform login with the given credentials. Note that credential verification is not 
       * performed at this time; it is delayed until the first interaction with the API.
       * @param {string} username - the username
       * @param {string} password - the password
       */
      login: async (username, password) => {
        const userloggedIn = service.login(username,password)
        return userloggedIn.then(user => {
          if(user.id) {
            sessionStorage.setItem(USERNAME, username)
            sessionStorage.setItem(AUTH_TOKEN_KEY, getAuthorizationToken(username, password))
            NotificationManager.success(`Welcome ${username}`, 'Login Sucessfull', 3000)
            return
          }
          NotificationManager.error(user.detail, user.status,5000)
        })
      },

      signup: async (username, password) => {
        const userInserted = service.insertUser(username,password)
        return userInserted.then(user => {
          if(user.id){
            sessionStorage.setItem(USERNAME, username)
            sessionStorage.setItem(AUTH_TOKEN_KEY, getAuthorizationToken(username, password))
            NotificationManager.success(`User ${username} created!`, 'Account Created Sucessfull', 3000)
            return
          }
          NotificationManager.error(user.detail,user.status,5000)
        })
      },

      /**
       * Checks whether the user is logged in or not
       */
      isLoggedIn: () => { 
          return sessionStorage.getItem(AUTH_TOKEN_KEY) != null
      },

      /**
       * Gets the authorization token if the user is logged in, or null
       */
      getToken: () => {
        return sessionStorage.getItem(AUTH_TOKEN_KEY)
      },

      getUser: () => {
        return sessionStorage.getItem(USERNAME)
      },

      logout: () => {
        sessionStorage.removeItem(USERNAME)
        return sessionStorage.removeItem(AUTH_TOKEN_KEY)
      }
  }
}

function apiService(apiHost) {
  return {
    login: async (username, password) => {
      const options = {
          method: 'POST',
          headers: {
              'Content-Type':'application/json'
          },
          body: JSON.stringify({
            "username": username,
            "password": password
          })
      }
      const response = await fetch(`${apiHost}/users/login`, options)
      return await response.json()
    },

    insertUser: async (username, password) => {
        console.log(`LoginService.insertUser()`)
        const options = {
            method: 'POST',
            headers: {
                'Content-Type':'application/json'
            },
            body: JSON.stringify({
              "username": username,
              "password": password
            })
        }
        const response = await fetch(`${apiHost}/users`, options)
        return await response.json()
    }
  }
}