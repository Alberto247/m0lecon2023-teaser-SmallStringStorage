import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SmallStringStorage {

	private MemoryStorage memstore;
	public static volatile Long lockedPage=(long) -1;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	SmallStringStorage() {
		System.out.println(
				"We need to configure the base size for your disk. Please provide a value you think will be the size of your storage. Don't worry, it will automatically expand if needed!");
		int size = this.readInt();
		if (size < 0 || size > 50000) {
			System.out.println("Invalid value received.");
			System.exit(-1);
		}
		this.memstore = new MemoryStorage(size);

	}

	public int readInt() {
		String intStr = "0";
		System.out.print("> ");
		try {
			intStr = reader.readLine();
		} catch (IOException e) {
			System.exit(-1);
		}
		
		int val = -1;
		try {
			val=Integer.parseInt(intStr);
		} catch(NumberFormatException e) {
			
		}
		return val;
	}

	public Long readLong() {
		String intStr = "0";
		System.out.print("> ");
		try {
			intStr = reader.readLine();
		} catch (IOException e) {
			System.exit(-1);
		}
		Long val = (long) 0;
		try {
			val=Long.parseLong(intStr);
		} catch(NumberFormatException e) {
			
		}
		return val;
	}

	public char readChar() {
		String intStr = "0";
		System.out.print("> ");
		try {
			intStr = reader.readLine();
		} catch (IOException e) {
			System.exit(-1);
		}
		if(intStr.length()==0) {
			return (char)' ';
		}
		char val = intStr.charAt(0);
		return val;
	}

	public void mainMenu() {
		while (true) {
			System.out.println("Main menu:");
			System.out.println("1. Create new page");
			System.out.println("2. Edit page");
			System.out.println("3. Unload page from memory storage");
			System.out.println("4. Write all memory storage to backend");
			System.out.println("5. Check page for target");
			System.out.println("6. Exit");
			int choice = this.readInt();
			if (choice > 0 && choice <= 6) {
				if (choice == 1) {
					System.out.println("Give me a numerical identifier for the page:");
					Long id = this.readLong();
					if(id<0) {
						System.out.println("Please provide a positive ID");
					}else {
						Page p = new Page(id);
						this.memstore.storePage(p);
						System.out.println("Page succesfully created in memory, write to backend to store permanently.");
					}
				} else if (choice == 2) {
					System.out.println("Give me a numerical identifier for the page:");
					Long id = this.readLong();
					Page p = this.memstore.getPage(id);
					if (p == null) {
						System.out.println("Page not found.");
					} else {
						this.pageMenu(p);
					}
				} else if (choice == 3) {
					System.out.println("Give me the numerical identifier for the page:"); // TODO: avoid race
					Long id = this.readLong();
					if(id==this.lockedPage) {
						System.out.println("Sorry, cannot unload page while checker is running.");
					}else {
						this.memstore.unloadPage(id);
						System.out.println("Page unloaded");
					}
				} else if (choice == 4) {
					this.memstore.syncWithDisk();
				} else if (choice == 5) {
					if(this.lockedPage!=-1) {
						System.out.println("Sorry, another checker is currently running. Only one per time.");
					}else {
						System.out.println("Give me the numerical identifier for the page:");
						Long id = this.readLong();
						Page p = this.memstore.getPage(id);
						if(p==null) {
							System.out.println("Page not found");
						}else {
							this.lockedPage=id;
							Checker c = new Checker(this.memstore, id);
							c.start();
						}
					}
				} else if (choice == 6) {
					System.out.println("I hope you enjoyed our PoC!");
					System.exit(0);
				}
			} else {
				System.out.println("Invalid choice");
			}
		}
	}

	public void pageMenu(Page p) {
		while (true) {
			System.out.println("Page menu, currently editing page " + p.getId() + " :");
			System.out.println("1. Get number of elements in page");
			System.out.println("2. Add element to page");
			System.out.println("3. Read element in page");
			System.out.println("4. Edit element in page");
			System.out.println("5. Execute element in page");
			System.out.println("6. Go back to main menu");
			int choice = this.readInt();
			if (choice > 0 && choice <= 6) {
				if (choice == 1) {
					System.out.println("There are " + p.getPageSize() + " elements in this page");
				} else if (choice == 2) {
					Integer id = p.addSmallString(new SmallString());
					System.out.println("Added empty string at index " + id);
				} else if (choice == 3) {
					System.out.println("Which index do you want to read?");
					int index = this.readInt();
					SmallString s = p.getSmallString(index);
					if (s == null) {
						System.out.println("String not found");
					} else {
						String generator = s.getFormattedGenerator();
						String value = s.getString();
						System.out.println("Generator: " + generator);
						System.out.println("Value: " + value);
					}
				} else if (choice == 4) {
					System.out.println("Which index do you want to edit?");
					int index = this.readInt();
					SmallString s = p.getSmallString(index);
					if (s == null) {
						System.out.println("String not found");
					} else {
						stringMenu(s);
					}
				} else if (choice == 5) {
					System.out.println("Which index do you want to execute?");
					int index = this.readInt();
					SmallString s = p.getSmallString(index);
					if (s == null) {
						System.out.println("String not found");
					} else {
						Boolean res = s.calculateString();
						if(res) {
							System.out.println("Generator executed");
						}else {
							System.out.println("Generator failed to execute");
						}
					}
				} else if (choice == 6) {
					return;
				}
			} else {
				System.out.println("Invalid choice");
			}
		}
	}

	public void stringMenu(SmallString s) {
		while (true) {
			System.out.println("String edit menu:");
			System.out.println("Current generator is: " + s.getFormattedGenerator());
			System.out.println("1. Edit generator index");
			System.out.println("2. Return to page menu");
			int choice = this.readInt();
			if (choice > 0 && choice <= 2) {
				if (choice == 1) {
					System.out.println("Which index do you want to edit?");
					int index = this.readInt();
					if (index < 0 || index >= 10) {
						System.out.println("Index out of bound");
					} else {
						System.out.println("What type of instruction do you want to set?");
						System.out.println("1. CHARACTER");
						System.out.println("2. JUMP");
						System.out.println("3. END");
						int type = this.readInt();
						if (type > 0 && type <= 3) {
							if (type == 1) {
								System.out.println("What character do you want to set?");
								char c = this.readChar();
								s.setGeneratorCharacter(index, c);
							} else if (type == 2) {
								System.out.println("Where do you want to jump?");
								int destination = this.readInt();
								if (destination >= 0 && destination < 10) {
									s.setGeneratorJump(index, destination);
								} else {
									System.out.println("Invalid destination");
								}
							} else if (type == 3) {
								s.setGeneratorEnd(index);
							}
						} else {
							System.out.println("Invalid type");
						}
					}
				} else if (choice == 2) {
					return;
				}
			} else {
				System.out.println("Invalid choice");
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Welcome to SmallStringStorage!");
		System.out.println(
				"As this is a PoC, don't expect this to be a final product. Plase wait while we set up everything for you.");

		SmallStringStorage storage = new SmallStringStorage();
		storage.mainMenu();
	}

}
