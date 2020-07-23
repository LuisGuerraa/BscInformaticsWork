import React from 'react'
import { Redirect } from 'react-router-dom'
import { UserManager } from 'oidc-client'
import { Button } from 'semantic-ui-react'

const settings = {
	authority: 'http://localhost:8080/openid-connect-server-webapp',
  	client_id: 'projectX',
  	redirect_uri: 'http://localhost:3000/index.html',
 	response_type: 'code',
  	scope: 'openid username'
}

export const userManager = new UserManager(settings)

export default class extends React.Component {
	constructor (props) {
		super(props)
		this.state = { authenticated: false }
		
		this.handleAuthentication = this.handleAuthentication.bind(this)
		this.handleSignout = this.handleSignout.bind(this)
	}

	render () {
		if(!this.state.authenticated)
		return ( 
			<Button disabled icon='checkmark' 
			labelPosition='right' content="Login with MitreID"
			onClick={this.handleAuthentication} />
		)
		const { from } = this.props.location.state || { from: { pathname: "/" } }
        return <Redirect to={from} />
	}

	async handleAuthentication () {
		let user = await userManager.getUser()
		if (!user) {
			try {
				user = await userManager.signinPopup()
				console.log("PAROU")
			} catch (error) {
				console.log(error)
				this.setState({ error: error })
				return
			}
		}
		this.setState({ authenticated: true, user: user })
	}

	isAuthenticated

	async handleSignout () {
		userManager.signoutPopup()
		this.setState({ authenticated: false })
	}
}

export function getBearerAuthorization(user) { // TODO call this token instead
	return 'Bearer ' + user.access_token
}