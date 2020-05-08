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
                 [org.openjfx/javafx-base "11.0.2"]
                 [org.openjfx/javafx-graphics "11.0.2"]
                 [org.openjfx/javafx-controls "11.0.2"]
                 [org.openjfx/javafx-web "11.0.2"]
                 [org.openjfx/javafx-swing "11.0.2"]
                 [org.openjfx/javafx-fxml "11.0.2"]
                 ;[cljfx "1.6.9"]
                 ]
  :main exobrain.core
  ;:aot [exobrain.core]
  :profiles {:uberjar {:aot :all}}
  ;:repl-options {:init-ns exobrain.core}
  :jvm-opts ["-Xmx2G"])
