package edu.cmu.zhiyuel.hw2.annotators;
/**
 * <p>
 * use lingPipe's confidence name entity recognizer to identify the names with high confidence
 * also use some heuristic rules to clear noises.
 * </p>
 **/
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.zhiyuel.types.LGene;
import edu.cmu.zhiyuel.types.Sentence;

public class LingPipeModelAnnotator extends JCasAnnotator_ImplBase {
  private final int MAX_N_BEST_CHUNK = 10;

  private final double conf_threshold = 0.5;// increase threshold

  public LingPipeModelAnnotator() {
    // TODO Auto-generated constructor stub
  }

  private int trimWhiteSpaces(String phrase) {
    int cnt = 0;
    for (int i = 0; i < phrase.length(); i++) {
      if (Character.isWhitespace(phrase.charAt(i))) {
        cnt++;
      }
    }
    return cnt;
  }
/**
 * use lingPipe's confidence name entity recognizer to identify the names with high confidence with some heuristic rules clearing noises.
 * @param aJCas
 * */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    System.out.println("Annotating Lingpipe...."+this.getClass().getName());
    File modelFile = new File("src/main/resources/dataset/ne-en-bio-genetag.HmmChunker");

    // last time:
    // ########correct###########15504
    // #####recall#########0.848836572679989
    // #####precision#########0.7685139288192724
    // #####geneName count#########20174

    ConfidenceChunker chunker = null;
    try {
      chunker = (ConfidenceChunker) AbstractExternalizable.readObject(modelFile);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();// ?
    int lines = 1;
//    while(it.hasNext()&&lines <= 600){
//      Sentence annotation = (Sentence) it.next();
//      lines++;
//    }
    while (it.hasNext()) {

      // get sentence annotator from CAS
      Sentence annotation = (Sentence) it.next();
      String id = annotation.getSentenceID();
      String text = annotation.getSentenceText();
      // System.out.println(id + "|" + text);
      char[] cs = text.toCharArray();
      Iterator<Chunk> chunkit = chunker.nBestChunks(cs, 0, cs.length, MAX_N_BEST_CHUNK);
      for (int n = 0; chunkit.hasNext(); n++) {
        Chunk c = chunkit.next();
        double conf = Math.pow(2.0, c.score());
        int start = c.start();
        int end = c.end();
        String phrase = text.substring(start, end);
        
          if (conf > 0.99 && !clearSymbol(phrase)) {
            LGene lg = new LGene(aJCas);
            lg.setId(id);
            lg.setBegin(start);
            lg.setEnd(end);
            int a = trimWhiteSpaces(text.substring(0, start));
            int s = start - a;
            int b = trimWhiteSpaces(phrase);
            int e = end - a - b - 1;
            lg.setGeneStart(s);
            lg.setGeneEnd(e);
            lg.setGeneName(phrase);
            lg.setCasProcessorId(this.getClass().getName());
            lg.setConfidence(conf);
            lg.addToIndexes();
//            System.out.println(lines + ":" + id + "|" + n + "\t" + conf + "       (" + s + ", " + e
//                    + ")       " + c.type() + "         " + phrase + "(" + a + " , " + b + "  )");
          } else if (conf > conf_threshold && clearHeuristic(phrase)) {// adjust n also for better
                                                                       // performance
            LGene lg = new LGene(aJCas);
            lg.setBegin(start);
            lg.setEnd(end);
            lg.setId(id);
            int a = trimWhiteSpaces(text.substring(0, start));
            int s = start - a;
            int b = trimWhiteSpaces(phrase);
            int e = end - a - b - 1;
            lg.setGeneStart(s);
            lg.setGeneEnd(e);
            lg.setGeneName(phrase);
            lg.setCasProcessorId(this.getClass().getName());
            lg.setConfidence(conf);
            lg.addToIndexes();
//            System.out.println(lines + ":" + id + "|" + n + "\t" + conf + "       (" + s + ", " + e
//                    + ")       " + c.type() + "         " + phrase + "(" + a + " , " + b + "  )");
          }
        
      }
      lines++;
    }
    // FSIterator<Annotation> iter = aJCas.getAnnotationIndex(LGene.type).iterator();
    // while (iter.hasNext()) {
    // LGene annotation = (LGene) iter.next();
    // System.out.println(annotation.getName());
    // }
  }
// a combination of some heuristic rules
  public boolean clearHeuristic(String phrase) {
    if (!clearName(phrase)&&!clearOneLetter(phrase) && !clearSymbol(phrase)
            && !clearNumbers(phrase) && !clearLowerFormat(phrase)) {
      return true;
    }
    return false;
  }

  // names in this hashset could not be used alone as gene name
  public boolean clearName(String s) {
    HashSet<String> hs = new HashSet<String>();
    hs.add("N1");
    hs.add("N0");
    hs.add("T1");
    hs.add("T2");
    hs.add("T3");
    hs.add("M2");
    hs.add("M3");
    hs.add("SGPT");
    hs.add("SGOT");
    hs.add("LAC");
    hs.add("CBA");
    hs.add("T-4");
    hs.add("M (muscle)");
    hs.add("L (liver)");
    if (hs.contains(s))
      return true;
    return false;
  }
//string like "f1","e1" could not be gene, the gene symbol are always capital letter 
  public boolean clearLowerFormat(String s) {
    if (s.length() == 2) {
      char a = s.charAt(0);
      char b = s.charAt(1);
      if (Character.isDigit(b) && Character.isLowerCase(a)) {
        return true;
      }
    }
    return false;
  }
//clear the names that has only one letter
  public boolean clearOneLetter(String s) {
    if (s.length() == 1) {
      // char a = s.charAt(0);
      // if (Character.isLowerCase(a)) {
      return true;
      // }
    }

    return false;
  }
//clear the recognized numbers
  public boolean clearNumbers(String s) {
    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    return pattern.matcher(s).matches();
  }
//clear unmatched parentheses, and the parentheses that wrap the whole name
  public boolean clearSymbol(String s) {
    if(s.startsWith("(")&&s.endsWith(")")){
      return true;
    }
    if (s.contains("(")) {
      if (!s.contains(")"))
        return true;
    } else if (s.contains(")")) {
      if (!s.contains("("))
        return true;
    }
    return false;
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
