(ns exobrain.webview
  (:use seesaw.core)
  (:import (java.awt.event ActionEvent ActionListener)
           (java.awt BorderLayout Component)
           (javax.swing JFrame JTextField)
           (org.cef CefApp CefClient OS)
           (org.cef.browser CefBrowser CefFrame)))

(defn run [& args]
  (invoke-later
    (-> (frame :title "Hello",
               :content "Hello, Seesaw",
               :on-close :exit)
        pack!
        show!)))

;(run "")

;(def cef-app (CefApp/getInstance))

(defn my-frame
  []
  (let [cef-app (CefApp/getInstance)
        cef-client (.createClient cef-app)
        browser (.createBrowser cef-client "http://www.douban.com" (OS/isWindows)  false)
        browser-ui (.getUIComponent browser)
        frame (JFrame. "Maze")]
    (.add (.getContentPane frame) browser-ui)
    ;(.setUndecorated frame true)
    (.pack frame)
    (.setSize frame 300 300)
    (.setVisible frame true)))

;(my-frame)

(defn my-frame2
  []
  (let [frame (JFrame. "Maze")]
    ;(.setUndecorated frame true)
    (.pack frame)
    (.setSize frame 300 300)
    (.setVisible frame true)))

;(my-frame2)

(defn my-frame3
  []
  (doto ()))