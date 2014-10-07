package edu.cmu.zhiyuel.hw2.annotators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import util.PosTagNamedEntityRecognizer;
import util.Util;
import edu.cmu.deiis.types.Token;
import edu.cmu.zhiyuel.types.Sentence;
/**
 * <p>
 * use stanford nlp to recognize nouns and retrieve them in the given gene name databases.
 * the strings in database are stored in hashset.
 * </p>
 * */
public class TokenAnnotator extends JCasAnnotator_ImplBase {
private static BufferedReader in = null;

private static String input = "src/main/resources/dataset/geneDBcleaned.tag";
private HashSet<String> hs=new HashSet<String>(); 
  public TokenAnnotator() {
    // TODO Auto-generated constructor stub
    
  }
  /**
   * <p>read the database and store them in hashset</p>
   * @param aContext 
   * */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    
    try {
      in = new BufferedReader(new FileReader(input));
      String strs = null;
      while ((strs = in.readLine()) != null) {
        //arr.add(strs);
        hs.add(strs);
      }
      }catch(Exception e){
        e.printStackTrace();
        }
    System.out.println("hashset size:"+hs.size());
      
  }
/**
 * use stanford nlp to recognize nouns and retrieve them in the given gene name databases.
 * Store the recognized terms into Token, and each confidence is assigned to 1.0, and its casProcessId is TokenAnnotator.
 * @param aJCas
 * */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    System.out.println("Annotating Token..."+this.getClass().getName());
    JCas jcas = (JCas) aJCas;

    PosTagNamedEntityRecognizer Tagger = null;
    try {
      Tagger = new PosTagNamedEntityRecognizer();
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();// 报错在这里！
    }
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();// ?
    int cnt=1;
    while (it.hasNext()) {
      // get sentence annotator from CAS
      Sentence annotation = (Sentence) it.next();
      String id = annotation.getSentenceID();
      String text = annotation.getSentenceText();
      // String test="Comparison with alkaline phosphatases and 5-nucleotidase";
      Map<Integer, Integer> m = Tagger.getGeneSpans(text);
      for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
        int begin = entry.getKey();
        int end = entry.getValue();
        String tokennoun = text.substring(begin, end);
        if(isGeneName(tokennoun)){
          //System.out.println("?");
          //System.out.println(text.substring(begin, end));
          Token t = new Token(jcas, begin, end);
          t.setId(id);
          t.setBegin(begin);
          t.setEnd(end);
          int within=Util.trimWhiteSpaces(tokennoun);
          int before=Util.trimWhiteSpaces(text.substring(0,begin));
          int s=begin-before;
          int e=s+tokennoun.length()-within-1;
          t.setGeneStart(s);
          t.setGeneEnd(e);
          t.setGeneName(tokennoun);
          t.setCasProcessorId(this.getClass().getName());
          t.setConfidence(1.0d);
          t.addToIndexes();
          //System.out.println(id+" ("+s+","+e+" ) "+tokennoun);
          cnt++;
        }
      }
    }
    System.out.println("Finishing tokenAnnotator and token cnt is "+cnt);

  }

  public boolean isGeneName(String s) {
    return hs.contains(s);
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
TokenAnnotator t=new TokenAnnotator();
System.out.println("...");
  }

}
