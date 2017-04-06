package org.nd4j.autodiff.graph.api;

import org.nd4j.autodiff.graph.exception.NoEdgesException;

import java.util.List;
import java.util.Random;
import org.nd4j.autodiff.graph.exception.NoEdgesException;

import java.util.List;
import java.util.Random;

/** Interface for a IGraph, with objects for each vertex and edge.
 * In the simplest case, edges and vertices may be labelled (i.e., IGraph<String,String> for example), or may be
 * any arbitrary object (or, null).<br>
 * IGraph may include directed edges, undirected edges, or a combination of both<br>
 * Note: Every vertex in the graph has an integer index, in range of 0 to numVertices() inclusive<br>
 * @param <V> type for vertex objects
 * @param <E> type for edge objects
 * @author Alex Black
 */
public interface IGraph<V, E> {

    /** Number of vertices in the graph */
    public int numVertices();

    /**Get a vertex in the graph for a given index
     * @param idx integer index of the vertex to get. must be in range 0 to numVertices()
     * @return vertex
     */
    Vertex<V> getVertex(int idx);

    /** Get multiple vertices in the graph
     * @param indexes the indexes of the vertices to retrieve
     * @return list of vertices
     */
    public List<Vertex<V>> getVertices(int[] indexes);

    /** Get multiple vertices in the graph, with secified indices
     * @param from first vertex to get, inclusive
     * @param to last vertex to get, inclusive
     * @return list of vertices
     */
    List<Vertex<V>> getVertices(int from, int to);

    /** Add an edge to the graph.
     */
    void addEdge(Edge<E> edge);

    /** Convenience method for adding an edge (directed or undirected) to graph */
    void addEdge(int from, int to, E value, boolean directed);

    /** Returns a list of edges for a vertex with a given index
     * For undirected graphs, returns all edges incident on the vertex
     * For directed graphs, only returns outward directed edges
     * @param vertex index of the vertex to
     * @return list of edges for this vertex
     */
    List<Edge<E>> getEdgesOut(int vertex);

    /** Returns the degree of the vertex.<br>
     * For undirected graphs, this is just the degree.<br>
     * For directed graphs, this returns the outdegree
     * @param vertex vertex to get degree for
     * @return vertex degree
     */
    int getVertexDegree(int vertex);

    /** Randomly sample a vertex connected to a given vertex. Sampling is done uniformly at random.
     * Specifically, returns a random X such that either a directed edge (vertex -> X) exists,
     * or an undirected edge (vertex -- X) exists<br>
     * Can be used for example to implement a random walk on the graph (specifically: a unweighted random walk)
     * @param vertex vertex to randomly sample from
     * @param rng Random number generator to use
     * @return A vertex connected to the specified vertex,
     * @throws NoEdgesException thrown if the specified vertex has no edges, or no outgoing edges (in the case
     * of a directed graph).
     */
    Vertex<V> getRandomConnectedVertex(int vertex, Random rng) throws NoEdgesException;

    /**Get a list of all of the vertices that the specified vertex is connected to<br>
     * Specifically, for undirected graphs return list of all X such that (vertex -- X) exists<br>
     * For directed graphs, return list of all X such that (vertex -> X) exists
     * @param vertex Index of the vertex
     * @return list of vertices that the specified vertex is connected to
     */
    List<Vertex<V>> getConnectedVertices(int vertex);

    /**Return an array of indexes of vertices that the specified vertex is connected to.<br>
     * Specifically, for undirected graphs return int[] of all X.vertexID() such that (vertex -- X) exists<br>
     * For directed graphs, return int[] of all X.vertexID() such that (vertex -> X) exists
     * @param vertex index of the vertex
     * @return list of vertices that the specified vertex is connected to
     * @see #getConnectedVertices(int)
     */
    int[] getConnectedVertexIndices(int vertex);
}
