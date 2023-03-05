package parser;

import java.util.function.Supplier;

/**
 * Is a flexible number, that can change over time.
 * It cannot be simplified.
 * @author MiKa
 * @version 1.0 (28.02.2023)
 * @since ALPHA
 */

public class VariableExpression implements Expression {

	private String name;
	private Supplier<Number> varSupplier;
	
	public VariableExpression(String name) {
		this.name = name;
		this.varSupplier = () -> 0;
	}
	
	public VariableExpression(String name, Supplier<Number> varSupplier) {
		this.name = name;
		this.varSupplier = varSupplier;
	}
	
	public String getName() {
		return name;
	}
	
	public void setValue(Supplier<Number> value) {
		this.varSupplier = value;
	}
	
	@Override
	public float computeFloat() {
		return varSupplier.get().floatValue();
	}

	@Override
	public int computeInt() {
		return varSupplier.get().intValue();
	}

	@Override
	public double computeDouble() {
		return varSupplier.get().doubleValue();
	}

	@Override
	public Expression simplify() {
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void setVariable(String name, Number number) {
		if(name.equals(this.name)) {
			varSupplier = () -> number;
		}
	}
	
	@Override
	public String getVariables() {
		return name+" = "+varSupplier.get()+", ";
	}

}
