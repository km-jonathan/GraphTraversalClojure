(ns graph-traversal-clojure.random-graph-builder
  (:require [graph-traversal-clojure.graph :as graph]
            [graph-traversal-clojure.graph-builder-base :as base]))

;; Implementing the RandomGraphBuilder
(defrecord RandomGraphBuilder [graph vertex-values connected-vertices unconnected-vertices existing-edges])

;; Factory function to create a new RandomGraphBuilder
(defn create-random-builder []
  (->RandomGraphBuilder
    (graph/create-graph) ;; graph
    []                   ;; vertex-values
    []                   ;; connected-vertices
    []                   ;; unconnected-vertices
    []))                 ;; existing-edges

;; Implementing the builder methods separately from the record definition
(defn add-vertices [builder count]
  (let [vertices (range 1 (inc count))
        new-graph (reduce graph/add-vertex (:graph builder) vertices)
        random-start (rand-nth (vec vertices))
        connected [random-start]
        unconnected (vec (filter #(not= % random-start) vertices))]
    (assoc builder
      :graph new-graph
      :vertex-values (vec vertices)
      :connected-vertices connected
      :unconnected-vertices unconnected)))

(defn ensure-connectivity [builder]
  (loop [g (:graph builder)
         connected (:connected-vertices builder)
         unconnected (:unconnected-vertices builder)
         edges (:existing-edges builder)]
    (if (empty? unconnected)
      (assoc builder
        :graph g
        :connected-vertices connected
        :unconnected-vertices unconnected
        :existing-edges edges)
      (let [source (rand-nth connected)
            target (first unconnected)
            weight (inc (rand-int 9))
            new-graph (graph/add-edge g source target weight)
            new-edges (conj edges [source target])]
        (recur new-graph
               (conj connected target)
               (rest unconnected)
               new-edges)))))

(defn add-remaining-edges [builder total-edges]
  (let [min-edges (dec (count (:vertex-values builder)))
        current-edges (count (:existing-edges builder))
        edges-to-add (- total-edges min-edges)]
    (if (<= edges-to-add 0)
      builder
      (loop [g (:graph builder)
             edges (:existing-edges builder)
             remaining edges-to-add]
        (if (<= remaining 0)
          (assoc builder :graph g :existing-edges edges)
          (let [source (rand-nth (:vertex-values builder))
                target (rand-nth (:vertex-values builder))
                weight (inc (rand-int 9))]
            (if (or (= source target)
                    (contains? (set edges) [source target]))
              (recur g edges remaining)
              (recur (graph/add-edge g source target weight)
                     (conj edges [source target])
                     (dec remaining)))))))))

(defn build [builder]
  (:graph builder))