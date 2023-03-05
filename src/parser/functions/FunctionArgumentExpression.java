package parser.functions;

import parser.Expression;

/**
 * A dummy {@link Expression} that holds multiple {@link Expression expressions} that can then be used as arguments for functional expressions.
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 */

public class FunctionArgumentExpression implements Expression {

	private Expression[] expressions;
	
	public FunctionArgumentExpression(Expression...expressions) {
		this.expressions = expressions;
	}
	
	@Override
	public float computeFloat() {
		return 0;
	}

	@Override
	public int computeInt() {
		return 0;
	}

	@Override
	public double computeDouble() {
		return 0;
	}

	@Override
	public Expression simplify() {
		return null;
	}
	
	public Expression[] getExpressions() {
		return expressions;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < expressions.length; i++) {
			
			s = s + expressions[i].toString();
			if(i < expressions.length-1) {
				s = s + ", ";
			}
		}
		return s;
	}

	@Override
	public void setVariable(String name, Number number) {
		for(int i = 0; i < expressions.length; i++) {
			expressions[i].setVariable(name, number);
		}
	}
	
	@Override
	public String getVariables() {
		String var = "";
		for(int i = 0; i < expressions.length; i++) {
			var += expressions[i].getVariables();
		}
		return var;
	}

}
