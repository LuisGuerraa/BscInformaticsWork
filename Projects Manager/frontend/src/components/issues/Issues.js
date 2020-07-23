import React from 'react'
import { Statistic, Icon, Dimmer, Loader, Card, Button, Popup } from 'semantic-ui-react'
import { Link } from 'react-router-dom';
import { getIssuesHateoas } from '../../services/Service'
import { getLoginService as LoginService } from '../../services/LoginService'
import NewIssueModal from './IssueModalNew'
import { NotificationContainer, NotificationManager } from 'react-notifications'
import HomeBar from '../HomeBar'

const loginService = LoginService()

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            pname: this.props.pname,
            loading : true, 
            count:0,
            issues:[]
        }
    }

    async componentDidMount() {
        this.updateIssues(null, this.state.pname)
    }

    updateIssues = async (response, pname) => {
        this.setState({pname: pname})
        if(response === null || response.status === 201){
            if(response !== null) NotificationManager.success(response.message,'',5000)
            await getIssuesHateoas(pname).then((issuesInfo) => {
                if (issuesInfo) { 
                    this.setState( { 
                        issues: issuesInfo.entities, 
                        actions: issuesInfo.actions,
                        count: issuesInfo.properties.count, 
                        loading : false } ) 
                }
            })
        } else
            NotificationManager.error(response.props,'',10000)
    }

    componentWillUnmount() {
        if (this.timerId) {
            clearInterval(this.timerId)
        }
    }

    render() {
        const { pname, issues, loading, actions, count } = this.state
        return (
            <>
                <HomeBar/>  
                { loading ?
                    <Dimmer active inverted>
                        <Loader inverted>Loading</Loader>
                    </Dimmer> :
                    <>
                        <br/> <h1>ISSUES OF PROJECT <br/>'{pname.toUpperCase()}'</h1>
                        <Statistic inverted horizontal size='mini'>
                            <Statistic.Value><Icon name='exclamation circle' /> {count}</Statistic.Value>
                            <Statistic.Label>{count === 1 ? 'Issue' : 'Issues'}</Statistic.Label>
                        </Statistic>
                        <br/>
                        { loginService.isLoggedIn() && actions && <NewIssueModal pname={pname} actions={actions} updateIssues={this.updateIssues}/> }
                        <br/><br/>
                        <Card.Group itemsPerRow={2} centered>
                            { issues.map( i => {
                                const issue = i.properties 
                                return (
                                    <>
                                        <Card style={{width:'400px'}}>
                                            <Card.Content>
                                                <Card.Header>{issue.name}</Card.Header>
                                                <Card.Meta>{issue.description}</Card.Meta>
                                                <Card centered style={{width:'100px'}}><strong>{issue.stateType.toUpperCase()}</strong></Card>
                                            </Card.Content>
                                            <Card.Content extra centered>
                                                <Icon name='calendar alternate'/>{issue.creationDate}
                                                <Popup content='Issue Details' size='tiny' trigger = {
                                                    <Button 
                                                        circular 
                                                        icon='info circle' 
                                                        floated='right' 
                                                        as={Link} 
                                                        to={`/projects/${pname}/issues/${issue.id}`}
                                                    />}>
                                                </Popup>
                                            </Card.Content>
                                        </Card>
                                    </>
                                )
                            })}
                        </Card.Group>
                        
                    </>
                }
                <NotificationContainer />
            </>
        )

    }
}