import React, { Component } from 'react'
// import { ContactItem } from '../'
import R from 'ramda'

export default class ContactList extends Component {
	componentDidMount() {
		console.log('mounted')
	}

	render() {
		const { contacts } = this.props
		// const { contacts, getUrlPath } = this.props
		let template

		console.log('render')
		console.log(this.props)

		if (R.isNil(contacts.result)) {
			template = (<div>Loading...</div>)
		} else {
			template = (
				<div>Loaded: {R.length(contacts.result)}</div>
			)
		}

		return template
	}
}
// <div className='list-group'>
// 	{contacts.map(contact => (
// 		<Link to={`${getUrlPath(contact.id)}`} key={contact.id} className='list-group-item' ><ContactItem {...contact} /></Link>
// 	))}
// </div>
