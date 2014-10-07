/**
 * 
 */
package edu.cmu.zhiyuel.hw2.casConsumer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceProcessException;
import org.xml.sax.SAXException;

import ed.cmu.zhiyuel.types.FinalGene;
import edu.cmu.zhiyuel.types.LGene;

/**
 * <p>
 * Cas consumer for reading the cas in the system, and write them into an output file.
 * The parameters used in initialize() is set in its configuration file  CasConsumerDescriptor.xml.
 * It also reads the sample.out as an gold standard for comparing its performance. 
 * </p>
 * @author zhiyuel
 * @param out <code>File</code>
 * @param bw <code>BufferedWriter</code>
 * @param br <code>BufferedReader</code>
 * @param correct record how many candidates hit in the gold standard file.
 * @param geneNamesize record how many candidates this system has recognized.
 * @param strset an HashSet to store the gold standards.
 */
public class GeneConsumer extends CasConsumer_ImplBase {
  private File out = null;

  private BufferedWriter bw = null;

  private int correct = 0;

  private int geneNamesize = 0;

  private BufferedReader br = null;

  private HashSet<String> strset;// reader from sample.out

  /**
   * 
   */
  public GeneConsumer() {
    // TODO Auto-generated constructor stub
  }
/**
 * Read sample file and output file from configuration file, and finish the initialization process.
 * */
  public void initialize() {
    String samplefile = (String) getConfigParameterValue("SAMPLE_FILE");
    strset = new HashSet<String>();
    try {
      br = new BufferedReader(new FileReader(samplefile));
      String str = null;
      while ((str = br.readLine()) != null) {
        strset.add(str);
        // System.out.println(str);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      System.out.println("OUTPUT_FILE:" + (String) getConfigParameterValue("OUTPUT_FILE"));
      out = new File((String) getConfigParameterValue("OUTPUT_FILE"));

      bw = new BufferedWriter(new FileWriter(out));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
/**
 * <p>
 * Read finalGene from the cas system, and see how many candidates could hit in the sample.out.
 * The finalGene has three types of casProcessId, recording where it comes from.
 * We set a threshold here that we consider those coming from at least two annotators as gene names.
 * In this case, we extract those whose casProcessId is ScoreAnnotator.
 * Finally, we write those finalGene into the output file(hw2-zhiyuel.out).
 * </p>
 * @param aCAS unstructured data type in UIMA
 *  @see org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org.apache.uima.cas.CAS)
 * */
  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org.apache.uima.cas.CAS)
   */
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // retrieve the filename of the input file from the CAS
    FSIterator<Annotation> it = jcas.getAnnotationIndex(FinalGene.type).iterator();
    System.out.println("Consuming CAS....");
    String geneIdentifier = "";
    String geneName = "";
    int start = 0, end = 0;
    double conf = 0;
    String cas = "";
    while (it.hasNext()) {
      FinalGene annotation = (FinalGene) it.next();
      geneIdentifier = annotation.getId();
      geneName = annotation.getGeneName();
      start = annotation.getGeneStart();
      end = annotation.getGeneEnd();
      conf = annotation.getConfidence();
      cas = annotation.getCasProcessorId();
      boolean flag = false;
      // threshold is set here
      if (cas.equals("edu.cmu.zhiyuel.hw2.annotators.TokenAnnotator")) {
        //flag=true;
      } else if (cas.equals("edu.cmu.zhiyuel.hw2.annotators.AbnerAnnotator")) {
        //flag=true;
      } else if (cas.equals("edu.cmu.zhiyuel.hw2.annotators.LingPipeModelAnnotator")) {
        //flag=true;
      } else if (cas.equals("edu.cmu.zhiyuel.hw2.annotators.ScoreAnnotator")&&conf>=1.0) {
        //at least combine two
        flag = true;
      }
      if (flag) {
        geneNamesize++;// /////
        // write to output file
        try {
          writeIntoFile(geneIdentifier, geneName, start, end);
        } catch (IOException e) {
          throw new ResourceProcessException(e);
        } catch (SAXException e) {
          throw new ResourceProcessException(e);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

  }
/**
 * <p>
 * Execute after all process finish their jobs.
 * Compute the recall, precision and f-score by using the data from processCas(CAS aCAS)
 * @see org.apache.uima.collection.base_cpm.CasObjectProcessor#destory()
 * </p>
 * */
  public void destroy() {

    try {
      if (bw != null) {
        double recall = (double) correct / strset.size();
        double precision = (double) correct / geneNamesize;
        double fscore = 2 * (recall * precision) / (recall + precision);
        System.out.println("########correct###########" + correct);
        System.out.println("#####recall#########" + recall);
        System.out.println("#####precision#########" + precision);
        System.out.println("#####f-score#########" + fscore);
        System.out.println("#####geneName count#########" + geneNamesize);
        bw.close();
        bw = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
/**
 * <p>
 * write the gene name information into the file with a specific format.
 * </p>
 * @param geneIdentifier tags of geneId
 * @param geneName 
 * @param start the start point of gene name in the sentence skipping white spaces before itself.
 * @param end the end position of gene name in the sentence skipping white spaces before itself.
 * */
  public void writeIntoFile(String geneIdentifier, String geneName, int start, int end)
          throws Exception {

    String phrase = geneIdentifier + "|" + start + " " + end + "|" + geneName;
    if (strset.contains(phrase)) {
      correct++;
    }
    bw.write(phrase);
    bw.newLine();
    bw.flush();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
