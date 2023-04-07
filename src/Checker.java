
public class Checker extends Thread {
	
	private MemoryStorage memstore;
	private Long pageId;
	
	Checker(MemoryStorage memstore, Long pageId){
		this.memstore=memstore;
		this.pageId=pageId;
	}

	public void run() {
		Page p = memstore.getPage(pageId);
		Integer size = p.getPageSize();
		Boolean found=false;
		for (int i=0;i<size;i++) {
			SmallString s=memstore.getPage(pageId).getSmallString(i);
			if(s!=null) {
				String check = s.getString();
				System.out.println(check);
				if(check.equals("i swear it's possible!")) {
					System.out.println("Checker found string! Here is your flag! ptm{flagbellissima}");
					found=true;
				}
			}
		}
		if(!found) {
			System.out.println("Checker finished, string not found :(");
		}
		SmallStringStorage.lockedPage=(long) -1;
	}
	
	
}
