(ns exobrain.store
  (:require [clojure.java.io :as io]
            [taoensso.nippy :as nippy])
  (:import [org.iq80.leveldb Options DB Logger CompressionType]
           [org.iq80.leveldb.impl Iq80DBFactory]))

(defn create-db
  [db-name]
  (let [options (.. (Options.)
                    (createIfMissing true)
                    (cacheSize (* 100 1048576))
                    (compressionType CompressionType/SNAPPY)
                    #_(logger (reify Logger
                              (log [this string]
                                (println string)))))]
    (.open Iq80DBFactory/factory (io/file (System/getProperty "user.home") ".exobrain" "data" db-name) options)))

(defn close-db
  [db]
  (.close db))

(defn put-document
  [db key document]
  (.put db (nippy/freeze key) (nippy/freeze document)))

(defn get-document
  [db key]
  (let [data (.get db (nippy/freeze key))
        document (if data
                   (nippy/thaw data)
                   nil)]
    document))

(defn delete-document
  [db key]
    (.delete db (nippy/freeze key)))

(def exo-db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (.getPath (io/file (System/getProperty "user.home")
                               ".exobrain"
                               "exobrain.db"))})

(defn create-exo-db
  "创建系统数据库"
  []
  )

(comment
  (def db (create-db "raw") )
  (put-document db "Tampa" "rocks111")
  (get-document db "Tampa")
  (get-document db "7040ad6d-2001-4efb-be3a-b49551d549cf")
  (delete-document db "Tampa")
  (close-db db))

