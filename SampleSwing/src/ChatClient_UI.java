import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;


public class ChatClient_UI implements ActionListener{

	JFrame Frame = new JFrame("Chat");
	JPanel Panel = new JPanel();
	JTextArea txtChatArea = new JTextArea(15,20);
	JTextArea txtTypeArea = new JTextArea(5,20);
	JButton butSend = new JButton("Send");
	
	public void Display()
	{
		Frame.setVisible(true);
		Frame.setSize(250,400);
		//Frame.
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//txtChatArea.setSize(250, 200);
		//txtTypeArea.setSize(250, 200);
		butSend.setSize(50, 50);
		butSend.addActionListener(this);
		
		//butSend.registerKeyboardAction(butSend.getActionForKeyStroke(v)), aKeyStroke, aCondition)
		
		Panel.add(txtChatArea);
		Panel.add(txtTypeArea);
		Panel.add(butSend);
		Frame.add(Panel);
		
		JRootPane rootPane = txtTypeArea.getRootPane();
	    rootPane.setDefaultButton(butSend);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		txtChatArea.append("cur:"+txtTypeArea.getText()+"\n");
		txtTypeArea.setText("");
	}
}
