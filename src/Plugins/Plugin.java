package Plugins;

public interface Plugin {
	
	public boolean onEnable();
	
	public boolean onDisable();
	
	public void setPluginManager(PManager pManager);

}
