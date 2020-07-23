import React from 'react'
import { Icon, Statistic, Button, Comment, Form , Divider } from 'semantic-ui-react'
import { getLoginService as LoginService } from '../../services/LoginService'
import { getCommentsHateoas, fetchActionRequest, getAction, API, getCommentDetailsHateoas } from '../../services/Service'
import { NotificationContainer, NotificationManager } from 'react-notifications'
import EditCommentModal from './CommentModalEdit'

const loginService = LoginService() 

export default class extends React.Component {
    
    constructor(props) {
        super(props)
        this.state = {
            comments : [],
            count : 0,
            text : undefined
        }
    }
    
    handleSubmit = async () => {
        this.notifyUser(
            await fetchActionRequest( 
                    getAction(this.state.actions, API.actions.postComment), { text:this.state.text }
                )
                , `Comment created for issue ${this.props.issueID} of project ${this.props.pname}.`, 201
        )
        await this.componentDidMount()
    }
  
    handleDelete = async (commentID) => {
        let actions = await this.getCommentActions(commentID)
        this.notifyUser(
            await fetchActionRequest( 
                getAction(actions, API.actions.deleteComment), null )
                , `Comment removed from issue ${this.props.issueID} of project ${this.props.pname}.`, 204)
        this.setState({deleteState : true})
        await this.componentDidMount()
    }

    notifyUser = (response,successMessage,successStatus) => {
        if(response.status === successStatus)
            NotificationManager.success(successMessage,'',5000)
        else
            NotificationManager.error(`Error on creatting a comment for issue ${this.props.issueID} of project ${this.props.pname}.`,'',10000)
    }

    async componentDidMount() {
        this.refreshComments(null, this.props.pname, this.props.issueID)
    }

    refreshComments = async (response, pname, issueID) => {
        if(response === null || response.status === 200){
            if(response !== null) NotificationManager.success(response.message,'',5000)
            await getCommentsHateoas(pname, issueID).then((commentsInfo) => {
                if (commentsInfo) { 
                    this.setState( { 
                        comments : commentsInfo.entities, 
                        actions: commentsInfo.actions,
                        count : commentsInfo.properties.count, 
                        loading : false } ) 
                }
            })
        } else
            NotificationManager.error(`Error on creatting a comment for issue ${this.props.issueID} of project ${this.props.pname}.`,'',10000)
    }

    getCommentActions(commentID) {
        return getCommentDetailsHateoas(this.props.pname, this.props.issueID, commentID).then(commentInfo => {
            if(commentInfo){
                this.setState({commentActions: commentInfo.actions})
                return this.state.commentActions
            }
        }).catch(err => err)
    }

    render() {
        return (
            <>
                <Statistic horizontal size='mini'>
                    <Statistic.Value><Icon name='comments' /> {this.state.count}</Statistic.Value>
                    <Statistic.Label>{this.state.count === 1 ? 'Comment' : 'Comments'}</Statistic.Label>
                </Statistic>
                { this.state.comments.map(c => {
                    const comment = c.properties
                    return ( 
                        <>
                       
                            <Comment.Group>
                                <Comment>
                                    <Comment.Content>
                                        
                                        <Comment.Author as='a'>{comment.createdBy}</Comment.Author> said:  
                                        
                                        <Comment.Text floated>{comment.text}</Comment.Text> 
                                        {loginService.isLoggedIn() && (comment.createdBy === loginService.getUser()) && this.state.actions &&
                                            <>
                                            <EditCommentModal 
                                                pname={this.props.pname} 
                                                issueID={this.props.issueID} 
                                                text={comment.text}
                                                commentID={comment.id}
                                                getActions={() => this.getCommentActions(comment.id)}
                                                updateIssuesDetails={this.refreshComments}
                                                /> 
                                            <Button floated='center' onClick={() => this.handleDelete(comment.id)} compact size='mini' icon='close'/>
                                            <br/>
                                            </>
                                        }
                                        <Comment.Metadata>Date: {comment.creationDate}</Comment.Metadata>
                                    </Comment.Content>
                                </Comment>
                            </Comment.Group>
                        </>
                    )      
                })}
                { loginService.isLoggedIn() &&
                    <>
                        <Divider/>
                        <Form reply>
                            <Form.Input fluid label='Comment down below:' onChange={(e)=> this.setState({text : e.target.value})}/> 
                            <Divider/>
                            <Button content='Add Comment' labelPosition='left' icon='plus square outline' primary onClick = {this.handleSubmit}/>
                        </Form>
                    </>
                }
                <NotificationContainer/>
            </>
            
        )
    }
}