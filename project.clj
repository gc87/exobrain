(defproject exobrain "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.xerial/sqlite-jdbc "3.30.1"]
                 [org.iq80.leveldb/leveldb "0.12"]
                 [org.iq80.leveldb/leveldb-api "0.12"]
                 [com.taoensso/nippy "2.14.0"]
                 [org.jsoup/jsoup "1.12.1"]
                 [clucie "0.4.2"]
                 [com.hankcs/hanlp "portable-1.7.6"]
                 [com.hankcs.nlp/hanlp-lucene-plugin "1.1.6"]
                 [cnuernber/libpython-clj "1.36"]
                 [seesaw "1.5.0"]
                 [org.jogamp.gluegen/gluegen-rt "2.3.2"]
                 [org.jogamp.gluegen/gluegen-rt-main "2.3.2"]
                 [org.jogamp.jogl/jogl-all "2.3.2"]
                 [org.jogamp.jogl/jogl-all-main "2.3.2"]
                 [exobrain/jcef "83.4.0+gfd6631b+chromium-83.0.4103.106"]]
  :main exobrain.core
  ;:aot [exobrain.core]
  :profiles {:uberjar {:aot :all}}
  ;:repl-options {:init-ns exobrain.core}
  :jvm-opts ["-Xmx2G"])
