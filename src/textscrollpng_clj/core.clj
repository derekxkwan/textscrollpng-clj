(ns textscrollpng-clj.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def bg-color 0)
(def text-color 255)
(def scroll-str "TEST-STRING")
(def font-sz 110)
(def text-gap 5)
(def base-speed 50)
(def base-dim {:w 2048 :h 512})

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 24)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ;(q/color-mode :hsb)
  (q/text-font (q/create-font "Liberation Mono Bold" font-sz))

  ; setup function returns initial state. It contains
  ; circle color and position.
  (let [cur-speed (* base-speed (+ 1 (rand-int 3)))
        num-frames (quot (get base-dim :w) cur-speed)
        cur-gap (+ font-sz text-gap)
        num-rows (+ (quot (get base-dim :h) cur-gap) 2)
        cur-pos (mapv (fn [n]
                        (let [cur-h (* (inc n) cur-gap)]
                          {:x (rand (get base-dim :h))
                           :y cur-h}
                          ))
                      (range num-rows)
                      )
        ]
    {:pos cur-pos :num-rows num-rows :speed cur-speed :num-frames num-frames :cur-frame 0}
  )
  )

(defn update-state [state]

  ; Update sketch state by changing circle color and position.
  (let [cur-speed (get state :speed)
        cur-pos (get state :pos)
        cur-frame (get state :cur-frame)
        num-rows (get state :num-rows)
        num-frames (get state :num-frames)
        cur-filename (str "pixout/txt-" cur-frame ".png")
        ]
    (when (< cur-frame num-frames) (q/save-frame cur-filename))

    {:pos (mapv (fn [i] 
                         {:x (mod (+ cur-speed (get i :x)) (get base-dim :w))
                         :y (get i :y)}
                         ) cur-pos)
     :speed cur-speed
     :num-rows num-rows
     :num-frames num-frames
     :cur-frame (inc cur-frame)
     }

    )
  )

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background bg-color)
  ; Set circle color.
  (q/fill text-color)
  ; Calculate x and y coordinates of the circle.
 (let [cur-speed (get state :speed)
        cur-pos (get state :pos)
        cur-frame (get state :cur-frame)
        num-rows (get state :num-rows)
        num-frames (get state :num-frames)
        ]
   (doall (map
           (fn [i]
             (let [cur-x (get i :x)
                   cur-y (get i :y)]
               (q/text scroll-str (- cur-x (get base-dim :w)) cur-y)
               (q/text scroll-str cur-x cur-y)
               )
             ) cur-pos
           ))
   
   )
  state
  )

(q/defsketch arblowtxr-clj
  :title "textscrollpng"
  :size [(get base-dim :w) (get base-dim :h)]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
