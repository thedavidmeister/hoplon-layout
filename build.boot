(def project 'thedavidmeister/hoplon-layout)
(def version "0.0.1")

(set-env!
 :source-paths #{"src"}
 :dependencies
 '[
   ; scaffolding....
   [org.clojure/clojure "1.10.0-alpha4"]
   [org.clojure/clojurescript "1.10.238"]
   [adzerk/boot-test "RELEASE" :scope "test"]
   [adzerk/bootlaces "RELEASE" :scope "test"]
   [adzerk/boot-cljs "2.1.4"]
   [crisptrutski/boot-cljs-test "0.3.5-SNAPSHOT" :scope "test"]
   [adzerk/boot-test "1.1.1" :scope "test"]
   [thedavidmeister/hoplon-elem-lib "0.2.0"]
   [hoplon "7.3.0-SNAPSHOT"]
   [samestep/boot-refresh "0.1.0" :scope "test"]
   [com.taoensso/timbre "4.10.0"]

   ; transitive deps...
   [doo "0.1.8"]

   ; everything else...
   [binaryage/oops "0.5.6"]
   [cljsjs/resize-observer-polyfill "1.4.2-0"]])

(task-options!
 pom {:project     project
      :version     version
      :description "Toolkit for layouts in Hoplon"
      :url         "https://github.com/thedavidmeister/hoplon-layout"
      :scm         {:url "https://github.com/thedavidmeister/hoplon-layout"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (pom) (jar) (install)))

(require
 '[adzerk.boot-cljs :refer [cljs]]
 '[hoplon.boot-hoplon :refer [hoplon]]
 '[tailrecursion.boot-jetty :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-test :refer [test]]
 '[samestep.boot-refresh :refer [refresh]])

(bootlaces! version)

(def cljs-compiler-options {})

(deftask deploy
 []
 (comp
  (build-jar)
  (push-release)))

(deftask tests-cljs
  "Run all the CLJS tests"
  [w watch? bool "Watches the filesystem and reruns tests when changes are made."]
  ; Run the JS tests
  (comp
    (if watch?
        (comp
          (watch)
          (speak :theme "woodblock"))
        identity)
    (test-cljs :exit? (not watch?)
               ; :js-env :chrome
               :cljs-opts (-> cljs-compiler-options
                              (merge {:load-tests true
                                      :process-shim false}))
               :namespaces [#".*"])))

(deftask front-dev
 "Build for local development."
 []
 (comp
  (watch)
  (speak)
  (hoplon)
  (cljs :compiler-options cljs-compiler-options)
  (serve :port 8000)))
