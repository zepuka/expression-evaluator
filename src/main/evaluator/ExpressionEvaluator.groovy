package evaluator

class ExpressionEvaluator {
    
    // Defines a closure for each operator key
    def static operatorMap = [
        "EQ":   { a,b -> a == b },
        "NE":   { a,b -> a != b },
        "AND":  { a,b -> a && b },
        "OR":   { a,b -> a || b }
    ]
    
    /**
     * Evaluate chains of expressions
     * @param rule
     * @return
     */
    boolean evaluateRule(Rule rule) {
        // Make sure we adhere to given order
        def sortedExpressions = rule?.expressions?.sort{it.order}
        
        // We need to store the last result from the prior expression for evaluating the join operations between the previous and current expression
        boolean previousResult;
        String previousOperator;
        
        // Evaluate each expression
        sortedExpressions?.eachWithIndex { exp, idx ->
            boolean result = evaluateOperation(createOperation(exp))
            
            boolean combinedResult = result
            if(idx > 0) { // Evaluate with previous expression
                combinedResult = evaluateOperation(createOperation(previousOperator, previousResult, result))
            }
            
            previousResult = combinedResult
            previousOperator = exp.joinOperator
        }
        
        if(previousResult == null) {
            return false
        } else {
            return previousResult
        }
    }
    
    /**
     * Run an operation
     * @param op
     * @return
     */
    boolean evaluateOperation(Operation op) {
        def evaluator = operatorMap.get(op.operator)
        evaluator.call(op.value1, op.value2)
    }
    
    /**
     * Create an operation object from individual expression data
     * @param operator
     * @param val1
     * @param val2
     * @return
     */
    Operation createOperation(String operator, Object val1, Object val2) {
        new Operation(value1: val1, value2: val2, operator: operator)
    }
    
    /**
     * Create an operation object from expression data
     * @param exp
     * @return
     */
    Operation createOperation(Expression exp) {
        createOperation(exp.operator, exp.value1, exp.value2)
    }
}
