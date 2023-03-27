import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class Backend {
	/*With a bit of imagination, you can think of this class as emulating any disk storage.
	 * Imagine the storePage function saving only the instructions to generate the string, and the 
	 * retrievePage re-generating all strings!
	 * Also, imagine this being very slow, a good reason to have a memory storage in the middle
	*/
	
	private Long2ObjectOpenHashMap<Page> filesystem;

	public Backend(int size) {
		super();
		this.filesystem  = new Long2ObjectOpenHashMap<Page>(size, (float) 0.9);
	}
	
	public void storePage(Page page) {
		Long id = page.getId();
		filesystem.put(id, page);
	}
	
	public Page retrievePage(Long id) {
		return filesystem.get(id);
	}
	

}
