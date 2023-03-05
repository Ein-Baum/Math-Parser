package parser;

import java.util.ArrayList;
import java.util.Stack;

import parser.functions.FunctionArgumentExpression;
import utils.TextUtils;

/**
 * A expression is a math equation structured as a tree. The base {@link Expression} class also
 * implements a method to {@link #parse(String, boolean) parse strings} containing math expressions.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */

public interface Expression extends ExpressionElement{

	/**
	 * Will the value of a specific variable.
	 * @param name The name of the variable.
	 * @param number The value of the variable.
	 */
	public void setVariable(String name, Number number);
	
	/**
	 * @return A string that holds all variable names and values.
	 */
	public String getVariables();
	
	/**
	 * @return The computed value of this expression as a float. !Note: Every value used in the calculation is casted to an float!
	 * @see #computeInt()
	 * @see #computeDouble()
	 */
	public float computeFloat();
	
	/**
	 * @return The computed value of this expression as a float. !Note: Every value used in the calculation is casted to an float!
	 * @see #computeInt()
	 * @see #computeDouble()
	 */
	public int computeInt();
	
	/**
	 * @return The computed value of this expression as a float. !Note: Every value used in the calculation is casted to an float!
	 * @see #computeInt()
	 * @see #computeDouble()
	 */
	public double computeDouble();
	
	/**
	 * Will simplify this expression. A expression is simplified, if no variables or resources are used to calculate its value. 
	 * @return The simplified version of the expression.
	 */
	public Expression simplify();
	
	/**
	 * @return A string that is formated to suit the {@link Text} format syntax system.
	 */
	public default String toEngineString() {
		return toString();
	}
	
	/**
	 * Will parse a math expression. There are following operators and functions available:<br>
	 * <h4>Operators:</h4>
	 * <ul>
	 * 	<li>+ - Addition</li>
	 *  <li>- - Subtraction</li>
	 *  <li>* - Multiplication</li>
	 *  <li>/ - Division</li>
	 *  <li>^ - Raising to the power of ...</li>
	 *  <li>% - Modulo</li>
	 * </ul>
	 * <h4>Functions:</h4>
	 * <ul>
	 * 	<li>pow(a, b) - Raising a to the power of b</li>
	 *  <li>root(a, b) - Taking the b'th root of a</li>
	 *  <li>cos(a), sin(a), tan(a) - Trigonometric functions. a in radians</li>
	 *  <li>acos(a), asin(a), atan(a) - Inverse of the trigonometric functions. Result in radians</li>
	 *  <li>toRadians(a) - Convert a from degree to radians</li>
	 *  <li>toDegree(a) - Convert a from radians to degree</li>
	 *  <li>abs(a) - Returns the absolute value of a</li>
	 *  <li>clamp(a,b,c) - Clamps c between a and b</li>
	 * </ul>
	 * @param expression The expression that should be parsed.
	 * @param simplify True if the parser should simplify everything that is not dependent on variables or resources.
	 * @return The expression as a tree of math equations.
	 */
	public static Expression parse(String expression, boolean simplify) {
		Expression out = null;
		
		// Delete all spaces, tabs and line breaks in the expression string for simplification.
		expression = TextUtils.deleteTokens(expression, " ", System.lineSeparator(), "	");
		
		ExpressionIterator iterator = new ExpressionIterator(expression);
		
		out = parseE(iterator, simplify);
		
		if(out != null && simplify) {
			out.simplify();
		}
		
		return out;
	}
	
	/**
	 * Parses an expression using an {@link ExpressionIterator} to iterate over it. In a way, this method is not really parsing
	 * the expression, but rather sorting the tokens.
	 * @param iterator The iterator that holds the current expression tokens.
	 * @param simplify True if the parser should simplify everything that is not dependent on variables or resources.
	 * @return The expression as a tree of math equations.
	 */
	private static Expression parseE(ExpressionIterator iterator, boolean simplify) {
		Expression out = null;
		Stack<ExpressionElement> tokenStack = new Stack<>();
		boolean isFunctionExpression = false;
		
//		System.out.println("parseE call -------------------------------------------------");
		
		// Loop through all tokens (a token is an operator, a number, a function or a variable)
		while(iterator.hasNextToken() && iterator.nextToken().getType() != ExpressionToken.BRACKET_CLOSE) {
			ExpressionToken token = iterator.currentToken();
			
//			System.out.println("TOKEN: "+token.toString()+"   "+isFunctionExpression);
			
			// If the current expression was not just closed (a closing bracket should not be part of an equation), add the token to the stack
			if(token.getType() != ExpressionToken.BRACKET_CLOSE) {
				
				// If the current token suggests that a new sub part of this equation should be opened (it sees a opening bracket), open it
				if(token.getType() == ExpressionToken.BRACKET_OPEN) {
					tokenStack.add(parseE(iterator, simplify));
				}else {
					
					// If the current token is a ",", than the current equation is a list of parameters for a function.
					if(token.getType() == ExpressionToken.OPERATOR && token.getValue().contains(",")) {
						isFunctionExpression = true;
					}
					// Save the current token
					tokenStack.add(iterator.currentToken());
				}
			}
		}
		
		// Convert the token stack to an array 
		ExpressionElement[] tokens = tokenStack.toArray(new ExpressionElement[tokenStack.size()]);
		
		// If the current token array is a list of arguments for a function, the parser has to compute all parameters split from each other (as "," is not a real math operator)
		if(isFunctionExpression) {
			ArrayList<Expression> expressionList = new ArrayList<>();
			
			ArrayList<ExpressionElement> tokenSublist = new ArrayList<>();
			
			// Loop through all tokens and build separate equations separated by the "," operator.
			for(int i = 0; i < tokens.length; i++) {
				
				if(tokens[i] instanceof ExpressionToken && ((ExpressionToken)tokens[i]).getValue().contains(",")) {
					expressionList.add(convertTokensToExpression(tokenSublist.toArray(new ExpressionElement[tokenSublist.size()]), simplify));
					tokenSublist.clear();
				}else {
					tokenSublist.add(tokens[i]);
				}
				
			}
			expressionList.add(convertTokensToExpression(tokenSublist.toArray(new ExpressionElement[tokenSublist.size()]), simplify));
			
			out = new FunctionArgumentExpression(expressionList.toArray(new Expression[expressionList.size()]));
		
		}else {
			
			// If the current tokens are not just a list of arguments, it can be parsed all together.
			out = convertTokensToExpression(tokens, simplify);
			
		}
		
//		System.out.println("Exit parseE > result: "+out.toString()+"-----------------------------------------");
		return out;
	}
	
	/**
	 * Converts an array of tokens into a expression. This method is where the real parsing happens.
	 * @param tokens The tokens that should be parsed into a math expression.
	 * @param simplify True if the parser should simplify everything that is not dependent on variables or resources.
	 * @return The expression as a tree of math equations.
	 */
	private static Expression convertTokensToExpression(ExpressionElement[] tokens, boolean simplify) {
		Expression out = null;
//		System.out.println("Converter start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// If there is only one token, it can be directly parsed without other rules.
		if(tokens.length == 1) {
			
			if(tokens[0] instanceof ExpressionToken) {
				out = ((ExpressionToken)tokens[0]).toExpression();
			}else {
				out = (Expression)tokens[0];
			}
			
		// If there are only two tokens, there are two destinct cases that have to be handled seperatly
		}else if(tokens.length == 2){
			
			// First it should be tested, if the first token is a expression or not. If its not, something went wrong.
			if(tokens[0] instanceof ExpressionToken) {
				
				ExpressionToken token = (ExpressionToken) tokens[0];
				
				// Second, it is checked if the first token is a function. The second token must be the arguments then
				if(token.getType() == ExpressionToken.FUNCTION) {
					
					out = token.toExpression(tokens[1].toExpression()); 
					
				// If the first token is a negate symbol, the second token should be negated.
				}else if(token.getValue().contains("-")) {
					
					out = new NegateExpression(tokens[1].toExpression());
					
				}else {
					System.err.println("Math parser: Operand \"-\" expected, but got \""+token.getValue()+"\" instead!");
				}
				
			}
			
		// If there are more than 2 tokens, the complex task of parsing the tokens begins.
		}else {
			
			ArrayList<ExpressionElement> elements = new ArrayList<>();
			
			// First, the alogrithm sweeps over the tokens, resolving every function token it finds, so that no argument list token 
			//is left over and every function token can be treated as one expression.
			for(int i = 0; i < tokens.length; i++) {
				
				ExpressionElement current = tokens[i];
				
				if(current instanceof ExpressionToken) {
						
					ExpressionToken token = (ExpressionToken) current;
					if(token.getType() == ExpressionToken.FUNCTION){
						
						if(i < tokens.length-1) {
//							System.out.println("Function:"+token.toString()+"    "+token.getType()+"    "+tokens[i+1].toString());
							elements.add(token.toExpression(tokens[i+1].toExpression()));
							i++;
						
						}else {
							System.err.println("Math parser: Wrong operator in function argument list (after \""+token.toString()+"\")!");
						}
						
					}else {
						elements.add(token);
					}
					
				}else {
					elements.add(current);
				}
			}
			
			// As a second step, all power ("^") operators should be resolved. For that, loop over
			// all tokens and replace all value operator value triplet with the resulting expression
			// that summarizes the three tokens.
			int p = 0;
			while(p < elements.size()) {
				
				ExpressionElement current = elements.get(p);
				
				if(current instanceof ExpressionToken) {
					
					ExpressionToken token = (ExpressionToken) current;
					
					if(token.getType() == ExpressionToken.OPERATOR && token.getValue().contains("^")) {
						
						if(p != 0 && p != elements.size()-1) {
							
							ExpressionElement left = elements.get(p-1);
							ExpressionElement right = elements.get(p+1);
							
							elements.remove(p-1);
							elements.remove(p-1);
							elements.remove(p-1);
							
							elements.add(p-1, token.toExpression(left.toExpression(), right.toExpression()));
							
							p--;
						}else {
							System.err.println("Math parser: Operator \""+token.getValue()+"\" is misplaced!");
						}
						
					}
				}
				
				p++;
				
			}
			
			p = 0;
			// Next, the multiplications, divisions and mod operators should be resolved. The technique is the
			// same as above where the pow operators get resolved, but this time, the algorithm also looks for 
			// negate symbols before the values, as this is important for the result of some equations.
			while(p < elements.size()) {
				
				ExpressionElement current = elements.get(p);
				
				if(current instanceof ExpressionToken) {
					
					ExpressionToken token = (ExpressionToken) current;
					
					if(token.getType() == ExpressionToken.OPERATOR && !(token.getValue().contains("+") || token.getValue().contains("-"))) {
						if(p != 0 && p != elements.size()-1) {
							
							int replaceBegin = p-1, replaceLenght = 3;
							
							ExpressionElement left = elements.get(p-1);
							ExpressionElement right = elements.get(p+1);
							
							if(p > 1 && elements.get(p-2) instanceof ExpressionToken && ((ExpressionToken)elements.get(p-2)).getValue().equals("-") && (p == 2 && !(elements.get(p-3) instanceof ExpressionToken && ((ExpressionToken)elements.get(p-3)).getType() != ExpressionToken.OPERATOR))) {
								left = new NegateExpression(elements.get(p-1).toExpression());
								replaceBegin -= 1;
								replaceLenght += 1;
							}
							
							if(p < elements.size()-1) {
								if(elements.get(p+1) instanceof ExpressionToken && ((ExpressionToken)elements.get(p+1)).getValue().equals("-") && p < elements.size()-2) {
									right = new NegateExpression(elements.get(p+2).toExpression());
									replaceLenght += 1;
								}else {
									replaceLenght += 1;
								}
							}
							
							for(int i = replaceBegin; i < replaceBegin + replaceLenght - 1; i++) {
								elements.remove(replaceBegin);
							}
							
							elements.add(replaceBegin, token.toExpression(left.toExpression(), right.toExpression()));
							
							p = replaceBegin;
						}else {
							System.err.println("Math parser: Operator \""+token.getValue()+"\" is misplaced!");
						}
					}
				}
				
				p++;
			}
			
			//Convert the dynamic token array created by sweeping over it and clearing functions while resolving other operators.
			tokens = elements.toArray(new ExpressionElement[elements.size()]);
			
//			System.out.println("Token count: "+tokens.length);
//			for(int i = 0; i < tokens.length; i++) {
//				System.out.println(tokens[i].toString());
//			}
			
			// If there is now only one token left (for example, when everything could be summarized in one functional expression,
			// the output should be that token than.
			if(tokens.length == 1) {
				
				out = tokens[0].toExpression();
				
			// If not, another look is running over all tokens.
			}else {
			
				Expression leftExpression = null;
				ExpressionElement left = null, operator = null, additionalR = null, additionalL = null, right = null;
				
				// In this loop, the parser collects all tokens until it ran into a left token, an operator and a right token.
				for(int i = 0; i < tokens.length; i++) {
					
					ExpressionElement current = tokens[i];
					
					// If it finds a undefined token on the way, it will skip it. This could result in errors in the final expression.
					if(current instanceof ExpressionToken) {
						ExpressionToken token = (ExpressionToken) current;
						if(token.getType() == ExpressionToken.UNDEFINED) {
							
							System.err.println("Math parser: Token \""+token.toString()+"\" is undefined and cannot be parsed! It will be skipped.");
							continue;
							
						}
					}
					
					// The first token could be a negate symbol. If it is, save this information to negate the left side of the first expression.
					if(i == 0 && additionalL == null && current instanceof ExpressionToken && ((ExpressionToken)current).getType() == ExpressionToken.OPERATOR) {
	
						ExpressionToken token = (ExpressionToken) current;
	
						if(token.getValue().contains("-")) {
	
							additionalL = token;
	
						}else {
							System.err.println("Math parser: Operand \"-\" expected, but got \""+token.getValue()+"\" instead!");
						}
					
					// If there is no left side of the expression yet, it shall now be set.
					}else if(left == null) {
						
						if(additionalL != null) {
							left = new NegateExpression(current.toExpression());
							additionalL = null;
						}else {
							left = current;
						}
						
					// The operator should be directly after the left token and should therefore be set next.
					}else if(operator == null) {
						
						if(current instanceof ExpressionToken && ((ExpressionToken)current).getType() == ExpressionToken.OPERATOR) {
							
							operator = (ExpressionToken)current;
							
						}else {
							System.err.println("Math parser: Operand expected, but got \""+current.toString()+"\" instead!");
						}
						
					// After the operator, there could be a negate token before the right side token. If there is, save this information to negate the right token afterwards.
					}else if(additionalR == null && right == null && current instanceof ExpressionToken && ((ExpressionToken)current).getType() == ExpressionToken.OPERATOR) {
						
						ExpressionToken token = (ExpressionToken) current;
						
						if(token.getValue().contains("-")) {
							
							additionalR = token;
							
						}else {
							System.err.println("Math parser: Operand \"-\" expected, but got \""+token.getValue()+"\" instead!");
						}
						
					// If there is no right token yet, it should be set.
					}else if(right == null) {
						
						if(additionalR != null) {
							right = new NegateExpression(current.toExpression());
						}else {
							right = current;
						}
						
					}
	
					// If at least a left token, an operator, and a right token has been found, the operator can now be used to build an expression.
					if(left != null && right != null && operator != null){
						
						// This expression is then saved as the new left token, as you go from left to right to solve an equation.
						leftExpression = ((ExpressionToken)operator).toExpression(left.toExpression(), right.toExpression());
						
						left = leftExpression;

						// After the expression is built,the right side, and the operator (as well as the right negate info) is reset.
						additionalR = null;
						operator = null;
						right = null;
						
					}
					
				}
				
				// Finally the output should equal the final expression that was created by the loop.
				out = leftExpression;
				
			}
			
		}
		
		if(out != null && simplify) {
			out.simplify();
		}

//		System.out.println("Converter end >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return out;
	}
	
}
