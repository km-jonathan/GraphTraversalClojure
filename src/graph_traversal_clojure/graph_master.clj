(ns graph-traversal-clojure.graph-master
  (:require [graph-traversal-clojure.graph :as graph]
            [graph-traversal-clojure.random-graph-builder :as random-builder]))

(defn- validate-graph-parameters
  [n s]
  (when (<= n 0)
    (throw (IllegalArgumentException. "Number of vertices must be positive")))

  (when (or (< s (dec n)) (> s (* n (dec n))))
    (throw (IllegalArgumentException.
             (str "Sparseness must be between " (dec n) " and " (* n (dec n))
                  " for a graph with " n " vertices")))))

;; Single function implementation instead of using a protocol/record
(defn construct-random-graph
  "Randomly generate a simple directed graph"
  [vertex-count edge-count]
  (validate-graph-parameters vertex-count edge-count)
  (-> (random-builder/create-random-builder)
      (random-builder/add-vertices vertex-count)
      (random-builder/ensure-connectivity)
      (random-builder/add-remaining-edges edge-count)
      (random-builder/build)))