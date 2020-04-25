/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class WordNet {

    private final List<String> synsetList = new ArrayList<>();

    private final Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFilename, String hypernymsFilename) {

        final In synsetsIn = new In(synsetsFilename);
        while (synsetsIn.hasNextLine()) {
            String[] fields = synsetsIn.readLine().split(",");
            if (fields.length == 0) {
                break;
            }
            this.synsetList.add(fields[1]);
        }

        this.digraph = new Digraph(this.synsetList.size());
        final In hypernymsIn = new In(hypernymsFilename);
        while (hypernymsIn.hasNextLine()) {
            String[] fields = hypernymsIn.readLine().split(",");
            if (fields.length == 0) {
                break;
            }
            this.synsetList.add(fields[1]);
            for (int hypernymIndex = 1; hypernymIndex < fields.length; ++hypernymIndex) {
                digraph.addEdge(Integer.parseInt(fields[0]),
                                Integer.parseInt(fields[hypernymIndex]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {

        return null;

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return "";
    }

    public static void main(String[] args) {

    }
}
