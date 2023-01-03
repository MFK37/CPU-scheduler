import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionEvent;

public class mainPage implements ActionListener{

	private JFrame frame;
	protected JTextField path;
	private JLabel title, fileLabel;
	private JButton open, start;
	private JRadioButton srtf, rr, priority;
	JFileChooser fileChososer;
	int algorithm = 0;
	protected process p[];
	public String url;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainPage window = new mainPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mainPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame("CPU Scheduler");
		frame.setBounds(100, 100, 450, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		title = new JLabel("CPU scheduler");
		title.setFont(new Font("Tahoma", Font.PLAIN, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(111, 11, 208, 54);
		frame.getContentPane().add(title);
		
		fileLabel = new JLabel("File");
		fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fileLabel.setBounds(54, 76, 78, 24);
		frame.getContentPane().add(fileLabel);
		
		open = new JButton("Open");
		open.setBounds(170, 109, 78, 24);
		open.addActionListener(this);
		frame.getContentPane().add(open);
		
		path = new JTextField();
		path.setEditable(false);
		path.setBounds(111, 78, 208, 20);
		frame.getContentPane().add(path);
		path.setColumns(10);
		
		srtf = new JRadioButton("SRTF");
		srtf.setBounds(111, 157, 67, 23);
		srtf.addActionListener(this);
		frame.getContentPane().add(srtf);
		
		rr = new JRadioButton("RR");
		rr.setBounds(180, 157, 48, 23);
		rr.addActionListener(this);
		frame.getContentPane().add(rr);
		
		priority = new JRadioButton("Priority");
		priority.setBounds(230, 157, 89, 23);
		priority.addActionListener(this);
		frame.getContentPane().add(priority);
		
		ButtonGroup group = new ButtonGroup();
		group.add(srtf);
		group.add(rr);
		group.add(priority);
		
		
		start = new JButton("Start");
		start.setBounds(153, 230, 113, 36);
		start.addActionListener(this);
		frame.getContentPane().add(start);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			fileChososer = new JFileChooser();
			fileChososer.showOpenDialog(null);
			
			if (fileChososer.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(null, "You must select a .txt file", "ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			url = fileChososer.getSelectedFile().getAbsolutePath();				
			if (url.isBlank() || !url.endsWith(".txt")) {
				JOptionPane.showMessageDialog(null, "You must select a .txt file", "ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			path.setText(url);
			
		}
		else if (e.getSource() == srtf) {
			algorithm = 1;
		}
		else if (e.getSource() == rr) {
			algorithm = 2;
		}
		else if (e.getSource() == priority) {
			algorithm = 3;
		}
		else if (e.getSource() == start) {
			if (algorithm != 0 && !path.getText().equals(null)) {
				if (algorithm == 1) {
					srtf pSRTF = new srtf(path.getText());
					frame.dispose();
				}	
				else if (algorithm == 2) {
					rr r = new rr(path.getText());
					frame.dispose();
				}
				else {
					priority p = new priority(path.getText());
					visibleF();
				}
			}
			else if (path.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "You must select a .txt file", "ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
				
		}		
	}
	public void visibleF() {
		frame.setVisible(false);
	}
	public void visibleT() {
		frame.setVisible(true);
	}
	public void frameDispose() {
		frame.dispose();
	}
}
