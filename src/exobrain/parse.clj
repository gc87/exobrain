(ns exobrain.parse
  (:require [libpython-clj.python
             :refer [as-python as-jvm
                     ->python ->jvm
                     get-attr call-attr call-attr-kw
                     get-item att-type-map
                     call call-kw initialize!
                     as-numpy as-tensor ->numpy
                     run-simple-string
                     add-module module-dict
                     import-module
                     python-type]]
            [clojure.java.io :as io]
            [clucie.core :as core]
            [clucie.analysis :as analysis]
            [clucie.store :as store])
  (:import (org.jsoup Jsoup)))

(def python-executable (.getPath (io/file (io/resource "python-3.7.6-embed-amd64/python.exe") )) )
(def library-path (.getPath (io/file (io/resource "python-3.7.6-embed-amd64/python37.dll")))  )

(initialize! :python-executable python-executable
             :library-path library-path)

(defn extract-text
  [is]
  (.text (Jsoup/parse is "UTF-8" "")))
