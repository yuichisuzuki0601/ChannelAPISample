$(document).ready(function() {
	// 1.まずは簡単接続用のクラスを生成！
	var roomConnector = new RoomConnector();

	// 2.端末IDを画面上に表示
	$('#device-id').text(roomConnector.getDeviceId());

	// 3.サーバーからのプッシュメッセージを受けたときに何をするかをここに記述
	roomConnector.setOnMessage(function(data) {
		if (data.substring(0, 21) === 'joinRoomAndDeviceIds:') {
			// 4.部屋に接続している端末一覧が送られてきたらそれを画面上に表示
			var joinRoomAndDeviceIds = data.substring(21).replaceAll([ '[', ']', ' ' ], '').split(',');
			$('#join-devices').empty();
			joinRoomAndDeviceIds.forEach(function(roomAndDeviceId) {
				var deviceId = roomAndDeviceId.replaceAll(roomConnector.getRoomId() + ':', '');
				$('#join-devices').append('<div>・' + deviceId + '</div>');
			});
		} else if (data.substring(0, 8) === 'message:') {
			// 5.メッセージが送られてきたらそれを画面上に表示
			var message = data.substring(8);
			var html = '<div>' + message + ':' + new Date().toLocaleString() + '</div>';
			$('#receive').append(html);
		}
	});

	// 6.部屋に入るボタンを押したとき
	$('#btn-enter-room').on('click', function() {
		var roomId = $('#enter-room-id').val();
		if (!roomId) {
			alert('部屋IDが空です');
			return;
		}
		// 7.セミコロンが部屋IDに指定されているとバグるのでそれを回避
		if (roomId.indexOf(':') > -1) {
			alert('部屋IDにコロン(:)は利用できません');
			return;
		}
		roomConnector.open(roomId);
		$('#enter-room').hide();
		$('#current-room-id').text(roomId);
		$('#current-room').show();
	});

	// 8.送信ボタンを押したとき
	$('#btn-send').on('click', function() {
		var message = $('#send').val();
		roomConnector.send(message + ':' + roomConnector.getDeviceId());
		$('#send').val('');
	});

});