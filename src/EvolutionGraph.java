import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EvolutionGraph extends JFrame {
    public String filenamex = "generations.txt";
    public String filenamey = "survivors.txt";
    public String filenamez = "fitnesses.txt";
    public ArrayList<int[]> points;
    public BufferedWriter bwx = null;
    public FileWriter fwx = null;
    public BufferedWriter bwy = null;
    public FileWriter fwy = null;
    public BufferedWriter bwz = null;
    public FileWriter fwz = null;
    public int numGens = 0;
    public EvolutionGraph(int n){
      super("EvolutionGraph");
      setContentPane(new DrawPane());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(300, 400);
      setVisible(true);
      numGens = n;
      points = new ArrayList<int[]>();
      try{
        fwx = new FileWriter(filenamex);
        bwx = new BufferedWriter(fwx);
        fwy = new FileWriter(filenamey);
        bwy = new BufferedWriter(fwy);
        fwz = new FileWriter(filenamez);
        bwz = new BufferedWriter(fwz);
      }catch(IOException e){
          e.printStackTrace();
      }
    }
    
    public void addPoint(int x, int y, int z){
        points.add(new int[]{x, y});
        try{
            bwx.write(x + "\n");
            bwy.write(y + "\n");
            bwz.write(z + "\n");
            if(x == numGens){
                bwx.close();
                fwx.close();
                bwy.close();
                fwy.close();
                bwz.close();
                fwz.close();
            }
        }catch(IOException e){
          e.printStackTrace();
        }
        repaint();
    }
  
    class DrawPane extends JPanel{
        public Graphics graphics;
        public void paintComponent(Graphics g){
            graphics = g;
            drawGraph();
        }

        public void drawGraph(){
            int width = getContentPane().getSize().width;
            int height = getContentPane().getSize().height;
            int heightMult = 5;
            int breakpoint = points.size()/width+1;
            int counter = 0;
            int max = 0;
            int y = 0;
            int x = 0;
            int avg = 0;
            int lastAvg = 0;
            int bestAvg = 0;
            for(int i = 0; i < points.size(); i++){
                int survivors = points.get(i)[1];
                avg += survivors;
                y+=survivors;
                counter++;
                if(counter >= breakpoint){
                    graphics.fillOval(x+25, height-40-(y/counter)*heightMult, 2, 2);
                    counter = 0;
                    y = 0;
                    x++;
                }
                if(survivors > max){
                    max = survivors;
                }
                if(points.get(i)[0]%100 == 0){
                    lastAvg = avg/100;
                    if(bestAvg < lastAvg){
                        bestAvg = lastAvg;
                    }
                    avg = 0;
                }
            }
            graphics.drawString("Max Survivors: " + max, 10, height-5);
            graphics.drawString("Last Avg: " + lastAvg + "   Best Avg: " + bestAvg, 10, height-20);
            for(int i = 0; i < 65; i+=5){
                graphics.drawString(""+i, 7, height - 35 - i*heightMult);
            }
            if(points.size() > 0){
                graphics.drawString("Generation " + points.get(points.size()-1)[0], width/3, 20);
            }
        }
    }
}