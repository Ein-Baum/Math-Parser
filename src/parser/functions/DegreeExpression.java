package parser.functions;

import parser.Expression;
import parser.NumberExpression;

/**
 * Converts the given value to degree.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class DegreeExpression implements Expression {

	private Expression expression;
	
	public DegreeExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.toDegrees(expression.computeFloat());
	}

	@Override
	public int computeInt() {
		return (int) Math.toDegrees(expression.computeInt());
	}

	@Override
	public double computeDouble() {
		return Math.toDegrees(expression.computeDouble());
	}

	@Override
	public Expression simplify() {
		expression = expression.simplify();
		if(expression instanceof NumberExpression) {
			return new NumberExpression(Math.toDegrees(expression.computeDouble()));
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String a = expression.toString();
		
		return "toDegree("+a+")";
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
