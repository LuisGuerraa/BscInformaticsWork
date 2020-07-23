import React from 'react'
import image from '../../files/create_projects.svg';
import { Button, Modal, Label, Form, Image, Input, Icon } from 'semantic-ui-react'
import { Redirect, Switch } from 'react-router-dom';
import { fetchActionRequest, getAction, API } from '../../services/Service'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            state_open: props.project.issueStates.some(state => state === 'open'),
            state_close: props.project.issueStates.some(state => state === 'closed'),
            state_archived: props.project.issueStates.some(state => state === 'archived'),

            label_defect: props.project.issueLabels.some(label => label === 'defect'),
            label_newFuncionality: props.project.issueLabels.some(label => label === 'new_functionality'),
            label_exploration: props.project.issueLabels.some(label => label === 'exploration'),

            oldName : props.project.name,
            afterSubmit: false,

            fields: [],
            putAction: getAction(this.props.actions, API.actions.putProject)
        }
        this.setUpperCaseFields()
    }

    setUpperCaseFields() {
        this.state.putAction.fields.forEach((field, index) => {
            let updatedFields = this.state.fields
            updatedFields[index] = field.name.toString().toUpperCase()
            this.setState({ fields: updatedFields })
        })
    }

    toogle_state_open = () => this.setState((prevState) => ({ state_open: !prevState.state_open }))
    toogle_state_closed = () => this.setState((prevState) => ({ state_close: !prevState.state_close }))
    toogle_state_archived = () => this.setState((prevState) => ({ state_archived: !prevState.state_archived }))

    toogle_label_defect = () => this.setState((prevState) => ({ label_defect: !prevState.label_defect }))
    toogle_label_newFuncionality = () => this.setState((prevState) => ({ label_newFuncionality: !prevState.label_newFuncionality }))
    toogle_label_exploration = () => this.setState((prevState) => ({ label_exploration: !prevState.label_exploration }))

    handleSubmit = async () => {
        this.setState({ isSubmitting:true})
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
        let response = await fetchActionRequest(getAction(this.props.actions, API.actions.putProject), project)
        await this.props.setResponse(response, this.state.name)
        this.setState({ isSubmitting:false, showModal: false, afterSubmit: true })   
    }

    openModal = () => this.setState({ showModal:true })
    closeModal = () => this.setState({ showModal: false }) 

    render(){
        const { showModal, state_open, state_close, state_archived, label_defect, label_newFuncionality, label_exploration, afterSubmit, putAction, fields } = this.state
        return(
            <>
            <Modal closeIcon onClose={this.closeModal} open={showModal} trigger = {
                <Button onClick={this.openModal} compact size='medium' basic>
                    <Icon name='edit'/>Edit Project
                </Button>
            } centered={false} size="small">
                <Modal.Header>{putAction.title}</Modal.Header>
                <Modal.Content image>
                    <Image rounded wrapped size='small' src={image} />
                    <Modal.Description>
                        <Form>
                            <Form.Field>
                                <Label size="large">{fields[0]}:</Label>
                                <Input required={true} onChange={(e) => this.setState({name: e.target.value})} style={{width:"79%"}} defaultValue={this.props.project.name}/>
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[1]}:</Label>
                                <Input onChange={(e) => this.setState({description: e.target.value})} style={{width:"70%"}} defaultValue={this.props.project.description} />
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
                    <Button className={!this.state.isSubmitting? "active" : "positive"} icon='checkmark' labelPosition='right' content="Submit" disabled={!this.state.name} loading={this.state.isSubmitting} onClick={this.handleSubmit} />
                </Modal.Actions>
            </Modal>
            {afterSubmit &&
                <>
                {this.setState({ afterSubmit:false })}
                <Switch>
                    <Redirect to={`/projects/${this.state.name}`}/>
                </Switch>
                </>
            }
            </>
        )
    }
}