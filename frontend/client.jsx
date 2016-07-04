import 'babel-polyfill'
import React from 'react'
import { Provider } from 'react-redux'
import { render } from 'react-dom'
import ApiClient from './apiClient'
import configureStore from './redux/create'
import { App } from './containers'

const client = new ApiClient()
const store = configureStore(client, {})

render(
	<Provider store={store}>
		<App />
	</Provider>,
	document.getElementById('root')
)
