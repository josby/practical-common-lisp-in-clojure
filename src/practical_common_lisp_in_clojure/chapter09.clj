(ns practical-common-lisp-in-clojure.chapter09
  (:require [clojure.pprint :refer [cl-format]]))

(defn test-+ []
  (and
   (= (+ 1 2) 3)
   (= (+ 1 2 3) 6)
   (= (+ -1 -3) -4)))

;(test-+)

(defn test-+ []
  ;(format t "~:[FAIL~;pass~] ... ~a~%" (= (+ 1 2) 3) '(= (+ 1 2) 3))
  ) ;TODO

(defn report-result [result form]
  (cl-format true "~:[FAIL~;pass~] ... ~a~%" result form))

(defn test-+ []
  (report-result (= (+ 1 2) 3) '(= (+ 1 2) 3))
  (report-result (= (+ 1 2 3) 6) '(= (+ 1 2 3) 6))
  (report-result (= (+ -1 -3) -4) '(= (+ -1 -3) -4)))

;(test-+)

(defmacro check [form]
  `(report-result ~form (quote ~form)))

;(check (= (+ 1 2) 3))

(defn test-+ []
  (check (= (+ 1 2) 3))
  (check (= (+ 1 2 3) 6))
  (check (= (+ -1 -3) -4)))

;(test-+)

(defmacro check [& forms]
  `(do
     ~@(for [form forms] `(report-result ~form (quote ~form)))))

(defn test-+ []
  (check
   (= (+ 1 2) 3)
   (= (+ 1 2 3) 6)
   (= (+ -1 -3) -4)))

;(test-+)

(defn report-result [result form]
  (cl-format true "~:[FAIL~;pass~] ... ~a~%" result form)
  result)

;(report-result (= (+ 1 2) 3) '(= (+ 1 2) 3))