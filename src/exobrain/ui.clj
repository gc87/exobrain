(ns exobrain.ui
  (:require [exobrain.systemtray :as st]
            [exobrain.cef :as cef]
            [seesaw.core :as ss]
            [seesaw.dev :as ssd])
  (:import (org.cef CefApp CefClient CefSettings)
           (javax.swing JOptionPane)))

(defn ui-initialize
  [& args]
  (st/make-tray-icon! args {:image "Pacman4.png"
                            :menu  {:open  (fn [event]
                                             (ss/invoke-later (-> (cef/make-frame)
                                                                  ;ss/pack!
                                                                  ss/show!)))
                                    :about (fn
                                             [event]
                                             (JOptionPane/showMessageDialog nil "Exobrain-了不起的外脑"))}})
  (cef/cef-initialize args))

(defn ui-dispose
  [& args]
  (st/delete-tray-icon!)
  (cef/cef-dispose))

(comment
  (ssd/show-events (ss/frame))

  (ui-initialize)
  (ss/invoke-later (-> (make-frame)
                       ss/pack!
                       ss/show!)))

