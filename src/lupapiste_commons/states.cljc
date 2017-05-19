(ns lupapiste-commons.states)

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
  ^{:doc "Possible state transitions for inforequests.
          Key is the starting state, first in the value vector is the default next state and
          the rest are other possible next states."}
  default-inforequest-state-graph
  (array-map
    :info     [:answered :canceled]
    :answered [:info]
    :canceled []))

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

(def ya-tyolupa-state-graph default-application-state-graph)

(def
  ^{:doc "Possible state transitions for YA käyttölupa applications."}
  ya-kayttolupa-state-graph
  (merge
    (select-keys default-application-state-graph
                 [:draft :open :submitted :sent
                  :complementNeeded :extinct :canceled])
    {:verdictGiven        [:finished :appealed :extinct :canceled]
     :finished            []
     :appealed            [:verdictGiven]}))

(def ya-sijoittaminen-shared-states                         ; Shared states for sijoittaminen
  {:draft               [:open :submitted :canceled]
   :open                [:submitted :canceled]
   :complementNeeded    [:sent :canceled]
   :canceled            []})

(def
  ^{:doc "Possible state transitions for YA sijoituslupa subtype."}
  ya-sijoituslupa-state-graph
  (merge ya-sijoittaminen-shared-states
         {:submitted           [:sent :draft :canceled :verdictGiven]
          :sent                [:verdictGiven :complementNeeded :canceled]
          :verdictGiven        [:finished :appealed :extinct]
          :appealed            [:verdictGiven]
          :finished            []
          :extinct             []}))

(def
  ^{:doc "Possible state transitions for YA sijoitussopimus subtype."}
  ya-sijoitussopimus-state-graph
  (merge ya-sijoittaminen-shared-states
         {:submitted         [:sent :draft :canceled :agreementPrepared]
          :sent              [:agreementPrepared :complementNeeded :canceled]
          :agreementPrepared [:agreementSigned :canceled]
          :agreementSigned   []}))

(def
  ^{:doc "Possible state transitions for YA jatkoaika applications."}
  ya-jatkoaika-state-graph
  {:draft               [:open :submitted :canceled]
   :open                [:submitted :canceled]
   :submitted           [:sent :draft :canceled]
   :sent                [:finished :complementNeeded :canceled]
   :complementNeeded    [:sent :finished :canceled]
   :finished            [:appealed :extinct :canceled]
   :appealed            [:finished :canceled]
   :canceled            []
   :extinct             [] ; Rauennut
   })

(def full-ya-application-state-graph
  (merge-with (comp vec distinct concat)
              ya-jatkoaika-state-graph
              ya-kayttolupa-state-graph
              ya-sijoituslupa-state-graph
              ya-sijoitussopimus-state-graph
              ya-tyolupa-state-graph))

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
              full-ya-application-state-graph
              tj-ilmoitus-state-graph
              tj-hakemus-state-graph
              tonttijako-application-state-graph
              default-inforequest-state-graph))
