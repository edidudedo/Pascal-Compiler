package enshud.s3.checker;

public class ParserSyntaxError extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lineNumber;
	
	public ParserSyntaxError(String lineNumber_){
		this.lineNumber = lineNumber_;
	}
	
	public void printError() {
		System.err.println("Syntax error: line " + this.lineNumber);
	}
}
