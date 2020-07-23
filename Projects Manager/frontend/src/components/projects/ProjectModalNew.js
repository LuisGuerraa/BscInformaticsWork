import React from 'react'
import image from '../../files/create_projects.svg';
import { Button, Modal, Label, Form, Image, Input } from 'semantic-ui-react'
import { API, getAction, fetchActionRequest } from '../../services/Service'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = { fields: [], postAction: getAction(this.props.actions, API.actions.postProject) }
        this.setUpperCaseFields()   
    }

    toogle_state_open = () => this.setState((prevState) => ({ state_open: !prevState.state_open }))
    toogle_state_closed = () => this.setState((prevState) => ({ state_close: !prevState.state_close }))
    toogle_state_archived = () => this.setState((prevState) => ({ state_archived: !prevState.state_archived }))

    toogle_label_defect = () => this.setState((prevState) => ({ label_defect: !prevState.label_defect }))
    toogle_label_newFuncionality = () => this.setState((prevState) => ({ label_newFuncionality: !prevState.label_newFuncionality }))
    toogle_label_exploration = () => this.setState((prevState) => ({ label_exploration: !prevState.label_exploration }))

    handleSubmit = async () => {
        this.setState({ isSubmitting:true })
        const issueStates = []
        const issueLabels = []
        if(this.state.state_open) issueStates.push("open")
        if(this.state.state_close) issueStates.push("closed")
        if(this.state.state_archived) issueStates.push("archived")

        if(this.state.label_defect) issueLabels.push("defect")
        if(this.state.label_newFuncionality) issueLabels.push("new_functionality")
        if(this.state.label_exploration) issueLabels.push("exploration")
        
        const project = { 
            name: this.state.name, 
            description: this.state.description, 
            issueStates, 
            issueLabels 
        }
        let response = await fetchActionRequest(this.state.postAction, project)
        if(response.status === 201)
            this.props.updateProjects(`Project '${project.name}' created!`)
        else
            this.props.errorOcurred(response)
        this.setState({ isSubmitting: false, showModal: false })
    }

    resetStatesLabels = () => {
        this.setState({
            name:undefined,
            description:undefined,
            state_open:false, 
            state_close:false, 
            state_archived:false, 
            label_defect:false, 
            label_newFuncionality:false,
            label_exploration:false,
            showModal:true
        })
    }

    closeModal = () => {
        this.setState({ showModal: false })
    }

    setUpperCaseFields() {
        this.state.postAction.fields.forEach((field, index) => {
            let updatedFields = this.state.fields
            updatedFields[index] = field.name.toString().toUpperCase()
            this.setState({ fields: updatedFields })
        })
    }

    render() {
        const { showModal, state_open, state_close, state_archived, label_defect, label_newFuncionality, label_exploration, fields, postAction } = this.state
        return (
            <Modal closeIcon onClose={this.closeModal} open={showModal} centered={false} size="small"
                trigger={ <Button variant='outline-dark' onClick={this.resetStatesLabels}>New</Button> }
            > 
                <Modal.Header>{postAction.title}</Modal.Header>
                <Modal.Content image>
                    <Image rounded wrapped size='small' src={image} />
                    <Modal.Description>
                        <Form>
                            <Form.Field>
                                <Label size="large">{fields[0]}:</Label>
                                <Input required={true} onChange={(e) => this.setState({name: e.target.value})} style={{width:"82.5%"}} placeholder='Project Name...' />
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[1]}:</Label>
                                <Input onChange={(e) => this.setState({description: e.target.value})} style={{width:"71%"}} placeholder='Project Description...' />
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[2]}:</Label>&#8195;
                                <Button toggle compact size="mini" active={state_open} onClick={this.toogle_state_open} icon='folder open' content='Open' />
                                <Button toggle compact size="mini" active={state_close} onClick={this.toogle_state_closed} icon='folder closed' content='Closed' />
                                <Button toggle compact size="mini" active={state_archived} onClick={this.toogle_state_archived} icon='archive' content='Archived' />
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[3]}:</Label>&#8195;
                                <Button toggle compact size="mini" active={label_defect} onClick={this.toogle_label_defect} icon='minus' content='Defect' />
                                <Button toggle compact size="mini" active={label_newFuncionality} onClick={this.toogle_label_newFuncionality} icon='plus' content='New Functionality' />
                                <Button toggle compact size="mini" active={label_exploration} onClick={this.toogle_label_exploration} icon='bug' content='Exploration' />
                            </Form.Field>
                        </Form>
                    </Modal.Description>
                </Modal.Content>
                <Modal.Actions>
                    <Button 
                        className={!this.state.isSubmitting? "active" : "positive"} 
                        icon='checkmark' 
                        labelPosition='right' 
                        content="Submit" 
                        disabled={!this.state.name} 
                        loading={this.state.isSubmitting} 
                        onClick={this.handleSubmit} />
                </Modal.Actions>
            </Modal>
        )
    }
}