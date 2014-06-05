package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Playlist {
	static int count = 1;
	HashMap<Integer, String> list = new HashMap<Integer, String>();
	static String name = null;
	
	
	public Playlist(String path){
		File file = new File(path);
		loadDirectory(file);
	}
	
	public Playlist(File file){
		loadFromFile(file);
	}

	private void loadFromFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			name = br.readLine();
			String line;
			while((line = br.readLine()) != null){
				list.put(count, line);
				count++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void loadDirectory(File file) {
		if(file.isDirectory()){
			for(File dir : file.listFiles()){
				if(dir.getName().endsWith(".mp3") || dir.getName().endsWith(".wav") || dir.getName().endsWith(".mid")|| dir.getName().endsWith(".aif")){
					list.put(count, dir.getAbsolutePath());
					count++;
				}
				
			}
		}else{
			list.put(count, file.getAbsolutePath());
		}
		
	}
	
	public String getName(){
		return name;
	}
	
	public int getCount(){
		return count;
	}
	
	public void shuffle(){
		List<Integer> keys = new ArrayList<Integer>(list.keySet());
		Collections.shuffle(keys);
		for(int in = 1; in < getCount(); in++){
			String temp = list.get(in);
			list.put(in, list.get(keys.get(in)));
			System.out.println("----------"+list.get(in));
			list.put(keys.get(in), temp);
			System.out.println("----------"+list.get(keys.get(in)));
			in++;
		}
		MediaPlayer.redoList(this);
	}
	
	public String get(int count){
		return list.get(count);
	}
	
	public void add(String path){
		list.put(count, path);
		count++;
	}
	
	public void remove(String name){
		for(int in = 1; in< getCount(); in++){
			if(list.get(in).endsWith(name)){
				list.remove(in);
				break;
			}
		}
	}
	
	public void swap(String name, String name2) {
		int one;
		String oneTemp = "";
		for(one = 1; one< getCount(); one++){
			if(get(one).endsWith(name)){
				oneTemp = get(one);
				list.remove(one);
				break;
			}
		}
		int two;
		String twoTemp = "";
		for(two = 0; two< getCount(); two++){
			if(get(two).endsWith(name)){
				twoTemp = get(two);
				list.remove(two);
				break;
			}
		}
		list.put(two, oneTemp);
		list.put(one, twoTemp);
		
	}
	
	public void replaceTitle(String name, String path) {
		for(int in = 0; in< getCount(); in++){
			if(get(in).endsWith(name)){
				list.remove(in);
				list.put(in, path);
				break;
			}
		}
		
	}
	

}
