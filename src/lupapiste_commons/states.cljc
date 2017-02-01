(ns lupapiste-commons.states
  (:require [clojure.set :refer :all]))

(def
  ^{:doc "Possible state transitions for applications.
          Key is the starting state, first in the value vector is the default next state and
          the rest are other possible next states."}
  default-application-state-graph
  {:draft               [:open :submitted :canceled]
   :open                [:submitted :canceled]
   :submitted           [:sent :draft :verdictGiven :canceled]
   :sent                [:verdictGiven :complementNeeded :canceled]
   :complementNeeded    [:sent :verdictGiven :canceled]
   :verdictGiven        [:constructionStarted :appealed :closed :extinct :canceled]
   :constructionStarted [:closed :extinct]
   :closed              []
   :canceled            []
   :extinct             [] ; Rauennut
   :appealed            [:verdictGiven :canceled]
   })

(def
  ^{:doc "See default-application-state-graph"}
  tj-ilmoitus-state-graph
  (merge
    (select-keys default-application-state-graph [:draft :open :canceled])
    {:submitted  [:acknowledged :draft :canceled]
     ; must be for tj-hakemus-state-graph compatibility:
     ; if foreman application is in complementNeeded state it can be converted
     ; to use this state graph
     :complementNeeded [:acknowledged :canceled]
     :acknowledged [:complementNeeded]}))

(def
  ^{:doc "See default-application-state-graph"}
  tj-hakemus-state-graph
  (merge
    (select-keys default-application-state-graph [:draft :open :canceled])
    {:submitted    [:sent :draft :canceled]
     :sent         [:foremanVerdictGiven :complementNeeded :canceled]
     :complementNeeded [:sent :canceled]
     :foremanVerdictGiven [:canceled :appealed]
     :appealed [:foremanVerdictGiven :canceled]}))

(def
  ^{:doc "See default-application-state-graph"}
  tonttijako-application-state-graph
  (merge
    (select-keys default-application-state-graph [:draft :open :canceled])
    {:submitted [:hearing :draft :canceled]
     :hearing [:proposal :canceled]
     :proposal [:proposalApproved :canceled]
     :proposalApproved [:final :appealed :canceled]
     :appealed [:final :canceled] ; Oikaisuvaatimus
     :final    [] ; Lain voimainen
     }))

(def full-ya-application-state-graph
  default-application-state-graph)

(def
  ^{:doc "All states for (currently R and P) applications.
  Includes new states from KRYSP reader, which currently can't be reached via UI."}
  full-application-state-graph
  (-> default-application-state-graph
      (assoc
        :verdictGiven        [:constructionStarted :inUse :onHold :appealed :closed :extinct :canceled]
        :constructionStarted [:inUse :onHold :closed :extinct]
        :inUse               [:closed :onHold :extinct]
        :onHold              [:closed :constructionStarted :inUse :extinct])))

(def all-transitions-graph
  (merge-with (comp vec distinct concat)
              full-application-state-graph
              tj-ilmoitus-state-graph
              tj-hakemus-state-graph
              tonttijako-application-state-graph))
