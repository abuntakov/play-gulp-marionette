import { createStore, applyMiddleware } from 'redux'
// import createMiddleware from './middleware/clientMiddleware'
import rootReducer from './modules/reducers'
import createSagaMiddleware from 'redux-saga'
import { watchContactsRequest } from './modules/contacts'

export default function configureStore(client, initialState) {
	// const middleware = [createMiddleware(client)]
	const sagaMiddleware = createSagaMiddleware()

	const store = createStore(
		rootReducer,
		initialState,
		applyMiddleware(sagaMiddleware)
	)

	// let finalCreateStore = applyMiddleware(createMiddleware(sagaMiddleware))(createStore)
	// const store = finalCreateStore(rootReducer, initialState)
	sagaMiddleware.run(watchContactsRequest)
	return store
}
