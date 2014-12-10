package com.bradkarels.simple

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import com.datastax.spark.connector._
import com.datastax.spark.connector.rdd._

object CaseStudy {

  // Our simple case classes for human types.
  case class SubHuman(id:String, firstname:String, lastname:String, isGoodPerson:Boolean)
  
  case class Human(
    id: String,
    firstname: Option[String],
    lastname: Option[String],
    isGood: Boolean
  )
  
  /**
   * In this example we will fetch a bunch of humans from the database and print a "report" about who is good and who
   * is not.  As you will see below we will do this two different ways.  One, a very concise way to pull data direct
   * into a case class.  The other, a bit more verbose, but often necessary set of steps to achieve the same result.
   */
  def main(args: Array[String]) {
    val conf = new SparkConf(true)
      .set("spark.cassandra.connection.host", "127.0.0.1")

    val sc = new SparkContext("spark://127.0.0.1:7077", "simple", conf)
    
    // We will fetch all humans from Cassandra, but we wil only be fetching a subset of the human table by limiting with
    // the use of 'select'--------------------------------------------------------->
    val subHumans:Array[SubHuman] = sc.cassandraTable[SubHuman]("nicecase", "human").select("id","firstname","lastname","isGoodPerson").toArray
    
    // Now that we have an array of 'sub-humans' we can quickly rip through and find who is good and who is not.
    subHumans.foreach { subHuman:SubHuman =>
      if (subHuman.isGoodPerson) 
        println("%s %s IS a good person.".format(subHuman.firstname, subHuman.lastname))
      else
        println("%s %s IS NOT a good person.".format(subHuman.firstname, subHuman.lastname))
    }
    
    /*
     * In the following section we are going to perform the same operation, but we are going to do so in a bit more
     * verbose way.  For more complex operations, filtering out bad data, dealing with missing bits (null), and other 
     * things of that nature, being able to do things like this is not only good, but will likely be necessary in the 
     * world of real data.
     */
    val rdd:CassandraRDD[CassandraRow] = sc.cassandraTable("nicecase", "human")
    
    val humans = rdd.toArray.map { row:CassandraRow =>
      // To display your column names for this row uncomment the following:
      //println(row.columnNames) // You can imagine there will be some cool things to do with the column names for a NoSQL row.
      // ...or show them individually:
      //row.columnNames.toArray.foreach(println)
      
      // As most of our fields are nullable we'll fetch Options in most cases.  ...and do that a couple different ways.
      val id:String                    = row.getString("id")
      val firstname:Option[String]     = row.get[Option[String]]("firstname") // One way to get String Option
      val lastname:Option[String]      = row.getStringOption("lastname") // Another way to get String Option
      val isGoodPerson:Option[Boolean] = row.getBooleanOption("isGoodPerson")
      // Here we'll make sure we have a boolean to work with.  Operations like this may also enforce any other client 
      // side rules (e.g. validate numbers stored as Strings)
      val isGood = isGoodPerson match {
        case Some(x) => x
        case _ => false
      }
      
      // And now we create our human...
      Human(id,firstname,lastname,isGood)
    }
    
    // Now, again, let us crudely see who is good and who is not...
    humans.foreach { human:Human =>
      // Null safety first
      val fname:String = human.firstname match {
        case Some(name) => name
        case _ => "Unknown"
      }
      
      val lname:String = human.lastname match {
        case Some(name) => name
        case _ => "Unknown"
      }
      
      if (human.isGood)
        println("%s %s is a good person.".format(fname, lname))
      else
        println("%s %s is not a good person.".format(fname, lname))
    }
  }
}

