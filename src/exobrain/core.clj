(ns exobrain.core
  (:require [clojure.java.io :as io]))

(defn env-init
  []
  (let [user-home (.getPath (io/file (System/getProperty "user.home" ) ".exobrain"))
        wiz-db-dir (io/file user-home "wizdb")
        rocksdb-dir (io/file user-home "rocksdb")
        lucene-dir (io/file user-home "lucene")]
    (.mkdir (io/file user-home))
    (.mkdir (io/file wiz-db-dir))
    (.mkdir (io/file rocksdb-dir))
    (.mkdir (io/file lucene-dir))))

(defn -main
  [& args]
  (env-init))
