package parser;

/**
 * A {@link NumberExpression} is a simple number that is plugged into an expression.
 * It cannot be simplified.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */

public class NumberExpression implements Expression {

	private Number number;
	
	public NumberExpression(Number number) {
		this.number = number;
	}
	
	@Override
	public float computeFloat() {
		return number.floatValue();
	}

	@Override
	public int computeInt() {
		return number.intValue();
	}

	@Override
	public double computeDouble() {
		return number.doubleValue();
	}

	@Override
	public Expression simplify() {
		return this;
	}
	
	@Override
	public String toString() {
		return number.toString();
	}

	@Override
	public void setVariable(String name, Number number) {
		
	}
	
	@Override
	public String getVariables() {
		return "";
	}

}
