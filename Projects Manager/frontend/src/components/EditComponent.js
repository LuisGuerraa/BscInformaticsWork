import React from 'react'
import { Button, Form, Grid } from  'semantic-ui-react'

export default ({state, propName, value, handleChange, handleSubmit, closeHandler}) => {
    return (
        <>
        {
            state ? 
            <Grid> 
                <Grid.Row > 
                    <Form size='small' onSubmit={handleSubmit} >
                        <Form.Group>
                            <Form.Input
                                placeholder={value} 
                                name={propName}
                                value={value}
                                onChange={handleChange}
                                size='small'
                            />
                            <Form.Button icon='check' circular size='mini' />
                        </Form.Group>
                    </Form>    
                        <Button style={{width:30, height:30}} size='mini' icon='close' circular onClick={closeHandler}/> 
                </Grid.Row>
            </Grid> :  value 
        }
        
        </>
    )
}