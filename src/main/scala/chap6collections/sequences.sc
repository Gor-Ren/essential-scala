// sequences are ordered, stable collections of elements
val seq = Seq(1, 2, 3)  // creates a list, behind the Seq interface

// access els
seq.head
seq.tail
seq(1)

Seq().headOption  // access possibly empty head

seq.length

// search for element
seq.contains(2)
seq.find(_ == 3)  // returns Option of index
seq.find(_ > 999)  // None
seq.filter(_ > 1)  // all matching elements

// sort
seq.sortWith(_ > _)  // input binary comparison func

// append / prepend elements
seq.:+(4)  // append
seq :+ 4  // more idiomatic to use infix

seq.+:(0)  // prepend
0 +: seq  // infix prepend; N.B. trailing colon makes operator
          // right-associative! seq +: 0 will not compile!

seq ++ Seq(3, 4, 5)  // concat sequences

// general syntax rule: methods ending in ':' are right associative when
// written as an infix operator -- replicates Haskell style operators

/* Lists */
// default Seq is a linked List; some libraries work with List directly
Nil  // empty list singleton object

// construct list by prepending elements
val list = 1 :: 2 :: 3 :: Nil  // :: is equiv to +::

4 :: 5 :: list

// List.apply is a more convenient constructor
List(1, 2, 3)