import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import * as contactActions from '~/redux/modules/contacts'
import { ContactList } from '~/components'

class Contacts extends Component {
	getUrlPath(id) {
		return `/contacts/${id}`
	}

	componentDidMount() {
		console.log('parent mountj')
		this.props.contactActions.loadContacts()
	}

	render() {
		const { contacts } = this.props
		console.log('root')
		console.log(this.props)
		// const { getContacts } = this.props.contactActions
		return (
			<div>
				<ContactList contacts={contacts} getUrlPath={this.getUrlPath} ></ContactList>
				<div>Conacts: {contacts.length}</div>
			</div>
		)
	}
}

function mapStateToProps(state = {}) {
	return {
		contacts: state.contacts || []
	}
}

function mapDispatchToProps(dispatch) {
	return {
		contactActions: bindActionCreators(contactActions, dispatch)
	}
}

export default connect(mapStateToProps, mapDispatchToProps)(Contacts)
