package sample;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jazmaster0601
 *
 */
public final class RoomManager {

	private static RoomManager instance;

	private Map<String, Room> allRooms;

	private RoomManager() {
		this.allRooms = new HashMap<>();
	}

	public static RoomManager getInstance() {
		if (instance == null) {
			instance = new RoomManager();
		}
		return instance;
	}

	/**
	 * 同じ部屋IDの部屋がなければ作成します
	 * あれば何もしません
	 * @param roomId
	 * @return
	 */
	public Room tryToOpenRoom(String roomId) {
		Room room = allRooms.get(roomId);
		if (room == null) {
			room = new Room();
			allRooms.put(roomId, room);
		}
		return room;
	}

	public Room getRoom(String roomId) {
		return allRooms.get(roomId);
	}

	/**
	 * 部屋IDに該当する部屋に参加者が一人も居なかったら部屋を閉設します
	 * 誰か1人でもいた場合は何もしません
	 * @param roomId
	 */
	public void tryToCloseRoom(String roomId) {
		Room room = allRooms.get(roomId);
		if (room != null && room.isEmpty()) {
			allRooms.remove(roomId);
		}
	}

}
