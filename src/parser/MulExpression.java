package parser;

/**
 * Multiplies two {@link Expression expressions} together.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */
public class MulExpression implements FunctionalExpression {

	private Expression first, sec;
	
	public MulExpression(Expression first, Expression sec) {
		this.first = first;
		this.sec = sec;
	}
	
	@Override
	public float computeFloat() {
		return first.computeFloat() * sec.computeFloat();
	}

	@Override
	public int computeInt() {
		return first.computeInt() * sec.computeInt();
	}

	@Override
	public double computeDouble() {
		return first.computeDouble() * sec.computeDouble();
	}

	@Override
	public Expression simplify() {
		first = first.simplify();
		sec = sec.simplify();
		if(first instanceof NumberExpression && sec instanceof NumberExpression) {
			return new NumberExpression(first.computeDouble() * sec.computeDouble());
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

		return a + " * " + b;
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
