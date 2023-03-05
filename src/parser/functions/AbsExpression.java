package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Computes the absolute value of the given value.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class AbsExpression implements Expression {

	private Expression expression;
	
	public AbsExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.abs(expression.computeFloat());
	}

	@Override
	public int computeInt() {
		return (int) Math.abs(expression.computeInt());
	}

	@Override
	public double computeDouble() {
		return Math.abs(expression.computeDouble());
	}

	@Override
	public Expression simplify() {
		expression = expression.simplify();
		if(expression instanceof NumberExpression) {
			return new NumberExpression(Math.abs(expression.computeDouble()));
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
		
		return "abs("+a+")";
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
