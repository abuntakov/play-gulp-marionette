import { createStore, applyMiddleware } from 'redux'
import createMiddleware from './middleware/clientMiddleware'
import rootReducer from './modules/reducers'

export default function configureStore(client, initialState) {
	const middleware = [createMiddleware(client)]

	let finalCreateStore = applyMiddleware(...middleware)(createStore)
	const store = finalCreateStore(rootReducer, initialState)
	return store
}
