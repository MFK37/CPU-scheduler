import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import com.sun.management.OperatingSystemMXBean;
import javax.swing.JProgressBar;
import java.awt.Color;

public class priority extends mainPage{
	private JFrame frame;
	private JTable table;
	private DefaultTableModel tModel;
	private String row[] = {"PID", "BT", "Priority"}, col[][];
	private JPanel ganttPanel, cpuPanel, averagePanel, queuePanel_1;
	private JLabel average, cpu, ganttChart, ganttLabelmid, ganttLabelBtm, sliderTitle, avWaiting, avCompletion, avgWaitingLabel, avgCompLabel, utilzation, ganttLabelTop, tableTitle;
	private JButton start, back, exit;
	public int counter, qCounter, totalBurst;
	public double avgWaiting, avgComp;
	private process p[];
	private JProgressBar progressBar;
	
	public priority(String path) {
		initialize(path);
	}

	private void initialize(String path) {
		frame = new JFrame("Priority");
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
		
		ganttLabelTop = new JLabel("");
		ganttLabelTop.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelTop.setBounds(10, 11, 656, 25);
		ganttPanel.add(ganttLabelTop);
		
		ganttLabelmid = new JLabel("");
		ganttLabelmid.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelmid.setBounds(10, 38, 656, 25);
		ganttPanel.add(ganttLabelmid);
		
		ganttLabelBtm = new JLabel("");
		ganttLabelBtm.setHorizontalAlignment(SwingConstants.LEFT);
		ganttLabelBtm.setBounds(10, 63, 656, 25);
		ganttPanel.add(ganttLabelBtm);
		
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
		exit.addActionListener(this);
		queuePanel_1.add(exit);
		
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
		
		back = new JButton("Back");
		back.setBounds(129, 80, 89, 23);
		queuePanel_1.add(back);
		
		progressBar = new JProgressBar(0, 10000);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setBounds(10, 25, 314, 26);
		queuePanel_1.add(progressBar);
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
			String jobName = line.split("Job")[1];
			line = bReader.readLine();
			String btFile = line.split(",")[0];
			String priFile = line.split(",")[1].strip();
			
			int pid = Integer.parseInt(jobName);
			int bt = Integer.parseInt(btFile);
			int pri = Integer.parseInt(priFile);
						
			p[counter++] = new process(pid, 0, bt, pri);
		}
		for (int i = 0 ; i < counter ; i++) {			
				tModel.addRow(p[i].tableDataPriority());
		}
		reader.close();
		bReader.close();
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			avgWaitingLabel.setText("0");
			avgCompLabel.setText("0");
			progressBar.setValue(0);
			averageTime(sortedP(p), counter);
			runCalc();
		}
		else if (e.getSource() == exit) {
			frame.dispose();
		}
		else if (e.getSource() == back) {
			frame.dispose();
			mainPage p = new mainPage();
			p.visibleT();
		}
	}
    public void setLabel() {
		String Waiting = new DecimalFormat("##.##").format(avgWaiting);
		String Completing = new DecimalFormat("##.##").format(avgComp);

		avgWaitingLabel.setText(Waiting);
		avgCompLabel.setText(Completing);
    }
	public void waitingTime(process pr[], int n, int wt[]){
		
		wt[0] = 0;
		
		for (int i = 1; i < n ; i++ )
			wt[i] = pr[i - 1].bt + wt[i - 1] ;
	}
	public void turnAroundTime(process pr[], int n, int wt[], int tat[]){
		for (int i = 0; i < n ; i++)
			tat[i] = pr[i].bt + wt[i];
	}

	public void averageTime(process pr[], int n){
		int waitingTime[] = new int[n];
		int turnAroundTime[] = new int[n];
		
		int total_wt = 0, total_tat = 0;
		
		waitingTime(pr, n, waitingTime);
		
		turnAroundTime(pr, n, waitingTime, turnAroundTime);
		
		for (int i = 0; i < n; i++){
			total_wt = total_wt + waitingTime[i];
			total_tat = total_tat + turnAroundTime[i];
		}
		setGantt(pr, n);
		
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
    public String cpuUtalization() {
    	OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    	double use = (osBean.getProcessCpuLoad() / 1) * 100;
    	String usage = new DecimalFormat("##.##").format(use);
    	return usage;
    }
	public process[] sortedP(process[] p) {
		process[] temp = new process[30];
		process[] sorted = new process[30];
		int sortedCounter = 0;
		int tempCounter = 0;
		
		for (int  i = 0 ; i < counter ; i++) {
			temp[i] = p[i];
			tempCounter++;
		}
		
		process lowest = temp[0];
		int lowestIndex = -1;
		
		for (int j = 0 ; j < counter ; j++) {
			for (int i = 0 ; i < tempCounter ; i++) {
				if (temp[i].priority <= lowest.priority) {
					lowest = temp[i];
					lowestIndex = i;
				}
			}
			sorted[sortedCounter++] = lowest;
			
			temp[lowestIndex] = temp[tempCounter - 1];
			temp[tempCounter - 1] = null;
			tempCounter--;
			
			lowest = temp[0];
		}
		for (int i = 0 ; i < sortedCounter ; i++)
			sorted[i].printPriority();
		return sorted;
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
