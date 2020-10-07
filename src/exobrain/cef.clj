(ns exobrain.cef
  (:require [seesaw.core :as ss]
            [seesaw.dev :as ssd]
            [seesaw.swingx :as swingx]
            [seesaw.keymap :as km]
            [clojure.java.io :as io])
  (:import (org.cef CefApp CefClient CefSettings CefApp$CefAppState)
           (javax.swing JFrame)
           (java.awt.event WindowAdapter)
           (org.cef.handler CefAppHandlerAdapter)
           (com.sun.jna StringArray)))

(defonce cef-app (atom nil))

(defn- init-cef-app
  [& args]
  (let [settings (CefSettings.)]
    (set! (.-windowless_rendering_enabled settings) false)
    (CefApp/addAppHandler (proxy [CefAppHandlerAdapter] [nil]
                            (stateHasChanged [state]
                              (if (= (CefApp$CefAppState/TERMINATED) state)
                                (println "CefApp Terminated.")))))
    (reset! cef-app (CefApp/getInstance settings))
    (when-not @cef-app
      (throw Exception))))

(defn make-frame
  []
  (let [cef-client (.createClient @cef-app)
        brower (.createBrowser cef-client "file:///C:/Users/chun/Documents/My%20Knowledge/temp/6d79be07-44e7-46a0-bd01-9e3b35221440/128/index.htm" false false)
        brower-ui (.getUIComponent brower)
        frame (ss/frame
                :title "CEF Window"
                :icon (io/resource "Pacman4.png")
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

(comment

  (-> (make-frame1)
      ss/pack!
      ss/show!))