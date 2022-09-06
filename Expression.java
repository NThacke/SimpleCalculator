public class Expression {

    private String input;
    private String inFix;
    private String postFix;

    public Expression(String expr) {
        setInput(expr);
    }
    public Expression() {
        this.input = null;
        this.inFix = null;
        this.postFix = null;
    }

    public void setInput(String expr) {
        expr = removeSpaces(expr);
        this.input = expr;

        if(expr.contains("(")) {
            //expr must be inFix if it contains a paranthesis
            if(isParanthesis(expr)) {
            this.inFix = expr;
            this.postFix = inFix2PostFix(this.inFix);
            }
            else {
                //input is not valid
                throw new IllegalArgumentException();
            }
        }
        else {
            //expr must be postFix
            this.postFix = expr;
            //make inFix from postFix
        }


    }

    public String getInput() {
        return this.input;
    }

    public String getInFix() {
        return this.inFix;
    }
    public String getPostFix() {
        return this.postFix;
    }

    public Double evaluate() {
        return postFixEvaluate(this.postFix);
    }
    private static void stackInsert(Stack<String> myStack, String s) {
        /*Inputs the String into myStack character by character.*/
        for(int i =0; i<s.length(); i++)
            myStack.push(s.substring(i, i+1));
    }

    private static String reverse(String s) {
        /*Reverses the input string*/
        String temp = "";
        for(int i = s.length() -1; i>=0; i--)
            temp += s.charAt(i);
        
        return temp;
    }
    private String removeNum(Stack<String> myStack)
    {
        /*Removes the topmost number from the Stack
         * e.g. Stack that contains 5&4 should return 45
         *                          54 should return 4
         *                          2&83 should return 3
        */
        String number = "";
        number += myStack.pop();
        while( (!(myStack.peek() == null) && ((myStack.peek().compareTo("&") == 0) || myStack.peek().compareTo(".") == 0)) )
        {
            //Pops off the number and also the token character
            number += myStack.pop();
            number += myStack.pop();

            if(myStack.isEmpty())
            break;
        }
        //Reverse it. You pushed the number into the stack, now when you pop it off, it'll be reversed. Reverse it before we return it.
        number = reverse(number);
        return number;

    }

    private static String unsymbolize(String s)
    {
        /*Takes the String s and unsymbolizes any occurance of "&" */

        String returnMe = "";
        
        if(s.length()>1) {
            for(int i =0; i<s.length(); i++) {
                if(s.charAt(i) == '&')
                continue;
                
                returnMe += s.charAt(i);
            }  
            return returnMe;
        }
        return s;
    }
    private static String symbolize(String expression)
    {   
        /*Symbolizes the input String that is a number into a symbolized String that represents the number character by character.
         * If there are multiple digits, separate the digits by a "&" character. If there is a decimal, no work needs to be done. The String already handles this.
         */
        if(expression.length()>1) //only symbolize an input that's more than one
        {
            String returnMe = "";
            for(int i =0; i<expression.length(); i++) {
                returnMe += expression.charAt(i);
                returnMe += "&";
            }
            return returnMe.substring(0, returnMe.length()-1);
        }
        return expression;
    }
    private void performAddition(Stack<String> stack) {
                
        //remove numbers from the stack
        String num1 = removeNum(stack);
        String num2 = removeNum(stack);
        //unsybmbolize them
        num2 = unsymbolize(num2);
        num1 = unsymbolize(num1);
        //Parse into Doubles
        Double number1 = Double.parseDouble(num1);
        Double number2 = Double.parseDouble(num2);
        //Perform operation
        Double result = number1 + number2;
        //Parse into String
        String answer = String.valueOf(result);
        /*Symbolize the answer*/
        answer = symbolize(answer);
        /*insert it one character at a time. This is required as the way we designed this is that we input numbers one digit at a time into the stack. */
        stackInsert(stack, answer);

    }

    private void performMultiplication(Stack<String> myNums) {
        //Multiplication
        String num1 = removeNum(myNums);
        String num2 = removeNum(myNums);

        num2 = unsymbolize(num2);
        num1 = unsymbolize(num1);

        Double number1 = Double.parseDouble(num1);
        Double number2 = Double.parseDouble(num2);
        
        Double result = number1 * number2;
        String answer = String.valueOf(result);
        answer = symbolize(answer);
        
        stackInsert(myNums, answer);
    }

    private void performDivision(Stack<String> myNums) {
        //Division
        String num1 = removeNum(myNums);
        String num2 = removeNum(myNums);
        
        num2 = unsymbolize(num2);
        num1 = unsymbolize(num1);
        
        Double number1 = Double.parseDouble(num1);
        Double number2 = Double.parseDouble(num2);
        
        Double result = number2 / number1;
        String answer = String.valueOf(result);
        answer = symbolize(answer);
        stackInsert(myNums, answer);
    }

    private void performSubtraction(Stack<String> myNums) {
          //perform subtraction
          String num1 = removeNum(myNums);
          String num2 = removeNum(myNums);
          num2 = unsymbolize(num2);
          num1 = unsymbolize(num1);
          Double number1 = Double.parseDouble(num1);
          Double number2 = Double.parseDouble(num2);
          Double result = number2 - number1;
          String answer = String.valueOf(result);
          answer = symbolize(answer);
          stackInsert(myNums, answer);
    }

    private void negationCheck(String expr, int i, Stack<String> myNums) {
    //ONLY USEFUL FOR POSTFIX
    //Checks to see if the index at element i within the expr is a negation.
    //If so, do nothing


    //if the current element is '-' and the next element is not '&', then this is subtraction
    //& symbolizes that characters are linked together
    //exact same as above but with a different operation
    try {
        if(expr.charAt(i+1)!='&') {//if the next element is not &, then perform subtraction
            performSubtraction(myNums);
        }
        if(expr.charAt(i+1)=='&') {
            myNums.push(expr.substring(i, i+1));
        }
    }
    catch (Exception e) {
        //Exception is when index is out of bounds.
        //This means that the last character is a '-' in inFix
        //This means we are performing subtraction.
        performSubtraction(myNums);
    }

    }
    public Double postFixEvaluate(String expr)
    {
        //Evaluates the given postFix expression
        //Algorithim:
        //Input any number of the postFix into a Stack
        //If there is an operator, pop two numbers and perform that operation on them
        //--> Then push that new number into the Stack
        //Once at the end of the postFix, the current number in the Stack is the result.

        //This is all because of the way that postFix works: you have a number, then another number, then the operation on that number. 
        
        Stack<String> myNums = new Stack<String>();
        for(int i =0; i<expr.length(); i++) {
            boolean operator = false;
            char c = expr.charAt(i);
            switch(c) {
                case '+' : performAddition(myNums);
                            operator = true;
                            break;
                case '*' : performMultiplication(myNums);
                            operator = true;
                            break;
                case '/' : performDivision(myNums);
                            operator = true;
                            break;
                case '-' : negationCheck(expr, i, myNums);           //need to implement negationCheck(). It's really just the '-' check in the big if else-if ladder.
                            operator = true;
                            break;
            }
            if(!operator)
            myNums.push(expr.substring(i, i+1));
        }

        //We have a stack that represents the number now!
        /*Remove the number */
        String returnMe = removeNum(myNums);

        //There may be a null at the end of the String that needs to be removed. Q: where does this come from?
        returnMe = fixUp(returnMe);
        
        //Unsymbolize the String
        returnMe = unsymbolize(returnMe);
        /*Now returnMe is just a String of the number */
        //parseDouble the String returnMe
        return Double.parseDouble(returnMe);

    }

    private static String fixUp(String s)
    {
        /*This method is used due to fix an unknown bug. It fixes the input String s into an output String temp that is only the symbolized number.
         * It deletes the first occurence of 'n' in the string s by copying every character over into a new String
        */
        String temp = "";
        for(int i =0; i<s.length(); i++){
            if(s.charAt(i)=='n')
             break;
            temp += s.charAt(i);
        }
        return temp;
    }
    private static boolean negation(int index, String expr) {
        //THIS IS ONLY USEFUL FOR INFIX, FULLY PARANTHESISZED EXPRESSIONS

        //If '-' appears after a '(' then '-' is a negation
        //  --> return true
        //Otherwise, '-' is subtraction
        //  --> return false
        try {
            if(expr.charAt(index-1) == '(')
            return true;
            return false;
        }
        catch (Exception e ){   //index out of bounds exception
            //There is no previous index, so this is negation.
            return true;
        }
    }

    private static boolean isNumber(char c) {
        switch(c) {
            case('0') : return true;
            case('1') : return true;
            case('2') : return true;
            case('3') : return true;
            case('4') : return true;
            case('5') : return true;
            case('6') : return true;
            case('7') : return true;
            case('8') : return true;
            case('9') : return true;
        }
        return false;
    }


    private  String inFix2PostFix(String expression) {

        /*
        Move through the inFix String. Push any open paranthesis into the stack. Push any Operator into the Stack. 
        Insert any number into the return postFix String. If the inFix String currently is close paranthesis, then pop every element 
        from the Stack and insert it into the postFix (that aren't paranthesis) String until a open paranthesis is popped. 
        Move on to the next element in the String 
        */

        String postFix = "";
        Stack<String> myStack = new Stack<String>();

        for(int i =0; i<expression.length(); i++)
        {
            if(expression.charAt(i) == '(') {
                //If the previous character was a '-', then we need to push "*" into the stack and add "1" onto the postFix string. This is equivalent to multiplying by one. The previous '-' is indicating this.
                //If the previous character was a ')', then we need to push "*" into the stack. This is when we are doing something like ((a+b)(c+d)) and are performing multiplication.
                try {
                    if(expression.charAt(i-1) == '-') {
                        //Push "* into stack, add "1" onto postFix, and push "(" into stack
                        myStack.push("*");
                        postFix += "1";
                    }
                    if(expression.charAt(i-1) == ')' || isNumber(expression.charAt(i-1))) {
                        myStack.push("*");
                    }
                }
                catch (Exception e) { //index out of bounds exception, do nothing 
                }
                finally {   //always push the "(" into stack
                myStack.push("(");
                }
            }
            else if(expression.charAt(i) == '+') {
                myStack.push("+");
            }
            else if(expression.charAt(i) == '-') {
                //push "-" if it's an operator, otherwise this '-' is actually part of a number.
                //Q: When is '-' an operator, and when is it negation?

                /*
                 * (-a)    (-(a-b))   (-((c-d)-(a-b)))          many options
                 *  ^       ^  ^       ^   ^  ^  ^
                 *  |       /  |       |   \  |  /
                 * negation    |       |      |
                 *          subtract   |   subtract     
                 *                  negation
                 * 
                 * If '-' is between two operands (numbers), then it's an operator
                 * If '-' is between an operand (number) and an operator, it is negation on the operand.
                 * 
                 * We will check to see if the character before the '-' is a '('
                 * -> If true, this is negation.
                 *     ~Add the "-" into the number with a "&" character.
                 * -> Otherwise, this is subtraction
                 *     ~Push the '-' into the operator stack
                 */
                if(negation(i, expression)) {
                    postFix += "-&";
                }
                else {
                    myStack.push("-");
                }
    
            }
            else if(expression.charAt(i) == '*') {
                myStack.push("*");
            }
            else if(expression.charAt(i) == '/') {
                myStack.push("/");
            }
            else if(expression.charAt(i) == ')') {
                /*Pop every element in the Stack until a closed paranthesis appears*/
                while(myStack.peek() != "(")
                {
                    postFix += myStack.pop();
                }
                myStack.pop(); //pop out that last paranthesis that was used for the while loop check
            }
            //It is an integer, we are assuming no spaces and the String only contains paranthesis, operands, or operators.
            /*If it's a number, we must read off each 'digit' of the number until something else appears.*/
            
            else {
                String number = "";
                while(expression.charAt(i) == '.' || expression.charAt(i) == '0' || expression.charAt(i) == '1' || expression.charAt(i) == '2' || expression.charAt(i) == '3' || expression.charAt(i) == '4' || expression.charAt(i) == '5' || expression.charAt(i) == '6' || expression.charAt(i) == '7' || expression.charAt(i) == '8' || expression.charAt(i) == '9')
                {
                    //If the previous digit was a number, then we need to symbolize that this next digit is part of that same number.
                    if(expression.charAt(i-1) == '.' || expression.charAt(i-1) == '0' || expression.charAt(i-1) == '1' || expression.charAt(i-1) == '2' || expression.charAt(i-1) == '3' || expression.charAt(i-1) == '4' || expression.charAt(i-1) == '5' || expression.charAt(i-1) == '6' || expression.charAt(i-1) == '7' || expression.charAt(i-1) == '8' || expression.charAt(i-1) == '9')
                    number += "&";

                    number += expression.charAt(i);
                    i++;
                }
                //expression at i is now a close paranthesis or a operator. Rememember, the for loop will increment and check at this spot. So we must decrement i so that the for loop does it's job.
                i--;
                //We now must add the String number onto the postFix String
                postFix += number;
            }
        }
        return postFix;
    }

    private static String removeSpaces(String expr) {
        //returns a String of the expr without any spaces.

        String s = "";
        for(int i =0; i<expr.length(); i++) {
            if(expr.charAt(i) != ' ')
            s += expr.charAt(i);
        }
        return s;
    }

    private static boolean isParanthesis(String expr) {
        Stack<Character> myStack = new Stack<Character>();
        for(int i =0; i<expr.length(); i++) {
            if(expr.charAt(i) == '(')
                myStack.push('(');
            if(expr.charAt(i) == ')') {
                if(!myStack.isEmpty())
                 myStack.pop();
                else
                 return false; //mismatch
            }
        }
        //if empty stack, then we popped off every paranthesis that we pushed
        if(myStack.isEmpty()) {
            return true;
        }
        //not empty stack, so there are too many '(' pushed for the amount of ')' popped
        else return false;
    }

}