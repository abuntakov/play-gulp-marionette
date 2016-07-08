import superagent from 'superagent'

const methods = ['get', 'post', 'put', 'patch', 'del']

function formatUrl(path) {
	const adjustedPath = path[0] !== '/' ? `/$path` : path
	return adjustedPath
}

export default class ApiClient {
	constructor() {
		const self = this

		function initMethods(method) {
			self[method] = (path, {params, data} = {}) => new Promise((resolve, reject) => {
				const request = superagent[method](formatUrl(path))

				if (params) {
					request.query(params)
				}

				if (data) {
					request.send(data)
				}

				request.end((err, {body} = {}) => err ? reject(body || err) : resolve(body))
			})
		}

		methods.forEach(initMethods)
	}

	/*
	 * There's a V8 bug where, when using Babel, exporting classes with only
	 * constructors sometimes fails. Until it's patched, this is a solution to
	 * "ApiClient is not defined" from issue #14.
	 * https://github.com/erikras/react-redux-universal-hot-example/issues/14
	 *
	 * Relevant Babel bug (but they claim it's V8): https://phabricator.babeljs.io/T2455
	 *
	 * Remove it at your own risk.
	 */
	empty() {}
}
