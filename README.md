# Object Oriented Programming - Assignment 2
Nir Sasson and Ofri Tavor's repository for the second assignment of the course Object Oriented Programming.

## Overview
Graph is probably one of the most useful data structures. There are many intersting algorithm that used on graph, such as Shortest path algorithms, Minimal spanning tree algorithms, Traveling salesman problem and many more.
In this Assigment we've been tasked with create a Directed Weightted Graph, create graph algorithms to find the center node, the best path for TSP, the shortest path and few more algorithm.

## Modeling the graph
We start to model the graph by reading the interfaces in order to understand what we need to do and if we need to handle time complexity in some function, which we need to adapt our model so it will handle the needed time complexities.

We decided to keep the basic Object: Node, Edge and Point3D as simple as possible and let the DWGraph take care of everything.

### Point3D
Point3D object implements the GeoLocation interface.
It represents the location of a vertex in 3D dimension.
Each Point3D object contain 3 double parameters (x,y,z) which we can use in order to set the position of a vertex, or the starting point or ending point of an edge

### Node
Node Object implements the NodeData interface.
It represents a vertex in the graph
Each node have it's unique id and a Point3D object which represent it's location

### Edge
Edge object implements EdgeData interface.
It represents an edge in directed weighted graph.
Each Edge object have it's source node id, destenation node id and Since the graph is weighted graph, each node have it's own weight.


### DWGraph
DWGraph object implements DirectedWeightedGraph interface.
It represents an Directed Weighted Graph.
This object have 2 HashMaps that used to contain the Nodes and the Edges, a counters for the number of nodes and edges, and mode counter which count the number of times we changed the graph in terms of adding or removing nodes and edges.

### DWGraphAlgorithms
DWGraphAlgorithms object implements DirectedWeightedGraphAlgorithms interface.
This object handle the algorithm we need to run on our DWGraph.
This object contain a DirectedWeightedGraph object, which we use our DwGraph in order to implement it.
With this class we run the following methods on the graph:
1. init(DirectedWeightedGraph graph): We assign the input as the current graph.
2. copy(): We return a copy of the current DirectedWeightedGraph.
3. getGraph(): We return the current DirectedWeightedGraph.
4.  

## Data Structures

### Directed Weighted Graph

There are a lot of ways to represent a graph such as adjacency list, adjacency matrix and incidence matrix.

|                                                                                  | Adjacency list                                                                    | Adjacency matrix                                                      | Incidence matrix                                                                |
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------------------|
| Store graph                                                                      | ![][EQ1]                                                                          | ![][EQ5]                                                              | ![][EQ6]                                                                        |
| Add vertex                                                                       | ![][EQ2]                                                                          | ![][EQ5]                                                              | ![][EQ6]                                                                        |
| Add edge                                                                         | ![][EQ2]                                                                          | ![][EQ2]                                                              | ![][EQ6]                                                                        |
| Remove vertex                                                                    | ![][EQ3]                                                                          | ![][EQ5]                                                              | ![][EQ6]                                                                        |
| Remove edge                                                                      | ![][EQ4]                                                                          | ![][EQ4]                                                              | ![][EQ6]                                                                        |
| Are vertices x and y adjacent (assuming that their storage positions are known)? | ![][EQ4]                                                                          | ![][EQ4]                                                              | ![][EQ3]                                                                        |
| Remarks                                                                          | Slow to remove vertices and edges, because it needs to find all vertices or edges | Slow to add or remove vertices, because matrix must be resized/copied | Slow to add or remove vertices and edges, because matrix must be resized/copied |

[Source](https://en.wikipedia.org/wiki/Graph_(abstract_data_type)#Common_Data_Structures_for_Graph_Representation)

[EQ1]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C&plus;%7CE%7C%29
[EQ2]: https://latex.codecogs.com/svg.latex?O%281%29
[EQ3]: https://latex.codecogs.com/svg.latex?O%28%7CE%7C%29
[EQ4]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%29
[EQ5]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%5E2%29
[EQ6]: https://latex.codecogs.com/svg.latex?O%28%7CV%7C%5Ccdot%20%7CE%7C%29
