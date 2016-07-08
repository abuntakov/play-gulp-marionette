import React, { Component } from 'react'

export default class ContactItem extends Component {
	render() {
		const { firstName } = this.props
		return (
			<div>{firstName}</div>
		)
	}
}
