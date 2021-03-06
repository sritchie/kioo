(ns kioo.om
  (:require [kioo.core :refer [component*]]
            [kioo.util :refer [convert-attrs]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This add better support for om
;; in particular om's wrapping
;; of form fields
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-om-sym [tag]
  (symbol "om.dom" (name tag)))


(defn emit-trans [node children]
  `(kioo.om/make-dom
    (~(:trans node) ~(-> node
                         (dissoc :trans)
                         (assoc :attrs (convert-attrs (:attrs node))
                                :content children
                                :sym (get-om-sym (:tag node)))))))

(defn emit-node [node children]
  `(apply ~(get-om-sym (:tag node))
        (cljs.core/clj->js ~(convert-attrs (:attrs node)))
        ~children))


(defn wrap-fragment [tag child-sym]
  `(apply ~(get-om-sym tag) nil ~child-sym))


(def om-emit-opts {:emit-trans emit-trans
                      :emit-node emit-node
                      :wrap-fragment wrap-fragment})

(defmacro component
  "React base component definition"
  [path & body]
  (component* path body om-emit-opts))
