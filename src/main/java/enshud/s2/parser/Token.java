package enshud.s2.parser;

public class Token {
	private String pasText;
	private String lexText;
	private String ID;
	private String lineNumber;
	
	public Token(String pasText_, String lexText_, String ID_, String lineNumber_) {
		this.pasText = pasText_;
		this.lexText = lexText_;
		this.ID = ID_;
		this.lineNumber = lineNumber_;
	}
	
	public String getPasText() {
		return this.pasText;
	}
	public String getLexText() {
		return this.lexText;
	}
	public String getID() {
		return this.ID;
	}
	public String getLineNumber() {
		return this.lineNumber;
	}
}
