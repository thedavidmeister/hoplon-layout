(ns hoplon-layout.resize-observer.hoplon-test
 (:require
  hoplon-layout.resize-observer.hoplon
  [javelin.core :as j]
  [hoplon.core :as h]
  [hoplon.svg :as svg]
  [cljs.test :refer-macros [deftest is are async]]
  hoplon-layout.test.util))

; TESTS

(deftest ??el
 (is (hoplon-layout.test.util/contains? (hoplon-layout.resize-observer.hoplon/el) "div"))
 (is (hoplon-layout.test.util/is? (hoplon-layout.resize-observer.hoplon/el) "div"))
 (is (hoplon-layout.test.util/is? (hoplon-layout.resize-observer.hoplon/el :f h/span) "span"))
 (is (hoplon-layout.test.util/is? (hoplon-layout.resize-observer.hoplon/el :f svg/svg) "svg"))
 (is (hoplon-layout.test.util/is? (hoplon-layout.resize-observer.hoplon/el :f svg/g) "g")))

(deftest ??div
 (async done
  (let [height (j/cell 0)
        width (j/cell 0)
        floated (h/div :css {
                             :float "left"
                             :height "2px"
                             :width "1px"
                             :outline "1px solid blue"})
        el (h/div :css {:width "3px"}
            (hoplon-layout.resize-observer.hoplon/div
             :height height
             :width width
             :css {:outline "1px solid red"}
             floated))]
   (is (hoplon-layout.test.util/contains? el floated))

   ; These should still be the initial values as el is not attached to the DOM.
   (is (= 0 @height))
   (is (= 0 @width))

   (h/body el)

   (let [correct-height? (j/cell= (= 2 height))
         correct-width? (j/cell= (= 3 width))]
    (j/cell= (when correct-height? (prn "ResizeObserver saw correct height.")))
    (j/cell= (when correct-width? (prn "ResizeObserver saw correct width.")))
    ; Shortcut the longer timeout once the dimensions are correct.
    (j/cell= (when (and correct-height? correct-width?) (done)))

    ; Short-circuit a failing test so it doesn't hang.
    (h/with-timeout 100
     (when-not (and @correct-height? @correct-width?)
      (is false (str "ResizeObserver did not see correct height and width. width: " @width ", height: " @height))
      (done)))))))
