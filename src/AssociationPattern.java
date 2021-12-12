import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by Shuhua Song
 */
public class AssociationPattern {
    static int numRows, numCols;
    static double threshold;
    String inputExpression;
    Map<String, Integer>[] typeToNum;
    Map<String, String> colToLetter;
    Map<String, String> letterToCol;
    Map<String, Integer> colToIndex;
    Map<String, Integer> inputVarToVal;
    Map<String, Integer> patternToFreq;
    Map<String, Integer>[] colToFreq;
    static String[][] dataAry, numAry;
    String[] inputVariables, inputValues;
    int[] inputColIdx;
    int totalCount = 0;
    int totalPattern = 0;
    double expressProb = 0.0;
    double termProb = 0.0;
    DecimalFormat decimalFormat = new DecimalFormat("#.000");
    //------------------------------------------------
    String[] patternType;
    String[] paterrnStatus;
    double[] patternProb;
    String[] inValues;
    public AssociationPattern(int numRows, int numCols, double threshold){
        this.numRows = numRows;
        this.numCols = numCols;
        this.threshold = threshold;
        typeToNum = new HashMap[numCols];
        colToFreq = new HashMap[numCols];
        colToLetter = new HashMap<>();
        letterToCol = new HashMap<>();
        colToIndex = new HashMap<>();
        inputVarToVal = new HashMap<>();
        patternToFreq = new HashMap<>();
        for(int i=0; i<numCols; i++){
            typeToNum[i] = new HashMap<>();
        }
        for(int i=0; i<numCols; i++){
            colToFreq[i] = new HashMap<>();
        }
        dataAry = new String[numRows][numCols];
        numAry =  new String[numRows][numCols];
    }

    private void loadData(Scanner inputFile, String[][] dataAry) {
        int currRow = 0;
        String s;
        while(inputFile.hasNextLine() && currRow < numRows) {
            s = inputFile.nextLine();
           // System.out.println("--  " + s);
            String[] parts = s.split("\\s+");
            int partsLen = parts.length;

            for(int c = 0; c < partsLen; c++) {
                dataAry[currRow][c] = parts[c];
                numAry[currRow][c] = parts[c];
            }
            ++currRow;
           // System.out.println();
        }


        for(int c=0; c<numCols; c++){
            int counter = 0;
            for(int r=1; r<numRows; r++){
                String data = dataAry[r][c];
                colToFreq[c].put(data, colToFreq[c].getOrDefault(data,0)+1);
                if(!typeToNum[c].containsKey(dataAry[r][c])){
                    typeToNum[c].put(dataAry[r][c], counter);
                    counter++;
                }
            }
        }
        for(int c=0; c<numCols; c++){
           // System.out.println();
            for(String key : typeToNum[c].keySet()){
                //System.out.println(key + " --##--" + typeToNum[c].get(key));
            }
            for(String key : colToFreq[c].keySet()){
              //  System.out.println(key + "--####--" + colToFreq[c].get(key));
            }
        }

        for(int c=1; c<numCols; c++){
            for(int r=1; r<numRows; r++){
                numAry[r][c] = typeToNum[c].get(numAry[r][c])+"";
            }
        }

        for(int i=1; i<numRows; i++){
            totalCount += Integer.parseInt(numAry[i][numCols-1]);
        }

        System.out.println("---------------------------------------------------------------------------");
        for(int c=0; c<numCols; c++){
            char letter = (char)('A'+(char)(c));
            colToLetter.put(dataAry[0][c], letter+"");
            letterToCol.put(letter+"", dataAry[0][c]);
            colToIndex.put(letter+"", c);
            //System.out.println(dataAry[0][c] + " $$$ " + letter + " $$$ " + c);
        }

        for(int c=0; c<numCols; c++){
           // System.out.println(numAry[0][c] + "*******" + colToLetter.get(numAry[0][c]));
            numAry[0][c] = colToLetter.get(numAry[0][c]) + "";
        }


        for(int c=0; c<numCols; c++){
            System.out.print(dataAry[0][c] + "(" + numAry[0][c] + ")" +  "             ");
        }

        System.out.println();
        for(int i = 1; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                System.out.print(dataAry[i][j] + "                   ");
            }
            System.out.println();
        }


       /* for(int i = 0; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                System.out.print(numAry[i][j] + "                    ");
            }
            System.out.println();
        } */
    }

    public double parseExpression(String expression){
         int start = 0, end = expression.length()-1;
         String subExpre = "";
         while(start < end){
             while(start < end && expression.charAt(start) != '('){
                 start++;
             }
             while(start < end && expression.charAt(end) != ')'){
                 end--;
             }
             if(start < end){
                 subExpre = expression.substring(start+1, end);
                 //System.out.println("SubExpress = " + subExpre);
                 break;
             }
         }
         String[] variables = subExpre.split(",");
         //A:1,B:0,C:2
        int n = variables.length;
        String[] colVars = new String[n];
        String[] values = new String[n];
        for(int i=0; i<n; i++){
            String term = variables[i];
            String[] terms = term.split(":");
            colVars[i] = terms[0];
            values[i] = terms[1];
           // System.out.println(colVars[i] + "--" + values[i]);
        }
        inputVariables = colVars.clone();
        inputValues = values.clone();
        /*
       1 - B--1
       2 - C--0
       3 - D--2
         */
        int sum = 0;
        //double expressProb = 0.0;
        for(int r=1; r<numRows; r++){
            boolean match = true;
            for(int k=0; k<n; k++){
                int colIdx = colToIndex.get(colVars[k]);
                if(!numAry[r][colIdx].equals(values[k])){
                    match = false;
                    break;
                }
            }
            if(!match) continue;
            if(match){
                sum += Integer.parseInt(numAry[r][numCols-1]);
               // System.out.println("SUM = " + sum);
            }
        }
        expressProb = (sum+0.0)/totalCount;

        //find input variable and corresponding value
        //Pr(B:1,C:0,D:2)
        // Pr(B:1) * Pr(C:0) * Pr(D:2)
        for(int k=0; k<n; k++){
            int colIdx = colToIndex.get(colVars[k]);
            int total = 0;
            for(int r=1; r<numRows; r++){
                if(numAry[r][colIdx].equals(values[k])){
                    total += Integer.parseInt(numAry[r][numCols-1]);
                }
            }
            inputVarToVal.put(colVars[k], total);
           // System.out.println(colVars[k] + "---@@@@--" + total);
        }
        return expressProb;
    }

    public void patternFreq_cal(){
        int n = inputVariables.length;
        inputColIdx = new int[n];
        StringBuilder input = new StringBuilder();
        for(int i=0; i<n; i++){
            input.append(inputVariables[i]).append(",");
            inputColIdx[i] = colToIndex.get(inputVariables[i]);
           // System.out.println(inputColIdx[i] + "--****--" + inputVariables[i]);
        }
        input.deleteCharAt(input.length()-1);
        inputExpression = input.toString();
        for(int r=1; r<numRows; r++){
            StringBuilder sb = new StringBuilder();
            for(int c=0; c<inputColIdx.length; c++){
                if(c!=inputColIdx.length-1){
                    sb.append(dataAry[r][inputColIdx[c]]).append(",");
                }else{
                    sb.append(dataAry[r][inputColIdx[c]]);
                }
            }
            String currPattern = sb.toString();
            patternToFreq.put(currPattern, patternToFreq.getOrDefault(currPattern,0)+1);
        }
        patternType = new String[patternToFreq.size()]; // "0,1  1,0  1,1  1,2
        paterrnStatus = new String[patternToFreq.size()]; // "Threshold Past ? Yes : No"
        for(String key : patternToFreq.keySet()){
            totalPattern += patternToFreq.get(key);
        }
        patternProb = new double[patternToFreq.size()];
        int i = 0;
        for(String key : patternToFreq.keySet()){
            patternType[i] = key;
            patternProb[i] = (patternToFreq.get(key)+0.0)/(double)totalPattern;
            //System.out.println(key + " " + patternToFreq.get(key) +  "  " + totalPattern);
            i++;
        }
        //Threshold Test
        for(int j=0; j<patternProb.length; j++){
            if(patternProb[j] > threshold){
                paterrnStatus[j] = "Yes";
            }else{
                paterrnStatus[j] = "No";
            }
        }
        thresholdTestPrint(paterrnStatus);
        System.out.println("-----------------------------Statiscally Significant Test-------------------------");
        for(int j=0; j<paterrnStatus.length; j++){
            if(paterrnStatus[j].equals("Yes")){
                termProb = patternProb[j];
                staticSignificantTest(patternType[j]);
                System.out.println("------------------------------------------------------------------------");
            }
        }
    }

    public void thresholdTestPrint(String[] paterrnStatus){

        int k = 0;
        System.out.println("-----------------------------------Threshold Test---------------------------------");
        System.out.println();
        System.out.println("Pattern" + "(" + inputExpression + ")     Frequency       Passing Threshold Test");
        for(String key : patternToFreq.keySet()){
            System.out.println(key + "                  " + patternToFreq.get(key) + "             " + paterrnStatus[k++]);
        }
    }

    private void staticSignificantTest(String pattern) {
         System.out.println("----------------------- Test pattern: (" + pattern + ") --------------------------------");
         inValues = pattern.split(",");
         double left = leftHandSide(totalPattern, inValues);
         System.out.println("  = " + left);
         System.out.print("values : ");
         for(String var : inValues){
             System.out.print(var + "  ");
         }
         System.out.println();
         double right = rightHandSide(totalPattern);
         System.out.println("Right Hand Side : " + right);
        if(left < right){
            System.out.println("Left Hand Side < Right Hand Side");
            System.out.println("Conclusion: " + inputExpression + " is not a statiscally significant association pattern because it fails" +
                    " the independency test");
        }else{
            System.out.println("Left Hand Side > Right Hand Side");
            System.out.println("Conclusion: " + inputExpression + " is a statiscally significant association pattern because it passes" +
                    " the independency test");
        }
    }

    // MI(x1, x2, ..., xn)
    public  double leftHandSide(double N, String[] vals) {
        System.out.print("Left Hand Side: MI(" + inputExpression + ")");
        double result = 0;
        double y = 1;
        for(int i=0; i<vals.length; i++){
            int col = inputColIdx[i];
            y *= (colToFreq[col].get(vals[i])+0.0)/totalPattern;
            //System.out.println(colToFreq[col].get(vals[i]) + " ---!!-- " + ((colToFreq[col].get(vals[i])+0.0)/totalPattern));
        }
        y = termProb/y;
        //System.out.println("y = " + y);
       // double N = (Math.pow(250, 2))/(Math.pow(44, 2));
        result = Math.log(y)/Math.log(2); //
        result = Math.round(result*10000.0)/10000.0;
       // System.out.println("leftHandSide = " + result);
        return result;
    }

    //  [1/pr(x1, x2, ..., xn)]*(X^2/2*N)^[(E/E')^O/2]
    public  double rightHandSide(double N){
        double pr = termProb;
        double chi_square = getChi_Square(N);
        double result = 0;
        double E_maxEntropy = getMaxEntropy(N);
        double E_sysEntropy = getSystemEntropy(N);
        double O = (inputVariables.length+0.0)/2; //
        double expo = Math.pow((E_sysEntropy+0.0)/E_maxEntropy, O);
        expo = Math.round(expo*10000.0)/10000.0;
         System.out.println("expo = " + expo);
        double temp_chi = Math.pow((chi_square/(2.0*N)), expo);
        System.out.println("Chi-square/2N = " + (chi_square/(2.0*N)));
        result = 1/pr * Math.pow(temp_chi, expo);
        //result = (chi_square/(2.0*N));
        //result = Math.round(result*10000.0)/10000.0;
         System.out.println("temp = " + temp_chi);
        System.out.println("rightHandSide = " + result);
        return result;
    }

    private  double getMaxEntropy(double N) {
        double totleCombState = 1;
        for(int c : inputColIdx){
            //System.out.print(typeToNum[c].size() + " ");
            totleCombState *= colToFreq[c].size();
        }
        double result = Math.log(totleCombState)/Math.log(2);
        result = Math.round(result*10000.0)/10000.0;
        System.out.println("maxEntropy = " + result);
        return result;
    }

    private  double getSystemEntropy(double N) {
        int[] elems = new int[patternToFreq.size()];
        int k = 0;
        for(String pattern : patternToFreq.keySet()){
            elems[k++] = patternToFreq.get(pattern);;
        }
        //for(int e : elems){
        //    System.out.println(e + " --&&--");
        //}
        double sysEntropy = 0;
        for(int elem : elems){
            sysEntropy += (elem*1.0/N) * Math.log(N*1.0/elem)/Math.log(2);
        }
        sysEntropy = Math.round(sysEntropy*10000.0)/10000.0;
        System.out.println("SysEntropy = " + sysEntropy);
        return sysEntropy;
    }

    private double getChi_Square(double N) {
        double Oi = N * termProb;
        double ei = N;
        for(int i=0; i<inValues.length; i++){
            int col = inputColIdx[i];
            ei *= (colToFreq[col].get(inValues[i])+0.0)/totalPattern;
        }
        Oi = Double.parseDouble(decimalFormat.format(Oi));
        ei = Double.parseDouble(decimalFormat.format(ei));
        double result = Math.pow(Oi-ei, 2)/ei;
        result = Math.round(result*10000.0)/10000.0;
        System.out.println("Oi = " + Oi + "; ei = " + ei + " termProb = " + termProb);
        System.out.println("Chi-square = " + result);
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner inputFile = new Scanner(new FileReader(args[0]));
        String inputName = args[0];
        String thresholdSt = args[1];
        String expression = args[2]; //expression==order
        //System.out.println(args[0] + " " + args[1]);
        System.out.println(expression);

        double threshold = Double.parseDouble(thresholdSt);
        System.out.println("threshoud = " + threshold);
        int numRows = inputFile.nextInt();
        int numCols = inputFile.nextInt();
        inputFile.nextLine();
        AssociationPattern pattern = new AssociationPattern(numRows, numCols, threshold);
        String[][] dataAry = pattern.dataAry;

        pattern.loadData(inputFile, dataAry);


        System.out.println("------------------#########################-------------------");

        int total = pattern.totalPattern;
       // System.out.println("Total = " + total);

        Map<String, String> colToLetter = pattern.colToLetter;
        for(String k : colToLetter.keySet()){
          //  System.out.println(k + "   " + colToLetter.get(k));
        }

        double  expressProb = pattern.parseExpression(expression);
        pattern.patternFreq_cal();
        String[] inputVariables = pattern.inputVariables;
        String[] inputValues = pattern.inputValues;
        Map<String, String> letterToCol = pattern.letterToCol;

        StringBuilder sb = new StringBuilder();
        sb.append("Pr(");
        for(int i=0; i<inputVariables.length; i++){
            sb.append(letterToCol.get(inputVariables[i]) + ":" + inputValues[i] + ", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        String formalInput = sb.toString();

/*
        System.out.println( "expressProb = " + expressProb);
        System.out.println("Given threshold " + threshold + ", evaluate probability " + expression + " ? ");
        System.out.println("Step1 test:  Is " + formalInput + " > " + threshold);
        System.out.println(expression + " = "  +  formalInput  + " = " + expressProb );
        System.out.println("Answer: ");
        if(expressProb > threshold){
            System.out.println(expression + " > " + "threshold");
            System.out.println("Passing the first test for support messure!");
            System.out.println("Please evaluate the level of depdendency in terms of mutual information measure in the next step.");
        }else{
            System.out.println(expression + " < " + "threshold");
            System.out.println("The first test is fail, don't need to move forward!");
            System.exit(0);
        }

        pattern.getMaxEntropy(total);
        pattern.getSystemEntropy(total);
        pattern.getChi_Square(total);

        double rightHand = pattern.rightHandSide(total);
        double leftHand = pattern.leftHandSide(total);
        System.out.println("Left Hand Side = " + leftHand);
        System.out.println("Right Hand Side = " + rightHand);
        if(leftHand < rightHand){
            System.out.println("Left Hand Side < Right Hand Side");
            System.out.println("Conclusion: " + expression + " is not a statiscally significant association pattern because it fails" +
                    " the independency test");
        }else{
            System.out.println("Left Hand Side > Right Hand Side");
            System.out.println("Conclusion: " + expression + " is a statiscally significant association pattern because it passes" +
                    " the independency test");
        }
 */
        inputFile.close();
    }

}
