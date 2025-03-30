(ns graph-traversal-clojure.graph-builder-base
  (:require [graph-traversal-clojure.graph :as graph]
            [graph-traversal-clojure.graph-builder :refer [IGraphBuilder]]))

(defrecord GraphBuilderBase [graph]
  IGraphBuilder
  ;; Abstract methods that derived types must implement
  (add-vertices [this count]
    (throw (UnsupportedOperationException. "Method not implemented")))

  (ensure-connectivity [this]
    (throw (UnsupportedOperationException. "Method not implemented")))

  (add-remaining-edges [this total-edges]
    (throw (UnsupportedOperationException. "Method not implemented")))

  ;; Default implementation for build method
  (build [this]
    graph))

;; Constants and helper functions
(def weight-range 10)

;; Factory function to create a new GraphBuilderBase instance
(defn create-base-builder []
  (->GraphBuilderBase (graph/create-graph)))

;; Helper function to get a random weight
(defn get-random-weight []
  (inc (rand-int (dec weight-range))))