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

(def
  ^{:doc "See default-application-state-graph"}
  tj-ilmoitus-state-graph
  (merge
    (select-keys default-application-state-graph [:draft :open :canceled])
    {:submitted  [:acknowledged :canceled]
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
    {:submitted    [:sent :canceled]
     :sent         [:foremanVerdictGiven :complementNeeded :canceled]
     :complementNeeded [:sent :canceled]
     :foremanVerdictGiven [:canceled :appealed]
     :appealed [:foremanVerdictGiven :canceled]}))

(def
  ^{:doc "See default-application-state-graph"}
  tonttijako-application-state-graph
  (merge
    (select-keys default-application-state-graph [:draft :open :canceled])
    {:submitted [:hearing :canceled]
     :hearing [:proposal :canceled]
     :proposal [:proposalApproved :canceled]
     :proposalApproved [:final :appealed :canceled]
     :appealed [:final :canceled] ; Oikaisuvaatimus
     :final    [] ; Lain voimainen
     }))

(def full-toj-application-state-graph
  (-> (assoc default-application-state-graph
         :verdictGiven        [:constructionStarted :inUse :onHold :appealed :closed :extinct :canceled]
         :constructionStarted [:inUse :onHold :closed :extinct]
         :inUse               [:closed :onHold :extinct]
         :onHold              [:closed :constructionStarted :inUse :extinct])
      (merge tj-ilmoitus-state-graph)
      (merge tj-hakemus-state-graph)
      (merge tonttijako-application-state-graph)))

(def full-application-state-graph
  (assoc default-application-state-graph
    :verdictGiven        [:constructionStarted :inUse :onHold :appealed :closed :extinct :canceled]
    :constructionStarted [:inUse :onHold :closed :extinct]
    :inUse               [:closed :onHold :extinct]
    :onHold              [:closed :constructionStarted :inUse :extinct]))
