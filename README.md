# Object Oriented Programming - Assignment 2
Nir Sasson and Ofri Tavor's repository for the second assignment of the course Object Oriented Programming.

## Overview

### The interfaces

### The classes

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