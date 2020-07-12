(ns exobrain.ui
  (:require [exobrain.systemtray :as st]))

(defonce tray-icon (atom nil))

(defn make-tray-icon!
  []
  (when (st/tray-supported?)
    (reset! tray-icon (st/make-tray-icon!))))

(defn reset-image!
  [img]
  (when-not (nil? @tray-icon)
    (st/reset-image! @tray-icon (st/icon-image img))))


(when-not (nil? @tray-icon)
  (st/reset-image! @tray-icon (st/icon-image "Pacman4.png")))

(defn delete-tray-icon!
  []
  (when-not (nil? @tray-icon)
    (st/delete-tray-icon! @tray-icon)))



