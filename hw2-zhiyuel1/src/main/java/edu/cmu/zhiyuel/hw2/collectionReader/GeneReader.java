/**
 * 
 */
package edu.cmu.zhiyuel.hw2.collectionReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

/**
 * <p>
 * Collection reader for gene text reading from original document, hw2.in
 * BufferReader read the text and store them in Cas.
 * </p>
 * @author zhiyuel
 *@param in <code> BufferedReader </code>
 *@param readflag flag if the system continues to read documents.
 *@param cas store the document text and write them into UIMA system.
 */
public class GeneReader extends CollectionReader_ImplBase {
  private BufferedReader in;
//this flag make UIMA read only document and stop after that.
  private int readflag = 1;
  private String cas = "";
  /**
   * 
   */
  public GeneReader() {
    // TODO Auto-generated constructor stub
  }
  /**
   * Get the configuration parameters, and write the document text into cas.
   * The INPUT_FILE is set in the configuration file.
   * */
  public void initialize() throws ResourceInitializationException {
    StringBuffer sb=new StringBuffer();
    String input = (String) getConfigParameterValue("INPUT_FILE");
    System.out.println("INPUT_FILE:" + input);
    System.out.println("Initializing Collection Reader....");
    ///////////////
    try {
      in = new BufferedReader(new FileReader(input));
      String strs=null;
      while((strs=in.readLine())!=null){
        sb.append(strs+"\n");
      }
    } catch (UnsupportedEncodingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    /////////////////////
  
    cas=sb.toString();
  }
  /**
   * update jcas, continuously add document text cas into jcas until finish one document.
   * @param aCAS
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   * */
  /* (non-Javadoc)
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */
  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }
    jcas.setDocumentText(cas);
    readflag = 0;//read only document and stop here
  }
/**
 * @return <code>true</code> the system could continue to read other documents.
 *          <code>false</code> stop reading documents.
 *@see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
 * */
  /* (non-Javadoc)
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
   */
  @Override
  public boolean hasNext() throws IOException, CollectionException {
    // TODO Auto-generated method stub
    if (readflag == 1)
      return true;
    return false;
  }

  /* (non-Javadoc)
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
   */
  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
   */
  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
