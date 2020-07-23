import React from 'react'
import { Dropdown } from 'semantic-ui-react'
import ContributorsModal from './ContributorsModal'
import { getContributorsHateoas } from '../../services/Service'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            contributors : [],
            count : 0,
        }
    }

    async componentDidMount() {
        return getContributorsHateoas(this.props.pname).then((contributorsInfo) => {
            if (contributorsInfo) { 
                this.setState( { 
                    contributors: contributorsInfo.entities, 
                    actions: contributorsInfo.actions,
                    count: contributorsInfo.properties.count
                }) 
            }
        })
    }

    componentWillUnmount() {
        if (this.timerId) {
            clearInterval(this.timerId)
        }
    }

    updateContributors = () => {
        this.componentDidMount()
    }

    render() {
        return (
            <>
            <Dropdown compact text='Contributors' icon='dropdown' floating labeled button className='icon'>
                <Dropdown.Menu>
                <Dropdown.Header icon='group' content={`TOTAL: ${this.state.count}`} />
                    { this.state.contributors.map(contributor => {
                            return ( <Dropdown.Item>{contributor.properties.username}</Dropdown.Item> )
                    })}
                </Dropdown.Menu>
            </Dropdown>
            { this.state.actions && 
                <ContributorsModal pname={this.props.pname} actions={this.state.actions} updateContributors={this.updateContributors}/> 
            }
            </>
        )
    }
}
