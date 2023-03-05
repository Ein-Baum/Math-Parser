package parser;

import parser.functions.AbsExpression;
import parser.functions.ArcCosExpression;
import parser.functions.ArcSinExpression;
import parser.functions.ArcTanExpression;
import parser.functions.ClampExpression;
import parser.functions.CosExpression;
import parser.functions.DegreeExpression;
import parser.functions.FunctionArgumentExpression;
import parser.functions.ModExpression;
import parser.functions.PowExpression;
import parser.functions.RadianExpression;
import parser.functions.SinExpression;
import parser.functions.SqrtExpression;
import parser.functions.TanExpression;
import utils.TextUtils;

/**
 * A {@link ExpressionToken} is a number, a variable, a function, a operator or opening or closing brackets in a math expression.
 * These tokens are filtered using the {@link ExpressionIterator} and used for expression parsing in the {@link Expression#parse(String, boolean)} method.
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 * @see Expression
 * @see ExpressionIterator
 */

class ExpressionToken implements ExpressionElement{
	public static final int UNDEFINED = -1, NUMBER = 0, BRACKET_OPEN = 1, BRACKET_CLOSE = 2, FUNCTION = 3, OPERATOR = 4, VARIABLE = 5;
	
	private String value = "";
	private int type;
	
	public ExpressionToken() {
		this.type = UNDEFINED;
	}
	
	/**
	 * Will find out, if a the token has ended given the next char in the expression string.
	 * @param c The next char in the expression string.
	 * @return True if the token has ended. False if the char c should still be added to this token.
	 */
	public boolean hasEnded(char c) {
//		System.out.println("     type: "+type+"    -> "+value);
		return switch(type) {
		
			case VARIABLE -> {
				yield TextUtils.testfor(c, " (){}+-*/^%");
			}
		
			case NUMBER -> {
				
				if(value.contains(".")) {
					yield !TextUtils.testfor(c, "1234567890");
				}else {
					yield !TextUtils.testfor(c, "1234567890.");
				}
				
			}
			
			case BRACKET_OPEN -> {
				yield true;
			}
			
			case BRACKET_CLOSE -> {
				yield true;
			}
			
			case FUNCTION -> {
				yield c == '(';
			}
			
			case OPERATOR -> {
				yield true;
			}
			
			case UNDEFINED -> {
				yield false;
			}
			
			default -> {
				yield false;
			}
		};
	}
	
	/**
	 * Adds a character to this token. Before adding a char to a 
	 * token, the method {@link #hasEnded(char)} can find out, if
	 * the token wants to accept this new char or not.
	 * @param c The character that should be added to this token.
	 */
	public void addToValue(char c) {
//		System.out.println(c);
		value+=c;
		value.trim();
		
		redoType();
	}
	
	/**
	 * Recalculated the type of token, that this token is.
	 */
	private void redoType() {
		
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/") || value.equals("^") || value.equals("%") || value.equals(",")) {
			
			this.type = OPERATOR;
			
		}else if(value.startsWith("sin") || value.startsWith("cos") || value.startsWith("tan") || value.startsWith("root") || value.startsWith("mod") || value.startsWith("abs") || value.startsWith("asin") || value.startsWith("acos") || value.startsWith("atan") || value.startsWith("pow")
			  || value.startsWith("clamp") || value.startsWith("toRadians") || value.startsWith("toDegree")) {
			
			this.type = FUNCTION;
			
		}else if(value.equals("(")) {
			
			this.type = BRACKET_OPEN;
			
		}else if(value.equals(")")) {
			
			this.type = BRACKET_CLOSE;
			
		}else if(!TextUtils.containsElse(value, "1234567890.")) {
			
			this.type = NUMBER;
			
		}else {
			
			this.type = VARIABLE;
			
		}
		
	}
	
	/**
	 * Parses this token to a expression.
	 */
	@Override
	public Expression toExpression() {
		return toExpression(null, null);
	}
	
	/**
	 * Parses this token to a expression while maybe using the given expressions as arguments.
	 * @param args The arguments for functions, or the left and right side of an operator.
	 * @return The parsed expression.
	 */
	public Expression toExpression(Expression... args) {
		
		Expression out = null;
		
		if(this.type == OPERATOR) {
			
			if(this.value.equals("+")) {
				
				out = new AddExpression(args[0], args[1]);
				
			}else if(this.value.equals("-")) {
				
				if(args[1] == null) {
					out = new NegateExpression(args[0]);
				}else {
					out = new SubExpression(args[0], args[1]);
				}
			}else if(this.value.equals("*")) {
				
				out = new MulExpression(args[0], args[1]);
				
			}else if(this.value.equals("/")) {
				
				out = new DivExpression(args[0], args[1]);
				
			}else if(this.value.equals("^")) {
				
				out = new PowExpression(args[0], args[1]);
				
			}else if(this.value.equals("%")) {
				
				out = new ModExpression(args[0], args[1]);
				
			}else if(this.value.equals(",")) {
				
				System.err.println("Math parser: Something went wront with the operators! Operator with the type \",\" is not allowed in this context.");
				
			}
			
		}else if(this.type == VARIABLE) {
			
			out = new VariableExpression(value);
			
		}else if(this.type == NUMBER) {
			
			if(value.contains(".")) {
				out = new NumberExpression(Double.parseDouble(value));
			}else {
				out = new NumberExpression(Integer.parseInt(value));	
			}
			
		}else if(this.type == FUNCTION) {
			
			if(args[0] instanceof FunctionArgumentExpression) {
				args = ((FunctionArgumentExpression)args[0]).getExpressions();
			}
			
			if(value.startsWith("sin")) {
				
				out = new SinExpression(args[0]);
				
			}else if(value.startsWith("cos")) {
				
				out = new CosExpression(args[0]);
				
			}else if(value.startsWith("tan")) {
				
				out = new TanExpression(args[0]);
				
			}else if(value.startsWith("asin")) {
				
				out = new ArcSinExpression(args[0]);
				
			}else if(value.startsWith("acos")) {
				
				out = new ArcCosExpression(args[0]);
				
			}else if(value.startsWith("atan")) {
				
				out = new ArcTanExpression(args[0]);
				
			}else if(value.startsWith("abs")) {
				
				out = new AbsExpression(args[0]);
				
			}else if(value.startsWith("root")) {
				
				out = new SqrtExpression(args[0], args[1]);
				
			}else if(value.startsWith("mod")) {
				
				out = new ModExpression(args[0], args[1]);
				
			}else if(value.startsWith("pow")) {
				
				out = new PowExpression(args[0], args[1]);
				
			}else if(value.startsWith("clamp")) {
				
				out = new ClampExpression(args[0], args[1], args[2]);
				
			}else if(value.startsWith("toRadians")) {
				
				out = new RadianExpression(args[0]);
				
			}else if(value.startsWith("toDegree")) {
				
				out = new DegreeExpression(args[0]);
				
			}
			
		}
		
		return out;
	}
	
	/**
	 * @return The string value that is saved in this token.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @return The type of token that this token is. One of {@link #UNDEFINED}, {@link #NUMBER}, {@link #FUNCTION}, {@link #BRACKET_OPEN}, {@link #BRACKET_CLOSE}, {@link #OPERATOR}, {@link #VARIABLE} and {@link #RESOURCE}.
	 * The type of a token is based on the content defined in its {@link #getValue() value}.
	 */
	public int getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return value;
	}
	

}
