(ns exobrain.store
  (:require [clj-rocksdb :as rocks]
            [clojure.java.io :as io]
            [taoensso.nippy :as nippy]))

(def rocksdb-dir (.getPath (io/file (System/getProperty "user.home") ".exobrain" "data" "raw")))

;(def db (rocks/create-db rocksdb-dir
;                         {:key-encoder nippy/freeze :key-decoder nippy/thaw
;                          :val-encoder nippy/freeze :val-decoder nippy/thaw}))

(defn init
  [])
