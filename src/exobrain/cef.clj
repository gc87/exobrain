(ns exobrain.cef
  (:require [seesaw.core :as ss]
            [seesaw.dev :as ssd])
  (:import (org.cef CefApp CefClient CefSettings)
           (javax.swing JFrame)
           (java.awt.event WindowAdapter)))

(defonce cef-app (atonil))

(defn- init-cef-app
  [& args]
  (let [settings (CefSettings.)]
    (set! (.-windowless_rendering_enabled settings) false)
    (reset! cef-app (CefApp/getInstance settings))
    (when-not @cef-app
      (throw Exception))))

(defn make-frame
  []
  (let [cef-client (.createClient @cef-app)
        brower (.createBrowser cef-client "http://www.baidu.com" false false)
        brower-ui (.getUIComponent brower)
        frame (ss/frame
                :title "CEF Window"
                ;:size [200 :by 500]
                :width 600
                :height 400
                :content brower-ui
                :on-close :dispose)]
    (ss/listen frame
               :window-closing (fn
                                 [e]
                                 (.dispose cef-client)))
    frame))

;(defn make-frame
;  []
;  (let [cef-client (.createClient @cef-app)
;        browser (.createBrowser cef-client "http://www.douban.com" false  false)
;        browser-ui (.getUIComponent browser)
;        frame (JFrame. "Maze")]
;    (.add (.getContentPane frame) browser-ui)
;    (.addWindowListener frame (proxy [WindowAdapter] []
;                                (windowClosing [event]
;                                  ;(.close browser true)
;                                  ;(.doClose browser)
;                                  (.dispose cef-client)
;                                  )))
;    ;(.setUndecorated frame true)
;    (.pack frame)
;    (.setSize frame 800 500)
;    (.setVisible frame true)))

(defn cef-initialize
  [& args]
  (init-cef-app args))

(defn cef-dispose
  []
  (-> (CefApp/getInstance)
      (.dispose)))