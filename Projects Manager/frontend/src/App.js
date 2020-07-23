import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom'
import Home         from './components/Home'
import Projects     from './components/projects/Projects'
import Details      from './components/projects/ProjectDetails'
import Issues       from './components/issues/Issues'
import IssueDetails from './components/issues/IssueDetails'
import './App.css'

export default function App() {
    return (
        <div className="App">
            <Switch>
                <Route exact path='/' component={Home} />

                <Route exact path='/projects' render={ () => 
                    <Projects /> }/>

                <Route exact path='/projects/:pname' render={({ match }) => 
                    <Details pname={match.params.pname} /> }/>

                <Route exact path='/projects/:pname/issues/' render={({ match }) => 
                    <Issues pname={match.params.pname}/> }/>

                <Route exact path='/projects/:pname/issues/:id' render={({ match }) => 
                    <IssueDetails pname={match.params.pname} issueID={match.params.id} />}/>

                <Route>
                    <Redirect to='/' /></Route>
            </Switch>
        </div>
    );
}
