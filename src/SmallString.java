
public class SmallString {

	private StringGenerator[] generator = {null,null,null,null,null,null,null,null,null,null};
	private String generated="";
	
	SmallString(){
		for(int i=0; i<10; i++) {
			generator[i] = new StringGenerator();
		}
	}
	
	public StringGenerator[] getGenerator() {
		return this.generator;
	}
	
	public String getString() {
		return this.generated;
	}
	
	public Boolean calculateString() {
		this.generated="";
		int i = 0;
		Instruction instruction = this.generator[i].getType();
		while(instruction!=Instruction.END) {
			if(instruction == Instruction.CHARACTER) {
				this.generated+=this.generator[i].getCharacter();
				i+=1;
				if(i>=10) {
					return false;
				}
			}
			else if(instruction==Instruction.JUMP) {
				Integer jump = this.generator[i].getJumpDest();
				if(!(jump>=0 && jump<10)) {
					return false;
				}
				i=jump;
			}
			instruction = this.generator[i].getType();
		}
		return true;
		
	}
	
	public Boolean setGeneratorCharacter(int index, char character) {
		if(index>=0 && index<10) {
			generator[index].setCharacter(character);
			return true;
		}
		return false;
	}
	
	public Boolean setGeneratorJump(int index, Integer destination) {
		if(index>=0 && index<10) {
			if(destination>=0 && destination<10) {
				this.generator[index].setJump(destination);
				return true;
			}
		}
		return false;
	}
	
	public Boolean setGeneratorEnd(int index) {
		if(index>=0 && index<10) {
			generator[index].setEnd();
			return true;
		}
		return false;
	}
	
	public String getFormattedGenerator() {
		String formatted="";
		for(int i=0; i<10; i++) {
			StringGenerator g = generator[i];
			formatted+="[ id: "+i+", type: ";
			if(g.getType()==Instruction.CHARACTER) {
				formatted+="CHARACTER, char: "+g.getCharacter()+" ";
			}else if(g.getType()==Instruction.JUMP) {
				formatted+="JUMP, destination: "+g.getJumpDest()+" ";
			}else if(g.getType()==Instruction.END) {
				formatted+="END ";
			}
			formatted+="], ";
		}
		return formatted;
	}
	
}
