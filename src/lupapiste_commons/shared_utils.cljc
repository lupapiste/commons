(ns lupapiste-commons.shared-utils)

(defn dissoc-vec [v i]
  (let [length (count v)]
    (vec (concat (subvec v 0 i) (subvec v (inc i) length)))))

(defn dissoc-vec-or-map [m k]
  (if (and (vector? m) (number? k))
    (dissoc-vec m k)
    (dissoc m k)))

(defn dissoc-in
  "Taken from core.incubator, added support for vectors, keeps empty maps/vectors (https://github.com/clojure/core.incubator/blob/master/src/main/clojure/clojure/core/incubator.clj#L62)"
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (assoc m k newmap))
      m)
    (dissoc-vec-or-map m k)))

(defn flip [function]
  (fn
    ([] (function))
    ([x] (function x))
    ([x y] (function y x))
    ([x y z] (function z y x))
    ([a b c d] (function d c b a))
    ([a b c d & rest]
     (->> rest
          (concat [a b c d])
          reverse
          (apply function)))))
