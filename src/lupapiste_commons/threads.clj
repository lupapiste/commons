(ns lupapiste-commons.threads
  (:import [java.util.concurrent Executors ThreadFactory Future ExecutorService]
           [clojure.lang IPending]))

(defn thread-factory ^ThreadFactory [worker-name]
  (let [security-manager (System/getSecurityManager)
        thread-group (if security-manager
                       (.getThreadGroup security-manager)
                       (.getThreadGroup (Thread/currentThread)))]
    (reify
      ThreadFactory
      (newThread [this runnable]
        (doto (Thread. thread-group runnable worker-name)
          (.setDaemon true)
          (.setPriority Thread/NORM_PRIORITY))))))

(defn threadpool ^ExecutorService [pool-size worker-name]
  (Executors/newFixedThreadPool pool-size (thread-factory worker-name)))

(defn submit-thread [^ExecutorService pool f]
  (let [fut (.submit pool ^Runnable (bound-fn* f))]
    (reify
      IPending
      (isRealized [_] (.isDone fut))
      Future
      (get [_] (.get fut))
      (get [_ timeout unit] (.get fut timeout unit))
      (isCancelled [_] (.isCancelled fut))
      (isDone [_] (.isDone fut))
      (cancel [_ interrupt?] (.cancel fut interrupt?)))))

(defmacro submit [pool & body]
  ;; :once is used to let the compiler know that the function will only be invoked once.
  ;; It allows the closed-over variables to get cleared while the lambda is *still running*.
  `(submit-thread ~pool (^:once fn* [] ~@body)))

(defn wait-for-threads [threads]
  (run! #(.get ^Future %) threads))
