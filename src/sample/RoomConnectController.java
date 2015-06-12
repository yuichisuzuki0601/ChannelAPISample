package sample;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
*
* @author jazzmaster0601
*
*/
@Controller
@RequestMapping("/room-connect")
public class RoomConnectController {

	private String getRoomId(Map<String, String> param) {
		return param.get("roomId");
	}

	/**
	 * 部屋へ参加させます
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "open", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String open(@RequestBody Map<String, String> param) {
		String roomId = getRoomId(param);
		String roomAndDeviceId = param.get("roomAndDeviceId");
		return RoomManager.getInstance().tryToOpenRoom(roomId).join(roomAndDeviceId);
	}

	/**
	 * 部屋へのメッセージを受け取り参加者に配信します
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "send", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String send(@RequestBody Map<String, String> param) {
		String roomId = getRoomId(param);
		String message = param.get("message");
		RoomManager.getInstance().getRoom(roomId).sendMessage(message);
		return "success";
	}

}
