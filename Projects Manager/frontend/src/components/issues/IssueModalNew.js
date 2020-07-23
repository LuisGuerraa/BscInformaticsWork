import React from 'react'
import image from '../../files/undraw_problem_solving_ft81.svg';
import { Button, Modal, Label, Form, Image, Input } from 'semantic-ui-react'
import { getAction, API, fetchActionRequest, getProjectDetailsHateoas } from '../../services/Service';

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            project : { issueLabels: [] },
            fields: [],
            postAction: getAction(this.props.actions, API.actions.postIssue)
        }
        this.setFields(this.state.postAction)   
    }

    setFields() {
        this.state.postAction.fields.forEach((field, index) => {
            let updatedFields = this.state.fields
            updatedFields[index] = field.name.toString().toUpperCase()
            this.setState({ fields: updatedFields })
        })
    }

    toogle_label_defect = () => this.setState((prevState) => ({ label_defect: !prevState.label_defect }))
    toogle_label_newFuncionality = () => this.setState((prevState) => ({ label_newFuncionality: !prevState.label_newFuncionality }))
    toogle_label_exploration = () => this.setState((prevState) => ({ label_exploration: !prevState.label_exploration }))

    handleSubmit = async () => {
        this.setState({ isSubmitting:true })
        const issueLabels = []

        if(this.state.label_defect) issueLabels.push("defect")
        if(this.state.label_newFuncionality) issueLabels.push("new_functionality")
        if(this.state.label_exploration) issueLabels.push("exploration")

        const issue = {
            name: this.state.name,
            description: this.state.description,
            labelType: issueLabels
        }
        let response = await fetchActionRequest(this.state.postAction, issue)
        response.message = `Created new issue '${issue.name}'`
        this.setState({ isSubmitting:false, showModal: false })
        await this.props.updateIssues(response,this.props.pname)
    }

    componentDidMount() {
        return getProjectDetailsHateoas(this.props.pname).then((projectInfo) => {
            if (projectInfo) { 
                this.setState( { project:projectInfo.properties } ) 
            }
        })
    }

    resetLabels = () => this.setState({
        name:undefined,
        description:undefined,
        label_defect:false, 
        label_newFuncionality:false,
        label_exploration:false ,
        showModal:true
    })

    closeModal = () => this.setState({ showModal: false })
    
    render(){
        const { showModal, label_defect, label_newFuncionality, label_exploration, fields } = this.state
        return(
            <Modal closeIcon onClose={this.closeModal} open={showModal} trigger={<Button variant='outline-dark' onClick={this.resetLabels}>New</Button>} centered={false} size="small">
                <Modal.Header>Creating a new Issue on {this.props.pname}</Modal.Header>
                <Modal.Content image>
                    <Image rounded wrapped size='small' src={image} />
                    <Modal.Description>
                        <Form>
                            <Form.Field>
                                <Label size="large">{fields[0]}:</Label>
                                <Input required={true} onChange={(e) => this.setState({name: e.target.value})} style={{width:"79%"}} placeholder='Issue Name...' />
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[1]}:</Label>
                                <Input onChange={(e) => this.setState({description: e.target.value})} style={{width:"70%"}} placeholder='Issue Description...' />
                            </Form.Field>
                            <Form.Field>
                                <Label size="large">{fields[2]}:</Label>&#8195;
                                <Button toggle compact size="mini" disabled={!this.state.project.issueLabels.some(label => label === 'defect')} active={label_defect} onClick={this.toogle_label_defect} icon='minus' content='Defect' />
                                <Button toggle compact size="mini" disabled={!this.state.project.issueLabels.some(label => label === 'new_functionality')} active={label_newFuncionality} onClick={this.toogle_label_newFuncionality} icon='plus' content='New Functionality' />
                                <Button toggle compact size="mini" disabled={!this.state.project.issueLabels.some(label => label === 'exploration')} active={label_exploration} onClick={this.toogle_label_exploration} icon='bug' content='Exploration' />
                            </Form.Field>
                        </Form>
                    </Modal.Description>
                </Modal.Content>
                <Modal.Actions>
                    <Button className={!this.state.isSubmitting? "active" : "positive"} icon='checkmark' labelPosition='right' content="Submit" disabled={!this.state.name} loading={this.state.isSubmitting} onClick={this.handleSubmit} />
                </Modal.Actions>
            </Modal>
        )
    }
}