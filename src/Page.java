import java.util.ArrayList;

public class Page {
	
	private ArrayList<SmallString> stringList = new ArrayList<SmallString>();
	private Long id;
	
	Page(Long id){
		this.id=id;
	}

	public Integer addSmallString(SmallString string) {
		stringList.add(string);
		return stringList.size()-1;
	}
	
	public SmallString getSmallString(Integer index) {
		if(index<0 || index>=stringList.size()) {
			return null;
		}
		return stringList.get(index);
	}
	
	public Boolean updateSmallString(Integer index, SmallString string) {
		if(index<0 || index>=stringList.size()) {
			return false;
		}
		stringList.add(index, string);
		stringList.remove(index+1);
		return true;
	}
	
	public void calculatePage() {
		stringList.forEach((SmallString s)->s.calculateString());
	}
	
	public Long getId() {
		return this.id;
	}
	
	public Integer getPageSize() {
		return this.stringList.size();
	}
	
}
