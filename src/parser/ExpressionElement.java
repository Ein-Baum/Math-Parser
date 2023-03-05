package parser;

/**
 * A grouping interface for {@link Expression} and {@link ExpressionToken}
 * @author MiKa
 * @version 1.0 (04.03.2023)
 * @since ALPHA
 */

interface ExpressionElement {
	
	public default Expression toExpression() {
		if(this instanceof Expression) {
			return (Expression) this;
		}else if(this instanceof ExpressionToken){
			return this.toExpression();
		}
		return null;
	}

}
