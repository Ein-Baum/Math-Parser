package example;

import parser.Expression;

/**
 * Contains an example of how to use the math parser.
 * @author MiKa
 */

public class Main {

	public static void main(String[] args) {
		
		String expressionString = "(pow(2,2)*cos(3.14*2)+3)*x";
		
		Expression expression = Expression.parse(expressionString, false);
		expression.setVariable("x", 10);
		
		System.out.println("");
		System.out.println("");
		System.out.println("                    "+expressionString);
		System.out.println("");
		System.out.println("                             \\/");
		System.out.println("");
		System.out.println("       "+expression.toString()+" = "+expression.computeFloat()+"   where "+expression.getVariables());
		
	}

}
