package utils;

import java.util.function.Function;

/**
 * A class, which provides some useful methods for text processing.
 * @author MiKa
 * @version 2.0 05.03.2023 (02.11.2022)
 * @since ALPHA
 */

public class TextUtils {

	/**
	 * Will delete all tokens specified from the string s.
	 * @param s The string to be modified.
	 * @param tokens The tokens that should be removed from s.
	 * @return The modified string.
	 */
	public static String deleteTokens(String s, String... tokens) {
		StringBuilder builder = new StringBuilder();
		char[] chars = s.toCharArray();
		
		StringBuilder toAdd = new StringBuilder();
		
		for(int i = 0; i < chars.length; i++) {
			
			toAdd.append(chars[i]);
			String tempAdd = toAdd.toString();
			
			for(int t = 0; t < tokens.length; t++) {
				
				if(tempAdd.endsWith(tokens[t])) {
					builder.append(toAdd.substring(0, tempAdd.length() - tokens[t].length()));
					toAdd = new StringBuilder();
					
					break;
				}
				
			}
			
		}
		
		if(toAdd.length() > 0) {
			builder.append(toAdd.toString());
		}
		
		return builder.toString();
	}
	
	/**
	 * Will delete all characters in the specified string contained in regex.
	 * @param s The string that should be modified.
	 * @param regex The {@link CharSequence} containing all characters that should be removed from s.
	 * @return The modified string.
	 */
	public static String deleteAll(String s, CharSequence regex) {
		StringBuilder builder = new StringBuilder();
		for(char c : s.toCharArray()) {
			if(!testfor(c, regex)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Tests if the string s only contains the characters in regex. If there is another character in s
	 * that is not present in regex, the method returns false.
	 * @param s The string to test
	 * @param regex The regex containing the allowed characters.
	 * @return True if s only contains characters specified in regex.
	 * @see #contains(String, String)
	 */
	public static boolean containsElse(String s, String regex) {
		char[] sA = regex.toCharArray();
		boolean somethingElse = false;
		for(char c : s.toCharArray()) {
			
			int matches = 0;
			
			for(char r : sA) {
				
				if(c == r) {
					matches++;
				}
				
			}
			
			if(matches == 0) {
				somethingElse = true;
				break;
			}
		}
		return somethingElse;
	}
	
	public static boolean contains(String s, String regex) {
		char[] sA = regex.toCharArray();
		boolean contains = false;
		for(char c : s.toCharArray()) {
			for(char r : sA) {
				if(c == r) {
					contains = true;
					break;
				}
			}
			if(contains)break;
		}
		return contains;
	}
	
	/**
	 * Will check if the given string is a number.
	 * @param s The string that should be tested
	 * @return True if s is a number.
	 */
	public static boolean isNumber(String s) {
		boolean number = !s.isBlank();
		boolean commaSet = false;
		s = s.trim();
		for(char c : s.toCharArray()) {
			if(!testfor(c, "123456789")) {
				if(c == '.') {
					if(commaSet) {
						number = false;
					}else {
						commaSet = true;
					}
				}else {
					number = false;
				}
			}
			if(!number)break;
		}
		return number;
	}
	
	/**
	 * Will test if s contains c.
	 * @param c
	 * @param s
	 * @return True if s contains c.
	 */
	public static boolean testfor(char c, CharSequence s) {
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == c)return true;
		}
		return false;
	}
	
	/**
	 * Will split a text into an array of lines, while also adding an array of line indices and comments generated with the onLineFunction.
	 * @param text The text that should be separated
	 * @param lineSeperator The String that separates lines in the text. Usually {@link System#lineSeparator()}
	 * @param numberOfWhitespaces The number of whitespaces to add for padding between a line index and the content of the line.
	 * @param onLineFunction A function to generate comments based on their line index.
	 * @return An array of the line indices (i = 0), the content (i = 1) and the comments (i = 2).
	 * @see #addLineNumbers(String, String, int)
	 * @see #addLineNumbers(String, String, int, Function)
	 */
	public static String[][] splitTextToLines(String text, String lineSeperator, int numberOfWhitespaces, Function<Integer, String> onLineFunction) {
		
		String[] textLines = text.split(lineSeperator);
		String[] numbers = new String[textLines.length];
		String[] comments = new String[textLines.length];
		
		for(int i = 0; i < textLines.length; i++) {
			String numberOut = i+"";
			for(int n = new String(i+"").length()-1; n < numberOfWhitespaces; n++) {
				numberOut += " ";
			}
			numbers[i] = numberOut;
			
			String online = onLineFunction.apply(i);
			
			if(online != null && !online.isBlank() && !online.isEmpty()) {
				comments[i] = online;
			}
		}
		
		return new String[][] {numbers, textLines, comments};
	}
	
	/**
	 * Adds line numbers in front of the lines of a text, while also adding comments generated with the onLineFunction.
	 * @param text The text that should be separated
	 * @param lineSeperator The String that separates lines in the text. Usually {@link System#lineSeparator()}
	 * @param numberOfWhitespaces The number of whitespaces to add for padding between a line index and the content of the line.
	 * @param onLineFunction A function to generate comments based on their line index.
	 * @return The text with comments and line indices.
	 * @see #addLineNumbers(String, String, int)
	 * @see #splitTextToLines(String, String, int, Function)
	 */
	public static String addLineNumbers(String text, String lineSeperator, int numberOfWhitespaces, Function<Integer, String> onLineFunction) {
		StringBuilder out = new StringBuilder();
		
		String[][] splitText = splitTextToLines(text, lineSeperator, numberOfWhitespaces, onLineFunction);
		
		for(int i = 0; i < splitText[0].length; i++) {
			out.append(splitText[0][i]);
			out.append(splitText[1][i]);
			if(splitText[2][i]!=null)out.append(splitText[2][i]);
			out.append(System.lineSeparator());
		}
		
		return out.toString();
	}
	
	/**
	 * Adds line numbers in front of the lines of a text.
	 * @param text The text that should be separated
	 * @param lineSeperator The String that separates lines in the text. Usually {@link System#lineSeparator()}
	 * @param numberOfWhitespaces The number of whitespaces to add for padding between a line index and the content of the line.
	 * @return The text with comments and line indices.
	 * @see #addLineNumbers(String, String, int, Function)
	 * @see #splitTextToLines(String, String, int, Function)
	 */
	public static String addLineNumbers(String text, String lineSeperator, int numberOfWhitespaces) {
		return addLineNumbers(text, lineSeperator, numberOfWhitespaces, (N) -> null);
	}
	
}
