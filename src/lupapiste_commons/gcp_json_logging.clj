(ns lupapiste-commons.gcp-json-logging
  (:require [clojure.stacktrace :as cs]
            [clojure.string :as str]))

(defn- log-event?
  [[first-arg :as _vargs]]
  (boolean (and (map? first-arg) (:isEvent first-arg))))

(defn- produce-message
  [[first-arg :as vargs]]
  (let [message (str/join " " vargs)]
    ;; Put logging args within a separate map, se we don't mix them
    ;; with various timbre fields or the logging context
    [message {:args (zipmap (range)
                            (if (string? first-arg)
                              (rest vargs)
                              vargs))}]))

(defn combine-args-mw
  "Middleware for formatting log arguments as strings

  The `timbre-json-appender` library eagerly considers logging
  arguments to be data. Logging calls like `(timbre/warn :foo 42)`
  would result in a) no message and b) {:args {:foo 42}}

  See https://github.com/viesti/timbre-json-appender?tab=readme-ov-file#format-of-the-log-invocation

  To work around this, we can use this middleware to produce the
  leading message string (and also include the arguments in a map). "
  [{:keys [vargs msg-type] :as data}]
  (if (or (log-event? vargs) ; Don't alter event messages
          (= :f msg-type))   ; Don't alter infof, warnf, etc
    data
    (update data :vargs produce-message)))

(defn string-stack-trace-mw
  "Adds `stack_trace` for logging errors for GCP and removes the `?err` key containing the exception.

  `timbre-json-appender` would create a nested map from the error with `Throwable->map`, but that is not useful
  for error reporting in GCP which does not recognize that format.

  Additionally, `timbre-json-appender` can fail to log JSON if the Throwable data is not serializable or if
  the serialization causes an error in itself (e.g. with a component system map in the exception resulting
  in a stack overflow). It falls back to the basic text appender in that situation, polluting logs. To avoid this,
  the `?err` key is removed completely from the log data."
  [{:keys [^Throwable ?err] :as data}]
  (if (some? ?err)
    (-> (assoc-in data [:context :stack_trace] (with-out-str (cs/print-cause-trace ?err)))
        (dissoc :?err))
    data))
