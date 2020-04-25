/* *****************************************************************************
 *  Name: Bogdan Sukonnov
 *  Date: 04.25.2020
 *  Description: Shortest ancestral path. An ancestral path between two vertices
 *  v and w in a digraph is a directed path from v to a common ancestor x,
 *  together with a directed path from w to the same ancestor x. A shortest
 *  ancestral path is an ancestral path of minimum total length. We refer to
 *  the common ancestor in a shortest ancestral path as a shortest common
 *  ancestor. Note also that an ancestral path is a path, but not a directed path.
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph digraph) {
        if (digraph == null) {
            throw new IllegalArgumentException();
        }
        this.digraph = digraph;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkArguments(v, w);
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkArguments(v, w);
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArguments(v, w);
        return getShortestPath(v, w).getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArguments(v, w);
        return getShortestPath(v, w).getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private static class ShortestPath {
        private int length = -1;
        private int ancestor = -1;

        private int getLength() {
            return length;
        }

        private void setLength(int length) {
            this.length = length;
        }

        private int getAncestor() {
            return ancestor;
        }

        private void setAncestor(int ancestor) {
            this.ancestor = ancestor;
        }
    }

    private ShortestPath getShortestPath(Iterable<Integer> v, Iterable<Integer> w) {
        ShortestPath shortestPath = new ShortestPath();
        int stepCounter = 0;
        List<Integer> vNextStepNodeList = new LinkedList<>();
        List<Integer> wNextStepNodeList = new LinkedList<>();
        // Map<ancestor, step>
        Map<Integer, Integer> vAncestors = new HashMap<>();
        for (Integer currV : v) {
            vAncestors.put(currV, stepCounter);
            vNextStepNodeList.add(currV);
        }
        Map<Integer, Integer> wAncestors = new HashMap<>();
        for (Integer currW : w) {
            wAncestors.put(currW, stepCounter);
            wNextStepNodeList.add(currW);
        }
        while (shortestPath.ancestor == -1
                && (vNextStepNodeList.size() > 0 || wNextStepNodeList.size() > 0)) {
            ++stepCounter;
            vNextStepNodeList = doStep(vNextStepNodeList, vAncestors, wAncestors, shortestPath,
                                       stepCounter);
            wNextStepNodeList = doStep(wNextStepNodeList, wAncestors, vAncestors, shortestPath,
                                       stepCounter);
        }
        return shortestPath;
    }

    private List<Integer> doStep(List<Integer> nodeList, Map<Integer, Integer> ancestors,
                                 Map<Integer, Integer> otherAncestors, ShortestPath shortestPath,
                                 int stepNumber) {
        List<Integer> nextStepNodeList = new LinkedList<>();
        for (Integer currV : nodeList) {
            for (int ancestor : digraph.adj(currV)) {
                if (otherAncestors.containsKey(ancestor) && (shortestPath.length == -1
                        || otherAncestors.get(ancestor) + stepNumber < shortestPath.length)) {
                    shortestPath.setLength(otherAncestors.get(ancestor) + stepNumber);
                    shortestPath.setAncestor(ancestor);
                }
                ancestors.put(ancestor, stepNumber);
                nextStepNodeList.add(ancestor);
            }
        }
        return nextStepNodeList;
    }

    private void checkArguments(int v, int w) {
        checkArgument(v);
        checkArgument(w);
    }

    private boolean checkArgument(int arg) {
        if (arg < 0 || arg >= digraph.V()) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    private void checkArguments(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        checkArgument(v);
        checkArgument(w);
    }

    private void checkArgument(Iterable<Integer> arg) {
        arg.forEach(currArg -> {
            if (currArg == null || !checkArgument(currArg)) {
                throw new IllegalArgumentException();
            }
        });
    }
}
