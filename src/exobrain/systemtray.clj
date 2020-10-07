(ns exobrain.systemtray
  (:require [clojure.java.io :as io])
  (:import [java.awt SystemTray TrayIcon Toolkit PopupMenu MenuItem Menu CheckboxMenuItem Font]
           [java.awt.event ActionListener MouseAdapter MouseEvent]
           (javax.swing JMenuItem JPopupMenu JOptionPane)
           (java.awt BorderLayout Component)
           (java.awt.event ActionEvent)
           (javax.swing JFrame JTextField)))

; 全局唯一SystemTray实例
(defonce tray-icon (atom nil))

(defn- tray-supported?
  "测试系统是否支持SystemTray"
  []
  (. SystemTray isSupported))

(defn- icon-image
  "根据文件生成image"
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

(defn- popup-menu
  "创建默认的Tray菜单"
  [menu-option]
  (println menu-option)
  (let [menu-open (MenuItem. "打开 Exobrain")
        menu-cb-autostart (CheckboxMenuItem. "Auto start")
        menu-about (MenuItem. "关于")
        menu-exit (MenuItem. "退出 Exobrain")
        menu (PopupMenu.)]
    (menu-action-listener menu-open (:open menu-option))
    (.add menu menu-open)

    (.addSeparator menu)


    (.add menu menu-cb-autostart)

    (menu-action-listener menu-about (:about menu-option))
    (.add menu menu-about)

    (.addSeparator menu)
    (.add menu menu-exit)
    (.setFont menu (Font. Font/SERIF Font/PLAIN 14))
    menu))

(defn- gen-tray-icon
  "使用默认设置生成SystemTray的实例"
  [option]
  (println option)
  (let [system-tray (SystemTray/getSystemTray)
        image (icon-image (:image option))
        tray-icon (TrayIcon. image "外脑辅助思考" (popup-menu (:menu option)))]
    (.setImageAutoSize tray-icon true)
    (.addMouseListener tray-icon (proxy [MouseAdapter] []
                                   (mouseClicked [event]
                                     (if (and (= MouseEvent/BUTTON1 (.getButton event))
                                              (= 1 (.getClickCount event)))
                                       (#(JOptionPane/showMessageDialog nil "Exobrain列表界面"))))))

    (.add system-tray tray-icon)
    tray-icon))

(defn make-tray-icon!
  "建立SystemTray实例并添加到系统托盘显示"
  [args option]
  (when (tray-supported?)
    (reset! tray-icon (gen-tray-icon option))))

(defn reset-image!
  "重新设置SystemTray的Icon，可以实现动态效果"
  [image]
  (when-not (nil? @tray-icon)
    (.setImage tray-icon image)))

(defn reset-popue-menu!
  "重置SystemTray的弹出菜单"
  [popue-menu]
  (when-not (nil? @tray-icon)
    (.setPopupMenu tray-icon popue-menu)))

(defn delete-tray-icon!
  "移除SystemTray"
  []
  (when-not (nil? @tray-icon)
    (.remove (SystemTray/getSystemTray) tray-icon)))

(comment)