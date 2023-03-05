package parser;

/**
 * An {@link Expression} to negate another {@link Expression} (*-1). Cannot be simplified.
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 */

public class NegateExpression implements Expression {

	private Expression toNegate;
	
	public NegateExpression(Expression toNegate) {
		this.toNegate = toNegate;
	}
	
	@Override
	public float computeFloat() {
		return -toNegate.computeFloat();
	}

	@Override
	public int computeInt() {
		return -toNegate.computeInt();
	}

	@Override
	public double computeDouble() {
		return -toNegate.computeDouble();
	}

	@Override
	public Expression simplify() {
		toNegate = toNegate.simplify();
		if(toNegate instanceof NumberExpression) {
			return new NumberExpression(-toNegate.computeDouble());
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		return "-"+toNegate.toString();
	}

	@Override
	public void setVariable(String name, Number number) {
		toNegate.setVariable(name, number);
	}
	
	@Override
	public String getVariables() {
		return toNegate.getVariables();
	}

}
