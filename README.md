# Graph Traversal (Clojure)

A Clojure implementation for graph simple calculations

## Description

1. Create a randomly generated a simple directed graph
2. Calculate the shortest path between 2 randomly selected vertices with Dijkstra's algorithm
3. Calculate distance properties of the graph (radius and diameter)
4. Calculate the eccentricity of a randomly selected vertex

## Getting Started

### Executing program

* How to run the program

There are two options to generate a graph

Option 1 - pass in two values as arguments

    (N - size of generated graph)
    
    (S - sparseness (number of directed edges actually; from N-1 (inclusive) to N(N-1) (inclusive))) 

  clj -M -m graph-traversal-clojure.core \<N\> \<S\> (eg. clj -M -m graph-traversal-clojure.core 5 8)
  
Option 2 - simply do "clj -M -m graph-traversal-clojure.core" [if there are less than 2 arguments being put in, the terminal will ask for the user input]
  
  clj -M -m graph-traversal-clojure.core

<img width="401" alt="image" src="https://github.com/user-attachments/assets/004304f9-bef7-4b8a-85c5-1711dff1e7ac" />

Once a graph is successfully created, all values will be calculated and displayed automatically 

<img width="523" alt="image" src="https://github.com/user-attachments/assets/0ebce4cb-7817-43ba-b0fd-a0f696c66ee5" />

## Remarks

This project is converted from a c# / .net implementation with the help of some AI tools.

The c# / .net implementation can be found here: https://github.com/km-jonathan/GraphTraversal

## Authors

Jonathan Leung  

## Version History

1.0 - Initial Release
