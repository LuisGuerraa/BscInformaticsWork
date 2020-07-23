import React from 'react'
import { List, Label, Card, Button, Dimmer, Loader, Icon, Dropdown, Modal, Popup } from 'semantic-ui-react'
import EditComponent from '../EditComponent'
import IssueComments from './Comments'
import LabelsModal from './LabelsModal'
import { Route, Redirect, Switch } from 'react-router-dom'
import { getLoginService as LoginService } from '../../services/LoginService'
import { getIssueDetailsHateoas, getProjectDetailsHateoas, getContributorsHateoas, fetchActionRequest, getAction, API } from '../../services/Service'
import { NotificationContainer, NotificationManager } from 'react-notifications'
import HomeBar from '../HomeBar'

const loginService = LoginService()

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            project : {
                name: this.props.pname,
                issueStates: [],
                issueLabels: []
            },
            issue: {
                id: this.props.issueID,
                issueLabels: []
            },
            editStateName : false,
            editStateDesc : false,
            
            editName : false,
            editDesc : false,
            
            editIssue : false,

            isContributor : false,
            
            loading : true,
            showComments: true
        }
    }

    showDeleteConfirm = () => this.setState({ openDeleteModal: true })
    closeDeleteConfirm = () => this.setState({ openDeleteModal: false })

    handleChange = (e, {name, value}) => this.setState(prevState => ({issue : { ...prevState.issue, [name] : value }}))
    
    handleSubmitIssueName = async () => {
        let response = await fetchActionRequest(
            getAction (this.state.actions, API.actions.patchIssueName), { newName: this.state.issue.name }
        )
        response.message = `Name updated!`
        this.notifyUser(response)
        this.setState({editName : false, editStateName: false})
    }

    handleSubmitIssueDescription = async () => {
        let response = await fetchActionRequest(
            getAction(this.state.actions, API.actions.patchIssueDescription), { description: this.state.issue.description }
        )
        response.message = `Description updated!`
        this.notifyUser(response)
        this.setState({editDesc : false, editStateDesc: false})
    }

    notifyUser = (response) => {
        if(response.status === 200)
            NotificationManager.success(response.message,'',5000)
        else
            NotificationManager.error(response.props,'',10000)
    }

    handleSubmitIssueState = async(state) => {
        let response = await fetchActionRequest(
            getAction(this.state.actions, API.actions.patchIssueState), { nextState: state }
        )
        response.message = `State set to '${state}'`
        await this.updateIssueDetails(response, this.state.project.name, this.state.issue.id)
    }

    handleEditName = () => {
        this.setState((prevState) => ({oldName: prevState.issue.name, editName : true, editStateName : true}))
    }

    handleEditDescription = () => {
        this.setState(prevState => ({oldDescription: prevState.issue.description, editDesc : true, editStateDesc : true}))
    }

    closeEditNameHandler = () => {
        let oldIssue = this.state.issue
        oldIssue.name = this.state.oldName
        this.setState({editName : false, editStateName : false, issue: oldIssue})
    }
    
    closeEditDescriptionHandler = () => {
        let oldIssue = this.state.issue
        oldIssue.description = this.state.oldDescription
        this.setState({editDesc : false, editStateDesc : false, issue: oldIssue})
    }

    handleDeleteIssue = async () => {
        let response = await fetchActionRequest( getAction(this.state.actions, API.actions.deleteIssue), null )
        if(response.status === 201) NotificationManager.success(`Issue '${this.state.issue.name}' deleted!`,'',5000)
        else NotificationManager.error(response.props,'',10000)
        this.setState({deleteState : true})
    }

    showComments = () => { this.setState({showComments : true}) }
    hideComments = () => { this.setState({showComments : false}) }

    componentDidMount() {
        this.updateIssueDetails(null, this.state.project.name, this.state.issue.id)
    }

    updateIssueDetails = async (response,pname,issueID) =>{
        if(response !== null){
            if(response.status !== 200){
                NotificationManager.error(response.props,'',10000)
                return
            }
            NotificationManager.success(response.message,'',5000)
        }
        let oldProject = this.state.project
        oldProject.name = pname
        let oldIssue = this.state.issue
        oldIssue.id = issueID
        this.setState({project: oldProject, issue: oldIssue})
        await getProjectDetailsHateoas(pname).then((project) => {
            if (project) { 
                this.setState( { project: project.properties } ) 
            }
        })
        await getContributorsHateoas(pname).then((response) => {
            if (response) { 
                response.entities.forEach(contributor => {
                    if(contributor.properties.username === loginService.getUser()){
                        return this.setState({ isContributor: true })
                    }
                })
            }
        })
        return await getIssueDetailsHateoas(pname, issueID).then((issue) => { 
            if (issue) { 
                this.setState({ 
                    issue: issue.properties, 
                    actions: issue.actions,
                    loading : false
                }) 
            }
        })
    } 
    
    componentWillUnmount() {
        if (this.timerId) {
            clearInterval(this.timerId)
        }
    }

    editNameFunc(editStateName, editName, issue){
        return (
            <Card.Header style={{'padding-left':'10%'}}>
                {editStateName ?
                    <EditComponent 
                        state={editName} 
                        propName='name' 
                        value={issue.name} 
                        handleChange={this.handleChange} 
                        handleSubmit={this.handleSubmitIssueName}
                        closeHandler={this.closeEditNameHandler}/>
                    :  <>
                        {issue.name}
                        <Popup content='Edit Project Description' trigger = {
                            <Button onClick={this.handleEditName} compact size='mini' floated='right' basic circular icon='edit'/>
                        }/>
                    </>
                }
            </Card.Header>
        )
    }

    editDescFunc(editStateDesc, editDesc, issue){
        return (
            <Card.Description style={{'padding-left':'10%'}}>
                { editStateDesc ?
                    <EditComponent 
                        state={editDesc} 
                        propName='description' 
                        value={issue.description} 
                        handleChange={this.handleChange} 
                        handleSubmit={this.handleSubmitIssueDescription}
                        closeHandler={this.closeEditDescriptionHandler}/>
                    :  <>
                        {issue.description}
                        <Popup content='Edit Project Description' trigger = {
                        <Button onClick={this.handleEditDescription} compact size='mini' floated='right' basic circular icon='edit'/>
                        }/>
                    </>
                }
            </Card.Description>
        )
    }

    render() {
        const {issue, loading, editStateName, editStateDesc, editName, editDesc, showComments, actions, isContributor} = this.state
        return (
            <>
            <HomeBar/>  
            <br/><br/>
                { loading ?
                    <Card.Content style={{height:"418px"}}>
                        <Dimmer active inverted>
                            <Loader inverted>Loading</Loader>
                        </Dimmer>
                    </Card.Content>
                :
                    <Card centered>
                        <Card.Content>
                            { loginService.isLoggedIn() && isContributor ?
                                <>
                                    {this.editNameFunc(editStateName, editName, issue)}
                                    {this.editDescFunc(editStateDesc, editDesc, issue)}
                                </>
                            :
                                <>
                                    <Card.Header> {issue.name} </Card.Header>
                                    <Card.Description> {issue.description} </Card.Description>
                                </>
                            }
                            <List>
                                {issue.labelType.map(issueLabel => {
                                    const label = issueLabel === "defect" ? 
                                        <Label icon='bug' content='Defect' /> :
                                        (issueLabel === "new_functionality" ? 
                                            <Label icon='plus' content='New Functionality' />:
                                            <Label icon='bug' content='Exploration' /> )
                                    return (
                                        <List.Item>{label}</List.Item>
                                    )
                                })}
                                { loginService.isLoggedIn() && isContributor && actions &&
                                    <LabelsModal 
                                        pname={this.state.project.name} 
                                        issue={issue} 
                                        projectLabels={this.state.project.issueLabels}
                                        updateIssueDetails={this.updateIssueDetails}
                                        actions={actions}
                                        /> 
                                    
                                }
                            </List>
                        </Card.Content>
                        <Card.Content>
                            <Card>
                                <Card.Content><h3>{issue.stateType.toUpperCase()}</h3></Card.Content>
                                { loginService.isLoggedIn() && isContributor && issue.stateType !== 'archived' && 
                                    <Dropdown button className='icon' labeled text='Change State' icon='arrow down' >
                                        <Dropdown.Menu >
                                            { issue.stateType === 'open' ? 
                                                <>
                                                    <Dropdown.Item 
                                                        disabled={!this.state.project.issueStates.some(state => state === 'closed')} 
                                                        onClick={() => this.handleSubmitIssueState('closed') } 
                                                        icon='folder closed' text='closed '
                                                    />
                                                    <Dropdown.Item 
                                                        disabled={!this.state.project.issueStates.some(state => state === 'archived')} 
                                                        onClick={() => this.handleSubmitIssueState('archived')} 
                                                        icon='archive' text='archived'
                                                    />
                                                </>
                                                :
                                                <>
                                                    <Dropdown.Item 
                                                        disabled={!this.state.project.issueStates.some(state => state === 'open')} 
                                                        onClick={() => this.handleSubmitIssueState('open')} 
                                                        icon='folder open' text='Open'
                                                    />
                                                    <Dropdown.Item 
                                                        disabled={!this.state.project.issueStates.some(state => state === 'archived')} 
                                                        onClick={() => this.handleSubmitIssueState('archived')} 
                                                        icon='archive' text='Archived'
                                                    />
                                                </>
                                            }
                                        </Dropdown.Menu>
                                    </Dropdown>
                                }
                            </Card>
                        </Card.Content>
                        <Card.Content style={{'floated':'left'}} extra>
                            <Icon left name='calendar alternate' />Date: {issue.creationDate}<br/> 
                            { issue.stateType !== 'open' && 
                                <><Icon name='calendar times' />CloseDate: {issue.closeDate}<br/> </>
                            }
                            <Icon name='user' /> Creator: {issue.createdBy}
                        { loginService.isLoggedIn() && isContributor &&
                            <><br/><br/>
                                <Modal size='mini' open={this.state.openDeleteModal} onClose={this.closeDeleteConfirm}
                                    trigger = {
                                        <Button labelPosition='left' compact size='mini' onClick={this.showDeleteConfirm} 
                                        icon><Icon name='delete'/>Delete Issue
                                        </Button>
                                    }
                                >
                                    <Modal.Header>Delete Issue</Modal.Header>
                                    <Modal.Content>
                                        <p>Are you sure you want to delete this issue?</p>
                                    </Modal.Content>
                                    <Modal.Actions>
                                        <Button negative onClick={this.closeDeleteConfirm}>No</Button>
                                        <Button
                                            positive
                                            icon='checkmark'
                                            labelPosition='right'
                                            content='Yes'
                                            onClick={this.handleDeleteIssue}
                                        />
                                    </Modal.Actions>
                                </Modal>
                                { this.state.deleteState && 
                                    <Switch>
                                        <Redirect to={`/projects/${this.state.project.name}/issues`}/>   
                                            <Route exact path={`/projects/${this.state.project.name}/issues`} />
                                    </Switch>
                                }
                           </>
                        }
                        </Card.Content>
                        <Card.Content>
                            <>
                            <Button onClick={this.showComments} labelPosition='left' size='medium' icon>
                                <Icon name='comment'/>Comments
                            </Button>
                            { showComments && 
                                <>
                                    <IssueComments pname={this.state.project.name} issueID={issue.id} isContributor={isContributor}/><br/>
                                    <Button cicular onClick={this.hideComments} compact size='medium' basic icon='hide'/>
                                </>
                            }
                            </>
                        </Card.Content>
                    </Card>
                }
                <NotificationContainer />
            </>
        )
    }
}