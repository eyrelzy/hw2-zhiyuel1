package edu.cmu.zhiyuel.hw2.annotators;
/**
 * <p>
 *         SentenceAnnotator is to split up the document into SentenceId and text.
 * </p>
 * @author zhiyuel
 *  
 *       
 **/       
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.zhiyuel.types.Sentence;

public class SentenceAnnotator extends JCasAnnotator_ImplBase {
  private Pattern mSentenceID;
  public SentenceAnnotator() {
    // TODO Auto-generated constructor stub
  }
  
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    // Get config. parameter valuesSentenceId
    //String[] SentenceIDStrings = (String[]) aContext.getConfigParameterValue("SentenceId");
    // System.out.println("Pattern:"+SentenceIDStrings[0]);
    // compile regular expressions
    //mSentenceID = Pattern.compile(SentenceIDStrings[0]);
    String SentenceID = (String) aContext.getConfigParameterValue("sentenceid");
    //System.out.println(SentenceID);
  }
  /**
   * @param aJCas get the Jcas from type system.
   * <p>The previous collection reader initializes the Jcas. This annotator get the document text form Jcas.
   * The span from start of each line to the first white space is the SentenceID. According to this property, we 
   * split up the original document and update the jcas.
   * @see org.apache.uima.collection.JCasAnnotator_ImplBase#process()
   * */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    System.out.println("Annotating Sentence..."+this.getClass().getName());
    int len=0;
    String[] strstext = aJCas.getDocumentText().trim().split("[\\n]");
    for (int i = 0; i < strstext.length; i++) {
      int firstSpace = strstext[i].indexOf(' ');//seperate the id and its content, since id doesn't always start with P, but has a whitespace.
      String sentenceId = strstext[i].substring(0, firstSpace).trim();
      String sentenceText = strstext[i].substring(firstSpace).trim();
      //System.out.println(i+":"+sentenceText);
      Sentence an = new Sentence(aJCas, len, len+strstext[i].length());//
      an.setSentenceID(sentenceId);
      len=len+strstext[i].length();
      an.setSentenceText(sentenceText);
      an.setCasProcessorId(this.getClass().getName());
      an.setConfidence( 1.0d );
      an.addToIndexes();
    }
    //test two gene are the same!
    /*
    Sentence a=new Sentence(aJCas);
    a.setSentenceID("1");
    a.setSentenceText("2");
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();// ?
    int cnt=1;
    while (it.hasNext()) {
      // get sentence annotator from CAS
      Sentence annotation = (Sentence) it.next();
      cnt++;
    }
    System.out.println(cnt);
    Sentence b=new Sentence(aJCas);
    b.setSentenceID("1");
    b.setSentenceText("2");
    it = aJCas.getAnnotationIndex(Sentence.type).iterator();
    cnt=1;
    while (it.hasNext()) {
      // get sentence annotator from CAS
      Sentence annotation = (Sentence) it.next();
      cnt++;
    }
    System.out.println(cnt);
    */
    
  }

}
