/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BTree;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        int maxDistance = -1;

        String outcast = null;

        BTree<String, BTree<String, Integer>> distances = new BTree<>();

        for (String noun1 : nouns) {
            int dustanceSum = 0;
            for (String noun2 : nouns) {
                if (noun1.equals(noun2)) {
                    continue;
                }
                BTree<String, Integer> noun1Tree = getNounTree(noun1, distances);
                Integer distance = noun1Tree.get(noun2);
                if (distance == null) {
                    distance = wordNet.distance(noun1, noun2);
                    noun1Tree.put(noun2, distance);
                    BTree<String, Integer> noun2Tree = getNounTree(noun2, distances);
                    noun2Tree.put(noun1, distance);
                }
                dustanceSum += distance;
            }
            if (dustanceSum > maxDistance) {
                maxDistance = dustanceSum;
                outcast = noun1;
            }
        }

        return outcast;
    }

    private BTree<String, Integer> getNounTree(String noun,
                                               BTree<String, BTree<String, Integer>> distances) {
        BTree<String, Integer> nounTree = distances.get(noun);
        if (nounTree == null) {
            nounTree = new BTree<>();
            distances.put(noun, nounTree);
        }
        return nounTree;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
