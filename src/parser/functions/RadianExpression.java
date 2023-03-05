package parser.functions;

import parser.Expression;
import parser.NumberExpression;

/**
 * Converts the given value to radians.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class RadianExpression implements Expression {

	private Expression expression;
	
	public RadianExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.toRadians(expression.computeFloat());
	}

	@Override
	public int computeInt() {
		return (int) Math.toRadians(expression.computeInt());
	}

	@Override
	public double computeDouble() {
		return Math.toRadians(expression.computeDouble());
	}

	@Override
	public Expression simplify() {
		expression = expression.simplify();
		if(expression instanceof NumberExpression) {
			return new NumberExpression(Math.toRadians(expression.computeDouble()));
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String a = expression.toString();
		
		return "toRadians("+a+")";
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
