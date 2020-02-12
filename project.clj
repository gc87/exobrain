(defproject exobrain "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.xerial/sqlite-jdbc "3.30.1"]
                 [kotyo/clj-rocksdb "0.1.3"]
                 [com.taoensso/nippy "2.14.0"]
                 [clj-http "3.10.0"]
                 [org.clojure/data.json "0.2.7"]
                 [org.clojars.hozumi/clj-commons-exec "1.2.0"]
                 [org.apache.tika/tika-core "1.23"]
                 [org.apache.tika/tika-parsers "1.23"]
                 [cljfx "1.6.2"]
                 [cnuernber/libpython-clj "1.36"]
                 [clojupyter "0.3.1"]]
  :main exobrain.core
  ;:aot [exobrain.ui]
  ;:plugins [[lein-jupyter "0.1.16"]]
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns exobrain.core})
