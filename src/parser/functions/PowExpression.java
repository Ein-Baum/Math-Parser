package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Raises the first value to the power of the second.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class PowExpression implements FunctionalExpression {

	private Expression first, sec;
	
	public PowExpression(Expression first, Expression sec) {
		this.first = first;
		this.sec = sec;
	}
	
	@Override
	public float computeFloat() {
		return (float) Math.pow(first.computeFloat(), sec.computeFloat());
	}

	@Override
	public int computeInt() {
		return (int) Math.pow(first.computeInt(), sec.computeInt());
	}

	@Override
	public double computeDouble() {
		return Math.pow(first.computeDouble(), sec.computeDouble());
	}

	@Override
	public Expression simplify() {
		first = first.simplify();
		sec = sec.simplify();
		if(first instanceof NumberExpression && sec instanceof NumberExpression) {
			return new NumberExpression(Math.pow(first.computeDouble(), sec.computeDouble()));
		}else {
			return this;
		}
	}
	
	@Override
	public String toEngineString() {
		String a = first.toString(), b = sec.toString();

		if(first instanceof FunctionalExpression) {
			a = "("+a+")";
		}
		
		return a + "<sup>" + b+"</sup>";
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

		return a + " ^ " + b;
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
