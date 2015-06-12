package sample;

import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

/**
 *
 * @author jazzmaster0601
 *
 */
public class Room {

	private Set<String> joinRoomAndDeviceIds;

	public Room() {
		this.joinRoomAndDeviceIds = new HashSet<>();
	}

	/**
	 * この部屋に端末を参加させます
	 * @param roomAndDeviceId
	 * @return
	 */
	public String join(String roomAndDeviceId) {
		joinRoomAndDeviceIds.add(roomAndDeviceId);
		return ChannelServiceFactory.getChannelService().createChannel(roomAndDeviceId);
	}

	/**
	 * この部屋から端末を退出させます
	 * @param roomAndDeviceId
	 */
	public void exit(String roomAndDeviceId) {
		joinRoomAndDeviceIds.remove(roomAndDeviceId);
	}

	public boolean isEmpty() {
		return joinRoomAndDeviceIds.isEmpty();
	}

	private void send(String data) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		for (String targetRoomConnectId : joinRoomAndDeviceIds) {
			channelService.sendMessage(new ChannelMessage(targetRoomConnectId, data));
		}
	}

	/**
	 * この部屋への参加者一覧をこの部屋の参加者のみにプッシュ配信します
	 */
	public void sendJoinRoomAndDeviceIds() {
		send("joinRoomAndDeviceIds:" + joinRoomAndDeviceIds.toString());
	}

	/**
	 * この部屋へのメッセージをこの部屋の参加者のみにプッシュ配信します
	 * @param message
	 */
	public void sendMessage(String message) {
		send("message:" + message);
	}

}
