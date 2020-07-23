import React from 'react'
import { Button, Modal, Segment, Form, Grid, Divider, Icon } from 'semantic-ui-react'
import {NotificationManager, NotificationContainer} from 'react-notifications';
import LoginOidc from '../../services/LoginOidc';

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            signup: false
        }
    }

    handleSubmit = async () => {
        this.setState({ isSubmitting:true })
        const { username, password } = this.state
        await this.props.loginService.login(username, password)
        this.props.isLoggedIn()
        this.setState({ isSubmitting:false, showModal: false })
    }

    handleSignUp = async () => {
        this.setState({ isSignedUp:true })
        const { username, password } = this.state
        if(this.state.password !== this.state.repeatPassword) {
            NotificationManager.error("Passwords do not match")
            return
        }
        let usernameHasSpecialChars = username.match(/[^\w]/) //anything not word
        let passwordHasSpecialChars = password.match(/[^\w]/)
        if(usernameHasSpecialChars != null){
            NotificationManager.error("Username cannot contain special characters")
            return
        }
        if(passwordHasSpecialChars != null){
            NotificationManager.error("Password cannot contain special characters")
            return
        }
        await this.props.loginService.signup(username, password)
        this.props.isLoggedIn()
        this.setState({ isSignedUp:false, showModal: false })
    }

    resetModal = () => {
        this.setState({ showModal: true })
    }

    closeModal = () => {
        this.setState({ showModal: false })
    }

    render(){
        return (
            <>
            <Modal closeIcon onClose={this.closeModal} open={this.state.showModal} 
                trigger={<Button as='a' inverted={true} onClick={this.resetModal}><Icon name='user' />User</Button>} 
                centered={false} size='small'>
            <Modal.Header>User Access</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <Segment>
                        <Grid columns={2} relaxed='very' stackable>
                            <Grid.Column>
                                <Form>
                                    <Form.Input
                                        icon='user'
                                        iconPosition='left'
                                        label='Username'
                                        placeholder='Username'
                                        onChange={(e) => this.setState({username: e.target.value})} style={{width:"70%"}}
                                    />
                                    <Form.Input
                                        icon='lock'
                                        iconPosition='left'
                                        label='Password'
                                        type='password'
                                        placeholder='Password'
                                        onChange={(e) => this.setState({password: e.target.value})} style={{width:"70%"}}
                                    />
                                    <>
                                        <Button className={!this.state.isSubmitting? "active" : "positive"} icon='checkmark' 
                                            labelPosition='right' content="Login" 
                                            disabled={!this.state.username || !this.state.password} 
                                            loading={this.state.isSubmitting} 
                                            onClick={this.handleSubmit} />
                                        <LoginOidc />
                                    </>
                                </Form>
                            </Grid.Column>
                            { this.state.signup ?
                                <Grid.Column>
                                    <Form>
                                        <Form.Input
                                            icon='user'
                                            iconPosition='left'
                                            label='Username'
                                            placeholder='Username'
                                            onChange={(e) => this.setState({username: e.target.value})} style={{width:"70%"}}
                                        />
                                        <Form.Input
                                            icon='lock'
                                            iconPosition='left'
                                            label='Password'
                                            type='password'
                                            placeholder='Password'
                                            onChange={(e) => this.setState({password: e.target.value})} style={{width:"70%"}}
                                        />
                                        <Form.Input
                                            icon='lock'
                                            iconPosition='left'
                                            label='Repeat Password'
                                            type='password'
                                            placeholder='Repeat Password'
                                            onChange={(e) => this.setState({repeatPassword: e.target.value})} style={{width:"70%"}}
                                        />
                                        <Button className={!this.state.isSubmitting? "active" : "positive"} icon='signup'
                                            labelPosition='right' content="Sign Up" 
                                            disabled={!this.state.username || !this.state.password} 
                                            loading={this.state.isSubmitting} 
                                            onClick={this.handleSignUp} 
                                        />
                                    </Form>
                                    <Button content='Cancel' circular floated='right' color='red' icon='delete' onClick={() => this.setState({ signup: false })}/>
                                </Grid.Column> :
                                <Grid.Column align='center' verticalAlign='middle'>
                                    <Button content='Sign up' icon='signup' size='big' onClick={() => this.setState({ signup: true })}/>
                                </Grid.Column>
                            }
                        </Grid>
                        <Divider vertical>Or</Divider>
                    </Segment>
                </Modal.Description>
            </Modal.Content>
        </Modal>
        <NotificationContainer/>
        </>
        )
    }
}