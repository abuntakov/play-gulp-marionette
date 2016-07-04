const LOAD_REQUEST = 'sample/contacts/load_request'
const LOAD_SUCCESS = 'sample/contacts/load_success'
// const LOAD_FAILURE = 'sample/contacts/load_failure'

const initialState = {
	loaded: false
}

export default function reducer(state = initialState, action = {}) {
	switch (action.type) {
		case LOAD_REQUEST:
			return {
				...state,
				loading: true
			}

		case LOAD_SUCCESS:
			return {
				...state,
				loading: false,
				loaded: true,
				data: action.payload
			}
		default:
			return state
	}
}
