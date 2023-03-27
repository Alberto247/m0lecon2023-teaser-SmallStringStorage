
public class StringGenerator {
	private Instruction type;
	private Integer jumpDest;
	private char character;

	public StringGenerator() {
		this.type = Instruction.END;
	}

	public Instruction getType() {
		return this.type;
	}

	public Integer getJumpDest() {
		return jumpDest;
	}

	public char getCharacter() {
		return character;
	}

	public void setJump(Integer jumpDest) {
		this.jumpDest = jumpDest;
		this.type = Instruction.JUMP;
	}

	public void setCharacter(char character) {
		this.character = character;
		this.type = Instruction.CHARACTER;
	}

	public void setEnd() {
		this.type = Instruction.END;
	}

}
