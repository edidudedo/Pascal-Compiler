package enshud.s4.compiler;

public class Type {
	public String typeName;
	public int startIndex;
	public int endIndex;
	public Type(String typeName_, int startIndex_, int endIndex_) {
		this.typeName = typeName_;
		this.startIndex = startIndex_;
		this.endIndex = endIndex_;
		
	}
}
