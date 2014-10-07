package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.uima.resource.ResourceInitializationException;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
/**
 * <p>
 * Recognize nouns with stanfordCoreNLP
 * </p>
 * */
public class PosTagNamedEntityRecognizer {

  private StanfordCoreNLP pipeline;
  public static void main(String[] args){
    try {
      @SuppressWarnings("unused")
      PosTagNamedEntityRecognizer Taggers=new PosTagNamedEntityRecognizer();
      String test="Comparison with alkaline phosphatases and 5-nucleotidase";
      Map<Integer, Integer> m=Taggers.getGeneSpans(test);
      for (Map.Entry<Integer, Integer> entry : m.entrySet())
      {
          int begin = entry.getKey();
          int end = entry.getValue();
          System.out.println(test.substring(begin,end));
      }
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();//报错在这里！
    }
    
  }
  /**
   * construction method, make sure you have enough heapsize.
   * */
  public PosTagNamedEntityRecognizer() throws ResourceInitializationException {
    long heapsize=Runtime.getRuntime().totalMemory();
    System.out.println("heapsize is::"+heapsize);
    Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos");
    pipeline = new StanfordCoreNLP(props);
  }
/**
 * @param text 
 * @return get a set of pairs of start and end point of the noun in the sentence.
 * */
  public Map<Integer, Integer> getGeneSpans(String text) {
    Map<Integer, Integer> begin2end = new HashMap<Integer, Integer>();
    Annotation document = new Annotation(text);
    pipeline.annotate(document);
    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
    for (CoreMap sentence : sentences) {
      List<CoreLabel> candidate = new ArrayList<CoreLabel>();
      for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
        String pos = token.get(PartOfSpeechAnnotation.class);
        if (pos.startsWith("NN")) {
          candidate.add(token);
        } else if (candidate.size() > 0) {
          int begin = candidate.get(0).beginPosition();
          int end = candidate.get(candidate.size() - 1).endPosition();
          begin2end.put(begin, end);
          candidate.clear();
        }
      }
      if (candidate.size() > 0) {
        int begin = candidate.get(0).beginPosition();
        int end = candidate.get(candidate.size() - 1).endPosition();
        begin2end.put(begin, end);
        candidate.clear();
      }
    }
    return begin2end;
  }
}
