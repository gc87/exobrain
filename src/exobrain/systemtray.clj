(ns exobrain.systemtray
  (:require [clojure.java.io :as io] )
  (:import [java.awt SystemTray TrayIcon Toolkit PopupMenu MenuItem Menu CheckboxMenuItem Font]
           [java.awt.event ActionListener MouseAdapter MouseEvent]
           (javax.swing JMenuItem JPopupMenu JOptionPane)
           (java.awt BorderLayout Component)
           (java.awt.event ActionEvent)
           (javax.swing JFrame JTextField)))

(defn tray-supported?
  []
  (. SystemTray isSupported))

(defn icon-image
  [filename]
  (let [rc (io/resource filename)
        image (.getImage (Toolkit/getDefaultToolkit) rc)]
    image))

(defn- menu-action-listener
  [menu fn]
  (doto menu
    (.addActionListener
      (proxy [ActionListener] []
        (actionPerformed [event]
          (fn event))))))

(defn popup-menu
  []
  (let [menu-open (MenuItem. "打开 Exobrain")
        menu-cb-autostart (CheckboxMenuItem. "Auto start")
        menu-about (MenuItem. "关于")
        menu-exit (MenuItem. "退出 Exobrain")
        menu (PopupMenu.)]
    (menu-action-listener menu-open (fn
                                      [e]
                                      ))
    (.add menu menu-open)

    (.addSeparator menu)


    (.add menu menu-cb-autostart)

    (menu-action-listener menu-about (fn
                                       [e]
                                       (JOptionPane/showMessageDialog nil "Exobrain-了不起的外脑")))
    (.add menu menu-about)

    (.addSeparator menu)
    (.add menu menu-exit)
    (.setFont menu (Font. Font/SERIF Font/PLAIN 14))
    menu))

(defn make-tray-icon!
  []
  (let [system-tray (SystemTray/getSystemTray)
        image (icon-image "Pacman4.png")
        tray-icon (TrayIcon. image "AKin-了不起的笔记软件" (popup-menu))]
    (.setImageAutoSize tray-icon true)
    (.addMouseListener tray-icon (proxy [MouseAdapter] []
                                   (mouseClicked [event]
                                     (if (and (= MouseEvent/BUTTON1 (.getButton event))
                                              (= 1 (.getClickCount event)))
                                       (#(JOptionPane/showMessageDialog nil "Exobrain列表界面"))))))

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