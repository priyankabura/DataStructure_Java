Following are the java files included-

BinaryHeap.java - Code for Binary Heap implementation

Driver.java - Driver code for MST and Shortest paths

Edge.java - Class that represents an edge of a Graph

Graph.java - Class to represent a graph

Index.java - Index interface

IndexedHeap.java - Code for Indexed heaps

MST.java - Code for Prim's MST algorithm

PQ.java - PriorityQueue interface

ShortestPath.java - Code for Dijkstra's Shortest path algorithm

Timer.java - class for roughly calculating running time of programs

Vertex.java - Class to represent a vertex of a graph

————————————————————————————————————

Steps to Run Driver.java:

Compile all the above files

Compilation - javac Driver.java
Execution - java Driver input_filename VERBOSE

Eg- java Driver g1.txt 5

input_filename(eg: g4-big.txt/g3.txt)

VERBOSE - 3 (PrimMST - priority queue of edges using Java’s PriorityQueue)

VERBOSE - 5 (DijkstraShortestPaths - priority queue of vertices)

VERBOSE - 7 (PrimMST2 - priority queue of vertices using IndexedHeap)

————————————————————————————————————
