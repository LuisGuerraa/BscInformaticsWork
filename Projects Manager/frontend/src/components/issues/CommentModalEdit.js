import React from 'react'
import { Button, Modal, TextArea } from 'semantic-ui-react'
import { fetchActionRequest, getAction, API } from '../../services/Service'

export default class extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            text: undefined,
            newText : undefined
        }
    }

    handleSubmit = async () => {
        this.setState({ isSubmitting: true })
        let actions = await this.props.getActions()
        let response = await fetchActionRequest(
            getAction(actions, API.actions.putComment), {text: this.state.newText}
        )
        response.message = `Comment edited!`
        this.setState({ isSubmitting: false, showModal: false })
        this.props.updateIssuesDetails(response, this.props.pname, this.props.issueID)
    }

    openModal = () => {
        this.setState({showModal : true})
    }

    closeModal = () => {
        this.setState({showModal : false})
    }

    render (){
        return (
            <Modal onClose = {this.closeModal} open = {this.state.showModal}
                trigger = {
                    <Button onClick = {this.openModal} size='mini' compact floated='center' icon='edit'/>
                }>
                <Modal.Header>Edit your comment here: </Modal.Header>  
                <Modal.Content>
                    <Modal.Description>
                        <TextArea onChange={(e) => this.setState({newText: e.target.value})} style={{width:"70%"}}>{this.props.text}</TextArea>
                    </Modal.Description>
                </Modal.Content>  
                <Modal.Actions>
                    <Button 
                        className={!this.state.isSubmitting? "active" : "positive"} 
                        icon='checkmark' 
                        labelPosition='right' 
                        content="Submit" 
                        disabled={!this.state.newText} 
                        loading={this.state.isSubmitting} 
                        onClick={this.handleSubmit} 
                    />
                </Modal.Actions>               
            </Modal>
        )
    }
}