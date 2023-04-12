(ns hackerio.world.data)

(def default-world {
  :tick 0
  :objects [
    {:id "abc-123" 
     :type :drone
     :x 44.5632
     :y 23.4565
     :target-x 2.3
     :target-y 20.3
     :speed 4.5
     :sensor-radiation 0}
    {:id "abc-124"
     :type :drone
     :x 10
     :y 10
     :target-x 2.3
     :target-y 20.3
     :speed 4.6
     :sensor-radiation 0}
    {:id "abc-125"
     :type :drone
     :x 34.0 
     :y -23.0
     :target-x 2.3
     :target-y 20.3
     :direction 0.0
     :speed 4.6
     :sensor-radiation 0}
    {:id "abc-346"
     :type :object
     :radioactive 192.04
     :x 33.12
     :y -21.01
     :target-x 33.12
     :target-y -21.01
     :speed 0}
  ]
})


