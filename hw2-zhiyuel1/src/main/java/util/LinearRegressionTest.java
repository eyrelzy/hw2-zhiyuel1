package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * Another idea about the voting process is to use regression analysis to consider half of the sample.out as training set and the other half as a test set. With gradient descent method, we could obtain the weight of each method mentioned in this system. 

 * </p>
 * */
public class LinearRegressionTest {
  private BufferedReader br = null;
private static String samplefile="regression.in";
  public LinearRegressionTest() {
    // TODO Auto-generated constructor stub
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
LinearRegressionTest lrt=new LinearRegressionTest();
List<Node> arr=lrt.readFile(samplefile);
lrt.gradientDescent(arr, 0, 0, 0, 0);
  }

  public void gradientDescent(List<Node> arr, double a, double b, double c, double d) {
    while (true) {
      double ta = a - getSum(arr, a, b, c, d, 0);
      double tb = b - getSum(arr, a, b, c, d, 1);
      double tc = c - getSum(arr, a, b, c, d, 2);
      double td = d - getSum(arr, a, b, c, d, 3);
      System.out.println(a+","+ta);
      System.out.println(b+","+tb);
     
      a = ta;
      b = tb;
      c = tc;
      d = td;
      double cost=getSum(arr, a, b, c, d, 4)/2;
      System.out.println(cost);
     if(cost<1){
       break;
     }
      
    }
    System.out.println(a+"|"+b+"|"+c+"|"+d);
  }
/**
 * @param x when x==0, we return the sum of the parameter x0's cost derivative, and so it is with x==1,x==2,x==3.
 *          When x==4, we return the square of the sum, in order to calculate the minimum cost.
 * */
  public double getSum(List<Node> arr, double a, double b, double c, double d, int x) {
    double sum = 0;
    for (int i = 0; i < arr.size(); i++) {
      if (x == 0) {
        sum += a + b * arr.get(i).x1 + c * arr.get(i).x2 + d * arr.get(i).x3 - arr.get(i).y;
      } else if (x == 1) {
        sum = sum + (a + b * arr.get(i).x1 + c * arr.get(i).x2 + d * arr.get(i).x3 - arr.get(i).y)
                * arr.get(i).x1;
      } else if (x == 2) {
        sum = sum + (a + b * arr.get(i).x1 + c * arr.get(i).x2 + d * arr.get(i).x3 - arr.get(i).y)
                * arr.get(i).x2;
      } else if (x == 3) {
        sum = sum + (a + b * arr.get(i).x1 + c * arr.get(i).x2 + d * arr.get(i).x3 - arr.get(i).y)
                * arr.get(i).x3;
      } else if (x == 4) {
        sum = sum
                + Math.pow(
                        (a + b * arr.get(i).x1 + c * arr.get(i).x2 + d * arr.get(i).x3 - arr.get(i).y),
                        2.0);
      }
    }
    return (double) sum / arr.size();

  }

  public List<Node> readFile(String samplefile) {
    List<Node> ret = new ArrayList<Node>();
    try {
      br = new BufferedReader(new FileReader(samplefile));
      String str = null;
      while ((str = br.readLine()) != null) {
        String[] strs = str.split(",");
        Node arr = new Node();
        arr.x1 = Integer.parseInt(strs[0]);
        arr.x2 = Integer.parseInt(strs[1]);
        arr.x3 = Integer.parseInt(strs[2]);
        arr.y = Integer.parseInt(strs[3]);
        ret.add(arr);
        // System.out.println(str);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  class Node {
    int x1;

    int x2;

    int x3;

    int y;

    Node() {
      x1 = 1;
      x2 = 1;
      x3 = 1;
      y = 0;
    }
  }
}
