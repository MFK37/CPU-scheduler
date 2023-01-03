
public class process {
	public int pid;
	public int at;
	public int bt;
	public int priority;

	public process(int pid, int at, int bt) {
		this.pid = pid;
		this.at = at;
		this.bt= bt;
	}
	public process (int pid, int at, int bt, int pri) {
		this.pid = pid;
		this.bt= bt;
		this.priority = pri;
	}
	public void print() {
		System.out.println(pid + "--" + at + "--" + bt);
	}
	public void printPriority() {
		System.out.println(pid + "--" + priority + "--" + bt);
	}
	public String[] tableData() {
		String a = Integer.toString(pid);
		String b = Integer.toString(at);
		String c = Integer.toString(bt);
		
		String f[] = {a,b,c};
		return f;
	}
	public String[] tableDataR() {
		String a = Integer.toString(pid);
		String c = Integer.toString(bt);
		
		String f[] = {a,c};
		return f;
	}
	public String[] tableDataPriority() {
		String a = Integer.toString(pid);
		String c = Integer.toString(bt);
		String d = Integer.toString(priority);

		String f[] = {a,c,d};
		return f;
	}
	public void setBt(int bt) {
		this.bt = bt;
	}
}
