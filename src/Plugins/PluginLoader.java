package Plugins;


import Main.Playlist;

public interface PluginLoader {
	
	public boolean setTitle(String name);
	
	public boolean setPlaylist(Playlist playlist);
	
	public boolean redoList(Playlist playlist);
	
	public boolean play();
	
	public boolean pause();
	
	public boolean replay(boolean replay);
	
	public String getMilliAsTime(long milli);
	
	public boolean nextTitle();
	
	public boolean lastTitle();
	
	public boolean setPosition(int seconds);
	
	public long getPosition();
	
	public long getTitleLength();
	
	public boolean addTitle(String path);
	
	public boolean removeTitle(String name);
	
	public boolean replaceTitle(String nameToReplace, String path);
	
	public boolean swapTitle(String name, String name2);
	
	public boolean hasPlaylist();
	
	public boolean startPlaylist(Playlist playlist);
	
	public Playlist getPlaylist();

}
