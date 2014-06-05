package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTextArea;

import Plugins.PManager;

import javax.swing.UIManager;

import java.awt.Toolkit;


public class MediaPlayer extends JFrame{
	 /**
	 * 
	 */
	/**
	 * File: JPlayer.jar
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
	private static final long serialVersionUID = 1L;
	public static JSlider frameslider;
	static String linee;
	static boolean play = true;
	static Object lock = new Object();
	private JButton playbutton;
	static JButton nextbutton;
	static boolean next;
	static  MediaPlayer g;
	static JFrame main;
	static boolean end;
	static boolean filechoosen;
	static JTextField textField;
	static JList<String> list;
	static ListModel<String> model;
	public static JTextArea debug;
	static Player p;
	static public JTextField currentTime;
	static public JTextField MaxDuration;
	public static boolean noPlaylist = true;
	private static Playlist playlist;
	public static PManager manager;
	  public static void main(String[] args) {
		 @SuppressWarnings("unused")
		Admin admin = startAdministartion();
		  g = new MediaPlayer();
		  g.setAlwaysOnTop(true);
		  if(args[0] != null){
			  playlist = new Playlist(args[0]);
			  p = new Player(playlist);
			  redoList(playlist);
		  manager = new PManager(p);
		  manager.start();
		  }else{
			  noPlaylist = true;
			  p = new Player();
			  manager = new PManager(p);
			  manager.start();
		  }
		  System.out.println(System.getProperty("user.dir"));
	        main.setVisible(true);
	  }
	  
	  

	private static Admin startAdministartion() {
		Admin ad = new Admin(readInstallLocation());
		return ad;
	}



	private static String readInstallLocation() {
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



	public MediaPlayer(){
		  main = new JFrame("MediaPlayer");
		  main.setTitle("JPlayer");
		  main.setSize(505,360);
		  main.setLocation(300,300);
		  main.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			        
			           close();
			   
			    }
			});
		  main.getContentPane().setBackground(UIManager.getColor("ComboBox.background"));
		  main.getContentPane().setLayout(new BorderLayout(3,2));
		  main.setIconImage(Toolkit.getDefaultToolkit().getImage(MediaPlayer.class.getResource("/pictures/test.jpg")));
	        @SuppressWarnings("unused")
			Font font = new Font("Serif", Font.ITALIC, 15);
		  JMenuBar menubar = new JMenuBar();
		  
		  JPanel MainPanel = new JPanel();
		  
		  MainPanel.setBackground(Color.white);
		  main.getContentPane().add(MainPanel, BorderLayout.CENTER);
		  MainPanel.setLayout(new BorderLayout(0, 0));
		  textField = new JTextField();
		  textField.setOpaque(false);
		  textField.setEditable(false);
		  MainPanel.add(textField, BorderLayout.NORTH);
		  textField.setColumns(10);
		  
		  model = new DefaultListModel<String>();
		  
		  list = new JList<String>(model);
		  list.setBackground(Color.white);
		  list.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println(arg0.getKeyCode());
				if(arg0.getKeyCode() == KeyEvent.VK_DELETE)
			    {  
					p.removeTitle(list.getSelectedValue());
					redoList(p.playlist);
			    }
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			  
		  });
		  list.setDropTarget(new DropTarget() {
		        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				public synchronized void drop(DropTargetDropEvent evt) {
		            try {
		                evt.acceptDrop(DnDConstants.ACTION_COPY);
		                @SuppressWarnings("unchecked")
						List<File> droppedFiles = (List<File>) evt
		                        .getTransferable().getTransferData(
		                                DataFlavor.javaFileListFlavor);
		                for (File file : droppedFiles) {
		                   p.addTitle(file.getAbsolutePath());
		                }
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }
		        }
		    });
		  list.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				String filename = list.getSelectedValue();
				p.changeTitle(filename);
			}
 
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			  
		  });
		  
		  MainPanel.add(list, BorderLayout.CENTER);
		  
		  JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		  MainPanel.add(scrollPane, BorderLayout.CENTER);
		  
		  JPanel SliderButtons = new JPanel();
		  MainPanel.add(SliderButtons, BorderLayout.SOUTH);
		  SliderButtons.setLayout(new BorderLayout(0, 0));
		  
		  JPanel Sliders = new JPanel();
		  SliderButtons.add(Sliders, BorderLayout.NORTH);
		  Sliders.setLayout(new BorderLayout(0, 0));
		  
		  JPanel Duration = new JPanel();
		  Sliders.add(Duration, BorderLayout.NORTH);
		  Duration.setLayout(new BorderLayout(0, 0));
		  
		  currentTime = new JTextField();
		  currentTime.setEditable(false);
		  Duration.add(currentTime, BorderLayout.WEST);
		  currentTime.setColumns(10);
		  
		  MaxDuration = new JTextField();
		  MaxDuration.setEditable(false);
		  Duration.add(MaxDuration, BorderLayout.EAST);
		  MaxDuration.setColumns(10);
		  
		  frameslider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
		  Duration.add(frameslider, BorderLayout.NORTH);
		  frameslider.setFont(new Font("Serif", Font.ITALIC, 15));
		  frameslider.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent e) {
					JSlider slider = (JSlider) e.getSource();
					if(p.isprogramm){
					}else{
					p.setMicrosecondPosition(slider.getValue());
					}
				}
				  
			  });
		  
		  JSlider slider_1 = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 80);
		  Sliders.add(slider_1, BorderLayout.SOUTH);
		  slider_1.setPaintTicks(true);
		  slider_1.setPaintLabels(true);
		  slider_1.setMinorTickSpacing(1);
		  slider_1.setMajorTickSpacing(10);
		  slider_1.setFont(new Font("Serif", Font.ITALIC, 15));
		  slider_1.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		  
		  JPanel Buttons = new JPanel();
		  SliderButtons.add(Buttons, BorderLayout.SOUTH);
		  playbutton = new JButton("Play");
		  Buttons.add(playbutton);
		  nextbutton = new JButton("Next");
		  Buttons.add(nextbutton);
		  
		  JButton back = new JButton("Back");
		  Buttons.add(back);
		  
		  JButton random = new JButton("Shuffle");
		  random.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent arg0) {
		  		
		  		p.random();
		  	}
		  });
		  Buttons.add(random);
		  
		  JButton replay = new JButton("Replay");
		  replay.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(p.replay){
					p.replay(false);
				}else{
					p.replay(true);
				}
			}
			  
		  });
		  Buttons.add(replay);
		  
		  JButton btnPlaylists = new JButton("Playlists");
		  Buttons.add(btnPlaylists);
		  btnPlaylists.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
			  
		  });
		  back.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					p.lastTitle();
					
				}
		  });
		  
		  
		 
		  nextbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				p.nextTitle();
				
			}
			  
		  });
		  playbutton.addActionListener(new ActionListener(){
			   public void actionPerformed(ActionEvent ae){
				   if(p.isPlaying){
					   playbutton.setText("Play");
					   p.pauseTitle();
				   }else{
					   playbutton.setText("Pause");
					   p.playTitle();
				   }
			   }
				  });
		  slider_1.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				 p.volume = (float)slider.getValue();
				p.volControl.setValue(p.volume-100F);
				
			}
			  
		  });
		  
		  debug = new JTextArea();
		  debug.setOpaque(false);
		  MainPanel.add(debug, BorderLayout.WEST);
		  debug.setEditable(false);
		  JMenu file = new JMenu("File");
	        file.setMnemonic(KeyEvent.VK_F);

	        JMenuItem exit = new JMenuItem("Exit");
	        exit.setMnemonic(KeyEvent.VK_E);
	        exit.setToolTipText("Exit application");
	        exit.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	                System.exit(0);
	            }
	        });
	        JMenuItem reload = new JMenuItem("Reload Plugins");
	        reload.setMnemonic(KeyEvent.VK_R);
	        reload.setToolTipText("Reload all Plugins");
	        reload.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					manager.reload();
				}
	        	
	        });
	        JMenuItem chooseFile = new JMenuItem("Select File");
	        chooseFile.setMnemonic(KeyEvent.VK_C);
	        chooseFile.setToolTipText("Choose File to play");
	        chooseFile.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent event) {
					
	                JFileChooser ch = new JFileChooser();
	                ch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	                ch.showDialog(null, "Wähle die abzuspielende Datei");
	                String chfile = ch.getSelectedFile().getAbsolutePath();
	                if(noPlaylist){
	                	noPlaylist = false;
	                	 playlist = new Playlist(chfile);
	                	p =  new Player(playlist);
	    	      		  
	                }else{
	                playlist = new Playlist(chfile);
	                p.setPlaylist(playlist);
	                }
	                
	            }
	        });

	        file.add(exit);
	        file.add(chooseFile);
	        file.add(reload);
	        
	        menubar.add(file);

	        main.setJMenuBar(menubar);

		  
	  }
	 private int close() {
		manager.stop();
		System.exit(0);
		return JFrame.EXIT_ON_CLOSE;
	}



	public BufferedImage scaleImage(BufferedImage img, Dimension d) {
	        img = scaleByHalf(img, d);
	        img = scaleExact(img, d);
	        return img;
	    }

	    private BufferedImage scaleByHalf(BufferedImage img, Dimension d) {
	        int w = img.getWidth();
	        int h = img.getHeight();
	        float factor = getBinFactor(w, h, d);

	        // make new size
	        w *= factor;
	        h *= factor;
	        BufferedImage scaled = new BufferedImage(w, h,
	                BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = scaled.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	        g.drawImage(img, 0, 0, w, h, null);
	        g.dispose();
	        return scaled;
	    }

	    private BufferedImage scaleExact(BufferedImage img, Dimension d) {
	        float factor = getFactor(img.getWidth(), img.getHeight(), d);

	        // create the image
	        int w = (int) (img.getWidth() * factor);
	        int h = (int) (img.getHeight() * factor);
	        BufferedImage scaled = new BufferedImage(w, h,
	                BufferedImage.TYPE_INT_RGB);

	        Graphics2D g = scaled.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.drawImage(img, 0, 0, w, h, null);
	        g.dispose();
	        return scaled;
	    }

	    float getBinFactor(int width, int height, Dimension dim) {
	        float factor = 1;
	        float target = getFactor(width, height, dim);
	        if (target <= 1) { while (factor / 2 > target) { factor /= 2; }
	        } else { while (factor * 2 < target) { factor *= 2; }         }
	        return factor;
	    }

	    float getFactor(int width, int height, Dimension dim) {
	        float sx = dim.width / (float) width;
	        float sy = dim.height / (float) height;
	        return Math.min(sx, sy);
	    }



		public static void redoList(Playlist playlist) {
			((DefaultListModel<String>) model).removeAllElements();
			File file;
			
			for(int i = 1; i< playlist.getCount(); i++ ) {
				file = new File(playlist.get(i));
				System.out.println(file.getName());
				((DefaultListModel<String>) model).addElement(file.getName());
			}
			
		}



		public static int getListLength() {
			return ((DefaultListModel<String>) model).getSize();
		}
}
