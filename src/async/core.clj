(ns async.core
  (:gen-class)
  (:require '[clojure.core.async :refer [chan go-loop >! <! timeout] :as async])
  (:import (javax.swing JFrame JButton JLabel JOptionPane)
           (java.awt BorderLayout)
           (java.awt.event ActionListener)))

(defn -main
  "Java Swing & core.async sample"
  [& args]
  (def ch (chan))
  (def frame (JFrame. ))
  (.setTitle frame "Hikazoh's Title")
  (.setSize frame 200 200 )
  (.setLocationRelativeTo frame nil)
  (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE)
  (def button (JButton. "Click Me!"))
  (.addActionListener button
   (reify ActionListener
     (actionPerformed [this e]
       (JOptionPane/showMessageDialog frame (JLabel. "Clicked!!")))))
  (.add frame button BorderLayout/NORTH)
  (.setVisible frame true)
  (go-loop[]
    (when-let [d (<! char)]
      (recur))))
