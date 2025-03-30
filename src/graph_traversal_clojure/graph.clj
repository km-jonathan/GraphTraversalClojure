(ns graph-traversal-clojure.graph
  (:require [clojure.string :as str]))

;; Task 1 - Extend the graph definition to include a weight between graph edges
;; Graph format: {vertex-id {destination weight, ...}, ...}
(defn create-graph
  "Creates an empty graph"
  []
  {})

(defn add-vertex
  "Add a vertex to the graph if it doesn't exist"
  [graph vertex]
  (if (contains? graph vertex)
    graph
    (assoc graph vertex {})))

(defn add-edge
  "Add a weighted edge from source to destination"
  [graph source destination weight]
  (let [graph (-> graph
                  (add-vertex source)
                  (add-vertex destination))]
    (assoc-in graph [source destination] weight)))

(defn get-random-vertex
  "Get a random vertex from the graph, optionally excluding a specific value"
  [graph exclude-value]
  (let [vertices (keys graph)
        filtered-vertices (if exclude-value
                            (filter #(not= % exclude-value) vertices)
                            vertices)]
    (when (seq filtered-vertices)
      (rand-nth (vec filtered-vertices)))))

(defn graph->str
  "Convert graph to a readable string representation"
  [graph]
  (str "{\n"
       (str/join "\n"
                 (for [[vertex edges] (sort graph)]
                   (str "  " vertex " ["
                        (str/join ", "
                                  (for [[dest weight] (sort edges)]
                                    (str "(" dest " " weight ")")))
                        "]")))
       "\n}"))

;; Task 2 - Randomly generate a simple directed graph
(defn generate-random-graph
  "Generate a random directed graph with n vertices and s edges"
  [n s]
  (when (or (<= n 0)
            (< s (dec n))
            (> s (* n (dec n))))
    (throw (IllegalArgumentException.
             (str "Sparseness must be between " (dec n) " and " (* n (dec n))
                  " for a graph with " n " vertices"))))

  (let [vertex-values (range 1 (inc n))
        random-start (rand-nth vertex-values)
        weight-range 10]

    ;; Build a connected graph first (spanning tree)
    (loop [graph (reduce add-vertex (create-graph) vertex-values)
           connected-vertices #{random-start}
           unconnected-vertices (set (filter #(not= % random-start) vertex-values))]
      (if (empty? unconnected-vertices)
        ;; Add remaining random edges to reach sparseness
        (let [existing-edges (count (for [[v edges] graph
                                          [dest _] edges]
                                      [v dest]))
              edges-to-add (- s (dec n))]
          (loop [g graph
                 remaining edges-to-add]
            (if (<= remaining 0)
              g
              (let [source (rand-nth vertex-values)
                    target (rand-nth vertex-values)
                    weight (inc (rand-int (dec weight-range)))]
                (if (or (= source target)
                        (get-in g [source target]))
                  (recur g remaining)
                  (recur (add-edge g source target weight) (dec remaining)))))))

        ;; Continue building the spanning tree
        (let [source (rand-nth (vec connected-vertices))
              target (rand-nth (vec unconnected-vertices))
              weight (inc (rand-int (dec weight-range)))]
          (recur (add-edge graph source target weight)
                 (conj connected-vertices target)
                 (disj unconnected-vertices target)))))))