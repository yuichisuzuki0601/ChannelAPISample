String.prototype.replaceAll = function(org, dest) {
	var data = this;
	if (org instanceof Array) {
		org.forEach(function(elm) {
			data = data.split(elm).join(dest);
		});
		return data;
	} else {
		return data.split(org).join(dest);
	}
};

var common = {};

common.randomAlphaNumeric = function(digit) {
	var text = '';
	var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
	for (var i = 0; i < digit; i++) {
		text += possible.charAt(Math.floor(Math.random() * possible.length));
	}
	return text;
};