package enshud.s2.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



public class Parser{

	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	

	static List<Token> tokens = null;
	static int index = 0;
	
	public static void main(final String[] args) {
		// normalの確認

			new Parser().run("data/ts/synerr08.ts");
//			new Parser().run("data/ts/synerr07.ts");
//			new Parser().run("data/ts/synerr08.ts");
//			new Parser().run("data/ts/normal08.ts");
//			new Parser().run("data/ts/normal19.ts");
//			new Parser().run("data/ts/normal18.ts");
//		new Parser().run("data/ts/normal02.ts");
//
//		// synerrの確認
//		new Parser().run("data/ts/synerr01.ts");
//		new Parser().run("data/ts/synerr02.ts");
	}

	/**
	 * TODO
	 * 
	 * 開発対象となるParser実行メソッド．
	 * 以下の仕様を満たすこと．
	 * 
	 * 仕様:
	 * 第一引数で指定されたtsファイルを読み込み，構文解析を行う．
	 * 構文が正しい場合は標準出力に"OK"を，正しくない場合は"Syntax error: line"という文字列とともに，
	 * 最初のエラーを見つけた行の番号を標準エラーに出力すること （例: "Syntax error: line 1"）．
	 * 入力ファイル内に複数のエラーが含まれる場合は，最初に見つけたエラーのみを出力すること．
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 * 
	 * @param inputFileName 入力tsファイル名
	 * @throws IOException 
	 */
	
	
	public void run(final String inputFileName) {
	
		int counter, counter2;
		char c;
		String word = "";
		String pasText = null;
		String lexText = null;
		String ID = null;
		String lineNumber = null;;
		tokens = new ArrayList<Token>();
		index=-1;
		// TODO
		
		counter2 = 0;
		// analyze the file
		try {
			Path fileName = Path.of(inputFileName);
			String line = Files.readString(fileName);
			for (counter=0; counter < line.length(); counter++) {
				c = line.charAt(counter);
				if (!isWhiteSpace(c)) {
					word+=c;
					continue;
				}
				switch(counter2) {
				case 0:
					pasText = word;
					break;
				case 1:
					lexText = word;
					break;
				case 2:
					ID = word;
					break;
				case 3:
					lineNumber = word;
					lineNumber = lineNumber.replaceAll("\\r", "");
					tokens.add(new Token(pasText,lexText, ID, lineNumber));
					break;
				default :
					break;
				}
				word = "";
				counter2 = ++counter2%4;	
			}
			
			
			program();
			
			System.out.println("OK");
		}
		catch(ParserSyntaxError e)  
		{  
			e.printError();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("File not found");
		}  
	}
	private static boolean isWhiteSpace(char c) {
		return c == '\t' || c == '\n';
	}
	
	private static void program() throws ParserSyntaxError {
		Token token = getToken();
		if (!token.getPasText().equals("program")) throw new ParserSyntaxError(token.getLineNumber());
		
		programName();
		
		token = getToken();
		if (!token.getPasText().equals(";")) throw new ParserSyntaxError(token.getLineNumber());
		
		block();
		
		complexStatement();
		
		token = getToken();
		if (!token.getPasText().equals(".")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void programName() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(token.getLineNumber());
	}

	private static void block() throws ParserSyntaxError{
		varDec();
		
		subDecGroup();
	}
	
	private static void varDec() throws ParserSyntaxError{
		Token token = LL(1);
		if (!token.getPasText().equals("var")) return;
		
		getToken();
		varDecSeq();
	}
	
	private static void varDecSeq() throws ParserSyntaxError{
			varNameSeq();
			
			Token token = getToken();
			if (!token.getPasText().equals(":")) throw new ParserSyntaxError(token.getLineNumber());
			
			type();
			
			token = getToken();
			if (!token.getPasText().equals(";")) throw new ParserSyntaxError(token.getLineNumber());
			
			token = LL(1);
			if(!token.getLexText().equals("SIDENTIFIER")) return;
			varDecSeq();
	}
	
	private static void varNameSeq() throws ParserSyntaxError{
		varName();
		
		Token token = LL(1);
		if(!token.getPasText().equals(",")) return;
		getToken();
		varNameSeq();
	}
	
	private static void varName() throws ParserSyntaxError{	
		Token token = getToken();
		if (!token.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void type() throws ParserSyntaxError{
		Token token = LL(1);
		String nextWord = token.getPasText();
		
		if(isStandardType(nextWord)) getToken();
		else if(nextWord.equals("array")) arrayType();
		else throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static boolean isStandardType(String typeName) throws ParserSyntaxError{
		if(typeName.equals("integer") || typeName.equals("char") || typeName.equals("boolean")) return true;
		return false;
	}
	
	private static void standardType() throws ParserSyntaxError{
		Token token = getToken();
		if(!isStandardType(token.getPasText())) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void arrayType() throws ParserSyntaxError{
		Token token = getToken();
		
		if (!token.getPasText().equals("array")) throw new ParserSyntaxError(token.getLineNumber());
		
		token = getToken();
		if (!token.getPasText().equals("[")) throw new ParserSyntaxError(token.getLineNumber());
		
		minIndex();
		
		token = getToken();
		if (!token.getPasText().equals("..")) throw new ParserSyntaxError(token.getLineNumber());
		
		maxIndex();

		token = getToken();
		if (!token.getPasText().equals("]")) throw new ParserSyntaxError(token.getLineNumber());
		
		token = getToken();
		if (!token.getPasText().equals("of")) throw new ParserSyntaxError(token.getLineNumber());
		
		standardType();
	}
	
	private static void minIndex() throws ParserSyntaxError {
		integer();
	}
	
	private static void maxIndex() throws ParserSyntaxError {
		integer();
	}
	
	private static void integer() throws ParserSyntaxError{
		Token token = getToken();
		if(isSymbol(token.getPasText()))token = getToken();
		
		if (!token.getLexText().equals("SCONSTANT")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static boolean isSymbol(String symbolValue) throws ParserSyntaxError{
		if(symbolValue.equals("+") || symbolValue.equals("-")) return true;
		return false;
	}
	
	private static void subDecGroup() throws ParserSyntaxError{
		Token token = LL(1);
		if(token.getPasText().equals("procedure")) {
			subDec();
			
			token = getToken();
			if (!token.getPasText().equals(";")) throw new ParserSyntaxError(token.getLineNumber());
			
			subDecGroup();
		}
	}
	
	private static void subDec() throws ParserSyntaxError{
		subDecHead();
				
		varDec();

		complexStatement();	
	}
	
	
	private static void subDecHead() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getPasText().equals("procedure")) throw new ParserSyntaxError(token.getLineNumber());
		
		procedureName();
		
		tempParameter();
		
		token = getToken();
		if (!token.getPasText().equals(";")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void tempParameter() throws ParserSyntaxError{
		Token token = LL(1);
		if (!token.getPasText().equals("(")) return;
		getToken();
		
		tempParameterSeq();
		
		token = getToken();
		if (!token.getPasText().equals(")")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void procedureName() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void tempParameterSeq() throws ParserSyntaxError{
		tempParameterNameSeq();
		
		Token token = getToken();
		if (!token.getPasText().equals(":")) throw new ParserSyntaxError(token.getLineNumber());
		
		standardType();
		
		token = LL(1);
		if (!token.getPasText().equals(";")) return;
		
		getToken();
		tempParameterSeq();
	}
	
	private static void tempParameterNameSeq() throws ParserSyntaxError{
		tempParameterName();
		
		Token token = LL(1);
		if (!token.getPasText().equals(",")) return;
		
		getToken();
		tempParameterNameSeq();
	}
	
	private static void tempParameterName() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void complexStatement() throws ParserSyntaxError{	
		Token token = getToken();
		if (!token.getPasText().equals("begin")) throw new ParserSyntaxError(token.getLineNumber());
		
		sentenceSeq();
		
		token = getToken();
		if (!token.getPasText().equals("end")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void sentenceSeq() throws ParserSyntaxError{
		sentence();
		
		Token token = getToken();
		if (!token.getPasText().equals(";")) throw new ParserSyntaxError(token.getLineNumber());
		
		token = LL(1);
		String nextLexWord = token.getLexText();
		
		if(nextLexWord.equals("SIF") || nextLexWord.equals("SWHILE") || nextLexWord.equals("SIDENTIFIER") 
				||nextLexWord.equals("SBEGIN") || nextLexWord.equals("SWRITELN") || nextLexWord.equals("SREADLN") ) {	
			sentenceSeq();
		}
	}
	
	private static void sentence() throws ParserSyntaxError{
		Token token = LL(1);
		String nextLexWord =token.getLexText();
		
		if(nextLexWord.equals("SIF")) ifStatement();
		else if(nextLexWord.equals("SWHILE")) whileStatement();
		else if(nextLexWord.equals("SREADLN") || nextLexWord.equals("SWRITELN") || nextLexWord.equals("SIDENTIFIER") || nextLexWord.equals("SBEGIN")) basicStatement();
		else throw new ParserSyntaxError(token.getLineNumber());
	}
	
	
	private static void ifStatement() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getPasText().equals("if")) throw new ParserSyntaxError(token.getLineNumber());
		
		equation();

		token = getToken();
		if (!token.getPasText().equals("then")) throw new ParserSyntaxError(token.getLineNumber());
		
		complexStatement();
		
		token = LL(1);
		if (!token.getPasText().equals("else")) return;
		
		getToken();
		complexStatement();
	}
	
	private static void whileStatement() throws ParserSyntaxError{
		Token token = getToken();
		if (!token.getPasText().equals("while")) throw new ParserSyntaxError(token.getLineNumber());
		
		equation();
		
		token = getToken();
		if (!token.getPasText().equals("do")) throw new ParserSyntaxError(token.getLineNumber());
		
		complexStatement();
	}
	
	private static void basicStatement() throws ParserSyntaxError{	
		Token token = LL(1);
		String nextLexWord = token.getLexText();
		
		if(nextLexWord.equals("SIDENTIFIER")) {
			token= LL(2);
			if(token.getPasText().equals(":=") || token.getPasText().equals("[")) subStatement();
			else procedureCallStatement();			
		}
		else if(nextLexWord.equals("SREADLN") || nextLexWord.equals("SWRITELN")) inOutStatement();
		else if(nextLexWord.equals("SBEGIN"))complexStatement();
		else throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void subStatement() throws ParserSyntaxError{
		leftSide();
		
		Token token = getToken();
		if (!token.getPasText().equals(":=")) throw new ParserSyntaxError(token.getLineNumber());
		
		equation();
	}
	
	private static void leftSide() throws ParserSyntaxError{
		variable();
	}
	
	private static void variable() throws ParserSyntaxError{
		Token token = LL(1);
		if(!token.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(token.getLineNumber());
		
		variable2();
	}
	
	private static void variable2() throws ParserSyntaxError{
		varName();
		
		Token token = LL(1);
		if(!token.getPasText().equals("[")) return;
		
		getToken();
		index();
		
		token = getToken();
		if(!token.getPasText().equals("]")) throw new ParserSyntaxError(token.getLineNumber());
	}
	
	private static void index() throws ParserSyntaxError{
		equation();
	}
	
	private static void procedureCallStatement() throws ParserSyntaxError{
		procedureName();
		
		Token token = LL(1);
		
		if(token.getPasText().equals("(")) {
			getToken();
			equationSeq();
			
			token = getToken();
			if (!token.getPasText().equals(")")) throw new ParserSyntaxError(token.getLineNumber());
		}
	}
	
	private static void equationSeq() throws ParserSyntaxError{
		equation();
		
		Token token = LL(1);
		if (token.getPasText().equals(",")) {
			getToken();
			equationSeq();
		}
	}
	
	private static void equation() throws ParserSyntaxError{
		simpleEquation();
		
		Token token = LL(1);
		if(isRelationalOperator(token.getPasText())) {
			getToken();
			equation();
		}
	}
	
	private static void simpleEquation() throws ParserSyntaxError{
		Token token = LL(1);
		String nextWord = token.getPasText();
		
		if(isSymbol(nextWord)) getToken();
		
		clause();
		
		simpleEquation2();
	}
	
	private static void simpleEquation2() throws ParserSyntaxError{
		Token token = LL(1);
		
		if(isAdditionOperator(token.getPasText())) {
			getToken();
			clause();
			simpleEquation2();
		}
	}
	
	
	private static void clause() throws ParserSyntaxError{
		factor();
		
		String nextWord = LL(1).getPasText();
				
		if(isMultiplicationOperator(nextWord)) {
			getToken();
			clause();
		}
	}
	
	private static void factor() throws ParserSyntaxError{
		Token token = LL(1);
		String nextLexWord = token.getLexText();
		
		if(isConstant(nextLexWord)) {
			getToken();
			return;
		}
		else if(nextLexWord.equals("SLPAREN")) {
			getToken();
			equation();
			
			token = getToken();
			nextLexWord = token.getLexText();
			
			if(!nextLexWord.equals("SRPAREN")) throw new ParserSyntaxError(token.getLineNumber());
		}
		else if(nextLexWord.equals("SNOT")) {
			getToken();
			factor();
		}
		else variable();
	}
	
	private static boolean isRelationalOperator(String symbol) throws ParserSyntaxError{
		if(symbol.equals("=") || symbol.equals("<>") || symbol.equals("<") || symbol.equals("<=") || symbol.equals(">") || symbol.equals(">=")) return true;
		return false;
	}
	
	private static boolean isAdditionOperator(String symbol) throws ParserSyntaxError{
		if(symbol.equals("+") || symbol.equals("-") || symbol.equals("or")) return true;
		return false;
	}
	
	private static boolean isMultiplicationOperator(String symbol) throws ParserSyntaxError{
		if(symbol.equals("*") || symbol.equals("/") || symbol.equals("div") || symbol.equals("mod") || symbol.equals("and")) return true;
		return false;
	}
	
	private static void inOutStatement() throws ParserSyntaxError{
		Token token = LL(1);
		String nextWord = token.getPasText();
		
		if(nextWord.equals("readln")) {
			getToken();
			nextWord = LL(1).getPasText();
			
			if(!nextWord.equals("(")) return;
			
			getToken();
			varSeq();
			
			token = getToken();
			nextWord = token.getPasText();
			if(!nextWord.equals(")")) throw new ParserSyntaxError(token.getLineNumber());
		}
		else if(nextWord.equals("writeln")) {
			getToken();
			
			nextWord = LL(1).getPasText();
			
			if(!nextWord.equals("(")) return;
			
			getToken();
			equationSeq();
			
			token = getToken();
			nextWord = token.getPasText();
			if(!nextWord.equals(")")) throw new ParserSyntaxError(token.getLineNumber());
		}
		else throw new ParserSyntaxError(token.getLineNumber());
		
	}
	
	private static void varSeq() throws ParserSyntaxError{
		variable();
		
		Token token = LL(1);
		if (!token.getPasText().equals(",")) return;
		
		getToken();
		varSeq();
	}
	
	private static boolean isConstant(String nextLexWord) throws ParserSyntaxError{		
		if(nextLexWord.equals("SCONSTANT") || nextLexWord.equals("SSTRING") || nextLexWord.equals("SFALSE") || nextLexWord.equals("STRUE")) return true;
		return false;
	}
	
	private static Token getToken() throws ParserSyntaxError{
		if((index+1) < tokens.size()) return tokens.get(++index);
		throw new ParserSyntaxError(tokens.get(index).getLineNumber());
	}
	
	private static Token LL(int k) throws ParserSyntaxError{
		if((index+k) < tokens.size()) return tokens.get(index+k);
		throw new ParserSyntaxError(tokens.get(index).getLineNumber());
	}
}
