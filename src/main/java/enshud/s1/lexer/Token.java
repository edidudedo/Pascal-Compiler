package enshud.s1.lexer;

public class Token {
	private int id;
	private String word;
	private String tokenName;

	public Token(int ids, String words, String tokenNames) {
		this.id = ids;
		this.word = words;
		this.tokenName = tokenNames;
	}
	public int getID() {
		return this.id;
	}
	public String getWord() {
		return this.word;
	}
	public String getTokenName() {
		return this.tokenName;
	}
}