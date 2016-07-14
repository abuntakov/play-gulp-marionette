import { takeEvery, delay } from 'redux-saga'
import { put } from 'redux-saga/effects'

const LOAD_REQUEST = 'sample/contacts/load_request'
const LOAD_SUCCESS = 'sample/contacts/load_success'
	// const LOAD_FAILURE = 'sample/contacts/load_failure'

const initialState = {}
// const initialState = {
// 	loaded: false
// }

export default function reducer(state = initialState, action = {}) {
	switch (action.type) {
		// case LOAD_REQUEST:
		// 	return {
		// 		...state,
		// 		loading: true
		// 	}

		case LOAD_SUCCESS:
			console.log('load success')
			console.log(state)
			console.log(action)
			return {
				...state,
				...action.payload,
				loading: false,
				loaded: true
			}

		default:
			return state
	}
}

export function loadContacts() {
	return {
		type: LOAD_REQUEST
	}
}

function * loadContactsAsync() {
	console.log('async')
	yield delay(2000)
	const result = {
		type: LOAD_SUCCESS,
		payload: {
			result: [1, 2, 3]
		}
	}
	yield put(result)
}

export function * watchContactsRequest() {
	yield * takeEvery(LOAD_REQUEST, loadContactsAsync)
}
