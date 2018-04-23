(ns ^{:hoplon/page "index.html"} pages.index
 (:require
  [hoplon.core :as h]
  [javelin.core :as j]
  syntax-highlighter.hoplon
  elem-lib.hoplon
  hoplon-layout.resize-observer.hoplon))

(h/html
 (h/head
  (syntax-highlighter.hoplon/stylesheet))
 (h/body
  (elem-lib.hoplon/elem
   "Resize Observer"
   "Takes height/width cells and tracks through resize observer"
   #'hoplon-layout.resize-observer.hoplon/el
   :height (j/cell 0)
   :width (j/cell 0)
   :css {:width "20vw"
         :height "20vh"
         :border "1px solid red"})))
