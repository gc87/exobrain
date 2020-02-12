(ns exobrain.parse
  (:import (org.jsoup Jsoup)))

(defn extract-text
  [is]
  (.text (Jsoup/parse is "UTF-8" "")))
