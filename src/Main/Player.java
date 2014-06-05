package Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player extends Thread implements Runnable{
	
	/**
	 * File: JPlayer
	 * Author: Felix Specht
	 * Date: 01.03.2014
	 * Time: 18:00
	 * Contact: info@evil-craft.de
	 *
	 * This file is part of JPlayer.
	 *
	 * JPlayer is free software: you can redistribute it and/or modify
	 * it under the terms of the GNU General Public License as published by
	 * the Free Software Foundation, either version 3 of the License, or
	 * (at your option) any later version.
	 *
	 * JPlayer is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 * GNU General Public License for more details.
	 *
	 * You should have received a copy of the GNU General Public License
	 * along with this programm.  If not, see <http://www.gnu.org/licenses/>.
	 *
	 * Diese Datei ist Teil von JPlayer.
	 *
	 * Der JPlayer ist Freie Software: Sie koennen es unter
	 * den Bedingungen der GNU General Public License, wie von der
	 * Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl)
	 * jeder spaeteren veroeffentlichten Version, weiterverbreiten und/oder modifizieren.
	 *
	 * Der JPlayer wird in der Hoffnung, dass es nuetzlich sein wird, aber
	 * OHNE JEDE GEWAEHELEISTUNG, bereitgestellt; sogar ohne die implizite
	 * Gewaehrleistung der MARKTFAEHIGKEIT oder EIGNUNG FUER EINEN BESTIMMTEN ZWECK.
	 * Siehe die GNU General Public License fuer weitere Details.
	 *
	 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
	 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
	 */
	float volume = 80;
	 public Playlist playlist;
	boolean stop = false;
	boolean next = false;
	boolean play = true;
	Object lock = new Object();
	int i;
	boolean repeat_list = true;
	private Clip clip;
	public FloatControl volControl;
	public boolean isprogramm = true;
	public AudioFormat format;
	public boolean isPlaying;
	public boolean replay;
	boolean random = false;
	public boolean noPlaylist;
	private boolean newPlaylist = false;
	
	public Player(){
		while(MediaPlayer.noPlaylist){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public Player(Playlist playlist){
		this.playlist = playlist;
		this.start();
		
	}
	@Override
	public void run() {
		try {
			String file = "";
		clip = AudioSystem.getClip();
		
		while(true){
			if(stop){
				stop = false;
				break;
			}
		for(i = 1; i<getPlaylistLength(); i++){
			if(newPlaylist){
				newPlaylist = false;
				i = 1;
			}
			if(stop){
				break;
			}
			if(random){
				random = false;
			}
			file = playlist.get(i);
			
			MediaPlayer.textField.setText(new File(file).getName()); 
            AudioInputStream sound = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.getAudioInputStream(new File (file)));
            format = sound.getFormat();
			clip.open(sound);
			clip.setFramePosition(0);
			MediaPlayer.frameslider.setMaximum((int)clip.getMicrosecondLength()/1000000);
			MediaPlayer.frameslider.setValue(0);
			MediaPlayer.MaxDuration.setText(getMilliAsTime(clip.getMicrosecondLength()));
			clip.start();
			volControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volControl.setValue(volume-100F);
			while(clip.getMicrosecondPosition()!= clip.getMicrosecondLength() && clip.isOpen() && !stop){
				if(clip.isRunning()){
				isprogramm = true;
				MediaPlayer.frameslider.setValue(MediaPlayer.frameslider.getValue()+1);
				MediaPlayer.currentTime.setText(getMilliAsTime(clip.getMicrosecondPosition()));
				isprogramm = false;
				}
				Thread.sleep(1000);
			}
			clip.close();
			if(replay){
				i--;
			}
		}
		i= 1;
		}
		} catch (LineUnavailableException e) {
			MediaPlayer.debug.append(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			MediaPlayer.debug.append(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			MediaPlayer.debug.append(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			MediaPlayer.debug.append(e.getMessage());
			e.printStackTrace();
		}
	}
	private int getPlaylistLength() {
		return playlist.getCount();
	}
	public void changeTitle(String filename){
		int count;
		for(count = 1; count < playlist.getCount(); count++){
			if(playlist.get(count).contains(filename)){
				break;
			}
		}
		i = (count-1);
		clip.close();
	}
	public void lastTitle(){
		i--;
		i--;
		clip.close();
	}
	public void nextTitle(){
		clip.close();
	}
	
	public void pauseTitle(){
		isPlaying = false;
		clip.stop();
	}
	public void playTitle(){
		isPlaying = true;
		clip.start();
	}
	
	public void setMicrosecondPosition(int position){
		clip.setMicrosecondPosition((long)position*1000000);
		MediaPlayer.currentTime.setText(getMilliAsTime(clip.getMicrosecondPosition()));
	}
	
	public long getMicrosecondPosition(){
		return clip.getMicrosecondPosition();
	}
	
	public long getClipLength(){
		return clip.getMicrosecondLength()/1000000;
	}
	public void setPlaylist(Playlist playlist) {
		this.playlist=playlist;
		if(!MediaPlayer.noPlaylist){
		newPlaylist = true;
		clip.close();
		MediaPlayer.redoList(playlist);
		}else{
			MediaPlayer.p = this;
			this.start();
			MediaPlayer.noPlaylist = false;
			MediaPlayer.redoList(playlist);
		}
	}
	public String getMilliAsTime(long milli){
		int seconds = (int)(milli/1000000);
		int minutes = Math.round(seconds/60);
		int mseconds = seconds - minutes*60;
		
		
		return minutes+":"+mseconds;
		
	}
	public void replay(boolean replay) {
		this.replay = replay;
		
	}
	public void random() {
		random = true;
		playlist.shuffle();
		MediaPlayer.redoList(playlist);
	}
	
	public void stopPlayer(){
		clip.close();
		stop = true;
		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		i = 0;
	}
	
	public void addTitle(String path){
		playlist.add(path);
		MediaPlayer.redoList(playlist);
	}
	public void removeTitle(String name) {
		playlist.remove(name);
	}
	public void replaceTitle(String name, String path) {
		playlist.replaceTitle(name, path);
		
	}
	public void swap(String name, String name2) {
	playlist.swap(name, name2);
		
	}
}
