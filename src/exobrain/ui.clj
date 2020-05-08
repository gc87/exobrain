(ns exobrain.ui
  (:gen-class
    :extends javafx.application.Application)

  (:require [clojure.java.io :as io]
    ;[cljfx.api :as fx]
            )
  (:import [java.awt SystemTray TrayIcon Toolkit PopupMenu MenuItem Menu]
           [java.awt.event ActionListener]
    ;[javafx.scene.input KeyCode KeyEvent]
    ;       [javafx.stage Screen]
           ))

(defn tray-supported?
  []
  (. SystemTray isSupported))

(defn icon-image
  [filename]
  (let [rc (io/resource filename)
        image (.getImage (Toolkit/getDefaultToolkit) rc)]
    image))

(defn popup-menu
  []
  (let [menu-open (MenuItem. "打开Exobrain")
        menu-about (MenuItem. "关于")
        menu-exit (MenuItem. "退出Exobrain")
        menu (PopupMenu.)]
    (.add menu menu-open)
    (.add menu menu-about)
    (.addSeparator menu)
    (.add menu menu-exit)
    menu))

(defn make-tray-icon!
  []
  (let [system-tray (SystemTray/getSystemTray)
        image (icon-image "Pacman4.png")
        tray-icon (TrayIcon. image "AKin-了不起的笔记软件" (popup-menu))]
    (.setImageAutoSize tray-icon true)
    (.add system-tray tray-icon)
    tray-icon))

(defn reset-image!
  [tray-icon image]
  (.setImage tray-icon image))

(defn reset-popue-menu!
  [tray-icon popue-menu]
  (.setPopupMenu tray-icon popue-menu))

(defn delete-tray-icon!
  [tray-icon]
  (.remove (SystemTray/getSystemTray) tray-icon))

(defn -start [app stage]
  (let [circ (javafx.scene.shape.Circle. 40 40 40)
        root (javafx.scene.Group.)                  ; Cannot use variadic arg constructor.
        scene (javafx.scene.Scene. root 400 300)]
    (.. root (getChildren) (add circ))              ; Add circle to group.
    (doto stage
      (.setTitle "My JavaFX Application")
      (.setScene scene)
      (.show))))

;(defn run
;  [& args]
;  (javafx.application.Application/launch exobrain.ui (into-array String args)))

(comment
  (defonce tray-icon (atom nil))

  (when (tray-supported?)
    (reset! tray-icon (make-tray-icon!)))

  (when-not (nil? @tray-icon)
    (reset-image! @tray-icon (icon-image "Clojure-icon.png")))

  (when-not (nil? @tray-icon)
    (reset-image! @tray-icon (icon-image "Pacman4.png")))

  (when-not (nil? @tray-icon)
    (delete-tray-icon! @tray-icon)))