package enshud.s1.lexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer {
	
	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
//		new Lexer().run("data/pas/normal06.pas", "tmp/out1.ts");
//		new Lexer().run("data/pas/test1.pas", "tmp/out2.ts");
//		new Lexer().run("data/pas/normal03.pas", "tmp/out3.ts");
		new Lexer().run("tmp/test4.pas", "tmp/test.ts");
	}
	
	enum State{
		S0,
		S1,
		S2,
		S3,
		S4,
		S5,
		S6,
		S7
	}

	/**
	 * TODO
	 * 
	 * 開発対象となるLexer実行メソッド．
	 * 以下の仕様を満たすこと．
	 * 
	 * 仕様:
	 * 第一引数で指定されたpasファイルを読み込み，トークン列に分割する．
	 * トークン列は第二引数で指定されたtsファイルに書き出すこと．
	 * 正常に処理が終了した場合は標準出力に"OK"を，
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 * 
	 * @param inputFileName 入力pasファイル名
	 * @param outputFileName 出力tsファイル名
	 * @throws FileNotFoundException 
	 */
	public void run(final String inputFileName, final String outputFileName) {
		int counter;
		List<Token> oprDicts = new ArrayList<Token>();
		int lineNumber = 1;
		String line; 
		String word = "";
		State currentState = State.S0;
		char c;
		File file = new File(outputFileName);
		FileWriter fr = null;

		try {
			new FileWriter(file,false).close(); //remove all the inside of the file
			fr = new FileWriter(file, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.err.println("File not found");  
		}
		BufferedWriter br = new BufferedWriter(fr);
		
		initializeDictionary(oprDicts); // putting all the token to the dictionary
		
		try  
		{  
			Path fileName = Path.of(inputFileName);
			line = Files.readString(fileName);
			for (counter=0; counter < line.length(); counter++) {
				c = line.charAt(counter);
				
				switch(currentState) {
				case S0 :
					if (isAlphabet(c)) {
						currentState = State.S1;
						word+=c;
					}
					else if (isNumber(c)) {
						currentState = State.S2;
						word+=c;
					}
					else if (isSingleSymbol(c)) {
						word += c;
						printToken(oprDicts,lineNumber,word, br);
						word = "";
					}
					else if (c == '\'') {
						currentState = State.S3;
						word+=c;
					}
					else if (isCompositeSymbol1(c)) {
						currentState = State.S4;
						word+=c;
					}
					else if (isCompositeSymbol2(c)) {
						currentState = State.S5;
						word+=c;
					}
					else if (isCompositeSymbol3(c)) {
						currentState = State.S6;
						word+=c;
					}
					else if (c == '{') currentState = State.S7;
					
					break;
				case S1 :
					if(isAlphabet(c)||isNumber(c)) {
						word+=c;
					}
					else {
						printToken(oprDicts,lineNumber,word, br);
						word ="";
						currentState = State.S0;
						counter = reduceCounter(c,counter);
					}
					break;
				case S2 :
					if(isNumber(c)) {
						word+=c;
					}
					else {
						printToken(oprDicts,lineNumber,word, br);
						word = "";
						currentState = State.S0;
						counter = reduceCounter(c,counter);
					}
					break;
				case S3 :
					if(c != '\'' && c != '\n') word+=c;
					else {
						if(c=='\'') word+=c;
						word = word.replaceAll("\\r", "");
						printToken(oprDicts,lineNumber,word, br);
						word = "";
						currentState = State.S0;
					}
					break;
				case S4:
					if(c == '=') word+=c;
					else counter = reduceCounter(c,counter);
					printToken(oprDicts,lineNumber,word, br);
					
					word = "";
					currentState = State.S0;
					break;
				case S5:
					if(c == '>' || c == '=') word+=c;
					else counter = reduceCounter(c,counter);
					
					printToken(oprDicts,lineNumber,word, br);
					word = "";
					currentState = State.S0;
					break;
				case S6:
					if(c == '.') word+=c;
					else counter = reduceCounter(c,counter);
					
					printToken(oprDicts,lineNumber,word, br);
					word = "";
					currentState = State.S0;
					break;
				case S7:
					
					if(c == '}') currentState = State.S0;
					break;
				default :
					break;
				}
				if(c == '\n') lineNumber++;
			}   
			if (word!="") printToken(oprDicts,lineNumber,word, br);
			System.out.println("OK"); 
			br.close();
			fr.close();
		}  
		catch(IOException e)  
		{  
			System.err.println("File not found");  
		}  
	}
	
	private static void initializeDictionary(List<Token> oprDicts) {
		oprDicts.add(new Token(0,"and", "SAND"));
		oprDicts.add(new Token(1,"array", "SARRAY"));
		oprDicts.add(new Token(2,"begin", "SBEGIN"));
		oprDicts.add(new Token(3,"boolean", "SBOOLEAN"));
		oprDicts.add(new Token(4,"char", "SCHAR"));
		oprDicts.add(new Token(5,"div", "SDIVD"));
		oprDicts.add(new Token(5,"/", "SDIVD"));
		oprDicts.add(new Token(6,"do", "SDO"));
		oprDicts.add(new Token(7,"else", "SELSE"));
		oprDicts.add(new Token(8,"end", "SEND"));
		oprDicts.add(new Token(9,"false", "SFALSE"));
		oprDicts.add(new Token(10,"if", "SIF"));
		oprDicts.add(new Token(11,"integer", "SINTEGER"));
		oprDicts.add(new Token(12,"mod", "SMOD"));
		oprDicts.add(new Token(13,"not", "SNOT"));
		oprDicts.add(new Token(14,"of", "SOF"));
		oprDicts.add(new Token(15,"or", "SOR"));
		oprDicts.add(new Token(16,"procedure", "SPROCEDURE"));
		oprDicts.add(new Token(17,"program", "SPROGRAM"));
		oprDicts.add(new Token(18,"readln", "SREADLN"));
		oprDicts.add(new Token(19,"then", "STHEN"));
		oprDicts.add(new Token(20,"true", "STRUE"));
		oprDicts.add(new Token(21,"var", "SVAR"));
		oprDicts.add(new Token(22,"while", "SWHILE"));
		oprDicts.add(new Token(23,"writeln", "SWRITELN"));
		oprDicts.add(new Token(24,"=", "SEQUAL"));
		oprDicts.add(new Token(25, "<>", "SNOTEQUAL"));
		oprDicts.add(new Token(26,"<", "SLESS"));
		oprDicts.add(new Token(27,"<=", "SLESSEQUAL"));
		oprDicts.add(new Token(28,">=", "SGREATEQUAL"));
		oprDicts.add(new Token(29,">", "SGREAT"));
		oprDicts.add(new Token(30,"+", "SPLUS"));
		oprDicts.add(new Token(31,"-", "SMINUS"));
		oprDicts.add(new Token(32,"*", "SSTAR"));
		oprDicts.add(new Token(33,"(", "SLPAREN"));
		oprDicts.add(new Token(34,")", "SRPAREN"));
		oprDicts.add(new Token(35,"[", "SLBRACKET"));
		oprDicts.add(new Token(36,"]", "SRBRACKET"));
		oprDicts.add(new Token(37,";", "SSEMICOLON"));
		oprDicts.add(new Token(38,":", "SCOLON"));
		oprDicts.add(new Token(39,"..", "SRANGE"));
		oprDicts.add(new Token(40,":=", "SASSIGN"));
		oprDicts.add(new Token(41,",", "SCOMMA"));
		oprDicts.add(new Token(42,".", "SDOT"));
//		oprDicts.add(new Token(43,"identifier", "SIDENTIFIER"));
//		oprDicts.add(new Token(44,"constant", "SCONSTANT"));
//		oprDicts.add(new Token(45,"string", "SSTRING"));	
	}
	private static void printToken(List<Token> oprDicts, int lineNumber, String word, BufferedWriter bw) {
		Token token = null ;
		Iterator<Token> iterator = oprDicts.iterator();
		boolean flag = true;
		if(isNumberString(word)) {
			flag = false;
			token = new Token(44,word, "SCONSTANT");
		}
		else if(isString(word)) {
			flag = false;
			token = new Token(45,word, "SSTRING");
		}
		else {
			while (iterator.hasNext()) {
		    	token = iterator.next();
		        if (token.getWord().equals(word)) {
		        	flag = false;
		        	break;
		        }
		    }
		}
		if(flag) {
			token = new Token(43,word, "SIDENTIFIER");
		}
		
		try {
			bw.write(token.getWord() + "\t" + token.getTokenName() + "\t" + token.getID() + "\t" + lineNumber + '\n');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static boolean isAlphabet(char c) {
		return Character.isAlphabetic(c);
	}
	private static boolean isNumber (char c) {
		return Character.isDigit(c);
	}
	
	private static boolean isNumberString(String strNum) {
	    if (strNum == null) return false;
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	private static boolean isString(String word) {
	    if(word.charAt(0)=='\'') {
	    	return true;
	    }
	    return false;	
	    
	}
	private static boolean isCompositeSymbol1(char c) {
		if (c == '>' || c == ':') return true;
		return false;
	}
	private static boolean isCompositeSymbol2(char c) {
		return c=='<';
	}
	private static boolean isCompositeSymbol3(char c) {
		return c == '.';
	}
	private static boolean isSingleSymbol(char c) {
		if (c == '=' || c == '*' || c == '/' || c == '+' 
			|| c == '-' || c == ';' || c == '(' || c == ')' 
			|| c == '[' || c == ']' || c == ',') return true;
		return false;
	}
	private static int reduceCounter(char c, int counter) {
		if(c == '\n') return counter;
		return --counter;
		
	}

	
}


