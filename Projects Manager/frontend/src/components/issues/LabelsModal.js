import React from 'react'
import image from '../../files/undraw_add_tasks_mxew.svg';
import { Button, Modal, Label, Form, Image, Icon } from 'semantic-ui-react'
import { Redirect, Switch, Route } from 'react-router-dom';
import { fetchActionRequest, getAction, API } from '../../services/Service';

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            label_defect: props.issue.labelType.some(label => label === 'defect'),
            label_newFuncionality: props.issue.labelType.some(label => label === 'new_functionality'),
            label_exploration: props.issue.labelType.some(label => label === 'exploration'),
            afterSubmit: undefined
        }
    }

    toogle_label_defect = () => this.setState((prevState) => ({ label_defect: !prevState.label_defect }))
    toogle_label_newFuncionality = () => this.setState((prevState) => ({ label_newFuncionality: !prevState.label_newFuncionality }))
    toogle_label_exploration = () => this.setState((prevState) => ({ label_exploration: !prevState.label_exploration }))

    handleSubmit = async () => {
        this.setState({ isSubmitting:true })
        const labelType = []
        const issue = this.props.issue
        if(this.state.label_defect) labelType.push("defect")
        if(this.state.label_newFuncionality) labelType.push("new_functionality")
        if(this.state.label_exploration) labelType.push("exploration")
        issue.labelType = labelType
        let response = await fetchActionRequest( getAction(this.props.actions, API.actions.putIssue), issue )
        response.message = `Labels set to '${labelType.toString()}'`
        this.setState({ isSubmitting: false, showModal: false, afterSubmit: true })
        await this.props.updateIssueDetails(response, this.props.pname, this.props.issue.id)
    }

    openModal = () => this.setState({ showModal:true })
    closeModal = () => this.setState({ showModal: false }) 

    render(){
        const { showModal, label_defect, label_newFuncionality, label_exploration, afterSubmit } = this.state
        return(
            <Modal closeIcon onClose={this.closeModal} open={showModal} trigger = {
                <Button onClick={this.openModal} compact size='medium' basic>
                    <Icon name='tags'/>Edit Labels
                </Button>
            } centered={false} size="small">
                <Modal.Header>Edit Labels</Modal.Header>
                <Modal.Content image>
                    <Image centered rounded wrapped size='tiny' src={image} />
                    <Modal.Description>
                        <Form>
                            <Form.Field>
                                <Label size="large">Labels:</Label>&#8195;
                                <Button toggle compact size="mini" active={label_defect} 
                                    disabled={!this.props.projectLabels.some(label => label === 'defect')} 
                                    onClick={this.toogle_label_defect} icon='minus' content='Defect' 
                                />
                                <Button toggle compact size="mini" active={label_newFuncionality} 
                                    disabled={!this.props.projectLabels.some(label => label === 'new_functionality')} 
                                    onClick={this.toogle_label_newFuncionality} icon='plus' content='New Functionality' 
                                />
                                <Button toggle compact size="mini" active={label_exploration} 
                                    disabled={!this.props.projectLabels.some(label => label === 'exploration')} 
                                    onClick={this.toogle_label_exploration} icon='bug' content='Exploration' 
                                />
                            </Form.Field>
                        </Form>
                    </Modal.Description>
                </Modal.Content>
                <Modal.Actions>
                    <Button className={!this.state.isSubmitting? "active" : "positive"} icon='checkmark' labelPosition='right' content="Submit" loading={this.state.isSubmitting} onClick={this.handleSubmit} />
                    { afterSubmit && 
                        <Switch>
                            <Redirect to={`/projects/${this.state.name}/issues/${this.props.issue.id}`}/>   
                                <Route exact path={`/projects/${this.state.name}/issues/${this.props.issue.id}`} />
                        </Switch> 
                    }
                </Modal.Actions>
            </Modal>
        )
    }
}