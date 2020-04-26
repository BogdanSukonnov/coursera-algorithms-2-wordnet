/* *****************************************************************************
 *  Name: Bogdan Sukonnov
 *  Date: 04.24.2020
 *  Description: The WordNet digraph. Your first task is to build the WordNet
 *  digraph: each vertex v is an integer that represents a synset, and each
 *  directed edge v→w represents that w is a hypernym of v. The WordNet digraph
 *  is a rooted DAG: it is acyclic and has one vertex—the root—that is an
 *  ancestor of every other vertex. However, it is not necessarily a tree
 *  because a synset can have more than one hypernym
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<String, LinkedList<Integer>> nounMap = new HashMap<>();

    private final List<String> synsetList = new ArrayList<>();

    private final Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFilename, String hypernymsFilename) {

        if (synsetsFilename == null || hypernymsFilename == null) {
            throw new IllegalArgumentException();
        }

        parseSynsetsFile(synsetsFilename);

        this.digraph = new Digraph(nounMap.size());

        parseHypernymsFile(hypernymsFilename);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNullArgument(word);
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        final SAP sap = new SAP(digraph);
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        final SAP sap = new SAP(digraph);
        final int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
        return synsetList.get(ancestor);
    }

    public static void main(String[] args) {

        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");

        System.out.printf("%s nouns, %s synsets \n",
                          wordnet.nounMap.size(), wordnet.synsetList.size());

        printSynsets(wordnet, "worm");

        printSynsets(wordnet, "bird");

        printDistance("municipality", "region", wordnet);

        printDistance("Brown_Swiss", "barrel_roll", wordnet);
    }

    private static void printDistance(String s1, String s2, WordNet wordnet) {
        System.out.printf("\n%s -> %s = %s \n", s1, s2, wordnet.distance(s1, s2));
    }

    private static void printSynsets(WordNet wordnet, String noun) {
        System.out.printf("\n%s in: \n", noun);
        wordnet.nounMap.get(noun)
                       .forEach(synsetIndex ->
                                        System.out.printf("%s %s \n", synsetIndex,
                                                          wordnet.synsetList.get(synsetIndex)));
    }

    private void checkNullArgument(String noun) {
        if (noun == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNounArgument(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkArguments(String nounA, String nounB) {
        checkNullArgument(nounA);
        checkNullArgument(nounB);
        checkNounArgument(nounA);
        checkNounArgument(nounB);
    }

    private void parseSynsetsFile(String synsetsFilename) {
        final In synsetsIn = new In(synsetsFilename);
        int synsetIndex = 0;
        while (synsetsIn.hasNextLine()) {
            String[] fields = synsetsIn.readLine().split(",");
            if (fields.length == 0) {
                break;
            }
            // first field is index, second is synset
            String synset = fields[1];
            synsetList.add(synset);
            for (String noun : synset.split(" ")) {
                LinkedList<Integer> nounSynsetList = nounMap.get(noun);
                if (nounSynsetList == null) {
                    nounSynsetList = new LinkedList<>();
                    nounMap.put(noun, nounSynsetList);
                }
                nounSynsetList.add(synsetIndex);
            }
            ++synsetIndex;
        }
    }

    private void parseHypernymsFile(String hypernymsFilename) {
        final In hypernymsIn = new In(hypernymsFilename);
        while (hypernymsIn.hasNextLine()) {
            String[] fields = hypernymsIn.readLine().split(",");
            if (fields.length == 0) {
                break;
            }
            // hypernyms starts from the second field
            for (int hypernymIndex = 1; hypernymIndex < fields.length; ++hypernymIndex) {
                digraph.addEdge(Integer.parseInt(fields[0]),
                                Integer.parseInt(fields[hypernymIndex]));
            }
        }
    }
}
