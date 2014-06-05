package Main;

import java.util.HashMap;

public class Admin {
	HashMap<String, Playlist> lists = new HashMap<String, Playlist>();

	public Admin(String readInstallLocation) {
		init();
	}

	private void init() {
		loadPlaylists();
		
	}

	private void loadPlaylists() {
		// TODO Auto-generated method stub
		
	}

}
