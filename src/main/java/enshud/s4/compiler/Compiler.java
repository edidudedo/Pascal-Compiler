package enshud.s4.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import enshud.casl.CaslSimulator;


public class Compiler {
	// kadai2
	static List<Token> tokens;
	static Token currentToken;
	static int index;

	// kadai3
	static LinkedList<LinkedList<Variable>> useableScopeVariables;
	static LinkedList<LinkedList<Variable>> allScopeVariables;
	static int scopeVariableCounter;
	static LinkedList<Variable> scopeVariables;
	static LinkedList<Function> functions;
	
	// kadai4
	static LinkedList<String> stringConstants;
	static Path fileName;
	static String wholeCASLBuffer;
	static int addressCounter;
	static int relationCounter;
	static int ifCounter;
	static int whileCounter;
	
	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
//		new Compiler().run("data/ts/normal20.ts", "tmp/out.cas");
//		CaslSimulator.run("tmp/out.cas", "tmp/out.ans");
		new Compiler().run("tmp/test.ts", "tmp/out.cas");
		CaslSimulator.run("tmp/out.cas", "tmp/out.ans");
	}

	/**
	 * TODO
	 * 
	 * 開発対象となるCompiler実行メソッド．
	 * 以下の仕様を満たすこと．
	 * 
	 * 仕様:
	 * 第一引数で指定されたtsファイルを読み込み，CASL IIプログラムにコンパイルする．
	 * コンパイル結果のCASL IIプログラムは第二引数で指定されたcasファイルに書き出すこと．
	 * 構文的もしくは意味的なエラーを発見した場合は標準エラーにエラーメッセージを出力すること．
	 * （エラーメッセージの内容はChecker.run()の出力に準じるものとする．）
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 * 
	 * @param inputFileName 入力tsファイル名
	 * @param outputFileName 出力casファイル名
	 */
	public void run(final String inputFileName, final String outputFileName) {
		tokens = new ArrayList<Token>();
		index=-1;
		useableScopeVariables = new LinkedList<LinkedList<Variable>>();
		allScopeVariables = new LinkedList<LinkedList<Variable>>();
		functions = new LinkedList<Function>();
		fileName = Path.of(outputFileName);
		stringConstants = new LinkedList<String>();
		relationCounter = 0;
		ifCounter = 0;
		whileCounter = 0;
		scopeVariableCounter = 0;
		addressCounter = 0;
		wholeCASLBuffer = "";
		
		try {
			tokenAnalysis(inputFileName);
			
			printCASLinitial();
			program();
			printCASLend();
			printCASLToFile(wholeCASLBuffer);
			
			System.out.println("OK");
		}
		catch(ParserSyntaxError e)  
		{  
			e.printError();
		} 
		catch (CheckerSemanticError e) {
			e.printError();
		}
		catch (IOException e) {
			System.err.println("File not found");
		}
		
	}
	
	private static void printCASLinitial() throws IOException{
		addToWholeCASLBuffer("CASL\tSTART\tBEGIN\t;\n");
		addToWholeCASLBuffer("BEGIN\tLAD GR6, 0\t;\n");
		addToWholeCASLBuffer("\tLAD GR7, LIBBUF\t;\n");
	}
	
	private static void printCASLend() throws IOException{
		int counter = 0;
		addToWholeCASLBuffer("VAR\tDS\t" + findTotalVariableNumber() + "\t;\n");
		for(String stringConstant : stringConstants) addToWholeCASLBuffer("CHAR" + counter++ + "\tDC\t" + stringConstant + "\t;\n");
		addToWholeCASLBuffer("LIBBUF\tDS\t256\t;\n");
		addToWholeCASLBuffer("\tEND\t;");
		
		addToWholeCASLBuffer("\n\n");
		Path fileName = Path.of("data/cas/lib.cas");
		String content = Files.readString(fileName);
		addToWholeCASLBuffer(content);
	}
	
	
	private static void tokenAnalysis(final String inputFileName) throws IOException {
		int counter, counter2;
		char c;
		String word = "";
		String pasText = null;
		String lexText = null;
		String ID = null;
		String lineNumber = null;
		
		counter2 = 0;
		// analyze the file
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
	}
	
	private static void program() throws ParserSyntaxError, CheckerSemanticError {
		String blockBuffer = "";
		String complexBuffer = "";
		getToken();
		if (!currentToken.getPasText().equals("program")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		programName();
		
		getToken();
		if (!currentToken.getPasText().equals(";")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		blockBuffer += block();
		
		scopeVariableCounter = 0;
		complexBuffer = complexStatement();
		addToWholeCASLBuffer(complexBuffer);
		
		getToken();
		if (!currentToken.getPasText().equals(".")) throw new ParserSyntaxError(currentToken.getLineNumber());
		addToWholeCASLBuffer("\tRET\t;\n");
		addToWholeCASLBuffer(blockBuffer);
	}
	
	private static void programName() throws ParserSyntaxError, CheckerSemanticError{
		getToken();
		if (!currentToken.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(currentToken.getLineNumber());
	}

	private static String block() throws ParserSyntaxError, CheckerSemanticError{
		String subDecGroupBuffer = "";
		// initialize scopeVariables
		scopeVariables = new LinkedList<Variable>();
		varDec();
		useableScopeVariables.addFirst(scopeVariables);
		allScopeVariables.addLast(scopeVariables);
		
		subDecGroupBuffer += subDecGroup();
		return subDecGroupBuffer;
	}
	
	private static void varDec() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		if (!tokenTMP.getPasText().equals("var")) return;
		getToken();
		
		varDecSeq();
	}
	
	private static void varDecSeq() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<String> varNames = new LinkedList<String>();
		Type varType;
		
		varNames = varNameSeq();
			
		getToken();
		if (!currentToken.getPasText().equals(":")) throw new ParserSyntaxError(currentToken.getLineNumber());
			
		varType = type();
			
		getToken();
		if (!currentToken.getPasText().equals(";")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		for(String varName : varNames) {
			scopeVariables.add(new Variable(varName, varType, addressCounter, (addressCounter = addressCounter + varType.endIndex - varType.startIndex)));
			addressCounter ++;
		}
		
		Token tokenTMP = LL(1);
		if(!tokenTMP.getLexText().equals("SIDENTIFIER")) return;
		varDecSeq();
	}
	
	private static LinkedList<String> varNameSeq() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<String> varNames = new LinkedList<String>();
		varNames.add(varName());
	
		Token tokenTMP = LL(1);
		if(tokenTMP.getPasText().equals(",")) {
			getToken();
			varNames.addAll(varNameSeq());
		}
		return varNames;
	}
	
	private static String varName() throws ParserSyntaxError, CheckerSemanticError{	
		getToken();
		if (!currentToken.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		checkCurrentVariableName(currentToken.getPasText());
		return currentToken.getPasText();
	}
	
	private static Type type() throws ParserSyntaxError, CheckerSemanticError{
		String typeName;
		Type type;
		Token tokenTMP = LL(1);
		String nextWord = tokenTMP.getPasText();
		
		if(isStandardType(nextWord)) {
			getToken();
			
			typeName = getCurrentVariableType();
			
			return new Type(typeName, 1,1);
		}
		else if(nextWord.equals("array")) {
			type = arrayType();
			return type;
		}
		else throw new ParserSyntaxError(tokenTMP.getLineNumber());
	}
	
	private static boolean isStandardType(String typeName) throws ParserSyntaxError{
		if(typeName.equals("integer") || typeName.equals("char") || typeName.equals("boolean")) return true;
		return false;
	}
	
	private static String standardType() throws ParserSyntaxError, CheckerSemanticError{
		String type;
		getToken();
		type = getCurrentVariableType();
		if(!isStandardType(currentToken.getPasText())) throw new ParserSyntaxError(currentToken.getLineNumber());
		return type;
	}
	
	private static Type arrayType() throws ParserSyntaxError, CheckerSemanticError{
		String typeName;
		int minIndex_, maxIndex_;
		getToken();
		
		if (!currentToken.getPasText().equals("array")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		getToken();
		if (!currentToken.getPasText().equals("[")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		minIndex_ = minIndex();
		
		getToken();
		if (!currentToken.getPasText().equals("..")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		maxIndex_ = maxIndex();

		getToken();
		if (!currentToken.getPasText().equals("]")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		getToken();
		if (!currentToken.getPasText().equals("of")) throw new ParserSyntaxError(currentToken.getLineNumber());
		typeName = "Array of ";
		typeName += standardType();
		
		return new Type(typeName, minIndex_, maxIndex_);
	}
	
	private static int minIndex() throws ParserSyntaxError {
		return integer();
	}
	
	private static int maxIndex() throws ParserSyntaxError {
		return integer();
	}
	
	private static String getCurrentVariableType() throws CheckerSemanticError{
		String currentTokenType = currentToken.getLexText();
		switch(currentTokenType) {
		case "SBOOLEAN" :
			return "boolean";
		case "SINTEGER" :
			return "integer";
		case "SCHAR" :
			return "char"; 
		default :
			throw new CheckerSemanticError(currentToken.getLineNumber());
		}
	}
	private static int integer() throws ParserSyntaxError{
		getToken();
		if(isSymbol(currentToken.getPasText())) getToken();
		
		if (!currentToken.getLexText().equals("SCONSTANT")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		return Integer.parseInt(currentToken.getPasText());
	}
	
	private static boolean isSymbol(String symbolValue) throws ParserSyntaxError{
		if(symbolValue.equals("+") || symbolValue.equals("-")) return true;
		return false;
	}
	
	private static String subDecGroup() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String subDecGroupBuffer = "";
		if(tokenTMP.getPasText().equals("procedure")) {		
			
			scopeVariableCounter++;
			subDecGroupBuffer += subDec();
			
			getToken();
			if (!currentToken.getPasText().equals(";")) throw new ParserSyntaxError(currentToken.getLineNumber());
			subDecGroupBuffer += "\tRET\t;\n";
			removeRecentScope();
			
			subDecGroupBuffer += subDecGroup();
		}
		return subDecGroupBuffer;
	}
	
	private static String subDec() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<Variable> parameters = new LinkedList<Variable>();
		int argCounter;
		String subDecBuffer = "";
		String complexBuffer = "";
		scopeVariables = new LinkedList<Variable>();
		parameters = subDecHead();
		
		subDecBuffer = addCASL(subDecBuffer, "PROC" + (scopeVariableCounter-1) + "\tNOP\t;\n");
		
		argCounter = parameters.size();
		
		varDec();
		
		useableScopeVariables.addFirst(scopeVariables);
		allScopeVariables.addLast(scopeVariables);
		
		subDecBuffer = addCASL(subDecBuffer, "\tLD\tGR1, GR8\t;\n");
		subDecBuffer = addCASL(subDecBuffer, "\tADDA\tGR1,=" + argCounter + "\t;\n");
		if (argCounter > 0) {
			for(int i = 0 ; i<argCounter; i++) {
				subDecBuffer = addCASL(subDecBuffer, "\tLD\tGR2, 0, GR1\t;\n");
				subDecBuffer = addCASL(subDecBuffer, "\tLD\tGR3, =" + findVariableNumber(parameters.get(i).name)+"\t;\n");
				subDecBuffer = addCASL(subDecBuffer, "\tST\tGR2, VAR, GR3\t;\n");
				subDecBuffer = addCASL(subDecBuffer, "\tSUBA\tGR1,=1\t;\n");
			}
			subDecBuffer = addCASL(subDecBuffer, "\tLD\tGR1, 0, GR8\t;\n");
			subDecBuffer = addCASL(subDecBuffer, "\tADDA\tGR8,=" + argCounter+ "\t;\n");
			subDecBuffer = addCASL(subDecBuffer, "\tST\tGR1, 0, GR8\t;\n");
		}
		complexBuffer = complexStatement();
		subDecBuffer = addCASL(subDecBuffer, complexBuffer);
		return subDecBuffer;
	}
	
	private static LinkedList<Variable> subDecHead() throws ParserSyntaxError, CheckerSemanticError{
		String procName;
		LinkedList<Variable> parameters = new LinkedList<Variable>();
		getToken();
		if (!currentToken.getPasText().equals("procedure")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		procName = procedureName();
		parameters = tempParameter();
		
		functions.add(new Function(procName, parameters, scopeVariableCounter-1));
		
		getToken();
		
		if (!currentToken.getPasText().equals(";")) throw new ParserSyntaxError(currentToken.getLineNumber());
		return parameters;
	}
	
	private static LinkedList<Variable> tempParameter() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<Variable> parameters = new LinkedList<Variable>();
		Token tokenTMP = LL(1);
		if (!tokenTMP.getPasText().equals("(")) return parameters;
		getToken();
		
		parameters = tempParameterSeq();
		
		getToken();
		if (!currentToken.getPasText().equals(")")) throw new ParserSyntaxError(currentToken.getLineNumber());
		return parameters;
	}
	
	private static String procedureName() throws ParserSyntaxError{
		getToken();
		if (!currentToken.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(currentToken.getLineNumber());
		return currentToken.getPasText();
	}
	
	private static LinkedList<Variable> tempParameterSeq() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<String> parNames = new LinkedList<String>();
		String typeName;
		LinkedList<Variable> parameters =  new LinkedList<Variable>();
		Token tokenTMP;
		
		parNames = tempParameterNameSeq();
		
		getToken();
		if (!currentToken.getPasText().equals(":")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		typeName = standardType();
		for(String parName : parNames) {
			Variable variable = new Variable(parName, new Type(typeName, 1,1), addressCounter, addressCounter++);
			variable.isReferenced = true;
			parameters.add(variable);
			scopeVariables.add(variable);
		}
		tokenTMP = LL(1);
		if (tokenTMP.getPasText().equals(";")) {
			getToken();
			parameters.addAll(tempParameterSeq());
		}
		return parameters;
	}
	
	private static LinkedList<String> tempParameterNameSeq() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<String> parNames = new LinkedList<String>();
		parNames.add(tempParameterName());
		
		Token tokenTMP = LL(1);
		if (tokenTMP.getPasText().equals(",")) {
			getToken();
			parNames.addAll(tempParameterNameSeq());
		}
		return parNames;
	}
	
	private static String tempParameterName() throws ParserSyntaxError, CheckerSemanticError{
		getToken();
		if (!currentToken.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(currentToken.getLineNumber());
		checkCurrentVariableName(currentToken.getPasText());
		return currentToken.getPasText();
	}
	
	private static String complexStatement() throws ParserSyntaxError, CheckerSemanticError{	
		String complexBuffer = "";
		getToken();
		if (!currentToken.getPasText().equals("begin")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		complexBuffer += sentenceSeq();
		
		getToken();
		if (!currentToken.getPasText().equals("end")) throw new ParserSyntaxError(currentToken.getLineNumber());
		return complexBuffer;
	}
	
	private static String sentenceSeq() throws ParserSyntaxError, CheckerSemanticError{
		String sentenceSeqBuffer = "";
		sentenceSeqBuffer += sentence();
		
		getToken();
		if (!currentToken.getPasText().equals(";")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		Token tokenTMP = LL(1);
		String nextLexWord = tokenTMP.getLexText();
		
		if(nextLexWord.equals("SIF") || nextLexWord.equals("SWHILE") || nextLexWord.equals("SIDENTIFIER") 
				||nextLexWord.equals("SBEGIN") || nextLexWord.equals("SWRITELN") || nextLexWord.equals("SREADLN") ) {	
			sentenceSeqBuffer += sentenceSeq();
		}
		return sentenceSeqBuffer;
	}
	
	
	private static String sentence() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String nextLexWord =tokenTMP.getLexText();
		
		if(nextLexWord.equals("SIF")) return ifStatement();
		else if(nextLexWord.equals("SWHILE")) return whileStatement();
		else if(nextLexWord.equals("SREADLN") || nextLexWord.equals("SWRITELN") || nextLexWord.equals("SIDENTIFIER") || nextLexWord.equals("SBEGIN")) return basicStatement();
		else throw new ParserSyntaxError(tokenTMP.getLineNumber());
	}
	
	private static String ifStatement() throws ParserSyntaxError, CheckerSemanticError{
		EquationReturnValue eqRet;
		String ifBuffer = "";
		String complexBuffer = "";
		int currentIfCounter = ifCounter++;
		
		getToken();
		
		if (!currentToken.getPasText().equals("if")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		eqRet = equation();
		
		if(!eqRet.type.equals("boolean")) throw new CheckerSemanticError(currentToken.getLineNumber());

		ifBuffer = addCASL(ifBuffer, eqRet.CASLBuffer);
		ifBuffer = addCASL(ifBuffer, "\tPOP GR1\t;\n");
		ifBuffer = addCASL(ifBuffer, "\tCPL\tGR1, =#FFFF\t;\n");
		ifBuffer = addCASL(ifBuffer, "\tJZE\tELSE" + currentIfCounter + "\t;\n");
		
		getToken();
		if (!currentToken.getPasText().equals("then")) throw new ParserSyntaxError(currentToken.getLineNumber());	
		
		complexBuffer = complexStatement();
		ifBuffer = addCASL(ifBuffer, complexBuffer);
		Token tokenTMP = LL(1);
		if (!tokenTMP.getPasText().equals("else")) {
			ifBuffer = addCASL(ifBuffer, "ELSE"+ currentIfCounter + "\tNOP\t;\n");
			return ifBuffer;
		}
		ifBuffer = addCASL(ifBuffer, "\tJUMP\tENDIF" + currentIfCounter + "\t;\n");
		ifBuffer = addCASL(ifBuffer, "ELSE"+ currentIfCounter + "\tNOP\t;\n");
		
		getToken();
		complexBuffer = complexStatement();
		
		ifBuffer = addCASL(ifBuffer, complexBuffer);
		ifBuffer = addCASL(ifBuffer, "ENDIF"+ currentIfCounter + "\tNOP\t;\n");
		return ifBuffer;
	}
	
	private static String whileStatement() throws ParserSyntaxError, CheckerSemanticError{
		int currentLoopCounter = whileCounter++;
		EquationReturnValue eqRet;
		String whileBuffer = "";
		String complexBuffer = "";
		
		getToken();
		
		if (!currentToken.getPasText().equals("while")) throw new ParserSyntaxError(currentToken.getLineNumber());
		whileBuffer = addCASL(whileBuffer, "LOOP" + currentLoopCounter + "\tNOP\t;\n");
		
		eqRet = equation();
		if(!eqRet.type.equals("boolean")) throw new CheckerSemanticError(currentToken.getLineNumber());
		
		whileBuffer = addCASL(whileBuffer, eqRet.CASLBuffer);
		whileBuffer = addCASL(whileBuffer, "\tPOP GR1\t;\n");
		whileBuffer = addCASL(whileBuffer, "\tCPL\tGR1, =#FFFF\t;\n");
		whileBuffer = addCASL(whileBuffer, "\tJZE\tENDLP" + currentLoopCounter + "\t;\n");
		
		getToken();
		
		if (!currentToken.getPasText().equals("do")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		complexBuffer = complexStatement();
		
		whileBuffer = addCASL(whileBuffer, complexBuffer);
		whileBuffer = addCASL(whileBuffer, "\tJUMP\tLOOP" + currentLoopCounter + "\t;\n");
		whileBuffer = addCASL(whileBuffer, "ENDLP" + currentLoopCounter + "\tNOP\t;\n");
		return whileBuffer;
	}
	
	private static String basicStatement() throws ParserSyntaxError, CheckerSemanticError{	
		Token tokenTMP = LL(1);
		String nextLexWord = tokenTMP.getLexText();
		
		if(nextLexWord.equals("SIDENTIFIER")) {
			tokenTMP= LL(2);
			if(tokenTMP.getPasText().equals(":=") || tokenTMP.getPasText().equals("[")) return subStatement();
			else if(tokenTMP.getPasText().equals(";") || tokenTMP.getPasText().equals("(")) return procedureCallStatement();
			else throw new ParserSyntaxError(tokenTMP.getLineNumber());
		}
		else if(nextLexWord.equals("SREADLN") || nextLexWord.equals("SWRITELN")) return inOutStatement();
		else if(nextLexWord.equals("SBEGIN")) return complexStatement();
		else throw new ParserSyntaxError(tokenTMP.getLineNumber());
	}
	
	private static String subStatement() throws ParserSyntaxError, CheckerSemanticError{
		EquationReturnValue leftValue, rightValue;
		String subBuffer = "";
		
		leftValue = leftSide();
		
		getToken();
		if (!currentToken.getPasText().equals(":=")) throw new ParserSyntaxError(currentToken.getLineNumber());
		
		rightValue = equation();
		if(!leftValue.type.equals(rightValue.type)) throw new CheckerSemanticError(currentToken.getLineNumber());
		
		if(!leftValue.value.equals(rightValue.value)) {
			subBuffer = addCASL(subBuffer, rightValue.CASLBuffer);
			subBuffer = addCASL(subBuffer, leftValue.CASLBuffer);
			subBuffer = addCASL(subBuffer, "\tPOP\tGR2\t;\n");
			subBuffer = addCASL(subBuffer, "\tPOP\tGR1\t;\n");
			subBuffer = addCASL(subBuffer, "\tST\tGR1, VAR, GR2\t;\n");
		}
		return subBuffer;
	}
	
	private static EquationReturnValue leftSide() throws ParserSyntaxError, CheckerSemanticError{
		return variable(true);
	}
	
	private static EquationReturnValue variable(boolean subFlag) throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		if(!tokenTMP.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(tokenTMP.getLineNumber());
		return variable2(subFlag);
	}
	
	private static EquationReturnValue variable2(boolean subFlag) throws ParserSyntaxError, CheckerSemanticError{
		EquationReturnValue varRet;
		String varName = LL(1).getPasText();
		varRet = varName_();
		EquationReturnValue indexRet;
		
		Token tokenTMP = LL(1);
		
		varRet.CASLBuffer = addCASL(varRet.CASLBuffer, "\tLD\tGR2, =" + findVariableNumber(varName) + "\t;\n");
		
		if(tokenTMP.getPasText().equals("[")) {
			getToken();
			indexRet = index();

			getToken();
			if(!currentToken.getPasText().equals("]")) throw new ParserSyntaxError(currentToken.getLineNumber());
			varRet.type = varRet.type.substring(9);
			varRet.value += "[" + indexRet.value + "]";
			varRet.CASLBuffer = indexRet.CASLBuffer;
			varRet.CASLBuffer  = addCASL(varRet.CASLBuffer, "\tPOP\tGR2\t;\n");
			varRet.CASLBuffer  = addCASL(varRet.CASLBuffer, "\tADDA\tGR2, ="+ findVariableNumber(varName) + "\t;\n");
		}
		if(!subFlag) {
			varRet.CASLBuffer  = addCASL(varRet.CASLBuffer,"\tLD\tGR1, VAR, GR2\t;\n");
			varRet.CASLBuffer = addCASL(varRet.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
		}
		else varRet.CASLBuffer = addCASL(varRet.CASLBuffer, "\tPUSH\t0, GR2\t;\n");
		return varRet;
	}
	
	private static EquationReturnValue varName_() throws ParserSyntaxError, CheckerSemanticError{	
		getToken();
		if (!currentToken.getLexText().equals("SIDENTIFIER")) throw new ParserSyntaxError(currentToken.getLineNumber());
		return new EquationReturnValue(checkVariableExistence(currentToken.getPasText()), currentToken.getPasText(), "");
	}
	
	private static EquationReturnValue index() throws ParserSyntaxError, CheckerSemanticError{
		EquationReturnValue eqRet;
		eqRet = equation();
		if(!eqRet.type.equals("integer")) throw new CheckerSemanticError(currentToken.getLineNumber());
		return eqRet;
	}
	
	private static String procedureCallStatement() throws ParserSyntaxError, CheckerSemanticError{
		String procName;
		String procBuffer = "";
		procName = procedureName();
		
		Function function = searchFunctions(procName);
		
		LinkedList<EquationReturnValue> eqRets = new LinkedList<EquationReturnValue>();
		int counter = 0;
		Token tokenTMP = LL(1);
		if(tokenTMP.getPasText().equals("(")) {
			getToken();
			
			eqRets = equationSeq();
			
			getToken();
			if (!currentToken.getPasText().equals(")")) throw new ParserSyntaxError(currentToken.getLineNumber());
		}
		if (eqRets.size() != function.parameters.size()) throw new CheckerSemanticError(currentToken.getLineNumber());
		for(Variable parameter : function.parameters) {
			String typeName = parameter.type.typeName;
			if(!typeName.equals(eqRets.get(counter++).type)) throw new CheckerSemanticError(currentToken.getLineNumber());
		}
		for (EquationReturnValue eqRet : eqRets) {
			procBuffer = addCASL(procBuffer, eqRet.CASLBuffer);
		}
		procBuffer = addCASL(procBuffer, "\tCALL\tPROC" + function.number + "\t;\n");
		return procBuffer;
	}
	
	private static LinkedList<EquationReturnValue> equationSeq() throws ParserSyntaxError, CheckerSemanticError{
		LinkedList<EquationReturnValue> eqRets = new LinkedList<EquationReturnValue>();
		LinkedList<EquationReturnValue> eqRets2 = new LinkedList<EquationReturnValue>();
		
		EquationReturnValue eqRet = equation();
		
		eqRets.add(eqRet);
		
		Token tokenTMP = LL(1);
		if (tokenTMP.getPasText().equals(",")) {
			getToken();
			eqRets2 = equationSeq();
			eqRets.addAll(eqRets2);
		}
		return eqRets;
	}
	
	private static EquationReturnValue equation() throws ParserSyntaxError, CheckerSemanticError{
		String operatorName;
		EquationReturnValue simpEqRet1, simpEqRet2;
		simpEqRet1 = simpleEquation();

		Token tokenTMP = LL(1);
		
		while(isRelationalOperator(tokenTMP.getPasText())) {
			operatorName = tokenTMP.getPasText();
			getToken();
			simpEqRet2 = simpleEquation();
			simpEqRet1.type = typeFinder(simpEqRet1.type, simpEqRet2.type, operatorName);
			
			if(simpEqRet1.value.equals("true") && operatorName.equals("=")) {
				simpEqRet1.value = simpEqRet2.value;
				simpEqRet1.CASLBuffer = simpEqRet2.CASLBuffer;
			}
			else if(simpEqRet2.value.equals("true") && operatorName.equals("=")) {
			}
			else if(!simpEqRet1.value.equals("") && simpEqRet1.value.equals(simpEqRet2.value) && operatorName.equals("=")) {
				simpEqRet1.value = "true";
				simpEqRet1.CASLBuffer = "\tPUSH\t=#0000\t;\n";
			}
			else if(simpEqRet1.value.equals("false") && operatorName.equals("<>")) {
				simpEqRet1.value = simpEqRet2.value;
				simpEqRet1.CASLBuffer = simpEqRet2.CASLBuffer;
			}
			else if(simpEqRet2.value.equals("false") && operatorName.equals("<>")) {
			}
			else if(!simpEqRet1.value.equals("") && simpEqRet1.value.equals(simpEqRet2.value) && operatorName.equals("<>")) {
				simpEqRet1.value = "false";
				simpEqRet1.CASLBuffer = "\tPUSH\t=#FFFF\t;\n";
			}
			else {
				simpEqRet1.value = simpEqRet1.value + operatorName + simpEqRet2.value;
				simpEqRet1.isPure = false;
				simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, simpEqRet2.CASLBuffer); 
				simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tPOP\tGR2\t;\n"); 
				simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tPOP\tGR1\t;\n");
				simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tCPA\tGR1,GR2\t;\n");
				switch(operatorName) {
				case "=" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJZE\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#FFFF\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#0000\t;\n");
					break;
				case "<>" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJNZ\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#FFFF\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#0000\t;\n");
					break;
				case "<" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJMI\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#FFFF\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#0000\t;\n");
					break;
				case "<=" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJPL\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#0000\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#FFFF\t;\n");
					break;
				case ">" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJPL\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#FFFF\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#0000\t;\n");
					break;
				case ">=" :
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJMI\tTRUE" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tLD GR1, =#0000\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "\tJUMP BOTH" + relationCounter + "\t;\n");
					simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "TRUE" + relationCounter +"\tLD GR1, =#FFFF\t;\n");
					break;
				}
				simpEqRet1.CASLBuffer = addCASL(simpEqRet1.CASLBuffer, "BOTH" + relationCounter + "\tPUSH\t0, GR1\t;\n");
			}
			relationCounter++;
			tokenTMP = LL(1);
		}
		return simpEqRet1;
	}
	
	private static EquationReturnValue simpleEquation() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String nextWord = tokenTMP.getPasText();
		EquationReturnValue clauseRet;
		String symbolName = "";
		
		if(isSymbol(nextWord)) {
			symbolName = nextWord;
			getToken();
		}
		clauseRet = clause();
		
		if(symbolName.equals("-")) {
			if(!clauseRet.value.equals(("0"))){
				clauseRet.isPure = false;
				clauseRet.value = "-" + clauseRet.value;
				clauseRet.CASLBuffer = addCASL(clauseRet.CASLBuffer, "\tPOP\tGR2\t;\n");
				clauseRet.CASLBuffer = addCASL(clauseRet.CASLBuffer, "\tLD\tGR1, =0\t;\n");
				clauseRet.CASLBuffer = addCASL(clauseRet.CASLBuffer, "\tSUBA\tGR1, GR2\t;\n");
				clauseRet.CASLBuffer = addCASL(clauseRet.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
			}
		}
		
		clauseRet = simpleEquation2(clauseRet);
		return clauseRet;
	}
	
	private static EquationReturnValue simpleEquation2(EquationReturnValue clauseRet) throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String operatorName;
		EquationReturnValue clauseRet1, clauseRet2;
		int value1 = -100000, value2 = -100000;
		boolean valueFlag1 = false, valueFlag2 = false;
		clauseRet1 = clauseRet;
		while(isAdditionOperator(tokenTMP.getPasText())) {
			operatorName = tokenTMP.getPasText();
			getToken();
			clauseRet2 = clause();
			clauseRet1.type = typeFinder(clauseRet1.type, clauseRet2.type, operatorName);
			try {
				value1 = Integer.parseInt(clauseRet1.value);
				valueFlag1 = true;
			}catch(NumberFormatException e) {
				valueFlag1 = false;
			}
			
			try {
				value2 = Integer.parseInt(clauseRet2.value);
				valueFlag2 = true;
			}catch(NumberFormatException e) {
				valueFlag2 = false;
			}
			
			if(valueFlag1 && valueFlag2) {
				int result = 0;
				switch(operatorName) {
				case "+" :
					result = value1 + value2;
					break;
				case "-" :
					result = value1 - value2;
					break;
				}
				clauseRet1.value = "" + result;
				clauseRet1.CASLBuffer = "\tPUSH\t" + result + "\t;\n";
			}
			else if(valueFlag1 && value1 == 0 && operatorName.equals("+")) {
				clauseRet1.value = clauseRet2.value;
				clauseRet1.CASLBuffer = clauseRet2.CASLBuffer;
			}
			else if (valueFlag2 && value2 == 0 && ((operatorName.equals("+") || operatorName.equals("-")))) {
				
			}
			else if(!clauseRet1.value.equals("") && !clauseRet2.value.equals("") && clauseRet1.value.equals(clauseRet2.value) && operatorName.equals("-")) {
				clauseRet1.value = "0";
				clauseRet1.CASLBuffer = "\tPUSH\t0\t;\n";
			}
			else if((clauseRet1.value.equals("true") || clauseRet2.value.equals("true")) && operatorName.equals("or")) {
				clauseRet1.value = "true";
				clauseRet1.CASLBuffer = "\tPUSH\t=#0000\t;\n";
			}
			else if(clauseRet1.value.equals("false") && operatorName.equals("or")) {
				clauseRet1.value = clauseRet2.value;
				clauseRet1.CASLBuffer = clauseRet2.CASLBuffer;
			}
			else if(clauseRet2.value.equals("false") && operatorName.equals("or")) {
			}
			else if(!clauseRet1.value.equals("") && clauseRet1.value.equals(clauseRet2.value) && operatorName.equals("or")) {
			}
			else {
				clauseRet1.value = clauseRet1.value + operatorName + clauseRet2.value;
				clauseRet1.isPure = false;
				clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, clauseRet2.CASLBuffer);
				clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tPOP\tGR2\t;\n"); 
				clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tPOP\tGR1\t;\n");
				switch(operatorName) {
				case "+" : 
					clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tADDA\tGR1, GR2\t;\n");
					break;
				case "-" :
					clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tSUBA\tGR1, GR2\t;\n");
					break;
				case "or" : 
					clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tOR\tGR1, GR2\t;\n"); 
					break;
				default :
					break;
	 			}
				clauseRet1.CASLBuffer = addCASL(clauseRet1.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
			}
			
			tokenTMP = LL(1);
		}
		return clauseRet1;
	}
	
	private static EquationReturnValue clause() throws ParserSyntaxError, CheckerSemanticError{
		EquationReturnValue factorRet1, factorRet2;
		boolean valueFlag1 = false, valueFlag2 = false;
		int value1 = -10000, value2 = -100000;
		String operatorName;
		
		factorRet1 = factor();
		
		while(isMultiplicationOperator(operatorName = LL(1).getPasText())) {
			getToken();
			factorRet2 = factor();
			factorRet1.type = typeFinder(factorRet1.type, factorRet2.type, operatorName);
			
			try {
				value1 = Integer.parseInt(factorRet1.value);
				valueFlag1 = true;
			}catch(NumberFormatException e) {
				valueFlag1 = false;
			}
			
			try {
				value2 = Integer.parseInt(factorRet2.value);
				valueFlag2 = true;
			}catch(NumberFormatException e) {
				valueFlag2 = false;
			}
			
			if(valueFlag1 && value1 == 0 && (operatorName.equals("*") || operatorName.equals("mod"))) {
				factorRet1.value = "0";
				factorRet1.CASLBuffer = "\tPUSH\t0\t;\n";
			}
			else if (valueFlag2 && value2 == 0 && operatorName.equals("*")) {
				factorRet1.value = "0";
				factorRet1.CASLBuffer = "\tPUSH\t0\t;\n";
			}
			else if(valueFlag1 && valueFlag2) {
				int result = 0;
				switch(operatorName) {
				case "*" :
					result = value1 * value2;
					break;
				case "/" :
				case "div" :
					result = value1 / value2;
					break;
				case "mod" :
					result = value1 % value2;
					break;
				}
				factorRet1.value = "" + result;
				factorRet1.CASLBuffer = "\tPUSH\t" + result + "\t;\n";
			}
			else if(valueFlag1 && value1 == 1 && operatorName.equals("*")) {
				factorRet1.value = factorRet2.value;
				factorRet1.CASLBuffer = factorRet2.CASLBuffer;
			}
			else if(valueFlag2 && value2 == 1 && (operatorName.equals("*")||operatorName.equals("div")||operatorName.equals("/"))) {
			}
			else if(valueFlag2 && value2 == 1 && operatorName.equals("mod")) {
				factorRet1.value = "0";
				factorRet1.CASLBuffer = "\tPUSH\t0\t;\n";
			}
			else if(!factorRet1.value.equals("") && factorRet1.value.equals(factorRet2.value) && operatorName.equals("mod")) {
				factorRet1.value = "0";
				factorRet1.CASLBuffer = "\tPUSH\t0\t;\n";
			}
			else if((factorRet1.value.equals("false") || factorRet2.value.equals("false")) && operatorName.equals("and")) {
				factorRet1.value = "false";
				factorRet1.CASLBuffer = "\tPUSH\t=#FFFF";
			}
			else if(factorRet1.value.equals("true") && operatorName.equals("and")){
				factorRet1.value = factorRet2.value;
				factorRet1.CASLBuffer = factorRet2.CASLBuffer;
			}
			else if(factorRet2.value.equals("true") && operatorName.equals("and")) {
			}
			else if(!factorRet1.value.equals("") && factorRet1.value.equals(factorRet2.value) && operatorName.equals("and")) {
			}
			else {
				factorRet1.isPure = false;
				factorRet1.value = factorRet1.value + operatorName + factorRet2.value;
				factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer,factorRet2.CASLBuffer);
				factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tPOP\tGR2\t;\n"); 
				factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tPOP\tGR1\t;\n");
				
				switch(operatorName) {
				case "*" : 
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tCALL\tMULT\t;\n"); 
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tPUSH\t0, GR2\t;\n");
					break;
				case "/" :
				case "div" : 
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tCALL\tDIV\t;\n"); 
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tPUSH\t0, GR2\t;\n");
					break;
				case "mod" : 
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer,"\tCALL\tDIV\t;\n"); 
					factorRet1.CASLBuffer  = addCASL(factorRet1.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
					break;
				case "and" :
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tAND\tGR1, GR2\t;\n");
					factorRet1.CASLBuffer = addCASL(factorRet1.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
					break;
				default :
					break;
	 			}
			}
		}
		return new EquationReturnValue(factorRet1.type, factorRet1.value, factorRet1.CASLBuffer);
	}
	
	private static EquationReturnValue factor() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String nextLexWord = tokenTMP.getLexText();
		EquationReturnValue facRet = new EquationReturnValue("", "" , "");
		if(isConstant(nextLexWord)) {
			getToken();
			facRet.value = currentToken.getPasText();
			facRet.type = findFactorTypeConstant(nextLexWord);
			switch(nextLexWord) {
			case "SCONSTANT" :
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t" + tokenTMP.getPasText() + "\t;\n");
				break;
			case "SSTRING" :
				if(tokenTMP.getPasText().length()!=3) {
					if(!stringConstants.contains(tokenTMP.getPasText())) stringConstants.add(tokenTMP.getPasText());
					
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tLD\tGR1, =" + (tokenTMP.getPasText().length()-2) + "\t;\n");
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tLAD\tGR2, CHAR" + searchStringConstants(tokenTMP.getPasText()) + "\t;\n");
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t0, GR2\t;\n");
				}
				else {
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tLD\tGR1, =" + tokenTMP.getPasText() + "\t;\n");
					facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
				}
				break;
			case "STRUE" :
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t#0000\t;\n");
				break;
			case "SFALSE" :
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t#FFFF\t;\n");
				break;
			}
			return facRet;
		}
		else if(nextLexWord.equals("SLPAREN")) {
			getToken();
			facRet = equation();
			
			getToken();
			nextLexWord = currentToken.getLexText();
			
			if(!nextLexWord.equals("SRPAREN")) throw new ParserSyntaxError(currentToken.getLineNumber());
			
			if(!facRet.isPure) facRet.value = "(" + facRet.value + ")";
			return facRet;
		}
		else if(nextLexWord.equals("SNOT")) {
			getToken();
			facRet = factor();
			if (!facRet.type.equals("boolean")) throw new CheckerSemanticError(currentToken.getLineNumber());
			
			if(facRet.value.equals("true")) facRet.CASLBuffer = "\tPUSH\t=#FFFF\t;\n";
			else if (facRet.value.equals("false"))facRet.CASLBuffer = "\tPUSH\t=#0000\t;\n";
			else {
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPOP\tGR1\t;\n");
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tXOR\tGR1, =#FFFF\t;\n");
				facRet.CASLBuffer = addCASL(facRet.CASLBuffer, "\tPUSH\t0, GR1\t;\n");
			}
			facRet.value = "not" + facRet.value;
			facRet.isPure = false;
			return facRet;
		}
		else {
			facRet = variable(false);
			return facRet;
		}
	}
	
	private static String inOutStatement() throws ParserSyntaxError, CheckerSemanticError{
		Token tokenTMP = LL(1);
		String nextWordTMP = tokenTMP.getPasText();
		String inOutBuffer = "";
		LinkedList<EquationReturnValue> eqRets = new LinkedList<EquationReturnValue>();
		
		if(nextWordTMP.equals("readln")) {
			getToken();
			nextWordTMP = LL(1).getPasText();
			
			if(!nextWordTMP.equals("(")) return inOutBuffer;
			
			getToken();
			varSeq();
			
			getToken();
			nextWordTMP = currentToken.getPasText();
			if(!nextWordTMP.equals(")")) throw new ParserSyntaxError(currentToken.getLineNumber());
		}
		else if(nextWordTMP.equals("writeln")) {
			getToken();
			
			nextWordTMP = LL(1).getPasText();
			
			if(!nextWordTMP.equals("(")) return inOutBuffer;
			
			getToken();
			
			eqRets = equationSeq();
			
			for(EquationReturnValue eqRet : eqRets) {
				String eqType = eqRet.type;
				
				inOutBuffer = addCASL(inOutBuffer, eqRet.CASLBuffer);
				inOutBuffer = addCASL(inOutBuffer, "\tPOP\t GR2\t;\n");
				if (eqRet.type.equals("Array of char")) inOutBuffer = addCASL(inOutBuffer, "\tPOP\t GR1\t;\n");
				inOutBuffer = addCASL(inOutBuffer, "\tCALL\tWRT" + changeEqType(eqType) + "\t;\n");
			}
			inOutBuffer = addCASL(inOutBuffer ,"\tCALL\tWRTLN\t;\n");
			
			getToken();
			nextWordTMP = currentToken.getPasText();
			if(!nextWordTMP.equals(")")) throw new ParserSyntaxError(currentToken.getLineNumber());
		}
		else throw new ParserSyntaxError(tokenTMP.getLineNumber());
		return inOutBuffer;
	}
	
	private static void varSeq() throws ParserSyntaxError, CheckerSemanticError{
		variable(false);
		
		Token token = LL(1);
		if (!token.getPasText().equals(",")) return;
		
		getToken();
		varSeq();
	}
	
/////// FROM HERE IS UTILITY
	
	private static String findFactorTypeConstant(String nextLexWord) throws CheckerSemanticError{
		switch(nextLexWord) {
		case "SFALSE" :
		case "STRUE"  :
			return "boolean";
		case "SCONSTANT" : 
			return "integer";
		case "SSTRING" :
			if(currentToken.getPasText().length() == 3) return "char";
			return "Array of char";
		default :
			throw new CheckerSemanticError(currentToken.getLineNumber());
		}			
	}
	
	private static boolean isRelationalOperator(String symbol){
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
	
	private static boolean isConstant(String nextLexWord) throws ParserSyntaxError{		
		if(nextLexWord.equals("SCONSTANT") || nextLexWord.equals("SSTRING") || nextLexWord.equals("SFALSE") || nextLexWord.equals("STRUE")) return true;
		return false;
	}
	
	private static boolean isIntegerOperator(String operatorName) {
		switch(operatorName) {
		case "+" :
		case "-" :
		case "*" :
		case "/" :
		case "mod" :
		case "div" :
			return true;
		default :
			return false;
		}
	}
	
	private static boolean isLogicalOperator(String operatorName) {
		switch(operatorName) {
		case "and":
		case "or" :
			return true;
		default :
			return false;
		}
	}
	
	private static boolean isWhiteSpace(char c) {
		return c == '\t' || c == '\n';
	}
	
	private static boolean isArray(String varName) {
		for(LinkedList<Variable> scopeVariables : useableScopeVariables) {
			for(Variable variable : scopeVariables) {
				if(varName.equals(variable.name)) {
					if(variable.endAddress == variable.startAddress) return false;
					else return true;
				}
			}
		}
		return false;
	}
	
	private static void getToken() throws ParserSyntaxError{
		if((index+1) >= tokens.size()) throw new ParserSyntaxError(tokens.get(index).getLineNumber());
		currentToken = tokens.get(++index);
	}
	
	private static Token LL(int k) throws ParserSyntaxError{
		if((index+k) < tokens.size()) return tokens.get(index+k);
		throw new ParserSyntaxError(tokens.get(index).getLineNumber());
	}
	
	private static void checkCurrentVariableName(String varName) throws CheckerSemanticError{
		for(Variable variable : scopeVariables) {
			if(varName.equals(variable.name)) throw new CheckerSemanticError(currentToken.getLineNumber());
		}
	}
	
	private static String checkVariableExistence(String varName) throws CheckerSemanticError{
		for(LinkedList<Variable> scopeVariables_ : useableScopeVariables) {
			for(Variable variable : scopeVariables_) {
				if(varName.equals(variable.name)) return variable.type.typeName;
			}
		}
		throw new CheckerSemanticError(currentToken.getLineNumber());
	}
	
	private static String typeFinder(String type1, String type2, String operator) throws CheckerSemanticError {
		if (isIntegerOperator(operator)) {
			if(type1.equals("integer") && type2.equals("integer")) return "integer";
		}
		else if (isLogicalOperator(operator)) {
			if(type1.equals("boolean") && type2.equals("boolean")) return "boolean";
		}
		else if (isRelationalOperator(operator)) return "boolean";
		throw new CheckerSemanticError(currentToken.getLineNumber());
	}
	private static void removeRecentScope() {
		addressCounter = useableScopeVariables.getFirst().getLast().startAddress;
		useableScopeVariables.remove(0);
	}
	
	private static String addCASL(String CASLBuffer, String words) {
		return CASLBuffer + words;
	}
	
	private static void addToWholeCASLBuffer(String words) {
		wholeCASLBuffer+=words;
	}
	private static void printCASLToFile(String words) {
		try {
			Files.writeString(fileName, words);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String changeEqType(String type) {
		switch(type) {
		case "integer" :
			return "INT";
		case "char" : 
			return "CH";
		case "Array of char" : 
			return "STR";
		default :
			return "";
 		}
	}

	private static int findVariableNumber(String varName) {
		int counter = 0;
		if(scopeVariableCounter!=0) {
			for (int i = 0; i<scopeVariableCounter; i++) {
				for(Variable variable : allScopeVariables.get(i)) {
					counter+=variable.endAddress-variable.startAddress+1;
				}
			}
			for(Variable variable : allScopeVariables.get(scopeVariableCounter)) {
				if(varName.equals(variable.name)) {
					if(!isArray(varName)) return counter;
					else return counter- variable.type.startIndex;
				}
				counter+=variable.endAddress-variable.startAddress+1;
			}
		}
		counter = 0;
		for(Variable variable : allScopeVariables.get(0)) {
			if(varName.equals(variable.name)) {
				if(!isArray(varName)) return counter;
				else return counter- variable.type.startIndex;
			}
			counter+=variable.endAddress-variable.startAddress+1;
		}
		return -1;
	}
	
	private static int findTotalVariableNumber() {
		int counter = 0;
		for(LinkedList<Variable>scopeVariables : allScopeVariables) {
			for (Variable variable : scopeVariables) {
				counter += variable.endAddress-variable.startAddress+1;
			}
		}
		return counter;
	}
	
	private static Function searchFunctions(String functionName) throws ParserSyntaxError, CheckerSemanticError{
		for(Function function : functions) {
			if(function.name.equals(functionName)) return function;
		}
		throw new CheckerSemanticError(currentToken.getLineNumber());
	}
	
	private static int searchStringConstants(String stringName) {
		int counter = 0;
		for(String stringName_ : stringConstants) {
			if(stringName_.equals(stringName)) return counter;
			counter++;
		}
		return -1;
	}
}