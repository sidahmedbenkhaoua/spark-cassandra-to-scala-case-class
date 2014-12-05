package com.bradkarels.simple

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import com.datastax.spark.connector._
import com.datastax.spark.connector.rdd._

object CaseStudy {
  
  // Our simple case class for human.
  case class Human(
    id: String,
    firstname: Option[String],
    lastname: Option[String],
    isGood: Boolean
  )
  
  def main(args: Array[String]) {
    val conf = new SparkConf(true)
      .set("spark.cassandra.connection.host", "127.0.0.1")

    val sc = new SparkContext("spark://127.0.0.1:7077", "simple", conf)
    
    val rdd:CassandraRDD[CassandraRow] = sc.cassandraTable("nicecase", "human")
    
    val humans = rdd.toArray.map { row:CassandraRow =>
      // To display your column names for this row uncomment the following:
      //println(row.columnNames) // You can imagine there will be some cool things to do with the column names for a NoSQL row.
      // ...or show them individually:
      //row.columnNames.toArray.foreach(println)
      
      // As most of our fields are nullable we'll fetch Options in most cases.
      val id:String                    = row.getString("id")
      val firstname:Option[String]     = row.get[Option[String]]("firstname") // One way to get String Option
      val lastname:Option[String]      = row.getStringOption("lastname") // Another way to get String Option
      val isGoodPerson:Option[Boolean] = row.getBooleanOption("isGoodPerson")
      val isGood = isGoodPerson match {
        case Some(x) => x
        case _ => false
      }
      
      Human(id,firstname,lastname,isGood)
    }
    
    // Now let us crudely see who is good and who is not...
    humans.foreach { human:Human =>
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

