/* global define, log, Marionette */
define(function(){
	log('inside app');

	var RootLayout = Marionette.LayoutView.extend({
		el: 'body',

		regions: {
			list: '#contact-list',
			form: '#contact-form'
		}

	});

	var app = new Marionette.Application();

	app.rootView = new RootLayout();

	return app;

});
