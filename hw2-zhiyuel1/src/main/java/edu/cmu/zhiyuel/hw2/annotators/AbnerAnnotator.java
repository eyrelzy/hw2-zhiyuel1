package edu.cmu.zhiyuel.hw2.annotators;
/**
 * Use abNER's BIOCREATIVE corups
 * This is the interface to the CRF that does named entity tagging. It contains methods for taking input text and returning tagged results in a variety of formats.

By default, the all methods in the Tagger class use ABNER's built-in tokenization. A single newline, e.g. '\n', is treated as a space, but two or more will conserve a paragraph break. You may also disable it and use your own pre-tokenized text if you prefer, though tokens must be whitespace-delimited, with newlines separating sentences.
@see http://pages.cs.wisc.edu/~bsettles/abner/javadoc/
 * */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import util.Util;
import abner.Tagger;
import edu.cmu.zhiyuel.types.AGene;
import edu.cmu.zhiyuel.types.Sentence;

public class AbnerAnnotator extends JCasAnnotator_ImplBase {
  private BufferedWriter bw = null;
  private File out = null;
  private Tagger t=null;
  public AbnerAnnotator() {
    // TODO Auto-generated constructor stub
    
  }
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    t = new Tagger(Tagger.BIOCREATIVE);// OR load another trained CRF by the external model
    }
/**
 * 
 * getEntities() returns all segments in the entire document that correspond to entities (e.g. "DNA," "protein," etc.). Segment text is stored in result[0][...] and entity tags (minus "B-" and "I-" prefixes) are stored in result[1][...].
 * generate AGene names preparing for the next voting process.
 * @param aJCas
 * */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    System.out.println("Annotating Abner..."+this.getClass().getName());
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Sentence.type).iterator();// ?
    int lines = 1;
    // while(it.hasNext()&&lines <= 600){
    // Sentence annotation = (Sentence) it.next();
    // lines++;
    // }
//    try {
//      
//      out = new File("Abner.out");
//
//      bw = new BufferedWriter(new FileWriter(out));
//      
//    } catch (Exception e) {
//      e.printStackTrace();
//    }                                  // file

    while (it.hasNext()) {
      Sentence san = (Sentence) it.next();
      String id = san.getSentenceID();
      String text = san.getSentenceText();
      String[][] result = t.getEntities(text);
      for (int i = 0; i < result[0].length; i++) {
        //String content=id+"|"+result[0][i] + "|" + result[1][i];
        int start=text.indexOf(result[0][i]);
        if(start==-1){
          break;
        }
        int a=Util.trimWhiteSpaces(text.substring(0, start));
        int b=Util.trimWhiteSpaces(result[0][i]);
        int s=start-a;
        int e=s+result[0][i].length()-b-1;
        AGene ag = new AGene(aJCas);
        ag.setId(id);
        ag.setGeneName(result[0][i]);
        ag.setGeneStart(s);
        ag.setGeneEnd(e);
        ag.setCasProcessorId(this.getClass().getName());
        ag.setConfidence(0.5d);
        ag.addToIndexes();
//        try {
//          content+="|"+ start+"("+s+","+e+")";
//          bw.write(content);
//          bw.newLine();
//          bw.flush();
//        } catch (IOException ex) {
//          // TODO Auto-generated catch block
//          ex.printStackTrace();
//        }
      }
    }
    System.out.println("Finishing AbnerAnnotator...");
    // String[][]
    // result=t.getEntities("an Peroxydase reaction stains were negative, chloroacetate esterase were strongly positive.");
    // System.out.println(result[0][0]+result[1][0]);
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Tagger t = new Tagger(new File("ab-test-model.ser"));
//    String text="an Peroxydase reaction stains were negative, chloroacetate esterase were strongly positive.";
    String s="NF-kappa B activation and IL-2 gene expression and NF-kappa B activation";
    String result=t.tagABNER(s);
    System.out.println(result);
//    Trainer thelper=new Trainer();
//    thelper.train("train.tag","ab-test-model.ser");
  }
  public void destroy(){
    System.out.println("destorying Abner...");

//    try {
//      bw.close();
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
  }

}
