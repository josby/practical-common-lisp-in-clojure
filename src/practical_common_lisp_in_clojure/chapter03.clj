(ns practical-common-lisp-in-clojure.core)

(defn make-cd [title artist rating ripped]
  {:title  title,
   :artist artist
   :rating rating
   :ripped ripped})

;(make-cd "Roses" "Kathy Mattea" 7 true)

(def db (atom '()))

(defn add-record [cd]
  (swap! db (fn [prev-db-state]
              (conj prev-db-state cd))))

;(add-record (make-cd "Roses" "Kathy Mattea" 7 true))
;(add-record (make-cd "Fly" "Dixie Chicks" 8 true))
;(add-record (make-cd "Home" "Dixie Chicks" 9 true))

(defn dump-db []
  (doseq [cd @db] ;for each record in the db
    (doseq [keyval cd] ;for each key value pair in a record
      (println (apply format "%-10s %s" [(key keyval) (val keyval)])))
    (newline)))

;(dump-db)

(defn promt-read [promt]
  (println (format "%s:" promt))
  (read-line))

;(promt-read "Please enter your name:")

(defn promt-for-cd []
  (make-cd
   (promt-read "Title")
   (promt-read "Artist")
   (promt-read "Rating")
   (promt-read "Ripped [y/n]")))

(defn parse-integer [s]
  (when-let [found-number (re-find  #"\d+" s)]
    (Integer/parseInt found-number)))

(defn y-or-n-p [msg]
  (loop [user-input (promt-read msg)]
    (if-let [y-or-n-as-string (re-matches #"[yn]" user-input)]
      (= "y" y-or-n-as-string)
      (recur (promt-read "Please answer with y or n")))))

(defn promt-for-cd []
  (make-cd
   (promt-read "Title")
   (promt-read "Artist")
   (parse-integer (promt-read "Rating"))
   (y-or-n-p "Ripped [y/n]")))

;(promt-for-cd)

(defn add-cds []
  (let [created-record (promt-for-cd)]
    (loop [record-to-add created-record]
      (add-record record-to-add) ;add record to db
      (when (y-or-n-p "Add another one?")
       (recur (promt-for-cd))))))

;(add-cds)

;Saving and Loading the Database

(defn save-db [filename]
  (let [serialized-db-state (prn-str @db)]
    (spit filename serialized-db-state)))

;(save-db "/home/josby/my-cds.db")

(defn load-db [filename]
  (let [loaded-db-state (clojure.edn/read-string (slurp filename))]
    (reset! db loaded-db-state)))

;(load-db "/home/josby/my-cds.db")


;Querying the Database

(defn select-by-artist [artist]
  (filter (fn[cd] (= artist (:artist cd))) @db))

;(select-by-artist "Dixie Chicks")

(defn select [selector-fn]
  (filter selector-fn @db))

(select (fn [cd] (= (:artist cd) "Dixie Chicks")))

(defn artist-selector [artist]
  (fn [cd] (= (:artist cd) artist)))
;(select (artist-selector "Dixie Chicks"))


(defn where [& {:keys [title artist rating ripped]}]
  (fn [cd]
    (and
     (if title (= (:title cd) title) true)
     (if artist (= (:artist cd) artist) true)
     (if rating (= (:rating cd) rating) true)
     (if (some? ripped) (= (:ripped cd) ripped) true))))

;(select (where :artist "Dixie Chicks"))
;(select (where :rating 5))

(defn update [selector-fn & {:keys [title artist rating ripped]}]
  (map (fn[row]
         (when (selector-fn row)
           (print row))
         row) @db))

;(update (where :artist "Dixie Chicks") :rating 11)