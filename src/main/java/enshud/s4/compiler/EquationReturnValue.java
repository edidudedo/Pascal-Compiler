package enshud.s4.compiler;

public class EquationReturnValue {
	public String type;
	public String value;
	public String CASLBuffer;
	public boolean isPure = true;
	
	public EquationReturnValue(String type_, String value_, String CASLBuffer_) {
		this.type = type_;
		this.value = value_;
		this.CASLBuffer = CASLBuffer_;
	}
}
