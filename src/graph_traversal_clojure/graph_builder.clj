(ns graph-traversal-clojure.graph-builder
  (:require [graph-traversal-clojure.graph :as graph]))

(defprotocol IGraphBuilder
  "Protocol for building graphs in a step-by-step manner"
  (add-vertices [this count] "Add a number of vertices to the graph")
  (ensure-connectivity [this] "Ensure the graph is connected")
  (add-remaining-edges [this total-edges] "Add additional edges to reach desired total")
  (build [this] "Build and return the final graph"))

;; Default implementation of the graph builder
(defrecord GraphBuilderImpl [current-graph vertices-list]
  IGraphBuilder
  (add-vertices [this count]
    (let [vertex-values (range 1 (inc count))
          new-graph (reduce graph/add-vertex current-graph vertex-values)]
      (assoc this
        :current-graph new-graph
        :vertices-list (vec vertex-values))))

  (ensure-connectivity [this]
    (if (< (count vertices-list) 2)
      this
      (let [random-start (rand-nth vertices-list)
            other-vertices (filter #(not= % random-start) vertices-list)]
        (loop [g current-graph
               connected #{random-start}
               unconnected (set other-vertices)]
          (if (empty? unconnected)
            (assoc this :current-graph g)
            (let [source (rand-nth (vec connected))
                  target (rand-nth (vec unconnected))
                  weight (inc (rand-int 9))]
              (recur (graph/add-edge g source target weight)
                     (conj connected target)
                     (disj unconnected target))))))))

  (add-remaining-edges [this total-edges]
    (let [current-edges (reduce + (map count (vals current-graph)))
          edges-to-add (- total-edges current-edges)]
      (if (<= edges-to-add 0)
        this
        (loop [g current-graph
               remaining edges-to-add]
          (if (<= remaining 0)
            (assoc this :current-graph g)
            (let [source (rand-nth vertices-list)
                  target (rand-nth vertices-list)
                  weight (inc (rand-int 9))]
              (if (or (= source target)
                      (get-in g [source target]))
                (recur g remaining)
                (recur (graph/add-edge g source target weight) (dec remaining)))))))))

  (build [this]
    current-graph))

(defn create-builder
  "Create a new graph builder instance"
  []
  (->GraphBuilderImpl (graph/create-graph) []))