import React from 'react'
import { List, Label, Card, Button, Dimmer, Loader , Modal, Icon, Popup } from 'semantic-ui-react'
import EditComponent from '../EditComponent'
import EditProjectModal from './ProjectModalEdit'
import Contributors from './Contributors'
import { Redirect, Switch, Link } from 'react-router-dom'
import { getProjectDetailsHateoas, getContributorsHateoas, getAction, API, fetchActionRequest, loginService } from '../../services/Service'
import { NotificationContainer, NotificationManager } from 'react-notifications';
import HomeBar from '../HomeBar'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            project : {
                name: undefined,
                description: undefined,
                issueStates: [],
                issueLabels: []
            },
            loading : true,
            isContributor : false,
            afterEditName: false
        }
    }

    showDeleteConfirm = () => () => this.setState({ openDeleteModal: true })
    closeDelete = () => this.setState({ openDeleteModal: false })

    handleChange = (e, {name, value}) => {
        this.setState(prevState => ({project : { ...prevState.project, [name] : value }}))
    }

    handleSubmitProjectName = async () => {
        let action = getAction(this.state.actions, API.actions.patchProjectName)
        this.notifyUser(
            await fetchActionRequest(
                action, { name: this.state.project.name }),
            `Project name changed to '${this.state.project.name}'`,
            200)

        this.setState({editName : false, editStateName: false, afterEditName: true})
    }

    handleSubmitProjectDescription = async () => {
        this.notifyUser(
            await fetchActionRequest(
                getAction(this.state.actions, API.actions.patchProjectDescription), { description: this.state.project.description }),
            `Project description changed to '${this.state.project.description}'`,
            200)
        this.setState({editDescription : false, editStateDesc: false})
    }

    handleEditName = () => {
        this.setState(prevState => ({oldName: prevState.project.name, editName : true, editStateName : true}))
    }

    handleEditDescription = () => {
        this.setState(prevState => ({oldDescription: prevState.project.description, editDescription : true, editStateDesc : true}))
    }

    closeEditNameHandler = () => {
        let oldProject = this.state.project
        oldProject.name = this.state.oldName
        this.setState({editName : false, editStateName : false, project : oldProject})
    }
    
    closeEditDescriptionHandler = () => {
        let oldProject = this.state.project
        oldProject.description = this.state.oldDescription
        this.setState({editDescription : false, editStateDesc : false, project : oldProject})
    }

    handleDeleteProject = async () => {
        this.notifyUser(
            await fetchActionRequest(
                getAction(this.state.actions, API.actions.deleteProject), null),
            `Project ${this.state.project.name} deleted!`,
            204)
        this.setState({deleteState : true})
    }

    componentDidMount() {
        this.getProjectsAndContributors(this.props.pname)
    }

    getProjectsAndContributors = (pname) => {
        this.updateProjectDetail(pname)
        getContributorsHateoas(pname).then((response) => {
            if (response) { 
                response.entities.forEach(contributor => {
                    if(contributor.properties.username === loginService.getUser()){
                        return this.setState({ isContributor: true })
                    }
                })
            }
        })
    }

    updateProjectDetail = async(pname) => {
        await getProjectDetailsHateoas(pname).then((project) => {
            if (project) { 
                this.setState({ 
                    project: project.properties, 
                    actions: project.actions,
                    loading : false
                }) 
            }
        })
    }

    setResponse = (response, pname) => {
        this.updateProjectDetail(pname)
        if(response.status === 200)
            NotificationManager.success(`Project ${pname} updated!`,'',5000)
        else
            NotificationManager.error(response.props,'',10000)
    }

    componentWillUnmount() {
        if (this.timerId) {
            clearInterval(this.timerId)
        }
    }

    notifyUser = (response,successMessage,successStatus) => {
        if(response.status === successStatus)
            NotificationManager.success(successMessage,'',5000)
        else
            NotificationManager.error(response.props,'',10000)
    }

    editNameFunc(editStateName, editName, project) {
        return (
            <Card.Header style={{'paddingLeft':'10%'}}>
                {editStateName ?
                    <EditComponent 
                        state={editName} 
                        propName='name' 
                        value={project.name}
                        handleChange={this.handleChange}  
                        handleSubmit={this.handleSubmitProjectName}
                        closeHandler={this.closeEditNameHandler}/>
                    :  
                    <>
                        {project.name}
                        <Popup content='Edit Project Name' trigger = {
                            <Button onClick={this.handleEditName} compact size='mini' floated='right' basic circular icon='edit'/>
                        }/>
                    </>
                }
            </Card.Header>
        )
    }

    editDescFunc(editStateDesc, editDescription, project) {
        return (
            <Card.Description style={{'paddingLeft':'10%'}}>
                {editStateDesc ?
                    <EditComponent 
                        state={editDescription} 
                        propName='description' 
                        value={project.description} 
                        handleChange={this.handleChange} 
                        handleSubmit={this.handleSubmitProjectDescription}
                        closeHandler={this.closeEditDescriptionHandler}/>
                    : 
                    <>
                        {project.description}
                        <Popup content='Edit Project Description' trigger = {
                            <Button onClick={this.handleEditDescription} compact size='mini' floated='right' basic circular icon ='edit'/>
                        }/>
                    </>
                }
            </Card.Description>
        )
    }

    render() {
        const { project, editName, editDescription, editStateName, editStateDesc, deleteState, loading, openDeleteModal, afterEditName, isContributor } = this.state
        return (
            <>
            <HomeBar/>
            <br/><br/>
            <Card centered>
            { loading ?
                <Card.Content style={{ opacity:0.8 }}>
                    <Dimmer active inverted>
                        <Loader inverted>Loading</Loader>
                    </Dimmer>
                </Card.Content>
                :
                <>
                    <Card.Content>
                        { loginService.isLoggedIn() && isContributor ?
                            <>
                                {this.editNameFunc(editStateName, editName, project)}
                                {this.editDescFunc(editStateDesc, editDescription, project)}
                                { project.issueStates || project.issueLabels ? 
                                    <><br/><EditProjectModal project={project} actions={this.state.actions} setResponse={this.setResponse}/><br/></> 
                                : null }
                                <br/><Contributors pname={this.state.project.name} />
                            </>
                            :
                            <>
                                <Card.Header>{project.name}</Card.Header>
                                <Card.Description>{project.description}</Card.Description>
                            </>
                        }
                        <br/>
                        </Card.Content>
                        <Card.Content>
                        <Card.Description>
                            <strong>Issue States: </strong>
                            <List>
                                {
                                    project.issueStates.map(issueState => {
                                        const state = issueState === "open" ? 
                                            <Label icon='folder open' content='Open' />  :
                                            (issueState === "closed" ? 
                                                <Label icon='folder' content='Closed' /> :
                                                <Label icon='archive' content='Archived' />)
                                        return (
                                            <List.Item>{state}</List.Item>
                                        )
                                    })
                                }
                            </List>
                            <strong>Issue Labels:</strong>
                            <List>
                                { project.issueLabels &&
                                    project.issueLabels.map(issueLabel => {
                                        const label = issueLabel === "defect" ? 
                                            <Label icon='bug' content='Defect' /> :
                                            (issueLabel === "new_functionality" ? 
                                                <Label icon='plus' content='New Functionality' />:
                                                <Label icon='bug' content='Exploration' /> )
                                        return (
                                            <List.Item>{label}</List.Item>
                                        )
                                    })
                                }
                            </List>
                        </Card.Description>
                    </Card.Content>
                    <Card.Content>
                        { loginService.isLoggedIn() && isContributor &&
                            <>
                                <Modal size={'mini'} open={openDeleteModal} onClose={this.closeDelete}
                                    trigger = {
                                        <Button labelPosition='left' compact size='mini' onClick={() => this.showDeleteConfirm} 
                                        icon><Icon name='delete'/>Delete Project
                                        </Button>
                                    }
                                >
                                    <Modal.Header>Delete Project</Modal.Header>
                                    <Modal.Content>
                                        <p>Are you sure you want to delete project '{project.name}'?</p>
                                    </Modal.Content>
                                    <Modal.Actions>
                                        <Button negative onClick={this.closeDelete}>No</Button>
                                        <Button
                                            positive
                                            icon='checkmark'
                                            labelPosition='right'
                                            content='Yes'
                                            onClick={this.handleDeleteProject}
                                        />
                                    </Modal.Actions>
                                </Modal>
                                { deleteState &&
                                    <Switch>
                                        <Redirect to='/projects'/>
                                    </Switch>
                                }
                                { afterEditName && 
                                    <>
                                        {this.setState({ afterEditName:false })}
                                        {this.getProjectsAndContributors(this.state.project.name)}
                                        <Switch>
                                            <Redirect to={`/projects/${this.state.project.name}`}/>
                                        </Switch>
                                    </>

                                }
                            </>
                        }
                        <Button floated='right' compact size='mini' icon='tasks' as={Link} to={`/projects/${this.props.pname}/issues`} content='Issues'/>
                    </Card.Content>
                </>
                }
            </Card>
            <NotificationContainer />
            </>
        )
    }
}

