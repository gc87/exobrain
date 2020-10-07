(ns exobrain.ui
  (:require [exobrain.systemtray :as st]
            [exobrain.cef :as cef]
            [seesaw.core :as ss]
            [seesaw.swingx :as swingx]
            [seesaw.keymap :as km]
            [seesaw.dev :as ssd]
            [seesaw.bind :as b])
  (:import (org.cef CefApp CefClient CefSettings)
           (javax.swing JOptionPane)))

(defonce main-frame (atom nil))

(def examples
  ['behave
   'bind
   'button-group
   'canvas
   'cell-renderers
   'clock
   'custom-border
   'custom-dialog
   'dialog
   'dnd
   'dynamic-layout
   'editor-pane-hyperlink
   'explorer
   'form
   'forms
   'full-screen
   'game-of-life
   'hotpotatoes
   'j18n
   'kitchensink
   'kotka-bind
   'log-window
   'make-widget
   'mig
   'paintable
   'pan-on-drag
   'pi
   'piano
   'popup
   'reorderable-listbox
   'rpn
   'rsyntax
   'scribble
   'scroll
   'slider
   'spinner
   'swingx
   'table
   'temp
   'text-editor
   'text-ref
   'toggle-listbox
   'tree
   'xyz-panel])

(defn- make-main-frame
  [& args]
  (when-not @main-frame
    (ss/frame
      :title "Seesaw Example Launcher"
      :size [200 :by 500]
      :content
      (ss/border-panel
        :hgap 5 :vgap 5 :border 5
        :center (ss/scrollable (swingx/listbox-x
                                 :id :list
                                 :model examples
                                 :selection-mode :single
                                 :highlighters [(swingx/hl-simple-striping)
                                                ((swingx/hl-color :background "#88F") :rollover-row)]))
        :south (ss/button :id :launch :text "Launch")))))

(defn add-behaviors [f]
  (let [{:keys [list launch]} (ss/group-by-id f)]
    (b/bind
      (b/selection list)
      (b/property launch :enabled?))

    (ss/listen list :mouse-clicked
            (fn [e]
              (when (= 2 (.getClickCount e))
                ;(launch-example (selection list))
                )))

    ; (ss/map-key list "ENTER" (fn [_] (launch-example (selection list))))

    ;(listen launch :action (fn [_] (launch-example (selection list))))

    (ss/selection! list (first examples)))
  f)

(defn ui-initialize
  [& args]
  (st/make-tray-icon! args
                      {:image "Pacman4.png"
                       :l-click-handle (fn
                                         [event])
                       :menu  {:open  (fn
                                        [event]
                                        (ss/invoke-later (-> (make-main-frame )
                                                             (add-behaviors)
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
  (ssd/show-options (ss/frame))

  (ui-initialize)
  (ss/invoke-later (-> (make-main-frame)
                       ss/pack!
                       ss/show!))
  )

