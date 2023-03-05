package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Computes the arc cosine of the given value.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class ArcCosExpression implements Expression {

	private Expression expression;
	
	public ArcCosExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.acos(expression.computeDouble());
	}

	@Override
	public int computeInt() {
		return (int) Math.acos(expression.computeDouble());
	}

	@Override
	public double computeDouble() {
		return Math.acos(expression.computeDouble());
	}

	@Override
	public Expression simplify() {
		expression = expression.simplify();
		if(expression instanceof NumberExpression) {
			return new NumberExpression(Math.acos(expression.computeDouble()));
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
		
		return "acos("+a+")";
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
