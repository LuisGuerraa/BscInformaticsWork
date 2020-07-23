import React from 'react'
import { Image } from 'semantic-ui-react'
import image from '../files/lamp_offline.gif';

export default class extends React.Component{
    render(){
        return (
            <div style={{flex: 1,backgroundColor: '#000000', opacity:1, backgroundSize:"cover"}}>
            <h1>Ups!</h1>
            <h3>{this.props.error}</h3>
            <Image centered size='huge' src={image}/>
        </div>
        )
    }
}