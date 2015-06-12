package sample;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.channel.ChannelServiceFactory;

/**
*
* @author jazzmaster0601
*
*/
@Controller
@RequestMapping("/_ah/channel")
public class ChannelController {

	private String getRoomAndDeviceId(HttpServletRequest req) throws IOException {
		return ChannelServiceFactory.getChannelService().parsePresence(req).clientId();
	}

	private String getRoomId(String deviceId) {
		return deviceId.split(":")[0];
	}

	/**
	 * 部屋への接続が完了したときに呼ばれます
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "connected", method = RequestMethod.POST)
	@ResponseBody
	public String connect(HttpServletRequest req) throws IOException {
		String roomAndDeviceId = getRoomAndDeviceId(req);
		String roomId = getRoomId(roomAndDeviceId);
		Room room = RoomManager.getInstance().getRoom(roomId);
		room.sendJoinRoomAndDeviceIds();
		return "success";
	}

	/**
	 * ブラウザが閉じられたときに呼ばれます
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "disconnected", method = RequestMethod.POST)
	@ResponseBody
	public String disconnect(HttpServletRequest req) throws IOException {
		String roomAndDeviceId = getRoomAndDeviceId(req);
		String roomId = getRoomId(roomAndDeviceId);
		RoomManager manager = RoomManager.getInstance();
		Room room = manager.getRoom(roomId);
		room.exit(roomAndDeviceId);
		manager.tryToCloseRoom(roomId);
		room = manager.getRoom(roomId);
		if (room != null) {
			room.sendJoinRoomAndDeviceIds();
		}
		return "success";
	}

}
