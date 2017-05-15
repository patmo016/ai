import cosc343.assig2.Creature;
import java.util.Random;

/**
* The MyCreate extends the cosc343 assignment 2 Creature.  Here you implement
* creatures chromosome and the agent function that maps creature percepts to
* actions.  
*
* @author  
* @version 1.0
* @since   2017-04-05 
*/
public class MyCreature extends Creature {

  // Random number generator
  Random rand = new Random();
 
  int[][] chromosome;
  /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
  */
  public MyCreature(int numPercepts, int numActions) {
      //chromosome should be compiled of 27*11 weights
      chromosome = new int [numActions][numPercepts];
      for (int i=0; i< chromosome.length; i++){
         // System.out.println();
          for (int j=0; j<chromosome[i].length; j++){
              chromosome[i][j] = (rand.nextInt(2));
              //System.out.print(chromosome[i][j]); 
          }
      }    
     // System.out.println();

  }
  
  /* This function must be overridden by MyCreature, because it implements
     the AgentFunction which controls creature behavoiur.  This behaviour
     should be governed by a model (that you need to come up with) that is
     parameterise by the chromosome.  
  
     Input: percepts - an array of percepts
            numPercepts - the size of the array of percepts depend on the percept
                          chosen
            numExpectedAction - this number tells you what the expected size
                                of the returned array of percepts should bes
     Returns: an array of actions 
  */
  @Override
  public float[] AgentFunction(int[] percepts, int numPercepts, int numExpectedActions) {
      
      // This is where your chromosome gives rise to the model that maps
      // percepts to actions.  This function governs your creature's behaviour.
      // You need to figure out what model you want to use, and how you're going
      // to encode its parameters in a chromosome.
      
      // At the moment, the actions are chosen completely at random, ignoring
      // the percepts.  You need to replace this code.
      double action=0;
      float []actionArray = new float[numExpectedActions];
      //[9] =1;
     
      for (int i=0; i<actionArray.length; i++){
          //System.out.println("action"+i);
          for (int j = 0; j<chromosome[i].length; j++){
           //  System.out.println("percept " +percepts[j]+" p+c:" +percepts[j]*chromosome[i][j]);
              actionArray[i] += percepts[j]*chromosome[i][j];
             // System.out.println(chromosome[i][j]);
             // System.out.println("percept: " + j +" " +percepts[j]);
             //System.out.print("action: " +i+ " " + actionArray[i] + "\n");           
             //System.out.println(i);       
          }    
         // System.out.println("action prob is" + actionArray[i]);
       
      }
      
      float actions[] = new float[numExpectedActions];
      for(int i=0;i<numExpectedActions;i++) {
         actions[i]=rand.nextFloat();
      } 
 
      
  return actionArray;
  //return actions;
     
  }
  
}