package parser;

/**
 * The {@link ExpressionIterator} is used to iterate over a raw expression. It is used
 * in the {@link Expression#parse(String, boolean)} method.
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 * @see ExpressionToken
 * @see Expression
 */

class ExpressionIterator {
	
	private ExpressionToken currentToken;
	private String expression;
	private char[] expressionChars;
	private int currentPointer;
	
	public ExpressionIterator(String expression) {
		this.expression = expression;
		this.expressionChars = expression.toCharArray();
		currentPointer = 0;
	}
	
	/**
	 * Will compute the next {@link ExpressionToken token} in the expression.
	 * @return The next token.
	 */
	public ExpressionToken nextToken() {
		
		boolean stop = false;
		ExpressionToken nextToken = new ExpressionToken();
		int pointer = currentPointer;
		nextToken.addToValue(expressionChars[pointer++]);
		
		while(!stop && pointer < expressionChars.length) {
			
			char currentChar = expressionChars[pointer];
			
			stop = nextToken.hasEnded(currentChar);
			
			if(!stop) {
				nextToken.addToValue(currentChar);
				pointer++;
			}
			
			
		}
		
		currentPointer = pointer;
		return currentToken = nextToken;
		
	}
	
	public boolean hasNextToken() {
		return currentPointer <= expressionChars.length-1;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public ExpressionToken currentToken() {
		return currentToken;
	}

}
