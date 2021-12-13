# Object-Oriented Programming - Assignment 2

Nir Sasson and Ofri Tavor's repository for the second assignment of the course Object-Oriented Programming.

## Getting Started

### Installation

Follow the steps in order to clone the project and install the required dependencies:

1. Clone the repo

   ```sh
   git clone https://github.com/SassonNir/OOP-Ex2.git
   ```

2. This project uses and requires the [Google's serialization/deserialization library for JSON in Java called gson](https://github.com/google/gson):

    Gradle:

    ```gradle
    dependencies {
        implementation 'com.google.code.gson:gson:2.8.9'
    }
    ```

    Maven:

    ```xml
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.9</version>
    </dependency>
    ```

    [Gson jar downloads](https://maven-badges.herokuapp.com/maven-central/com.google.code.gson/gson) are available from Maven Central.

### Usage

In order to run the graphical user interface run the following command into the teminal:

```bash
java -jar ./path/to/jar/Ex2.jar ./path/to/json/Graph.json
```

Which will open and load the specified json into the GUI.

## Overview

### The Task

We are given an [API](https://github.com/SassonNir/OOP-Ex2/tree/main/src/api) (Application Programing Interface),
defined using `Java` interfaces, and we were tasked to implement all the interfaces using the documentation specified in the Javadoc.

The API requires us to build new data structures which overall help us build a data structure which represents a directed weighted graph (weighted digraph).

After we are done implementing the data structures we are tasked to create a graphical interface to visualize the algorithms
specified in the [`DirectedWeightedGraphAlgorithms.java`](https://github.com/SassonNir/OOP-Ex2/blob/main/src/api/DirectedWeightedGraphAlgorithms.java).

### Assumptions and Restrictions

We are given that the directed weighted graph needs to be able to load in a (reasonable amount of time)
large graphs such as 100 - 1,000 - 10,000 - 100,000 - 1,000,000 nodes and up to 20 edges, 10 outgoing and 10 ingoing.

As well as the assumption above we are given in some methods defined in the interfaces a recommended
algorithm complexity such as ![O\(1\)][EQ2] for addition of nodes/edges and ![O\(k\)][EQ7] for the removal of a node (with all of it's connected edges).

## Designing the system

### Time complexity

In order to meet the time complexity of some operations it is clear that we need to use some
variation of a hash-table for storing both the nodes and the edges whilst taking into
consideration to not make any copies of the objects and working on the pointers to the objects.

Since the graph is directed we are faced with the problem of uniquely identifying the edges since
the edge from `A` to `B` is different from the edge from `B` to `A` and doing so efficiently.

### Space complexity

Since the graph needs to allow graphs as big as 1,000,000 we cannot use the traditional methods of storing the edges and the nodes,
and we cannot use any recursion since at the scale of the larger graphs most computers will get `OutOfMemoryError: Java heap space`
since the recursion stack is too large for java to handle.

This restriction makes all the algorithms/implementations iterative.

## The Implementations

### The Node

The node is a simple data structure identified by a single key (an integer), but we can use the structure to hold some
additional data for use in some algorithms and the graphical user interface such as position, colour, and meta-data.

### The Edge

The edge which specifies a connection from a source node to a destination node
whilst also containing a weight which can represent the distance between the source and the destination.
In order to make the data structure minimal both the source and the destination nodes are integers (representing the id of each node).

### The Graph

There are a lot of ways to represent a graph such as adjacency list, adjacency matrix and incidence matrix.

* Adjacency list - Vertices are stored as records or objects, and every vertex stores a list of adjacent vertices. This data structure allows the storage of additional data on the vertices. Additional data can be stored if edges are also stored as objects, in which case each vertex stores its incident edges and each edge stores its incident vertices.
* Adjacency matrix - A two-dimensional matrix, in which the rows represent source vertices and columns represent destination vertices. Data on edges and vertices must be stored externally. Only the cost for one edge can be stored between each pair of vertices.
* Incidence matrix -  A two-dimensional matrix, in which the rows represent the vertices and columns represent the edges. The entries indicate the incidence relation between the vertex at a row and edge at a column.

|                                                                                  | Adjacency list                                                                    | Adjacency matrix                                                      | Incidence matrix                                                                |
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------------------|
| Store graph                                                                      | ![O\(\|V\| + \|E\|\)][EQ1]                                                                          | ![O\(\|V\|^2\)][EQ5]                                                              | ![O\(\|V\|*\|E\|\)][EQ6]                                                                        |
| Add vertex                                                                       | ![O\(1\)][EQ2]                                                                          | ![O\(\|V\|^2\)][EQ5]                                                              | ![O\(\|V\|*\|E\|\)][EQ6]                                                                        |
| Add edge                                                                         | ![O\(1\)][EQ2]                                                                          | ![O\(1\)][EQ2]                                                              | ![O\(\|V\|*\|E\|\)][EQ6]                                                                        |
| Remove vertex                                                                    | ![O\(\|E\|\)][EQ3]                                                                          | ![O\(\|V\|^2\)][EQ5]                                                              | ![O\(\|V\|*\|E\|\)][EQ6]                                                                        |
| Remove edge                                                                      | ![O\(\|V\|\)][EQ4]                                                                          | ![O\(1\)][EQ2]                                                              | ![O(\|V\|*\|E\|\)][EQ6]                                                                        |
| Are vertices x and y adjacent (assuming that their storage positions are known)? | ![O\(\|V\|\)][EQ4]                                                                          | ![O\(1\)][EQ2]                                                              | ![O\(\|E\|\)][EQ3]                                                                        |
| Remarks                                                                          | Slow to remove vertices and edges, because it needs to find all vertices or edges | Slow to add or remove vertices, because matrix must be resized/copied | Slow to add or remove vertices and edges, because matrix must be resized/copied |

[Source](https://en.wikipedia.org/wiki/Graph_(abstract_data_type)#Common_Data_Structures_for_Graph_Representation)

For our implementation, in order to deal with the constraints we decide to model the graph using hash tables.
The graph structure holds two hash tables, one for the nodes and one for the edges.

The nodes hash table maps from the id (integer) to its data (node structure), i.e., a map from integer to node.

The edges hash table is used to get all outgoing connections made from a source node, to achieve this whilst also achieving the specified complexities.
We used a hash table which maps from the source node's id (integer) into a hash table which maps from the destination node's id (integer) into the data (edge structure), i.e., a map from integer into another map which maps an integer to an edge.

Consider the following example:

```{1 -> {2 -> e, 3 -> e}, 4 -> {2 -> e, 5 -> e}}```

From the map we can see the graph contains the following edges:

* from 1 to 2
* from 1 to 3
* from 4 to 2
* from 4 to 5

## DWGraphAlgorithms

DWGraphAlgorithms object implements DirectedWeightedGraphAlgorithms interface.
This object handle the algorithm we need to run on our DWGraph.
This object contains a DirectedWeightedGraph object, which we use our DwGraph in order to implement it.
With this class we run the following methods on the graph:

1. init(DirectedWeightedGraph graph): We assign the input as the current graph.
2. copy(): We return a copy of the current DirectedWeightedGraph.
3. getGraph(): We return the current DirectedWeightedGraph.

As well as the following algorithms:

1. isConnected(): Return true if the graph is strongly connected, otherwise it will return false.
2. shortestPathDist(int src, int dest): Return the distance of the shortest path from source vertex to destination vertex
3. shortestPath(int src, int dest): Return a list of NodeData which represent the nodes throughout the way in the shortest path from source vertex to destination vertex.
4. center(): Return the NodeData which it's maximal shortest path is the minimal among all the vertices.
5. tsp(List\<NodeData\>): return list of node which represent the optimal path that include all the NodeData in the input.

[EQ1]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C&plus;%7CE%7C%29
[EQ2]: https://latex.codecogs.com/svg.latex?O%281%29
[EQ3]: https://latex.codecogs.com/svg.latex?O%28%7CE%7C%29
[EQ4]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%29
[EQ5]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%5E2%29
[EQ6]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%5Ccdot%20%7CE%7C%29
[EQ7]: https://latex.codecogs.com/svg.latex?O%28k%29
