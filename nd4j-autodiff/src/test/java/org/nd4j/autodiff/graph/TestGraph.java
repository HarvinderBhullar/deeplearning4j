package org.nd4j.autodiff.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.nd4j.autodiff.graph.api.*;
import org.nd4j.autodiff.graph.graph.Graph;
import org.nd4j.autodiff.graph.iterator.RandomWalkIterator;
import org.nd4j.autodiff.graph.iterator.WeightedRandomWalkIterator;
import org.nd4j.linalg.io.ClassPathResource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


public class TestGraph {

    @Test
    public void testSimpleGraph() {

        Graph<String, String> graph = new Graph<>(false);


        for (int i = 0; i < 10; i++) {
            //Add some undirected edges
            String str = i + "--" + (i + 1) % 10;
            graph.getVertices().add(new Vertex<>(i,str));
            Edge<String> edge = new Edge<>(i, (i + 1) % 10, str, false);
            graph.addEdge(edge);
        }

        assertEquals(10, graph.numVertices());


        for (int i = 0; i < 10; i++) {
            List<Edge<String>> edges = graph.getEdgesOut(i);
            assertEquals(2, edges.size());

            //expect for example 0->1 and 9->0
            Edge<String> first = edges.get(0);
            if (first.getFrom() == i) {
                //undirected edge: i -> i+1 (or 9 -> 0)
                assertEquals(i, first.getFrom());
                assertEquals((i + 1) % 10, first.getTo());
            } else {
                //undirected edge: i-1 -> i (or 9 -> 0)
                assertEquals((i + 10 - 1) % 10, first.getFrom());
                assertEquals(i, first.getTo());
            }

            Edge<String> second = edges.get(1);
            assertNotEquals(first.getFrom(), second.getFrom());
            if (second.getFrom() == i) {
                //undirected edge: i -> i+1 (or 9 -> 0)
                assertEquals(i, second.getFrom());
                assertEquals((i + 1) % 10, second.getTo());
            } else {
                //undirected edge: i-1 -> i (or 9 -> 0)
                assertEquals((i + 10 - 1) % 10, second.getFrom());
                assertEquals(i, second.getTo());
            }
        }
    }


    @Test
    public void testRandomWalkIterator() {
        Graph<String, String> graph = new Graph<>(false);

        for (int i = 0; i < 10; i++) {
            //Add some undirected edges
            String str = i + "--" + (i + 1) % 10;
            graph.getVertices().add(new Vertex<>(i,str));
            Edge<String> edge = new Edge<>(i, (i + 1) % 10, str, false);
            graph.addEdge(edge);
        }

        assertEquals(10, graph.numVertices());

        int walkLength = 4;
        RandomWalkIterator<String> iter =
                        new RandomWalkIterator<>(graph, walkLength, 1235, NoEdgeHandling.EXCEPTION_ON_DISCONNECTED);

        int count = 0;
        Set<Integer> startIdxSet = new HashSet<>();
        while (iter.hasNext()) {
            count++;
            IVertexSequence<String> sequence = iter.next();
            int seqCount = 1;
            int first = sequence.next().vertexID();
            int previous = first;
            while (sequence.hasNext()) {
                //Possible next vertices for this particular graph: (previous+1)%10 or (previous-1+10)%10
                int left = (previous - 1 + 10) % 10;
                int right = (previous + 1) % 10;
                int current = sequence.next().vertexID();
                assertTrue("expected: " + left + " or " + right + ", got " + current,
                                current == left || current == right);
                seqCount++;
                previous = current;
            }
            assertEquals(seqCount, walkLength + 1); //walk of 0 -> 1 element, walk of 2 -> 3 elements etc
            assertFalse(startIdxSet.contains(first)); //Expect to see each node exactly once
            startIdxSet.add(first);
        }
        assertEquals(10, count); //Expect exactly 10 starting nodes
        assertEquals(10, startIdxSet.size());
    }

}
