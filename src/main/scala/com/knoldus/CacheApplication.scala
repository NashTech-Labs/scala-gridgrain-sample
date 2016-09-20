package com.knoldus

import java.sql.ResultSet
import javax.cache.Cache
import javax.sql.DataSource

import org.apache.ignite.{IgniteCache, Ignition}
import org.apache.ignite.cache.query.{SqlFieldsQuery, QueryCursor}
import org.apache.ignite.cache.store.CacheStore
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.apache.ignite.lang.IgniteBiInClosure
import org.apache.ignite.resources.SpringResource
import org.gridgain.grid.configuration.GridGainConfiguration
import org.jetbrains.annotations.Nullable



/**
  * Created by shivansh on 22/8/16.
  */
object CacheApplication extends App {

  Ignition.setClientMode(true)
  val gridCfg = new GridGainConfiguration().setRollingUpdatesEnabled(true)
  val ignite = Ignition.start("src/main/resources/example-ignite.xml")
  val cache = ignite.getOrCreateCache("departmentCache")
  cache.loadCache(null)
  val cursor = cache.query(new SqlFieldsQuery(
    "select deptId, deptname, clientId from Department;"))
  println(cursor.getAll())
}

object CacheApplication2 extends App {

  Ignition.setClientMode(true)
  val gridCfg = new GridGainConfiguration().setRollingUpdatesEnabled(true)
  val ignite = Ignition.start("src/main/resources/example2-ignite.xml")
  val cache = ignite.getOrCreateCache("employeeCache")
  cache.loadCache(null)
  val cursor2=cache.query(new SqlFieldsQuery(
    "select empId, deptId, empName, clientId from Employee;"))
  println(cursor2.getAll())
}

case class Department(deptId: Int, deptName: String, clientId: String)


class DepartmentStore extends CacheStoreAdapter[Int, Department] with CacheStore[Int, Department] {

  @SpringResource(resourceName = "dataSource")
  private var dataSource: DataSource = _

  override def loadCache(clo: IgniteBiInClosure[Int, Department], @Nullable objects: Object*): Unit = {

    println(">> Loading cache from store...")

    val conn = dataSource.getConnection()

    val st = conn.prepareStatement("select * from Department")
    val rs: ResultSet = st.executeQuery()

    while (rs.next()) {

      val triple = Department(rs.getInt(1), rs.getString(2), rs.getString(3))
      println("Triple here is :::::", triple)
      clo.apply(rs.getInt(1), triple)

    }
  }

  override def load(key: Int): Department = {
    println(">> Loading triple from store...")

    val conn = dataSource.getConnection()
    val st = conn.prepareStatement("select * from Department where deptId = ?")
    st.setInt(1, key)
    val rs = st.executeQuery()
    val ss = if (rs.next()) Department(rs.getInt(1), rs.getString(2), rs.getString(3)) else null
    println("ss data in load :", ss)
    ss
  }

  override def delete(a: Object): Unit = {}


  override def write(e: Cache.Entry[_ <: Int, _ <: Department]): Unit = {}

}
