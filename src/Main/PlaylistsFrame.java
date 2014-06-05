package Main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;

public class PlaylistsFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void start(Player p, MediaPlayer media) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlaylistsFrame frame = new PlaylistsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PlaylistsFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 334, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JList<Object> playlists = new JList<Object>();
		 JScrollPane PlaylistList = new JScrollPane(playlists, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(PlaylistList, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton Play_List = new JButton("Play List");
		panel.add(Play_List);
		
		JButton DeleteList = new JButton("Delete List");
		panel.add(DeleteList);
		
		JButton CreateList = new JButton("Create List");
		panel.add(CreateList);
	}

}
