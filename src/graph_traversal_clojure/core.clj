(ns graph-traversal-clojure.core
  (:gen-class)
  (:require [graph-traversal-clojure.graph-master :as graph-master]
            [graph-traversal-clojure.graph :as graph]
            [graph-traversal-clojure.graph-helper :as helper]
            [clojure.string :as str]))

(defn- parse-int [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException _ nil)))

(defn- get-input-params [args]
  (if (>= (count args) 2)
    (let [size (parse-int (first args))
          sparseness (parse-int (second args))]
      (if (and size sparseness)
        [size sparseness]
        (do
          (println "Error: Size (N) and Sparseness (S) must be integers.")
          nil)))
    (do
      (println "Please enter size (N) for the graph:")
      (let [size-input (parse-int (read-line))]
        (if size-input
          (do
            (println "Please enter sparseness (S) for the graph:")
            (let [sparseness-input (parse-int (read-line))]
              (if sparseness-input
                [size-input sparseness-input]
                (do
                  (println "Error: Sparseness (S) must be an integer.")
                  nil))))
          (do
            (println "Error: Size (N) must be an integer.")
            nil))))))

(defn -main
  "Main entry point for the graph traversal application"
  [& args]
  (try
    (when-let [[graph-size graph-sparseness] (get-input-params args)]
      (if (< graph-size 1)
        (println "Error: Graph Size (N) must be greater than 0.")
        (if (or (< graph-sparseness (dec graph-size))
                (> graph-sparseness (* graph-size (dec graph-size))))
          (println (str "Error: Sparseness (S) must be between " (dec graph-size) " and "
                        (* graph-size (dec graph-size)) " for a graph with " graph-size " vertices."))

          ;; Generate and analyze graph
          (let [g (graph-master/construct-random-graph graph-size graph-sparseness)]
            ;; Print the generated graph
            (println (str "Graph: " (graph/graph->str g)))

            ;; Print graph properties
            (println (str "Radius: " (helper/get-radius g)))
            (println (str "Diameter: " (helper/get-diameter g)))

            ;; Find the shortest path between random vertices
            (let [start (graph/get-random-vertex g nil)
                  end (graph/get-random-vertex g start)]
              (println (str "Randomly selected two vertices: [" start "], [" end "]"))

              (let [shortest-path (helper/get-shortest-path g start end)]
                (if (seq shortest-path)
                  (println (str "Shortest path from [" start "] to [" end "]: "
                                (str/join "," shortest-path)))
                  (println (str "No path found between [" start "] and [" end "]")))))

            ;; Calculate eccentricity of random node
            (let [random-node (graph/get-random-vertex g nil)]
              (println (str "Random node selected for eccentricity calculation: [" random-node "]"))
              (println (str "Eccentricity of vertex [" random-node "]: "
                            (helper/get-eccentricity g random-node))))))))

    (catch Exception e
      (println "Exception:" (.getMessage e))
      (.printStackTrace e))))