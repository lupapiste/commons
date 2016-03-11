(ns lupapiste-commons.states)

(def
  ^{:doc "Possible state transitions for applications.
          Key is the starting state, first in the value vector is the default next state and
          the rest are other possible next states."}
  default-application-state-graph
  {:draft               [:open :submitted :canceled]
   :open                [:submitted :canceled]
   :submitted           [:sent :verdictGiven :canceled]
   :sent                [:verdictGiven :complementNeeded :canceled]
   :complementNeeded    [:sent :verdictGiven :canceled]
   :verdictGiven        [:constructionStarted :appealed :closed :extinct :canceled]
   :constructionStarted [:closed :extinct]
   :closed              []
   :canceled            []
   :extinct             [] ; Rauennut
   :appealed            [:verdictGiven :canceled]
   })

(def full-application-state-graph
  (assoc default-application-state-graph
         :verdictGiven        [:constructionStarted :inUse :onHold :appealed :closed :extinct :canceled]
         :constructionStarted [:inUse :onHold :closed :extinct]
         :inUse               [:closed :onHold :extinct]
         :onHold              [:closed :constructionStarted :inUse :extinct]))
