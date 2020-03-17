package TweetCleaner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.core.Instances;

public class Weka {



    Instances unlabeled = new Instances(
            new BufferedReader(
                    new FileReader("/")));


 unlabeled.setClassIndex(unlabeled.numAttributes() - 1);


    Instances labeled = new Instances(unlabeled);


 for (int i = 0; i < unlabeled.numInstances(); i++) {
        double clsLabel = tree.classifyInstance(unlabeled.instance(i));
        labeled.instance(i).setClassValue(clsLabel);
    }
    
    BufferedWriter writer = new BufferedWriter(
            new FileWriter("/some/where/labeled.arff"));
 writer.write(labeled.toString());
 writer.newLine();
 writer.flush();
 writer.close();






}
