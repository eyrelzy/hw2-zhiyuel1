package util;
import java.io.IOException;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
/**
 * A test class for stanford ner approach.
 * To test how ner-model.ser.gz model performs on the input file.
 * */
public class NERDemo {

    public static void main(String[] args) throws IOException {    	
    	String serializedClassifier = "src/main/resources/dataset/ner-model.ser.gz";
    	AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

        String s1 = "Takayasu's disease: association with HLA-B5.";
        //Takayasu_TAG 's_TAG disease_TAG :_TAG association_TAG with_TAG HLA_GENE1 -_GENE1 B5_GENE1 ._TAG
        System.out.println(classifier.classifyWithInlineXML(s1));
        //当hello Protein单独出现的时候才认为是基因
        String s2 = "Studies on immunoglobulin E: the impact of a sojourn with Professor Dan H.";
        System.out.println(classifier.classifyToString(s2));
//        
        String s3 = "an Peroxydase reaction stains were negative, chloroacetate esterase were strongly positive.";
        System.out.println(classifier.classifyToString(s3));
        System.out.println(classifier.classifyWithInlineXML(s3));
//        
//        String s4 = "DNA RNA Protein ASB";
//        System.out.println(classifier.classifyToString(s4));
//        System.out.println(classifier.classifyToString("Today is 11/20 alalala not Monday"));
        //轻微改动一点点就会有很大的改变 如第二个is 添加上后11、20 就识别不出来了
        //发现根据两者间的距离而决定是否识别，比如多于not三个字母以上 就识别出来两个
        
    }
}
