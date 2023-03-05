package parser.functions;

import parser.Expression;
import parser.FunctionalExpression;
import parser.NumberExpression;

/**
 * Will clamp a value between a minimum bound and a maximum bound.
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 */

public class ClampExpression implements Expression {

	private Expression min, max, value;
	
	public ClampExpression(Expression min, Expression max, Expression value) {
		this.min = min;
		this.max = max;
		this.value = value;
	}

	@Override
	public void setVariable(String name, Number number) {
		min.setVariable(name, number);
		max.setVariable(name, number);
		value.setVariable(name, number);
	}

	@Override
	public String getVariables() {
		return min.getVariables()+max.getVariables()+value.getVariables();
	}

	@Override
	public float computeFloat() {
		return Math.max(min.computeFloat(), Math.min(max.computeFloat(), value.computeFloat()));
	}

	@Override
	public int computeInt() {
		return Math.max(min.computeInt(), Math.min(max.computeInt(), value.computeInt()));
	}

	@Override
	public double computeDouble() {
		return Math.max(min.computeDouble(), Math.min(max.computeDouble(), value.computeDouble()));
	}

	@Override
	public Expression simplify() {
		min = min.simplify();
		max = max.simplify();
		value = value.simplify();
		if(min instanceof NumberExpression && max instanceof NumberExpression && value instanceof NumberExpression) {
			return new NumberExpression(Math.max(min.computeDouble(), Math.min(max.computeDouble(), value.computeDouble())));
		}else {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String a = min.toString(), b = max.toString(), v = value.toString();
		
		if(min instanceof FunctionalExpression) {
			a = "("+a+")";
		}
		
		if(max instanceof FunctionalExpression) {
			b = "("+b+")";
		}
		
		if(value instanceof FunctionalExpression) {
			v = "("+v+")";
		}
		
		return "clamp("+a+", "+b+", "+v+")";
	}

}
