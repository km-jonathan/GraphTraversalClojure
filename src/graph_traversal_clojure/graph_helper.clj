(ns graph-traversal-clojure.graph-helper)

;; Task 3 - Dijkstra's algorithm for shortest path
(defn get-shortest-path
  "Find the shortest path between two vertices using Dijkstra's algorithm"
  [graph start end]
  (when (and (contains? graph start)
             (contains? graph end)
             (not-empty graph))
    (let [vertices (keys graph)
          ;; Initialize distances
          initial-distances (reduce #(assoc %1 %2 Integer/MAX_VALUE) {} vertices)
          initial-distances (assoc initial-distances start 0)
          ;; Initialize previous nodes
          initial-previous (reduce #(assoc %1 %2 nil) {} vertices)]

      ;; Run Dijkstra's algorithm
      (loop [unvisited (set vertices)
             distances initial-distances
             previous initial-previous]
        (if (empty? unvisited)
          ;; No more vertices to process, build path
          (let [path (loop [path []
                            current end]
                       (if (nil? current)
                         path
                         (recur (cons current path) (get previous current))))]
            (if (= (first path) start) path []))

          ;; Find vertex with minimum distance
          (let [current (apply min-key #(get distances % Integer/MAX_VALUE) unvisited)]
            (if (or (= current end)
                    (= (get distances current) Integer/MAX_VALUE))
              ;; Reached destination or no path exists
              (let [path (loop [path []
                                curr end]
                           (if (nil? curr)
                             path
                             (recur (cons curr path) (get previous curr))))]
                (if (= (first path) start) path []))

              ;; Process neighbors
              (let [neighbors (get graph current)
                    unvisited-new (disj unvisited current)
                    ;; Update distances and previous for neighbors
                    [distances-new previous-new]
                    (reduce (fn [[d p] [neighbor weight]]
                              (if (not (contains? unvisited-new neighbor))
                                [d p]
                                (let [new-dist (+ (get d current) weight)]
                                  (if (< new-dist (get d neighbor))
                                    [(assoc d neighbor new-dist) (assoc p neighbor current)]
                                    [d p]))))
                            [distances previous]
                            neighbors)]
                (recur unvisited-new distances-new previous-new)))))))))

;; Calculate total distance of a path
(defn get-path-distance
  "Calculate the total distance of a path"
  [graph path]
  (if (< (count path) 2)
    0
    (reduce + (map (fn [[from to]]
                     (get-in graph [from to]))
                   (partition 2 1 path)))))

;; Task 4.1 - Calculate eccentricity
(defn get-eccentricity
  "Calculate the eccentricity of a vertex"
  [graph vertex]
  (when (contains? graph vertex)
    (let [other-vertices (filter #(not= % vertex) (keys graph))
          distances (for [other other-vertices
                          :let [path (get-shortest-path graph vertex other)]
                          :when (seq path)]
                      (get-path-distance graph path))]
      (if (seq distances)
        (apply max distances)
        0))))

;; Task 4.2 - Calculate radius
(defn get-radius
  "Calculate the radius of the graph"
  [graph]
  (let [eccentricities (for [vertex (keys graph)
                             :let [ecc (get-eccentricity graph vertex)]
                             :when (pos? ecc)]
                         ecc)]
    (if (seq eccentricities)
      (apply min eccentricities)
      0)))

;; Task 4.3 - Calculate diameter
(defn get-diameter
  "Calculate the diameter of the graph"
  [graph]
  (let [eccentricities (for [vertex (keys graph)
                             :let [ecc (get-eccentricity graph vertex)]
                             :when (pos? ecc)]
                         ecc)]
    (if (seq eccentricities)
      (apply max eccentricities)
      0)))