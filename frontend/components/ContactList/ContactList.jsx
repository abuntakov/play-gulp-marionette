import React, { Component } from 'react'
import { Link } from 'react-router'
import { ContactItem } from '../'

export default class ContactList extends Component {
	render() {
		const { contacts, getUrlPath } = this.props
		return (
			<div className='list-group'>
				{contacts.map(contact => (
					<Link to={`${getUrlPath(contact.id)}`} key={contact.id} className='list-group-item' ><ContactItem {...contact} /></Link>
				))}
			</div>
		)
	}
}
