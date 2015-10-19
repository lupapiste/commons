(ns lupapiste-commons.states)

(def
  ^{:doc "Possible state transitions for applications.
          Key is the starting state, first in the value vector is the default next state and
          the rest are other possible next states."}
  default-application-state-graph
  {:draft      [:open :submitted :canceled]
   :open       [:submitted :canceled]
   :submitted  [:sent :verdictGiven :canceled]
   :sent       [:verdictGiven :complementNeeded :canceled]
   :complementNeeded   [:sent :verdictGiven :canceled]
   :verdictGiven        [:constructionStarted :canceled]
   :constructionStarted [:closed :canceled]
   :closed   []
   :canceled []
   :extinct  [] ; Rauennut
   })
