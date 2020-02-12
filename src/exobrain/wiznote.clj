(ns exobrain.wiznote
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]
            [exobrain.store :as store]
            [exobrain.tika :as tika])
  (:import (java.util.zip ZipEntry ZipFile ZipInputStream)))

(defn db-path
  [wiz-data-dir user db-file]
  (io/file wiz-data-dir user db-file))

(defn copy-db
  [wiz-data-dir wiz-db-dir user]
  (doseq [file ["index.db" "index.db-shm" "index.db-wal"]]
    (try
      (= nil (io/copy (db-path wiz-data-dir user file) (io/file wiz-db-dir file)))
      (catch Exception e false))))

(defn db-exists
  [wiz-db-dir]
  (.exists (io/file wiz-db-dir "index.db")))

(defn delete-db
  [wiz-db-dir]
  (doseq [file ["index.db" "index.db-shm" "index.db-wal"]]
    (try
      (io/delete-file (.getPath (io/file wiz-db-dir file)))
      (catch Exception e false))))

(defn sync-query
  [db-spec]
  (jdbc/with-db-connection [conn db-spec]
                           (jdbc/query conn "select * from WIZ_DOCUMENT order by DT_CREATED desc")))

(defn wiz-is-valid
  [file]
  (try (ZipFile. file)
        (catch Exception e nil)))

;; C:\Users\chun\Documents\My Knowledge\Data\sfme@qq.com\My Notes\one.ziw
;; C:\\Users\\chun\\Documents\\My Knowledge\\Data\\sfme@qq.com\\微信收藏\\特斯拉嫌弃 Python，追捧 C++.ziw
;; (def file-w (io/file ""))

(defn wiz-file-process
  [file]
  (let [zip-file (try (ZipFile. file) (catch Exception e nil))
        zip-entry (if (nil? zip-file)
                    nil
                    (try (.getEntry zip-file "index.html") (catch Exception e nil)))
        input (if (or (nil? zip-file) (nil? zip-entry))
                nil
                (.getInputStream zip-file zip-entry))]
    (when-not (nil? input) (println (slurp input)))))

(defn wiz-document-process
  [wiz-data-dir user row]
  (wiz-file-process (io/file wiz-data-dir user (:document_location row) (:document_name row))))

(defn sync!
  [user]
  (let [windows-dir (.getPath (io/file (System/getProperty "user.home")
                                       "Documents"
                                       "My Knowledge"
                                       "Data"))
        wiz-data-dir (when (>= (.indexOf "win"
                                         (.toLowerCase (System/getProperty "os.name")) 0))
                       windows-dir)
        wiz-db-dir (.getPath (io/file (System/getProperty "user.home")
                                      ".exobrain"
                                      "wizdb"))
        db-spec {:classname   "org.sqlite.JDBC"
                 :subprotocol "sqlite"
                 :subname     (.getPath (io/file wiz-db-dir "index.db"))}]
    (when (db-exists wiz-db-dir) (delete-db wiz-db-dir))
    (when (not (db-exists wiz-db-dir)) (copy-db wiz-data-dir wiz-db-dir user))
    (when (db-exists wiz-db-dir) (doseq [row (sync-query db-spec)]
                                   (wiz-document-process wiz-data-dir user row)))
    (when (db-exists wiz-db-dir) (delete-db wiz-db-dir))))

(sync! "sfme@qq.com")
