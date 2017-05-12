import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.util.*;

/**
 * The MyWorld extends the cosc343 assignment 2 World. Here you can set some
 * variables that control the simulations and override functions that generate
 * populations of creatures that the World requires for its simulations.
 *
 * @author
 * @version 1.0
 * @since 2017-04-05
 */
public class MyWorld extends World {

    /* Here you can specify the number of turns in each simulation
   * and the number of generations that the genetic algorithm will 
   * execute.
     */
    private final int _numTurns = 100;
    private final int _numGenerations = 50000;
    
    public int genNum;
    public EvolutionGraph evol;

    public int actions = 11;
    public int percepts = 27;


    /* Constructor.  
   
     Input: grid    Size - the size of the world
            windowWidth - the width (in pixels) of the visualisation window
            windowHeight - the height (in pixels) of the visualisation window
            repeatableMode - if set to true, every simulation in each
                             generation will start from the same state
            perceptFormat - format of the percepts to use: choice of 1, 2, or 3
     */
    public MyWorld(int gridSize, int windowWidth, int windowHeight, boolean repeatableMode, int perceptFormat) {
        // Initialise the parent class - don't remove this
        super(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);

        // Set the number of turns and generations
        this.setNumTurns(_numTurns);
        this.setNumGenerations(_numGenerations);
        genNum = 0;
        evol = new EvolutionGraph();
    }

    /* The main function for the MyWorld application

     */
    public static void main(String[] args) {
        // Here you can specify the grid size, window size and whether torun
        // in repeatable mode or not
        //
        int gridSize = 24;
        int windowWidth = 700;
        int windowHeight = 500;
        boolean repeatableMode = false;

        /* Here you can specify percept format to use - there are three to
         chose from: 1, 2, 3.  Refer to the Assignment2 instructions for
         explanation of the three percept formats.
         */
        int perceptFormat = 2;

        // Instantiate MyWorld object.  The rest of the application is driven
        // from the window that will be displayed.
        MyWorld sim = new MyWorld(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);
    }


    /* The MyWorld class must override this function, which is
     used to fetch a population of creatures at the beginning of the
     first simulation.  This is the place where you need to  generate
     a set of creatures with random behaviours.
  
     Input: numCreatures - this variable will tell you how many creatures
                           the world is expecting
                            
     Returns: An array of MyCreature objects - the World will expect numCreatures
              elements in that array     
     */
    @Override
    public MyCreature[] firstGeneration(int numCreatures) {

        int numPercepts = this.expectedNumberofPercepts();
        int numActions = this.expectedNumberofActions();
        // This is just an example code.  You may replace this code with
        // your own that initialises an array of size numCreatures and creates
        // a population of your creatures
        MyCreature[] population = new MyCreature[numCreatures];
        for (int i = 0; i < numCreatures; i++) {
            population[i] = new MyCreature(numPercepts, numActions);
        }
        return population;
    }

    public MyCreature newCreature(MyCreature parentOne, MyCreature parentTwo) {
           
        MyCreature newCreature = new MyCreature(percepts, actions);
        float[][] crossOver = new float[actions][percepts];
      //  Random rand = new Random();
        
        //now I have found 2 parents- cross their chromosomes;
        //numPercepts*numActions
        for (int i = 0; i < crossOver.length; i++) {
          //  if(i<6){
            //    crossOver[i]=parentOne.chromosome[i];               
            //}else{
              //  crossOver[i] = parentTwo.chromosome[i];
            //}
           
           for(int j=0; j<parentOne.chromosome[i].length;j++){
               
               
           //This crosses the chromosomes vertically     
           if (j < 13)  {
                crossOver[i][j] = parentOne.chromosome[i][j];
            } else {
                crossOver[i][j] = parentTwo.chromosome[i][j];
            }
           }
        }
        newCreature.chromosome = crossOver;
        return newCreature;

    }

    public MyCreature getParent(MyCreature[] old_population, float fitsum) {
      //Tournament selection
      MyCreature [] subset = new MyCreature[10];
      MyCreature selected = new MyCreature(27, 11);
//      int count=0;
//      Random rand = new Random();    
//          while(count<subset.length){
//              subset[count]= old_population[rand.nextInt(old_population.length)];
//              count++;
//          }       
//          for (int i=0; i<subset.length; i++){
//             int fitness = subset[i].getEnergy()+subset[i].timeOfDeath();
//             if (i!=9 && fitness>(subset[i+1].getEnergy()+subset[i+1].timeOfDeath())){
//                 selected= subset[i];            
//             }           
//          }
//       
//       //roulette selection
        float roulette = (float) Math.random();
        float sumProb = 0;
        for (MyCreature creature : old_population) {
            //gives the creature the range of its probability theoretically should be a percentage.
            //then finds whether its the random value;
            if (sumProb < roulette && roulette < sumProb + (creature.getEnergy()+creature.timeOfDeath())/fitsum) {
                   
                //   System.out.println("roulette: " + roulette);
                //   System.out.println("sumProb " + sumProb);
               
                selected = creature;
            }
            sumProb = sumProb + (creature.getEnergy()+creature.timeOfDeath())/fitsum;
        }
        return selected;
    }

    /* The MyWorld class must override this function, which is
     used to fetch the next generation of the creatures.  This World will
     proivde you with the old_generation of creatures, from which you can
     extract information relating to how they did in the previous simulation...
     and use them as parents for the new generation.
  
     Input: old_population_btc - the generation of old creatures before type casting. 
                              The World doesn't know about MyCreature type, only
                              its parent type Creature, so you will have to
                              typecast to MyCreatures.  These creatures 
                              have been simulated over and their state
                              can be queried to compute their fitness
            numCreatures - the number of elements in the old_population_btc
                           array
                        
                            
  Returns: An array of MyCreature objects - the World will expect numCreatures
           elements in that array.  This is the new population that will be
           use for the next simulation.  
     */
    @Override
    public MyCreature[] nextGeneration(Creature[] old_population_btc, int numCreatures) {
        // Typcast old_population of Creatures to array of MyCreatures
        MyCreature[] old_population = (MyCreature[]) old_population_btc;
        // Create a new array for the new population
        MyCreature[] new_population = new MyCreature[numCreatures];
        
        MyCreature parentOne;
        MyCreature parentTwo;
        float fitsum = 0;
        // Here is how you can get information about old creatures and how
        // well they did in the simulation
        float avgLifeTime = 0f;
        int nSurvivors = 0;
        for (MyCreature creature : old_population) {
            // The energy of the creature.  This is zero if creature starved to
            // death, non-negative oterhwise.  If this number is zero, but the 
            // creature is dead, then this number gives the enrgy of the creature
            // at the time of death.
            int energy = creature.getEnergy();
            fitsum += (energy+creature.timeOfDeath());
            // System.out.println("count the damn energy"+ energy);
            // This querry can tell you if the creature died during simulation
            // or not.  
            boolean dead = creature.isDead();

            if (dead) {
                // If the creature died during simulation, you can determine
                // its time of death (in turns)
                int timeOfDeath = creature.timeOfDeath();
                avgLifeTime += (float) timeOfDeath;
            } else {
                nSurvivors += 1;
                avgLifeTime += (float) _numTurns;
            }
        }

        System.out.println("total fitness " + fitsum);
        
        genNum++;
        evol.addPoint(genNum, nSurvivors);
        // Right now the information is used to print some stats...but you should
        // use this information to access creatures fitness.  It's up to you how
        // you define your fitness function.  You should add a print out or
        // some visual display of average fitness over generations.
        avgLifeTime /= (float) numCreatures;
        System.out.println("Simulation stats:");
        System.out.println("  Survivors    : " + nSurvivors + " out of " + numCreatures);
        System.out.println("  Avg life time: " + avgLifeTime + " turns");

        // Having some way of measuring the fitness, you should implement a proper
        // parent selection method here and create a set of new creatures.  You need
        // to create numCreatures of the new creatures.  If you'd like to have
        // some elitism, you can use old creatures in the next generation.  This
        // example code uses all the creatures from the old generation in the
        // new generation.
        for (int i = 0; i < numCreatures; i++) {
            parentOne = getParent(old_population, fitsum);
            parentTwo = getParent(old_population, fitsum);

            new_population[i] = newCreature(parentOne, parentTwo);

//        new_population[i] = old_population[i];
        }

        // Return new population of cratures.
        return new_population;
    }

}
