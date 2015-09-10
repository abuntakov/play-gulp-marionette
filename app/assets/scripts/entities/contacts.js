/* global define, Backbone */
define(function(require){
	var Contact = require('entities/contact');

	var Contacts = Backbone.Collection.extend({
		model: Contact
	});

	return Contacts;
});
