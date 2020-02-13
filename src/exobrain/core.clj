(ns exobrain.core
  (:require [clojure.java.io :as io]))

(defn env-init
  []
  (let [user-home (.getPath (io/file (System/getProperty "user.home" ) ".exobrain"))
        wiz-db-dir (io/file user-home "temp")
        rocksdb-dir (io/file user-home "data")
        lucene-dir (io/file user-home "index")]
    (.mkdir (io/file user-home))
    (.mkdir (io/file wiz-db-dir))
    (.mkdir (io/file rocksdb-dir))
    (.mkdir (io/file lucene-dir))))

(defn -main
  [& args]
  (env-init))
