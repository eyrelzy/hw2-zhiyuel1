package edu.cmu.zhiyuel.hw2.annotators;

import java.util.List;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.zhiyuel.types.Sentence;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class StanfordNerModelAnnotator extends JCasAnnotator_ImplBase {

  public StanfordNerModelAnnotator() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    // training set: cleared_data.tag third party: stanford-ner method: trained offline
    String serializedClassifier = "src/main/resources/dataset/ner-model.ser.gz";
    @SuppressWarnings("unchecked")
    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    /*
     * String s3 =
     * "an Peroxydase reaction stains were negative, chloroacetate esterase were strongly positive."
     * ; System.out.println(classifier.classifyToString(s3));
     * System.out.println(classifier.classifyWithInlineXML(s3));
     */
    System.out.println("Annotating StanfordNer...");
    JCas jcas = (JCas) aJCas;

    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();// ?
    int cnt = 1;
    while (it.hasNext()&&cnt<=100) {
      // get sentence annotator from CAS
      Sentence annotation = (Sentence) it.next();
      String id = annotation.getSentenceID();
      String text = annotation.getSentenceText();
      String xmlres = classifier.classifyWithInlineXML(text);
      int pos = 0;
      // if have mutilple gene in one sentence
      System.out.println("text:"+xmlres);
      cnt++;
//      while(true){
//      int start=findStart(xmlres,pos);
//      
//      int end=xmlres.indexOf("<",start);
//      if(start>0)
//        {
//          System.out.println(xmlres.substring(start, end));
//          pos=end+8;
//        }
//      else{
//        break;
//      }
//      }
     /*
      while (pos != -1) {
        
        int start = findStart(xmlres, "GENE", pos);
        int end = 0;
        if (start > 0) {
          end = xmlres.indexOf(">",start);
          System.out.println(start+","+end);
          String content = xmlres.substring(start, end);
          System.out.println(start+";"+content+";"+end);
          SGene sg = new SGene(jcas, start, end);
          sg.setBegin(start);
          sg.setEnd(end);
          sg.setName(content);
          sg.setCasProcessorId(this.getClass().getName());
          sg.setConfidence(1.0d);
          sg.addToIndexes();
          // store into type
          pos = end;
        } else {
          pos = -1;
        }
      }
*/
    }
  }

  // "<GENE1>"
  public int findStart(String s,  int pos) {
   if(s.contains("GENE1")||s.contains("GENE2")){
    int x = s.indexOf("GENE", pos);
   
    int y = s.indexOf(">",x);
    System.out.println("x,y:"+x+","+y);
    return y + 1;
   }
   return -1;
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String serializedClassifier = "src/main/resources/dataset/ner-model.ser.gz";

    CRFClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

    String test="Studies on immunoglobulin E: the impact of a sojourn with Professor Dan H.";
    System.out.println(classifier.classifyWithInlineXML(test));
      String s3 ="an Peroxydase reaction stains were negative, chloroacetate esterase were strongly positive.";
      System.out.println(classifier.classifyToString(s3));
      System.out.println(classifier.classifyWithInlineXML(s3));
     
    System.out.println("Annotating StanfordNer...");
  }

}
