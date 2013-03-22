import java.awt.CardLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.ListSelectionModel;


@SuppressWarnings("serial")
public class ServerUI extends JFrame {
	
	List<Client> Clients; 
	JList list;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI frame = new ServerUI();
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
	public ServerUI() {
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, "name_1334116049652236000");
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitPane.setRightComponent(list);
		
		JLabel lblClients = new JLabel("Clients");
		splitPane.setLeftComponent(lblClients);
		
		//updateList();
	}

	public void SetClients(List<Client> alClients) {
		// TODO Auto-generated method stub
		Clients = alClients;
		
	}

	public void updateList() {
		// TODO Auto-generated method stub
		
		if(Clients!=null)
		{
			List<String> names = new ArrayList<String>();			
			for (Client tempClient : Clients) {
				names.add(String.format("%d", tempClient.s.getPort()));
			}		
			list = new JList((String[])names.toArray());
		}
		return;
	}
}
