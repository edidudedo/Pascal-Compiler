package enshud.s4.compiler;

public class CheckerSemanticError extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lineNumber;
	
	public CheckerSemanticError(String lineNumber_){
		this.lineNumber = lineNumber_;
	}
	
	public void printError() {
		System.err.println("Semantic error: line " + this.lineNumber);
	}
}
