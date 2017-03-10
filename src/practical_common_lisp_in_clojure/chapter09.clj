(ns practical-common-lisp-in-clojure.chapter09
  (:require [clojure.pprint :refer [cl-format]]))

(defn test-+ []
  (and
   (= (+ 1 2) 3)
   (= (+ 1 2 3) 6)
   (= (+ -1 -3) -4)))

;(test-+)

(defn test-+ []
  (cl-format true "~:[FAIL~;pass~] ... ~a~%" (= (+ 1 2) 3) '(= (+ 1 2) 3))
  (cl-format true "~:[FAIL~;pass~] ... ~a~%" (= (+ 1 2 3) 6) '(= (+ 1 2 3) 6))
  (cl-format true "~:[FAIL~;pass~] ... ~a~%" (= (+ -1 -3) -4) '(= (+ -1 -3) -4)))

;(test-+)

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
     ~@(for [form forms]
         `(report-result ~form (quote ~form)))))

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

(defmacro combine-results [& forms]
  `(every? identity (list ~@forms)))

;(combine-results
; (= (+ 1 2) 3)
; (= (+ 1 2 3) 6)
; (= (+ -1 -3) -4))

(defmacro check [& forms]
  `(combine-results
    ~@(for [form forms]
        `(report-result ~form (quote ~form)))))

; redefine test-+ to make use of new check macro
(defn test-+ []
  (check
   (= (+ 1 2) 2)
   (= (+ 1 2 3) 6)
   (= (+ -1 -3) -4)))

;(test-+)
