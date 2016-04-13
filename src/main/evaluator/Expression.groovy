package evaluator

import groovy.transform.Canonical;
/**
 * TODO keep this simple. Make a seperate 'Operation' class for order and join operator. Expression should be an ExpressionEvaluator internal object
 * @author Aleicia Evans
 *
 */
@Canonical
class Expression {
    
    String operator
    String joinOperator
    Object value1
    Object value2
    Integer order
    
}
