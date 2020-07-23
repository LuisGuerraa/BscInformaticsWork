import React from 'react'
import { Visibility, Segment, Menu, Container} from 'semantic-ui-react'
import { Link } from 'react-router-dom'
export default class extends React.Component {
	constructor(props) {
        super(props)
        this.state = { fixed: false }
    }

	hideFixedMenu = () => this.setState({ fixed: false})
	showFixedMenu = () => this.setState({ fixed: true })

	render() {
		return(
			<Visibility once={false} onBottomPassed={this.showFixedMenu} onBottomPassedReverse={this.hideFixedMenu}>
				<Segment inverted vertical raised>
						<Menu inverted pointing secondary size='large'>
							<Container>
								<Menu.Item as={Link} to="/">Home</Menu.Item>
								<Menu.Item as={Link} to="/projects">Projects</Menu.Item>
							</Container>
						</Menu>
				</Segment>
			</Visibility>
		)
	}
}
