import React from 'react'
import { Modal, Button, Popup, List, Label,Form, Input} from 'semantic-ui-react'
import { fetchActionRequest, API, getAction, getUsernames } from '../../services/Service'
import { NotificationContainer, NotificationManager } from 'react-notifications'
import { getContributorDetailsHateoas } from '../../services/Service'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            users : [],
            count : 0
        }
    }

    async componentDidMount() {
        getUsernames().then((users) => {
            if (users) { 
                this.setState( { 
                    users: users.usernames, 
                    count: users.count, 
                    loading:false 
                } ) 
            }
        })
    }

    getContributorActions = (username) => {
        return getContributorDetailsHateoas(this.props.pname, username).then(contributorInfo => {
            if(contributorInfo) {
                this.setState({ userActions: contributorInfo.actions }) 
            }
        }).catch(err => err)
    }

    handleSubmit = async () => {
        this.setState({ showModal: false })
        this.notifyUser(
            await fetchActionRequest(
                getAction(this.props.actions, API.actions.postContributor), {username: this.state.username}),
            `${this.state.username} is now a contributor`,
            201)
        this.props.updateContributors()
    }

    handleDelete = async () => {
        this.setState({deleteState : true})
        let getContributorActions = await this.getContributorActions(this.state.username)
        if(getContributorActions)
            return NotificationManager.error(`${this.state.username} is not a contributor in project '${this.props.pname}'`,'',10000)
        this.notifyUser(
            await fetchActionRequest( getAction(this.state.userActions, API.actions.deleteContributor), null ),
            `${this.state.username} removed from contributor`,
            204)
        this.props.updateContributors()
        this.setState({ showModal: false })
    }

    
    notifyUser = (response,successMessage,successStatus) => {
        if(response.status === successStatus)
            NotificationManager.success(successMessage,'',5000)
        else
            NotificationManager.error(`User ${this.state.username} is already a contributor`,'',10000)
    }

    openModal = () => this.setState({ showModal: true })
    closeModal = () => this.setState({ showModal: false })

    render() {
        const {showModal} = this.state
        return (
            <>
            <Modal closeIcon onClose={this.closeModal} open={showModal} centered={false} size='mini' trigger = {
                <Popup content='Add/Remove project contributor' trigger = {
                    <Button compact size='small' icon='setting' variant='outline-dark' onClick={this.openModal}/>
                }/>   
            }>
                <Modal.Header>Add/Remove '{this.props.pname}' contributor</Modal.Header>
                <Modal.Content>
                    <List horizontal>
                        <Label icon='user' content='Users'/>
                    {this.state.users.map(username => 
                            <List.Item><strong>'{username}'</strong></List.Item>
                        )}
                    </List>
                    <br/><br/>
                    <Form><Form.Field>
                            <Input required={true} onChange={(e) => this.setState({username: e.target.value})} placeholder='Username' />
                    </Form.Field></Form>
                </Modal.Content>
                <Modal.Actions>
                    <Button 
                        className={!this.state.isSubmitting? "active" : "positive"} 
                        size='small'
                        icon='checkmark' 
                        labelPosition='right' 
                        content="Add" 
                        color='green'
                        disabled={!this.state.username} 
                        loading={this.state.isSubmitting} 
                        onClick={this.handleSubmit} />
                    <Button 
                        className={!this.state.isSubmitting? "active" : "positive"} 
                        size='small'
                        icon='remove circle' 
                        color='red'
                        labelPosition='right' 
                        content="Remove" 
                        disabled={!this.state.username} 
                        loading={this.state.isSubmitting} 
                        onClick={this.handleDelete} />
                </Modal.Actions>
            </Modal>
            <NotificationContainer />
            </>
        )
    }

}