import PropTypes from 'prop-types'
import React from 'react'
import { Container, Header, Menu, Responsive, Segment, Visibility, Button, Label, Dimmer, Loader } from 'semantic-ui-react'
import { Link } from 'react-router-dom';
import LoginModal from './login/LoginModal';
import Error from './Error'
import { getHomeJsonDocument, loginService } from '../services/Service'
import { NotificationContainer } from 'react-notifications';
import 'react-notifications/lib/notifications.css';

const HomepageHeading = ({ mobile }) => (
    <Container text style={{position:'absolute',left:'200px'}}>
        <Header as='h1'content='Project X' inverted style={{
            fontSize: mobile ? '2em' : '4em',
            fontWeight: 'normal',
            marginBottom: 0,
            marginTop: mobile ? '1.5em' : '3em',
        }}/>
        <Header as='h2' content='Create and Manage your projects with issues and comments!' inverted style={{
            fontSize: mobile ? '1.5em' : '1 .7em',
            fontWeight: 'normal',
            marginTop: mobile ? '0.5em' : '1.5em',
        }}/>
    </Container>
)
  
HomepageHeading.propTypes = { mobile: PropTypes.bool }

export default class extends React.Component{
    constructor(props) {
        super(props)
        this.state = { 
            fixed: false, 
            height: window.innerHeight, 
            isLoggedIn: loginService.isLoggedIn(),
            homeLoading: true,
            homeError: false
        }
    }

    hideFixedMenu = () => this.setState({ fixed: false})
    showFixedMenu = () => this.setState({ fixed: true })

    logout = () => {
        loginService.logout()
        this.setState({ isLoggedIn: loginService.isLoggedIn() })
    }

    async componentDidMount() {
        try {
            await getHomeJsonDocument()
            this.setState({ homeLoading: false })
        } catch(error) {
            this.setState({ error: error, homeError: true, homeLoading: false })
            console.log(error)
        }
    }

    verifyIfUserIsLogged = () => {
        let userIsNowLogged = loginService.isLoggedIn()
        if(userIsNowLogged)
            this.setState({ isLoggedIn: userIsNowLogged })
    }

    render(){
        const { homeError, homeLoading, fixed, isLoggedIn, error } = this.state
        
        return (
        <Responsive>
            { homeLoading ? 
                <Dimmer><Loader indeterminate/>Preparing Files</Dimmer> : null
            }
            { !homeError ? 
            <>
                <HomepageHeading /> 
                <Visibility once={false} onBottomPassed={this.showFixedMenu} onBottomPassedReverse={this.hideFixedMenu}>
                    <Segment inverted vertical raised>
                            <Menu inverted pointing={!fixed} secondary={!fixed} size='large' fixed={fixed && 'top'}>
                                <Container inverted>
                                    <Menu.Item as='a' active>Home</Menu.Item>
                                    <Menu.Item as={Link} to="/projects">Projects</Menu.Item>
                                    <Menu.Item position='right'>
                                        { isLoggedIn ? 
                                            <>
                                                <Label icon='user circle' size='large' content={loginService.getUser().toUpperCase()}></Label>
                                                <Button as='a' inverted={!fixed} primary={fixed} 
                                                    content="Logout" style={{ marginLeft: '0.5em' }} onClick={this.logout} />
                                            </>
                                            : 
                                            <LoginModal loginService={loginService} isLoggedIn={this.verifyIfUserIsLogged} />
                                        }
                                    </Menu.Item>
                                </Container>
                            </Menu>
                    </Segment>
                </Visibility>
            </> : 
                <Error error={error.message}/>
            }
            <NotificationContainer/>
        </Responsive>
        )
    }
}