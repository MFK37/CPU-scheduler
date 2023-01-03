import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import com.sun.management.OperatingSystemMXBean;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import java.awt.Color;

public class rr extends mainPage{	
	private JFrame frame;
	JTable table;
	DefaultTableModel tModel;
	private JLabel tableTitle;
	String row[] = {"PID", "BT"}, col[][];
	private JPanel ganttPanel;
	private JPanel cpuPanel;
	private JPanel averagePanel;
	private JPanel queuePanel_1;
	private JLabel average;
	private JLabel cpu, ganttLabelmid, ganttLabelTop, ganttLabelBtm;
	private JLabel ganttChart;
	private JLabel sliderTitle;
	private JButton start;
	public int counter, qCounter, totalBurst, remainingBurstTime[], quantumTime;
	public double avgWaiting, avgComp;
	private JLabel avWaiting;
	private JLabel avCompletion;
	private process p[];
	private JButton exit ;
	private JLabel avgWaitingLabel;
	private JLabel avgCompLabel;
	private JTextField quantumField;
	private JProgressBar progressBar;
	private JButton back;
	private JLabel utilzation;
	private JLabel quantumLabel;
	/**
	 * Create the application.
	 */
	public rr(String path) {
		initialize(path);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String path) {
		frame = new JFrame("Round Robin");
		frame.setSize(800,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table = new JTable();
		tModel = new DefaultTableModel(col, row);
		frame.getContentPane().setLayout(null);
		table.setModel(tModel);
		JScrollPane sp = new JScrollPane();
		sp.setBounds(10, 40, 200, 300);
		sp.setViewportView(table);
		frame.getContentPane().add(sp);
		
		tableTitle = new JLabel("Jobs table");
		tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableTitle.setBounds(10, 11, 200, 20);
		frame.getContentPane().add(tableTitle);
		
		ganttPanel = new JPanel();
		ganttPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		ganttPanel.setBounds(98, 351, 676, 99);
		frame.getContentPane().add(ganttPanel);
		ganttPanel.setLayout(null);
		
		ganttLabelBtm = new JLabel("");
		ganttLabelBtm.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelBtm.setBounds(10, 63, 656, 25);
		ganttPanel.add(ganttLabelBtm);
		
		ganttLabelmid = new JLabel("");
		ganttLabelmid.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelmid.setBounds(10, 38, 656, 25);
		ganttPanel.add(ganttLabelmid);
		
		ganttLabelTop = new JLabel("");
		ganttLabelTop.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelTop.setBounds(10, 11, 656, 25);
		ganttPanel.add(ganttLabelTop);
		
		cpuPanel = new JPanel();
		cpuPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cpuPanel.setBounds(230, 40, 200, 130);
		frame.getContentPane().add(cpuPanel);
		cpuPanel.setLayout(null);
		
		utilzation = new JLabel("0");
		utilzation.setHorizontalAlignment(SwingConstants.CENTER);
		utilzation.setBounds(10, 11, 180, 108);
		cpuPanel.add(utilzation);
		
		averagePanel = new JPanel();
		averagePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		averagePanel.setBounds(230, 210, 200, 130);
		frame.getContentPane().add(averagePanel);
		averagePanel.setLayout(null);
		
		avWaiting = new JLabel("Waiting");
		avWaiting.setHorizontalAlignment(SwingConstants.CENTER);
		avWaiting.setBounds(10, 11, 75, 14);
		averagePanel.add(avWaiting);
		
		avCompletion = new JLabel("Completion \r\n");
		avCompletion.setHorizontalAlignment(SwingConstants.CENTER);
		avCompletion.setBounds(115, 11, 75, 14);
		averagePanel.add(avCompletion);
		
		avgWaitingLabel = new JLabel("0");
		avgWaitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avgWaitingLabel.setBounds(10, 36, 75, 83);
		averagePanel.add(avgWaitingLabel);
		
		avgCompLabel = new JLabel("0");
		avgCompLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avgCompLabel.setBounds(115, 36, 75, 83);
		averagePanel.add(avgCompLabel);
		
		queuePanel_1 = new JPanel();
		queuePanel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		queuePanel_1.setBounds(440, 40, 334, 300);
		frame.getContentPane().add(queuePanel_1);
		queuePanel_1.setLayout(null);
		
		exit = new JButton("Exit");
		exit.setBounds(235, 80, 89, 23);
		queuePanel_1.add(exit);
		exit.addActionListener(this);
		
		JLabel label = new JLabel("New label");
		label.setBounds(65, 14, 200, 0);
		queuePanel_1.add(label);
		
		sliderTitle = new JLabel("Simulation progress\r\n");
		sliderTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sliderTitle.setBounds(111, 7, 107, 14);
		queuePanel_1.add(sliderTitle);
		
		start = new JButton("Start");
		start.addActionListener(this);

		start.setBounds(10, 80, 89, 23);
		queuePanel_1.add(start);
		
		quantumField = new JTextField();
		quantumField.setBounds(123, 114, 89, 23);
		queuePanel_1.add(quantumField);
		quantumField.setColumns(10);
		
		progressBar = new JProgressBar(0, 10000);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setBounds(10, 25, 314, 26);
		queuePanel_1.add(progressBar);
		
		back = new JButton("Back");
		back.setBounds(123, 80, 89, 23);
		queuePanel_1.add(back);
		
		quantumLabel = new JLabel("Quantum size");
		quantumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		quantumLabel.setBounds(39, 114, 80, 23);
		queuePanel_1.add(quantumLabel);
		back.addActionListener(this);
		
		
		average = new JLabel("Average");
		average.setHorizontalAlignment(SwingConstants.CENTER);
		average.setBounds(229, 181, 201, 20);
		frame.getContentPane().add(average);
		
		cpu = new JLabel("CPU utilization");
		cpu.setHorizontalAlignment(SwingConstants.CENTER);
		cpu.setBounds(229, 14, 201, 20);
		frame.getContentPane().add(cpu);
		
		ganttChart = new JLabel("Gantt Chart\r\n");
		ganttChart.setHorizontalAlignment(SwingConstants.CENTER);
		ganttChart.setBounds(10, 351, 78, 99);
		frame.getContentPane().add(ganttChart);
		try {
			readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readFile(String path) throws Exception{
		FileReader reader = new FileReader(path);
		BufferedReader bReader = new BufferedReader(reader);
		String line;
		p = new process[30];
		counter = 0;
		
		while ((line = bReader.readLine()) != null) {

			String c1 = line.split("Job")[1];
			String c2 = bReader.readLine();
			
			int a = Integer.parseInt(c1);
			int b = Integer.parseInt(c2);
			
			p[counter++] = new process(a,0,b);
		}
		for (int i = 0 ; i < counter ; i++) {			
				tModel.addRow(p[i].tableDataR());
		}
		reader.close();
		bReader.close();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			progressBar.setValue(0);
			String s = quantumField.getText();
			if (!s.matches(".*\\d.*")) {
				JOptionPane.showMessageDialog(null, "You must enter only numbers", "ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			quantumTime = Integer.parseInt(quantumField.getText());
			runCalc();
			findavgTime(p, counter, quantumTime);
		}
		else if (e.getSource() == exit) {
			frame.dispose();
			mainPage p = new mainPage();
			p.frameDispose();
		}
		else if (e.getSource() == back) {
			frame.dispose();
			mainPage p = new mainPage();
			p.visibleT();
		}
	}	
    public void waitingTime(process proc[], int n, int wt[], int quantum){
    	int rem_bt[] = new int[n];
	    for (int i = 0 ; i < n ; i++)
	        rem_bt[i] =  proc[i].bt;
	 
	    int t = 0;
	    while(true){
	        boolean done = true;
	        for (int i = 0 ; i < n; i++){
	            if (rem_bt[i] > 0){
	                done = false;
	                if (rem_bt[i] > quantum){
	                    t += quantum;
	                    rem_bt[i] -= quantum;
	                }
	                else
	                {
	                    t = t + rem_bt[i];
	                    wt[i] = t - proc[i].bt;
	                    rem_bt[i] = 0;
	                }
	            }
	        }
	       if (done == true)
	         break;
	   }
	}
	public void turnAroundTime(process proc[], int n, int wt[], int tat[]){
	   for (int i = 0; i < n ; i++)
	       tat[i] = proc[i].bt + wt[i];
	}
	public void findavgTime(process proc[], int n, int quantum){
		int waitingTime[] = new int[n];
		int turnAroundTime[] = new int[n];
	    int total_wt = 0, total_tat = 0;
	    
	    waitingTime(proc, n, waitingTime, quantum);
	    turnAroundTime(proc, n, waitingTime, turnAroundTime);
	 
	    for (int i=0; i<n; i++){
	        total_wt = total_wt + waitingTime[i];
	        total_tat = total_tat + turnAroundTime[i];
	    }
	    
	    setGantt(proc, n, quantum);
       avgWaiting = (double)total_wt / (double)n;
       avgComp = (double)total_tat / (double)n;
	}
	public void setGantt(process[] pr , int n, int q) {
	    int test = 0;
	    int sumBt = 0;
	    
		String top = "";
		String mid = "";
		String btm = "";
	    
	    for (int e = 0 ; e < counter ; e++) {
		    sumBt += pr[e].bt;
	    }
	   
	    process[] temp = new process[30];
	    int tempC = 0;
	    for (int i = 0; i < counter ; i++) {
		    temp[i] = pr[i];
		    tempC++;
	    }
	   
	    System.out.print("-");
	    top += "-";
	    for (int j = 0 ; j < sumBt ; j++)
	    	top += "--";
	    top += "-";
	    
	    int s = sumBt;
	    mid += "|";
	    boolean done = false;
	   
	    while(!done) {
		    for (int j = 0, h = 0; j < q ; j++) {
		 	   if (temp[test].bt == 0) {
			 	   break;
			   }
			   if (h == 0) {
				   mid += " ";
				   mid += ("P" + temp[test].pid);
				   mid += " ";
				   h++;
			   }

			   temp[test].bt--;
			   s--;
		   }
		   if (s == 0)
			   done = true;
		   test++;
		   if (test == tempC)
			   test = 0;
		   if (temp[test].bt != 0)
			   mid += "|";
	   }
	    mid += "|";
	   System.out.print("|\n");
	   
	   btm += "-";
	   
	   System.out.print("-");
	   for (int j = 0 ; j < sumBt ; j++) 
		   btm += "--";
	   btm += "-";
		ganttLabelTop.setText(top);
		ganttLabelmid.setText(mid);
		ganttLabelBtm.setText(btm);

		
	}
    public void setLabel() {
		String Waiting = new DecimalFormat("##.##").format(avgWaiting);
		String Completing = new DecimalFormat("##.##").format(avgComp);

		avgWaitingLabel.setText(Waiting);
		avgCompLabel.setText(Completing);
    }
    public void runCalc() {
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        TwoWorker task = new TwoWorker();
        task.execute();
    }
    public String cpuUtalization() {
    	OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    	double use = (osBean.getProcessCpuLoad() / 1) * 100;
    	String usage = new DecimalFormat("##.##").format(use);
    	return usage;
    }
    private class TwoWorker extends SwingWorker<Integer, Integer> {     
        @Override
        protected Integer doInBackground() throws Exception {
            for (int i = 0; i < 100; i++) {                
                progressBar.setValue(progressBar.getValue()+100);
                utilzation.setText(cpuUtalization() + "%");
                Thread.sleep(10); // simulate latency
            }
            setLabel();
            return Integer.valueOf(10);
        }
    }
}
