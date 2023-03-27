import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class MemoryStorage {
	private Long2ObjectOpenHashMap<Page> memstore;
	private Backend backend;

	public MemoryStorage(int size) {
		super();
		this.memstore  = new Long2ObjectOpenHashMap<Page>(size, (float) 0.9);
		this.backend = new Backend(size);
	}
	
	private Page getFromBackend(Long id) {
		return this.backend.retrievePage(id);
	}
	
	public Page getPage(Long id) {
		Page p = this.memstore.get(id);
		if(p==null) {
			System.out.println("Page not found in memory storage, loading from backend.");
			p = this.getFromBackend(id);
			if(p!=null) {
				p.calculatePage();
				this.memstore.put(id, p);
			}
		}
		return p;
	}
	
	public void storePage(Page p) {
		long id = p.getId();
		this.memstore.put(id, p);
	}
	
	public void unloadPage(Long id) {
		this.memstore.remove(id);
	}
	
	public void syncWithDisk() {
		this.memstore.forEach((Long id, Page p)->this.backend.storePage(p));
	}
}
