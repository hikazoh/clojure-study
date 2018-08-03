(ns async.core
  (:gen-class)
  (:require [clojure.core.async :refer [chan go-loop go >! <! timeout] :as async])
  (:import (javax.swing JFrame JButton JLabel JOptionPane)
           (java.awt BorderLayout)
           (java.awt.event ActionListener)
           (java.util Date)))

(defn -main
  "Java Swing & core.async sample"
  [& args]
  (def ch (chan))
  (def data (ref (.toString (Date.))))
  (def frame
    (proxy [JFrame] []
      (paint[g]
        (.drawString  g @data 10 10))))
  (.setTitle frame "Hikazoh's Title")
  (.setSize frame 200 200 )
  (.setLocationRelativeTo frame nil)
  (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE)
  (def button (JButton. "Click Me!"))
  (.addActionListener button
   (reify ActionListener
     (actionPerformed [this e]
       (do
         (JOptionPane/showMessageDialog frame (JLabel. "Clicked!!"))
         (go
           (>! ch "Clicked"))))))
  (.add frame button BorderLayout/NORTH)
  (.setVisible frame true)
  (go-loop[]
    (when-let [d (<! ch)]
      (dosync (ref-set data d))
      (.repaint frame 1 0 0 200 200)
      (recur))))


