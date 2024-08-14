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
  "Adds a `stack_trace` field when logging errors for GCP.

  The `timbre-json-appender` already creates a nested map from the
  error with `Throwable->map`, but it seems error reporting in GCP
  does not recognize that format."
  [{:keys [^Throwable ?err] :as data}]
  (if (some? ?err)
    (update data :context assoc :stack_trace (with-out-str (cs/print-stack-trace ?err)))
    data))
