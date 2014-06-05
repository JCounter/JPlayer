	package Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import Main.MediaPlayer;
import Main.Player;
import Main.Playlist;

public class PManager implements PluginLoader{
	
	Player p;
	private Vector<Plugin> loadedplugins = new Vector<Plugin>();
	public PManager(Player p){
		this.p = p;
	}
	
	public void start(){
		System.out.println(readInstallLocation());
		 File[] files = new File(readInstallLocation()+"\\plugins").listFiles();
		  for(File f:files){
			  if(f.getName().endsWith(".jar")){
		    loadPlugin(f);
			  }
		  }
		  for(Plugin pi:loadedplugins){
			  System.out.println("Plugin loaded!");
			    pi.setPluginManager(this);
		    pi.onEnable();
		  }
		}
	
	public void reload(){
		if(loadedplugins.size()!= 0)
			 for(Plugin pi:loadedplugins){
				 try{
				    pi.onDisable();
				 }catch(NullPointerException e){
				 }
				  }
		 File[] files = new File(readInstallLocation()+"\\plugins").listFiles();
		  for(File f:files){
			  if(f.getName().endsWith(".jar")){
		    loadPlugin(f);
			  }
		  }
		  for(Plugin pi:loadedplugins){
			  System.out.println("Plugin loaded!");
			    pi.setPluginManager(this);
		    pi.onEnable();
		  }
	}
	
	private String readInstallLocation() {
		 try {
			Process process = Runtime.getRuntime().exec("reg query HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\JPlayer /v InstallLocation");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String returned = "";
			String line;
			while((line = br.readLine()) != null){
				returned = returned + line;
			}
			 int index = returned.lastIndexOf("REG_SZ");
			 return returned.substring(index).replaceFirst("\\s\\s\\s\\s", "").replaceFirst("REG_SZ", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stop(){
		if(loadedplugins.size()!= 0)
		 for(Plugin pi:loadedplugins){
			 try{
			    pi.onDisable();
			 }catch(NullPointerException e){
			 }
			  }
	}
	private void loadPlugin(File f) {
		//Erzeugen des JAR-Objekts
		try {
			//Laden der MANIFEST.MF
			@SuppressWarnings("resource")
			InputStream in =new URLClassLoader(new URL[]{f.toURI().toURL()}).getResourceAsStream("plugin.properties");
			Properties props = new Properties();
			props.load(in);
			// holen der Mainclass aus den Attributen
			String main = props.getProperty("Main-Class");
			// laden der Klasse mit dem File als URL und der Mainclass
			@SuppressWarnings("resource")
			Class<?> cl = new URLClassLoader(new URL[]{f.toURI().toURL()}).loadClass(main);
			in.close();
			// holen der Interfaces die die Klasse impementiert
			Class<?>[] interfaces = cl.getInterfaces();
			// Durchlaufen durch die Interfaces der Klasse und nachsehn ob es das passende Plugin implementiert
			boolean isplugin = false;
			for(int y = 0; y < interfaces.length && !isplugin; y++)
			  if(interfaces[y].getName().equals("Plugins.Plugin"))
			    isplugin = true;
			if(isplugin){
			  Plugin plugin = (Plugin) cl.newInstance();
			  loadedplugins.add(plugin);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean setTitle(String name) {
		p.changeTitle(name);
		return true;
	}

	@Override
	public boolean setPlaylist(Playlist playlist) {
		p.setPlaylist(playlist);
		return true;
	}

	@Override
	public boolean redoList(Playlist playlist) {
		MediaPlayer.redoList(playlist);
		return true;
	}

	@Override
	public boolean play() {
		p.playTitle();
		return true;
	}

	@Override
	public boolean pause() {
		p.pauseTitle();
		return true;
	}

	@Override
	public boolean replay(boolean replay) {
		p.replay(replay);
		return true;
	}

	@Override
	public String getMilliAsTime(long milli) {
		int seconds = (int)(milli/1000000);
		int minutes = Math.round(seconds/60);
		int mseconds = seconds - minutes*60;
		
		
		return minutes+":"+mseconds;
	}

	@Override
	public boolean nextTitle() {
		p.nextTitle();
		return true;
	}

	@Override
	public boolean lastTitle() {
		p.lastTitle();
		return true;
	}

	@Override
	public boolean setPosition(int seconds) {
		p.setMicrosecondPosition(seconds);
		return true;
	}

	@Override
	public long getPosition() {
		return p.getMicrosecondPosition();
	}

	@Override
	public long getTitleLength() {
		return p.getClipLength();
	}

	@Override
	public boolean addTitle(String path) {
		p.addTitle(path);
		return true;
	}

	@Override
	public boolean removeTitle(String name) {
		p.removeTitle(name);
		return true;
	}

	@Override
	public boolean replaceTitle(String nameToReplace, String path) {
		p.replaceTitle(nameToReplace, path);
		return true;
	}

	@Override
	public boolean swapTitle(String name, String name2) {
		p.swap(name, name2);
		return true;
	}

	@Override
	public boolean hasPlaylist() {
		if(p.noPlaylist){
		return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean startPlaylist(Playlist playlist) {
		if(MediaPlayer.noPlaylist){
			p.setPlaylist(playlist);
			return true;
		}else{
		return false;
		}
		
	}

	@Override
	public Playlist getPlaylist() {
		return p.playlist;
	}

}
