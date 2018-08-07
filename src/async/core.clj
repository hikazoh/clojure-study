(ns async.core
  (:gen-class)
  (:require [clojure.core.async :refer [chan go-loop go >! <! timeout] :as async])
  (:import (javax.swing JFrame JButton JLabel JOptionPane SwingUtilities JPanel)
           (java.awt BorderLayout Color)
           (java.awt.event ActionListener)
           (java.util Date)))

(defn -main
  "Java Swing & core.async sample"
  [& args]
  (def ch (chan))
  (def data (ref (.toString (Date.))))
  (def color (ref Color/black))
  (def frame (JFrame. "hikazoh's test frame"))
  (def panel
    (proxy [JPanel] []
      (paintComponent[g]
        (do
          (.setColor g Color/white )
          (.fillRect g 0 0 500 500)
          (.setColor g @color)
          (.drawString  g @data 100 100 )))))
  (.setTitle frame "Hikazoh's Title")
  (.setSize frame 200 200 )
  (.setLocationRelativeTo frame nil)
  (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE)
  (def button (JButton. "Click Me!"))
  (.addActionListener button
   (reify ActionListener
     (actionPerformed [this e]
       (do
         (JOptionPane/showMessageDialog frame (JLabel. "Change Color"))
         (dosync (ref-set color Color/red))))))

  (.add frame button BorderLayout/NORTH)
  (.add frame panel BorderLayout/CENTER)
  (go-loop[]
    (when (>! ch (.toString (Date.)))
      (Thread/sleep 100)
      (recur)))
  (go-loop[]
    (when-let [d (<! ch)]
      (dosync (ref-set data d))
      (SwingUtilities/updateComponentTreeUI frame)
      (recur)))
  (.setVisible frame true))

