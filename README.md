# Data Mining & Warehousing

# Project 3 - *Discovery Significant Associate Pattern*

## 1. What the problem is solving ? 

The program is aiming to find the significant association patterns for a large data set(e.g. market basket transaction) according to the two criteria, the program can get the 2nd, 3rd or higher order pattern, and decide which pattern passing the support and which pattern is statiscally significant associate pattern. 

(1)	Support Measure (cirteria-1) </br>
(2)	Interestingness/level of dependency (criteria-2) </br>
Mutual information measure in event pattern level:  log_2[Pr(A:0,B:1)/(Pr(A:0) * Pr(B:1))] </br>
(3)	Discovering significant association patterns requires: </br>
-	Join probability information : Pr(x1, x2, ….., xn) </br>
-	Marginal Probability information: Pr(x1), Pr(x2), …, Pr(xn) </br>
-	Appropriate support threshold a related to population size N </br>
[ ]	Properties for significant association patterns:  </br>
-	Support measure Pr(x1, x2, …xn) > a%  </br>
-	MI(x1, x2, …., xn)  > (1/ Pr(x1, x2, ….., xn)) ^[(lamda^2/2N)^[(E^/E’)^(O/2)]  </br>
-	MI(x1, x2, …., xn)  = Log_2Pr(x1, x2, …, xn)/(Pr(x1)*Pr(x2)*…*Pr(xn)) </br>
-	N = smaple population size </br>
-	(lamda^2) = Pearson Chi-square test statistic defined as (Oi-Ei)^2/Ei </br>
-	E^ = Expected entropy measure of estimated probability model </br>
-	E’ = Maximal possible entropy of estimated probability model </br>
-	O = order of the association pattern (n is this case)  </br>

(4)	Unfortunately, statistical convergence does not behave well in high order patterns with multiple variables. </br>

## 2. Status of Code

   The program run well, and it can help us to detect with pattern pass the threshold, and after that, it can help us 
   calculate the left hand side and right hand side, in order to check if it is statistically significant association pattern not. 
   
## 3. Disclose limitation and discussed the lessons learned(All the limiations on the following have been fixed, Please look at video walk through-Improving Version)  

(1)	The program doesn’t check the input which is out of range of status. </br>      
####    Solving Solution: 
      In order to check if the input data from input table are out of range of specific column(attribute) or not, 
      I am using an array of HashMap to store all the status of each column. If any data point is unvalid, the program 
      will skip the pattern without showing in the result.
      
(2)	The program only capture the pattern according to the input, it doesn’t permute very kind of patters, for example, 
if there are A, B, C, D, E, F 6 columns, the program should permute all the 3rd order patters(ABC, BCD, CDE, DEF, …)
####    Solving Solution: 
       The program reads the number of order from input, and produces all the combination of pattern by applying the 
       Backtrack algorithm. After get all the permutation, we can process each pattern and calculate the freequecy 
       of each pattern. 
####    Code Snippes
       
        
        private Set<String> getPatternPermute(int numOrder) {
        Set<String> allPatterns = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        backtrack(1, numOrder, sb, allPatterns);
        System.out.println("All combination of patterns: size = " + allPatterns.size());
        for(String p : allPatterns){
            System.out.println("Pattern-letter = " + p);
        }
        
        //Produce all the possible probability of pattern according to the all the pattern combinations
        //e.g. "BCD"--> "Pr(B:0,C:0,D:0)"
        
        allExprPatterns = new HashSet<>();
        for(String p : allPatterns){
            StringBuilder comSb = new StringBuilder();
            char[] letters = p.toCharArray();
            comSb.append("Pr(");
            for(char a : letters){
                comSb.append(a);
                comSb.append(":0,");
            }
            comSb.deleteCharAt(comSb.length()-1);
            comSb.append(")");
            allExprPatterns.add(comSb.toString());
        }
        System.out.println("size = " + allExprPatterns.size());
        for(String pat: allExprPatterns){
            System.out.println("Pattern-expression = " + pat);
        }
        return allExprPatterns;
   } 
  
    //Produce all the combinations of patterns from all the columns, e.g. "BCD","CDE","CDE",....
    
    private void backtrack(int start, int numOrder,  StringBuilder sb, Set<String> allPatterns) {
        if(numOrder==0){
            allPatterns.add(sb.toString());
            return;
        }
        for(int i=start; i<colLetters.length; i++){
            sb.append(colLetters[i]);
            backtrack(i+1, numOrder-1, sb, allPatterns);
            sb.deleteCharAt(sb.length()-1);
        }
    }
    
## 4. Software Solution
###      Video Walkthrough

<img src='https://recordit.co/W3v1uFJ3NO.gif' width='600' alt='Video Walkthrough' />

###      Last Assignment-Q3

<img src='https://recordit.co/TMtP0F66aQ.gif' width='600' alt='Video Walkthrough' />

###    User specify the frequency count

<img src='https://recordit.co/kaq6drkYHw.gif' width='600' alt='Video Walkthrough' />

****************************************************************************
### I.	Data Structure
****************************************************************************
   ####     AssociationPattern class
   ##### Variables 
   
-	(int) numRows    
-	(int) numCols
-	(double) threshold
-	(int) numOrder
-	(HashMap) colToFreq :  a map array, to store the total frequency for each state on each column
-	(HashMap) colToLetter: a mapping between column name and letter 
-	(HashMap) colToIndex: a mapping between column letter representation and Index 
-	(HashMap) PatternToFreq: a mapping between pattern and number of showing frequency
-	Int[] colNumStates: store the total number of states for each column
-	String[] patternType: a String array to store all the patterns. E.g. “0,1”,    “1,0”,   “1,1”   1,2
-	String[] patternStatus: a String array to store the pattern status. E.g “Yes”, “No”
-	TreeSet<String> allExprPatterns: store all the expression of patterns. E.g Pr(B:0,C:0,D:0)
-	TreeMap<String, Integer> patternToAllFreq: a mapping between the pattern and frequency. E.g  patternToAllFreq.put(“Pr(B:0, C:1))”, 2)
   
   #####     Methods: 
   
-	loadData() : read from input file and write it into dataAry and nummary;
  store all the columns’ name and mapping them into a unique letter  representation(colToLetter, letterToCol); 
And mapping the column to index by using map colToIndex;
-	parseExpression(String expression) : parse the probality, e.g. Pr(B:1,C:0,D:2); extract the variable B, C, D (array variables) and the corresponding values 0 and 1(array values) ;
-	patternFreq_cal() : calculate how many a specified pattern occur , e.g Pr(B:1, C:0,D:2), return 2 when pattern (B:1, C:0,D:2) occur 2 times. After calculation, store all the pattern and its' frequency in the map(patternToFreq)
-  thresholdTestPrint( ) : print the pattern, frequency count, pattern probability, and if it pass the threshold test or not.
-  staticSignificantTest( ) : If the pattern pass the threshold test, continue to the second step to test if the pattern pass independent test.
-  leftHandSide( ) : compute the information measurement, e.g.  IM(B:0, C:0, D:2) = log_2(P(B C D)/(P(B)*P(C)*P(D)))
-  rightHandSide( ) : compute maximum entropy, system entropy, Chi-Square, and finally get : RHS = [1/pr(x1, x2, ..., xn)]*(Chi-Square/2*N)^[(E/E')^(O/2)].
-  getMaxEntropy( ) - E': get the maximum entropy possible for system: Max_Entropy = log_2(# of combination of states among all the variables)     
                      e.g. the possible states for each column is (B C D) = (2 3 4), then max_entropy = log_2(2*3*4) 
-  getSystemEntropy( ) - E^ : get the entropy of system: System_Entropy = - SUM_i (Pr_i * log_2(Pr_i))
                      e.g. Pr(B:1,C:0,D:2) ==> system_entropy = 5*(2/250)log_2(250/) + (40/250)*log_2(250/40) + (2*100/250*log_2(250/100)                
-  getChi_Square( ) : compute: Chi-square = (Oi-ei)^2/ei, Oi = N * Pr(B:1,C:0,D:2), ei = N * Pr(B:1) * Pr(C:0) * Pr(D:2)
-  getPatternPermute( ) : permuate all the possible combination of patterns given all the columns, e.g. given columns list = {B, C, D, E, F), when the orderNumber is 2, the function will returen : results = {BC, BD, BE, BF, CD, CE, CF, DE, DF, EF }
-  backtrack( ) : use recursion to permute all the possible combination of patterns

## 5. how to run the code?
      In IntelliJ IDE: 
      1) Place the input file under the src folder
      2) Configure the input parameter with 3 parameters,
      
      1st parameter: the name of input file
      2nd parameter: the number of threshold
      3rd parameter: the number of order
      
      eg.  inputData1.txt 0.13 3
      
      Please look at the video walkthrough


## 6. Resource reference
   https://www-users.cse.umn.edu/~kumar001/dmbook/ch6.pdf 


