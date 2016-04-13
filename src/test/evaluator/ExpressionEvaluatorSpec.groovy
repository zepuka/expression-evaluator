package evaluator

import spock.lang.*

class ExpressionEvaluatorSpec extends Specification {
    
    ExpressionEvaluator evaluator = new ExpressionEvaluator()
    
    @Unroll
    void "Test equals - #description"() {
        given:
        Expression myExpression = new Expression(value1: val1, value2: val2, operator: "EQ")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == expectedResult
        
        where:
        val1    | val2  | expectedResult    | description
        3       | 3     | true              | 'where we expect true because 3 == 3'
        3       | 2     | false             | 'where we expect false because 3 != 2'
        'a'     | 'a'   | true              | 'where we expect true because a == a'
        'a'     | 'b'   | false             | 'where we expect false because a != b'
        false   | false | true              | 'where we expect true because false == false'
        false   | true  | false             | 'where we expect false because false != true'
    }
    
    void "Test equals - where we expect identical objects to be equal"() {
        given:
        def obj1 = new Expression(value1: 3, value2: 2, operator: "EQ")
        def obj2 = new Expression(value1: 3, value2: 2, operator: "EQ")
        Expression myExpression = new Expression(value1: obj1, value2: obj2, operator: "EQ")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == true
    }
    
    void "Test equals - where we expect different objects to be unequal"() {
        given:
        def obj1 = new Expression(value1: 3, value2: 2, operator: "EQ")
        def obj2 = new Expression(value1: 4, value2: 2, operator: "EQ")
        Expression myExpression = new Expression(value1: obj1, value2: obj2, operator: "EQ")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == false
    }
    
    @Unroll
    void "Test not equals - #description"() {
        given:
        Expression myExpression = new Expression(value1: val1, value2: val2, operator: "NE")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == expectedResult
        
        where:
        val1    | val2  | expectedResult    | description
        3       | 3     | false             | 'where we expect false because 3 == 3'
        3       | 2     | true              | 'where we expect true because 3 != 2'
        'a'     | 'a'   | false             | 'where we expect false because a == a'
        'a'     | 'b'   | true              | 'where we expect true because a != b'
        false   | false | false             | 'where we expect false because false == false'
        false   | true  | true              | 'where we expect true because false != true'
    }
    
    void "Test not equals - where we expect identical objects to be equal"() {
        given:
        def obj1 = new Expression(value1: 3, value2: 2, operator: "EQ")
        def obj2 = new Expression(value1: 3, value2: 2, operator: "EQ")
        Expression myExpression = new Expression(value1: obj1, value2: obj2, operator: "NE")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == false
    }
    
    void "Test not equals - where we expect different objects to be unequal"() {
        given:
        def obj1 = new Expression(value1: 3, value2: 2, operator: "EQ")
        def obj2 = new Expression(value1: 4, value2: 2, operator: "EQ")
        Expression myExpression = new Expression(value1: obj1, value2: obj2, operator: "NE")
        Rule myRule = new Rule(expressions:[myExpression])
        
        when:
        def result = evaluator.evaluateRule(myRule)
        
        then:
        assert result == true
    }
    
    @Unroll
    void "Test Rule Evaluation for two expressions - #description"() {
        given:
        Expression exp1 = new Expression(value1: val1_1, value2: val2_1, operator: expOp_1, order: 1, joinOperator: joinOp_1)
        Expression exp2 = new Expression(value1: val1_2, value2: val2_2, operator: expOp_2, order: 2, joinOperator: joinOp_2)
        Rule rule = new Rule(expressions: [exp1,exp2])
        
        when:
        def result = evaluator.evaluateRule(rule)
        
        then:
        assert result == expectedResult
        
        where:
        val1_1  | val2_1    | expOp_1   | joinOp_1  | val1_2    | val2_2    | expOp_2   | joinOp_2  | expectedResult    | description
        3       | 3         | "EQ"      | "AND"     | 2         | 1         | "NE"      | ''        | true              | 'where we expect true because (3==3) && (2!=1)'
        1       | 3         | "EQ"      | "OR"      | 2         | 1         | "NE"      | ''        | true              | 'where we expect true because (1==3) || (2!=1)'
        3       | 3         | "EQ"      | "AND"     | 2         | 1         | "EQ"      | ''        | false             | 'where we expect false because (3==3) && (2==1)'
    }
}
