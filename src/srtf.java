import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionEvent;
import com.sun.management.OperatingSystemMXBean;
import java.awt.Color;
import javax.swing.JProgressBar;


public class srtf extends mainPage{

	private JFrame frame;
	JTable table;
	DefaultTableModel tModel;
	private JLabel tableTitle;
	String row[] = {"PID", "AT", "BT"}, col[][];
	private JPanel ganttPanel;
	private JPanel cpuPanel;
	private JPanel averagePanel;
	private JPanel queuePanel_1;
	private JLabel average, utilzation;
	private JLabel cpu;
	private JLabel ganttChart;
	private JLabel sliderTitle;
	private JButton start;
	private int counter;
	public double avgWaiting, avgComp;
	private JLabel avWaiting;
	private JLabel avCompletion;
	private process p[];
	private JButton exit ;
	private JLabel avgWaitingLabel;
	private JLabel avgCompLabel;
	private JProgressBar progressBar;
	private JButton back;
	private JLabel ganttLabelBtm;
	private JLabel ganttLabelmid;
	private JLabel ganttLabelTop;
	/**
	 * Create the application.
	 */
	public srtf(String path) {
		initialize(path);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String path) {
		frame = new JFrame("Shortest remaining time first");
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
		
		avgWaitingLabel = new JLabel("");
		avgWaitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avgWaitingLabel.setBounds(10, 36, 75, 83);
		averagePanel.add(avgWaitingLabel);
		
		avgCompLabel = new JLabel("");
		avgCompLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avgCompLabel.setBounds(115, 36, 75, 83);
		averagePanel.add(avgCompLabel);
		
		avgWaitingLabel.setText("0");
		avgCompLabel.setText("0");
		
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
		
		sliderTitle = new JLabel("Simulation progress");
		sliderTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sliderTitle.setBounds(111, 7, 107, 14);
		queuePanel_1.add(sliderTitle);
		
		start = new JButton("Start");
		start.addActionListener(this);

		start.setBounds(10, 80, 89, 23);
		queuePanel_1.add(start);
		
		progressBar = new JProgressBar(0,10000);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setBounds(10, 25, 314, 26);
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		queuePanel_1.add(progressBar);
		
		back = new JButton("Back");
		back.setBounds(121, 80, 89, 23);
		queuePanel_1.add(back);
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
			char c1 = line.charAt(0);
			char c2 = line.charAt(2);
			char c3 = line.charAt(4);
			
			int a = Character.getNumericValue(c1);
			int b = Character.getNumericValue(c2);
			int c = Character.getNumericValue(c3);
			
			p[counter++] = new process(a,b,c);
		}
		
		for (int i = 0 ; i < counter ; i++) {			
				tModel.addRow(p[i].tableData());
		}
		reader.close();
		bReader.close();		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			progressBar.setValue(0);
			avgWaitingLabel.setText("0");
			avgCompLabel.setText("0");
			runCalc();
			findavgTime(p, counter);
		}
		else if (e.getSource() == exit)
			frame.dispose();
		
		else if (e.getSource() == back) {
			frame.dispose();
			mainPage p = new mainPage();
			p.visibleT();
		}
	}
    public void waitingTime(process proc[], int n, int wt[]){
		int rt[] = new int[n];
		
		for (int i = 0; i < n; i++)
			rt[i] = proc[i].bt;
		
		int complete = 0, t = 0, minm = Integer.MAX_VALUE;
		int shortest = 0, finish_time;
		boolean check = false;
		while (complete != n) {
			
			for (int j = 0; j < n; j++){
				if ((proc[j].at <= t) && (rt[j] < minm) && rt[j] > 0) {
					minm = rt[j];
					shortest = j;
					check = true;
				}
			}
			if (check == false) {
				t++;
				continue;
			}
			rt[shortest]--;
			minm = rt[shortest];
			if (minm == 0)
				minm = Integer.MAX_VALUE;
			if (rt[shortest] == 0) {
				complete++;
				check = false;
				finish_time = t + 1;
				wt[shortest] = finish_time - proc[shortest].bt - proc[shortest].at;
				if (wt[shortest] < 0)
					wt[shortest] = 0;
			}
			t++;
		}
    }
    public void turnAroundTime(process proc[], int n, int wt[], int tat[]){
		for (int i = 0; i < n; i++)
			tat[i] = proc[i].bt + wt[i];
	}
    public void findavgTime(process proc[], int n){
        int waitingTime[] = new int[n];
        int turnAroundTime[] = new int[n];
        int  total_wt = 0, total_tat = 0;
        
        waitingTime(proc, n, waitingTime);
        turnAroundTime(proc, n, waitingTime, turnAroundTime);  

        for (int i = 0; i < n; i++) {
            total_wt = total_wt + waitingTime[i];
            total_tat = total_tat + turnAroundTime[i];
        }
        setGantt(proc, n);
//        boolean done = false;
//        while (!done) {
//        	for (int i = 0 ; i < )
//        }
//        
        avgWaiting = (double)total_wt / (double)n;
        avgComp = (double)total_tat / (double)n;
      }
	public void setGantt(process[] pr , int n) {
		int btSum = 0;
		String gantt = "";
		String top = "";
		String mid = "";
		String btm = "";

		
		for (int i = 0 ; i < counter ; i++) {
			btSum += p[i].bt;
		}
		gantt += "-";
		top += "-";
		
		for (int j = 0 ; j < btSum ; j++) {
			gantt += "---";
			top += "---";
		}
		gantt += "-\n|";
		top += "-";
		
		mid = "|";
		for (int i = 0; i < n; i++){
			for (int j = 0 ; j < pr[i].bt/2-1 ; j++) {
				gantt += "   ";
				mid += "   ";
			}
			gantt += "P" + pr[i].pid;
			mid += "P" + pr[i].pid;

			for (int j = 0 ; j < pr[i].bt/2-1 ; j++) {
				gantt += "   ";
				mid += "   ";
			}
			gantt += "|";
			mid += "|";
		}
		gantt += "|\n-";
		mid += "|";
		
		btm += "-";
		for (int j = 0 ; j < btSum ; j++) {
			gantt += "---";
			btm += "---";
		}
		gantt += "-";
		btm += "-";
		
		System.out.println(gantt);
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
    public String cpuUtalization() {
    	OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    	double use = (osBean.getProcessCpuLoad() / 1) * 100;
    	String usage = new DecimalFormat("##.##").format(use);
    	return usage;
    }
    public void runCalc() {
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        TwoWorker task = new TwoWorker();
        task.execute();
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
