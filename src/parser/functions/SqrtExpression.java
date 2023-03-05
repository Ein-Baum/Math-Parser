package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Raises the first value to the power of the 1/second.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class SqrtExpression implements FunctionalExpression {

	private Expression first, sec;
	
	public SqrtExpression(Expression first, Expression sec) {
		this.first = first;
		this.sec = sec;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.pow(first.computeFloat(), 1.0/sec.computeFloat());
	}

	@Override
	public int computeInt() {
		return (int) Math.pow(first.computeInt(), 1.0/sec.computeInt());
	}

	@Override
	public double computeDouble() {
		return Math.pow(first.computeDouble(), 1.0/sec.computeDouble());
	}

	@Override
	public Expression simplify() {
		first = first.simplify();
		sec = sec.simplify();
		if(first instanceof NumberExpression && sec instanceof NumberExpression) {
			return new NumberExpression(Math.pow(first.computeDouble(), 1.0/sec.computeDouble()));
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String a = first.toString(), b = sec.toString();

		if(first instanceof FunctionalExpression) {
			a = "("+a+")";
		}

		if(sec instanceof FunctionalExpression) {
			b = "("+b+")";
		}

		return "sqrt("+ a + ", " + b +")";
	}
	
	@Override
	public void setVariable(String name, Number number) {
		first.setVariable(name, number);
		sec.setVariable(name, number);
	}

	@Override
	public String getVariables() {
		return first.getVariables()+sec.getVariables();
	}

}
