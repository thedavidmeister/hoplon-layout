(ns hoplon-layout.resize-observer.hoplon
 (:require
  [hoplon.core :as h]
  [javelin.core :as j]
  hoplon-layout.clearfix.hoplon
  cljsjs.resize-observer-polyfill))

(h/defelem el
 [{:keys [width height f] :as attributes} children]
 (let [f' (or f h/div)
       el (f'
           (dissoc attributes :width :height)
           children
           (hoplon-layout.clearfix.hoplon/clearfix))
       width (or width (j/cell 0))
       height (or height (j/cell 0))
       ; https://stackoverflow.com/questions/220188/how-can-i-determine-if-a-dynamically-created-dom-element-has-been-added-to-the-d
       el-attached? #(-> % .-ownerDocument .-body (.contains %))
       cb (fn [es] (let [rect (-> es first .-contentRect)]
                    ; Don't trigger a resize when the el is detached from the
                    ; DOM as it will always be 0 and lead to a FOUC.
                    ; There is another resize when the el is re-attached anyway.
                    (when (el-attached? el)
                     (j/dosync
                      (reset! width (.-width rect))
                      (reset! height (.-height rect))))))]
  (.observe (js/ResizeObserver. cb) el)
  ; for the resize observer to reliably measure its children there must be an
  ; immediate parent with height auto.
  ; https://stackoverflow.com/questions/19375715/parent-div-not-expanding-to-childrens-height
  ; example showing a flex parent breaking child measurements.
  ; https://codepen.io/anon/pen/veRKOy?editors=1111
  ; we don't wrap el if f is provided as divs can break svg, etc.
  ; any wrappers become the responsibility of f.
  (if f el (h/div el))))

(def div (partial el :f h/div))
