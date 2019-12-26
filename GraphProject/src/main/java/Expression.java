import java.util.ArrayList;
import java.util.List;

public class Expression {

    private String exp; //String to hold the original expression
    private String var = "x"; //Variable is set to be used as x

    private ArrayList<String> simple = new ArrayList(); //Hold each value and operator as an individual element
    private double[][] results = new double[20001][2]; //Stores the input and output values of the function




    public Expression(){
    }

    public Expression(String e){
        exp = e.replaceAll("\\s","");
    }

    //This method splits each value and operator and sets it to its own element in an ArrayList
    private void combine(){
        char[] c = exp.toCharArray();
        simple.clear(); //clears the arraylist in case the expression is changed

        String s = ""; //String to build numbers

        for(int i = 0; i < c.length; i++){
            //If the element is a number, variable, or a decimal point it adds to the string builder denoted by 's'
            if(Character.isDigit(c[i]) || Character.isAlphabetic(c[i]) || c[i] == '.'){
                s += c[i];

            }

            //Add the negative sign to a number if the previous element was an operator (+ - * /)
            else if(c[i] == '-'){
                if(i > 0){
                    if(c[i-1] == '+' || c[i-1] == '-' || c[i-1] == '*' || c[i-1] == '/'){
                        s+= c[i];

                    }
                    else{
                        simple.add(s);
                        simple.add(Character.toString(c[i]));
                        s = "";

                    }
                }

                //Automatically adds if it is the first element in the expression
                else if (i == 0){
                    s += c[i];

                }

            }

            //If element is an operator or parentheses it adds what the string builder had built to the arraylist
            //adds the operator to be the next element then clears the string builder
            else{
                simple.add(s);
                simple.add(Character.toString(c[i]));
                s = "";

            }

            //If at last index
            if(i == c.length-1){
                simple.add(s);
            }
        }

        //Gets rid of any elements that are empty
        for(int j = 0; j < simple.size(); j++){
            if(simple.get(j).equals("")){
                simple.remove(j);
                j--;
            }
        }


    }



    //This simplifies the expression to a single numerical value
    private double simplify(List<String> simp){
        if(simp.contains("(")){
            //Takes the index of the corresponding parentheses
            int a = simp.indexOf("(") + 1;
            int b = simp.lastIndexOf(")") - 1;


            //Removes the corresponding parentheses
            simp.remove(b + 1);
            simp.remove(a - 1);

            //Recalls function to simplify the expression of only what was in the parentheses
            simplify(simp.subList(a-1, b));

        }

        Double value;

        //Loops through until all exponents are dealt with
        while(simp.contains("^")){

            int i = simp.indexOf("^");

            //Takes the two numbers on both sides of the corresponding operator
            //Find the result from those numbers and operator
            //Replace operator with that result and remove the two old numbers from list
            if(isNumber(simp.get(i-1)) && isNumber(simp.get(i+1))){
                value = Math.pow(Double.parseDouble(simp.get(i-1)), Double.parseDouble(simp.get(i+1)));
                simp.set(i, value.toString());

                simp.remove(i+1);
                simp.remove(i-1);
            }
        }

        //Loops through until all multiplication and division is dealt with
        while((simp.contains("*") || simp.contains("/"))){
            int i = 0;

            //Returns index of the lowest index of * and /
            for(int j = 0; j < simp.size(); j++){
                if(simp.get(j).equals("*") || simp.get(j).equals("/")){
                    i = j;
                    break;

                }
            }

            //Takes the two numbers on both sides of the corresponding operator
            //Find the result from those numbers and operator
            //Replace operator with that result and remove the two old numbers from list

            if(isNumber(simp.get(i-1)) && isNumber(simp.get(i+1))){
                if(simp.get(i).charAt(0) == '*'){
                    value = Double.parseDouble(simp.get(i-1))* Double.parseDouble(simp.get(i+1));
                }

                else{
                    value = Double.parseDouble(simp.get(i-1))/ Double.parseDouble(simp.get(i+1));
                }

                simp.set(i, value.toString());

                simp.remove(i+1);
                simp.remove(i-1);
            }

        }

        //Loops through until all addition and subtraction is dealt with
        while((simp.contains("+") || simp.contains("-"))){

            int i = 0;
            //Returns index of the lowest index of * and /
            for(int j = 0; j < simp.size(); j++){
                if(simp.get(j).equals("+") || simp.get(j).equals("-")){
                    i = j;
                    break;

                }

            }

            //Takes the two numbers on both sides of the corresponding operator
            //Find the result from those numbers and operator
            //Replace operator with that result and remove the two old numbers from list
            if(isNumber(simp.get(i-1)) && isNumber(simp.get(i+1))){
                if(simp.get(i).charAt(0) == '+'){
                    value = Double.parseDouble(simp.get(i-1)) + Double.parseDouble(simp.get(i+1));
                }

                else{
                    value = Double.parseDouble(simp.get(i-1)) - Double.parseDouble(simp.get(i+1));
                }

                simp.set(i, value.toString());

                simp.remove(i+1);
                simp.remove(i-1);
            }
        }

        //returns the calculated value, which should be the only element left in the list
        return Double.parseDouble(simp.get(0));


    }

    //replaces the variable in the list with a number
    private List<String> replace(List<String> simp, String c, double val){
        //create a copy of the arraylist
        List<String> temp = new ArrayList<>(simp);
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).equals(c)){
                temp.set(i, val+"");
            }

        }
        //return an edited copy of the arraylist with the variable replaced
        return temp;

    }

    //Method that can be used outside of class to call other relevant methods
    public void solve(){
        combine();

        //This creates a systems of coordinates with x ranging from -100 to +100
        //and incrementing by 0.01
        //It takes this x value and finds the corresponding y value from the expression
        //for each x point by simplifying the replaced expression
        for(int i = 0; i < results.length; i++){
            double xVal = i/100.0 -results.length/200;
            results[i][0] = xVal; //x value, input
            results[i][1] = simplify(replace(simple, var, xVal)); //y value, output
        }

    }

    //Sets expression
    public void setExpression(String e){
        exp = e.replaceAll("\\s",""); //removes any whitespace
    }

    //Returns the array of coordinates
    public double[][] getPoints(){
        return results;
    }


    //Checks if a string can be converted into a double
    private boolean isNumber(String s){
        try{
            Double.parseDouble(s);
            return true;
        }
        catch(Exception e){
            return false;
        }

    }

}
