(ns hoplon-layout.test.util
 (:require
  goog.dom
  oops.core)
 (:refer-clojure :exclude [contains? find]))

; taken from wheel dom traversal

(defn el?
 [el]
 (goog.dom/isElement el))

(defn is?
 [el sel]
 ; http://youmightnotneedjquery.com/#matches_selector
 (let [possible-methods ["matches" "matchesSelector" "msMatchesSelector" "mozMatchesSelector" "webkitMatchesSelector" "oMatchesSelector"]
       matches (some
                #(when (oops.core/oget+ el (str "?" %)) %)
                possible-methods)]
  (oops.core/ocall+ el matches sel)))

(defn find
 [el sel]
 (array-seq
  (.querySelectorAll el sel)))

(defn contains?
 [el el-or-sel]
 (if (el? el-or-sel)
  (and
   (not (= el el-or-sel))
   (goog.dom/contains el el-or-sel))
  (some? (find el el-or-sel))))
