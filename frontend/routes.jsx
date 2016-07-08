import React from 'react'
import {Route} from 'react-router'
import {App, Contacts} from './containers'

export default (store) => {
	return (
		<Route path='/' component={App}>
			<Route path='/contacts' component={Contacts}></Route>
		</Route>
	)
}
