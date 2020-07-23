import React from 'react'
import { Icon, Statistic, Card, Responsive, Dimmer, Loader } from 'semantic-ui-react'
import ProjectDropdown from './ProjectDropdown'
import NewProjectModal from './ProjectModalNew'
import { getProjectsHateoas, loginService } from '../../services/Service'
import { NotificationContainer, NotificationManager } from 'react-notifications'
import HomeBar from '../HomeBar'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            loading : true, 
            count:0,
            projects:[]
        }
    }

    async getProjects() {
        try{
            await getProjectsHateoas().then((projectsInfo) => {
                if (projectsInfo) { 
                    this.setState({ 
                        projects: projectsInfo.entities, 
                        actions: projectsInfo.actions,
                        count: projectsInfo.properties.count, 
                        loading: false 
                    }) 
                }
            })
        } catch (error){
            this.errorOcurred(error)
        }
    }

    async componentDidMount() {
        this.getProjects()
    }

    errorOcurred = (error) => {
        NotificationManager.error(error.props,'',10000)
        this.setState({ loading: false })
    }

    updateProjects = async (message) => {
        NotificationManager.success(message,'',5000)
        this.getProjects()
    }

    componentWillUnmount() {
        if (this.timerId) {
            clearInterval(this.timerId)
        }
    }

    render() {
        const { actions, loading, projects, count } = this.state
        return (     
            <>   
            <HomeBar/>
            <Responsive>
                { loading ?
                    <Dimmer active inverted>
                        <Loader inverted>Loading</Loader>
                    </Dimmer> 
                    :
                    <>
                        <br/> <h1>ALL PROJECTS</h1> 
                        <Statistic inverted horizontal size='mini'>
                            <Statistic.Value><Icon name='folder' /> {count}</Statistic.Value>
                            <Statistic.Label>{count === 1 ? 'Project' : 'Projects'}</Statistic.Label>
                        </Statistic><br/>
                        { loginService.isLoggedIn() && actions ? 
                            <NewProjectModal actions={actions} errorOcurred={this.errorOcurred} updateProjects={this.updateProjects}/>: null }
                        <br/><br/>
                        <Card.Group itemsPerRow={2} centered>
                            {projects.map((p,index) => {
                                const project = p.properties
                                return (
                                    <>
                                        <Card style={{width:'500px'}}>
                                            <Card.Content>
                                                <ProjectDropdown pname={project.name}/>
                                                <Card.Header>{project.name}</Card.Header>
                                                <Card.Meta>{project.description}</Card.Meta>
                                            </Card.Content>
                                        </Card>
                                    </>
                                )
                            })}
                        </Card.Group>
                    </>
                }
                <NotificationContainer/>
            </Responsive>
            </>
        )
    }
}
