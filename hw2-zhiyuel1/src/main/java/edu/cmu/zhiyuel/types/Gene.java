

/* First created by JCasGen Sun Oct 05 22:32:50 EDT 2014 */
package edu.cmu.zhiyuel.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import edu.cmu.deiis.types.Annotation;


/** 
 * Updated by JCasGen Mon Oct 06 23:33:08 EDT 2014
 * XML source: /Users/zhiyuel/git/hw2-zhiyuel1/hw2-zhiyuel1/src/main/resources/hw2-zhiyuel-aae.xml
 * @generated */
public class Gene extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Gene.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Gene() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Gene(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Gene(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Gene(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.zhiyuel.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.zhiyuel.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: geneName

  /** getter for geneName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGeneName() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneName == null)
      jcasType.jcas.throwFeatMissing("geneName", "edu.cmu.zhiyuel.types.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_geneName);}
    
  /** setter for geneName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneName(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneName == null)
      jcasType.jcas.throwFeatMissing("geneName", "edu.cmu.zhiyuel.types.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_geneName, v);}    
   
    
  //*--------------*
  //* Feature: geneStart

  /** getter for geneStart - gets 
   * @generated
   * @return value of the feature 
   */
  public int getGeneStart() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneStart == null)
      jcasType.jcas.throwFeatMissing("geneStart", "edu.cmu.zhiyuel.types.Gene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Gene_Type)jcasType).casFeatCode_geneStart);}
    
  /** setter for geneStart - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneStart(int v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneStart == null)
      jcasType.jcas.throwFeatMissing("geneStart", "edu.cmu.zhiyuel.types.Gene");
    jcasType.ll_cas.ll_setIntValue(addr, ((Gene_Type)jcasType).casFeatCode_geneStart, v);}    
   
    
  //*--------------*
  //* Feature: geneEnd

  /** getter for geneEnd - gets 
   * @generated
   * @return value of the feature 
   */
  public int getGeneEnd() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneEnd == null)
      jcasType.jcas.throwFeatMissing("geneEnd", "edu.cmu.zhiyuel.types.Gene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Gene_Type)jcasType).casFeatCode_geneEnd);}
    
  /** setter for geneEnd - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneEnd(int v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_geneEnd == null)
      jcasType.jcas.throwFeatMissing("geneEnd", "edu.cmu.zhiyuel.types.Gene");
    jcasType.ll_cas.ll_setIntValue(addr, ((Gene_Type)jcasType).casFeatCode_geneEnd, v);}    
  }

    