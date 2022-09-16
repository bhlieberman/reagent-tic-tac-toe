(ns app.hello
  (:require [reagent.core :as r]))

(defn calculate-winner [squares]
  (let [winners [[0 1 2] [3 4 5]
                 [6 7 8] [0 3 6]
                 [1 4 7] [2 5 8]
                 [0 4 8] [2 4 6]]
        idx (partial nth squares)]
    (first (mapcat (fn [[a b c]] (when (and (some? (idx a))
                                            (= (idx a) (idx b))
                                            (= (idx a) (idx c)))
                                   (str (idx a)))) winners))))

(defn board []
  (let [init (into [] (repeat 9 nil))
        state (r/atom init)
        winner (r/atom nil)
        x-is-next (r/atom true)
        handler (fn [i]
                  (swap! state assoc i (if @x-is-next \X \O))
                  (swap! x-is-next not)
                  (swap! winner #(calculate-winner @state)))
        render-square (fn [i] [:button
                               {:class-name "square"
                                :on-click (fn [_evt] (handler i) 
                                            #(.removeEventListener js/document "click" handler))}
                               (nth @state i)])]
    (fn [] [:div
            [:div {:class-name "status"}
             (if (some? @winner)
               (str "Winner is: " @winner)
               (str "Next player is:" (if @x-is-next \X \O)))]
            (for [idx (partition 3 (range 0 9))]
              (into [:div {:class-name "board-row"}]
                    (map (fn [i] [render-square i]) idx)))])))

(defn game []
  [:div {:class-name "game"}
   [:div {:class-name "game-board"}
    [board]]
   [:div {:class-name "game-info"}
    [:div]
    [:ol]]])

(comment)
