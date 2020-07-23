import React from 'react'
import { Link } from 'react-router-dom'
import { Dropdown } from 'semantic-ui-react'

export default ({ pname }) => {
    return (
        <Dropdown button className='icon' labeled text='More' style={{float:'right'}} icon='filter' >
            <Dropdown.Menu >
            <Dropdown.Item as={Link} to={`/projects/${pname}`} icon='info circle' text='Details'/>
            <Dropdown.Item as={Link} to={`/projects/${pname}/issues`} icon='exclamation' text='Issues'/>
            </Dropdown.Menu>
        </Dropdown>
    )
}