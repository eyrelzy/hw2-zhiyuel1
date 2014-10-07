package util;
/**
 * <p>a tool class provides other classes some methods.</p>
 * */
public class Util {

  public Util() {
    // TODO Auto-generated constructor stub
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }
  /**
   * @param phrase the string we want to calculate the number of white spaces in it. 
   * */
  public static int trimWhiteSpaces(String phrase) {
    int cnt = 0;
    for (int i = 0; i < phrase.length(); i++) {
      if (Character.isWhitespace(phrase.charAt(i))) {
        cnt++;
      }
    }
    return cnt;
  }

}
