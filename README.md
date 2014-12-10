spark-cassandra-to-scala-case-class
===================================

A simple example using Spark to pull data from Cassandra into a Scala case class, transforming that data, and writing back to Cassandra from a case class.

Currently having some issues using the spark-cassandra-connector to pull data direct into a case class as should be possible.  E.g.

```
case class WordCount(word: String, count: Int)
sc.cassandraTable[WordCount]("test", "words").toArray
```

Simply put I am getting the error:

```
No RowReaderFactory can be found for this type
```

I have posted up the following stackoverflow issue if anyone has any ideas:

http://stackoverflow.com/questions/27404736/no-rowreaderfactory-can-be-found-for-this-type-error-when-trying-to-map-cassandr

As this is a learning project - I'll clean all this out and have the working code out here as soon as I resolve this .

As is becoming normal for this series of examples, all things README are located:

http://spark-fu.blogspot.com/2014/12/spark-111-to-read-from-cassandra-into.html
