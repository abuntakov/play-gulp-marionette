import 'babel-polyfill'
import React from 'react'
import { Provider } from 'react-redux'
import { render } from 'react-dom'
import ApiClient from './apiClient'
import configureStore from './redux/create'
import {Router, browserHistory} from 'react-router'

import getRoutes from './routes'

const client = new ApiClient()
const store = configureStore(client, { contacts: {} })

/*
contacts: [{
		id: 1,
		firstName: 'Mike'
	}, {
		id: 2,
		firstName: 'John'
	}]
*/

const component = (
	<Router history={browserHistory}>
		{getRoutes(store)}
	</Router>
)

render(
	<Provider store={store}>
		{component}
	</Provider>,
	document.getElementById('root')
)
