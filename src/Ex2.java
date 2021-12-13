import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import datastructures.DWGraph;
import datastructures.DWGraphAlgorithms;
import gui.Graph;

import java.util.Arrays;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        return Ex2.getGrapgAlgo(json_file).getGraph();
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms alg = new DWGraphAlgorithms();
        alg.load(json_file);
        return alg;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = Ex2.getGrapgAlgo(json_file);
        Graph gui = new Graph(alg);
    }
    
    public static void main(String[] args) {
        try {
            Ex2.runGUI(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}