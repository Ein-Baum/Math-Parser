package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Computes the tangent of the given value.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class TanExpression implements Expression {

	private Expression expression;
	
	public TanExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.tan(expression.computeDouble());
	}

	@Override
	public int computeInt() {
		return (int) Math.tan(expression.computeDouble());
	}

	@Override
	public double computeDouble() {
		return Math.tan(expression.computeDouble());
	}

	@Override
	public Expression simplify() {
		expression = expression.simplify();
		if(expression instanceof NumberExpression) {
			return new NumberExpression(Math.tan(expression.computeDouble()));
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String a = expression.toString();

		if(expression instanceof FunctionalExpression) {
			a = "("+a+")";
		}
		
		return "tan("+a+")";
	}
	
	@Override
	public void setVariable(String name, Number number) {
		expression.setVariable(name, number);
	}

	@Override
	public String getVariables() {
		return expression.getVariables();
	}

}
