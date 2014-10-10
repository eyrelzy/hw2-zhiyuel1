package edu.cmu.zhiyuel.hw2.annotators;
/**
 * <p>This is a voting process to merge all three methods together.
 * If a name is recognized by at least two annotators, we consider it as a gene name.
 * </p>
 * 
 * */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import ed.cmu.zhiyuel.types.FinalGene;
import edu.cmu.deiis.types.Token;
import edu.cmu.zhiyuel.types.AGene;
import edu.cmu.zhiyuel.types.LGene;

public class ScoreAnnotator extends JCasAnnotator_ImplBase {
  private File out = null;

  private BufferedWriter bw = null;

  private HashSet<String> strset;// reader from sample.out

  public ScoreAnnotator() {
    // TODO Auto-generated constructor stub
  }
/**
 * First, add the stanford nlp method's results, and merge the lingpipe's results, and finally abner's results.
 * */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    System.out.println("Annotating Score..."+this.getClass().getName());
    System.out.println("Merging token...");
    FSIterator<Annotation> it = aJCas.getAnnotationIndex(Token.type).iterator();
    while (it.hasNext()) {
      Token t = (Token) it.next();
      // Sentence an = new Sentence(aJCas, len, len+strstext[i].length());//
      FinalGene fg = new FinalGene(aJCas);

      fg.setId(t.getId());
      fg.setGeneStart(t.getGeneStart());
      fg.setGeneEnd(t.getGeneEnd());
      fg.setGeneName(t.getGeneName());
      fg.setConfidence(t.getConfidence());
      fg.setCasProcessorId(t.getCasProcessorId());//
      // System.out.println(fg.getConfidence()+"|"+fg.getGeneName());
      fg.addToIndexes();
    }
    System.out.println("Merging lingpipe...");
    FSIterator<Annotation> itl = aJCas.getAnnotationIndex(LGene.type).iterator();
    FSIterator<Annotation> itf = aJCas.getAnnotationIndex(FinalGene.type).iterator();
    boolean flag = false;
    while (itl.hasNext()) {
      LGene lg = (LGene) itl.next();
      itf = aJCas.getAnnotationIndex(FinalGene.type).iterator();
      // System.out.println(lg.getConfidence()+"|"+lg.getGeneName());

      while (itf.hasNext()) {
        FinalGene fg = (FinalGene) itf.next();
        // System.out.println(fg.getId() + "|" + lg.getId());
        if (lg.getGeneName().equals(fg.getGeneName()) && lg.getId().equals(fg.getId())
                && lg.getGeneStart() == fg.getGeneStart()) {// lg
          fg.setConfidence(fg.getConfidence() + lg.getConfidence());
          fg.setCasProcessorId(this.getClass().getName());
          flag = true;
          /**
           * we may add a confidence twice, if a word occurs more than once in a sentence
           * */
          // fg.addToIndexes();//??need or not
          // System.out.println(fg.getConfidence() + "|" + fg.getGeneName());
        }
      }
      if (flag == false) {
        FinalGene fgn = new FinalGene(aJCas);
        fgn.setId(lg.getId());
        fgn.setGeneStart(lg.getGeneStart());
        fgn.setGeneEnd(lg.getGeneEnd());
        fgn.setGeneName(lg.getGeneName());
        fgn.setConfidence(lg.getConfidence());
        fgn.setCasProcessorId(lg.getCasProcessorId());// change source
        fgn.addToIndexes();
      }
      flag = false;
      // System.out.println("LG:" + fgn.getConfidence() + "|" + fgn.getGeneName());
    }
    flag = false;
    System.out.println("Merging abner...");
    FSIterator<Annotation> ita = aJCas.getAnnotationIndex(AGene.type).iterator();
    while (ita.hasNext()) {
      AGene ag = (AGene) ita.next();
      itf = aJCas.getAnnotationIndex(FinalGene.type).iterator();
      while (itf.hasNext()) {
        FinalGene fg = (FinalGene) itf.next();
        if (ag.getGeneName().equals(fg.getGeneName()) && ag.getId().equals(fg.getId())
                && ag.getGeneStart() == fg.getGeneStart()) {
          fg.setConfidence(fg.getConfidence() + ag.getConfidence());
          fg.setCasProcessorId(this.getClass().getName());
          flag = true;
        }
      }
      if (flag == false) {
        FinalGene fgn = new FinalGene(aJCas);
        fgn.setId(ag.getId());
        fgn.setGeneStart(ag.getGeneStart());
        fgn.setGeneEnd(ag.getGeneEnd());
        fgn.setGeneName(ag.getGeneName());
        fgn.setConfidence(ag.getConfidence());
        fgn.setCasProcessorId(ag.getCasProcessorId());// change its resource
        fgn.addToIndexes();
      }
      flag = false;
      // System.out.println("AG:" + fgn.getConfidence() + "|" + fgn.getGeneName());
    }
    flag=false;
    System.out.println("Finishing ScoreAnnotator...");
  }

  public void writeIntoFile(String geneIdentifier, String geneName, int start, int end, double conf)
          throws Exception {

    String phrase = geneIdentifier + "|" + start + " " + end + "|" + geneName + " [" + conf + "]";
    bw.write(phrase);
    bw.newLine();
    bw.flush();
  }

  public void destory() {
    System.out.println("Destroying ScoreAnnotator...");
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    
  }

}
