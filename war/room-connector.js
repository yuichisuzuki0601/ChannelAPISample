// 1.部屋に接続するよ！というリクエストを飛ばす先のURL(環境に合わせて変更しましょう)
var OPEN_URL = '/room-connect/open';
// 2.部屋に接続しているみんなにメッセージを送信するよ！というリクエストを飛ばす先のURL(環境に合わせて変更しましょう)
var SEND_URL = '/room-connect/send';

var RoomConnector = function() {
	// 3.この端末のIDを適当にランダム8桁で生成します
	this.deviceId = common.randomAlphaNumeric(8);
	this.roomId = null;
	this.socket = null;

	this.onOpen = null;
	this.onMessage = null;
	this.onError = null;
	this.onClose = null;

	this.getDeviceId = function() {
		return this.deviceId;
	};

	this.getRoomId = function() {
		return this.roomId;
	};

	this.open = function(roomId) {
		this.roomId = roomId;
		var param = {
			roomId : roomId,
			roomAndDeviceId : roomId + ':' + this.deviceId,
		};
		var that = this;
		this.ajax(OPEN_URL, param, function(token) {
			that.socket = new goog.appengine.Channel(token).open();
			that.setOnOpen();
			that.setOnMessage();
			that.setOnError();
			that.setOnClose();
		});
	};

	this.send = function(message) {
		if (!message) {
			return;
		}
		var param = {
			roomId : this.roomId,
			message : message,
		};
		this.ajax(SEND_URL, param);
	};

	this.ajax = function(url, param, callback) {
		$.ajax({
			type : 'POST',
			url : url,
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(param),
		}).done(function(result) {
			if (callback) {
				callback(result);
			}
		});
	};

	this.setOnOpen = function(onOpen) {
		if (onOpen) {
			this.onOpen = onOpen;
		}
		if (this.socket && this.onOpen) {
			this.socket.onopen = this.onOpen;
		}
	};

	this.setOnMessage = function(onMessage) {
		if (onMessage) {
			this.onMessage = function(data) {
				onMessage(data.data);
			};
		}
		if (this.socket && this.onMessage) {
			this.socket.onmessage = this.onMessage;
		}
	};

	this.setOnError = function(onError) {
		if (onError) {
			this.onError = onError;
		}
		if (this.socket && this.onError) {
			this.socket.onerror = this.onError;
		}
	};

	this.setOnClose = function(onClose) {
		if (onClose) {
			this.onClose = onClose;
		}
		if (this.socket && this.onClose) {
			this.socket.onClose = this.onClose;
		}
	};
};
