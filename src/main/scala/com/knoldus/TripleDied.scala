package com.knoldus

import java.sql.ResultSet
import javax.cache.Cache
import javax.sql.DataSource

import org.apache.ignite.cache.store.{CacheStore, CacheStoreAdapter}
import org.apache.ignite.lang.IgniteBiInClosure
import org.apache.ignite.resources.SpringResource
import org.jetbrains.annotations.Nullable

/**
  * Created by shivansh on 22/8/16.
  */


case class Employee(empId: Int, empName: String, deptId: Int, clientId: String)

class EmployeeStore extends CacheStoreAdapter[Int, Employee] with CacheStore[Int, Employee] {

  @SpringResource(resourceName = "dataSource")
  private var dataSource: DataSource = _

  override def loadCache(clo: IgniteBiInClosure[Int, Employee], @Nullable objects: Object*): Unit = {

    println(">> Loading cache from store...")

    val conn = dataSource.getConnection()

    val st = conn.prepareStatement("select * from Employee")
    val rs: ResultSet = st.executeQuery()

    while (rs.next()) {

      val triple = Employee(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4))
      println("Triple here is :::::", triple)
      clo.apply(rs.getInt(1), triple)

    }
  }

  override def load(key: Int): Employee = {
    println(">> Loading triple from store...")

    val conn = dataSource.getConnection()
    val st = conn.prepareStatement("select * from Employee where deptId = ?")
    st.setInt(1, key)
    val rs = st.executeQuery()
    val ss = if (rs.next()) Employee(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4)) else null
    println("ss data in load :", ss)
    ss
  }

  override def delete(a: Object): Unit = {}


  override def write(e: Cache.Entry[_ <: Int, _ <: Employee]): Unit = {}

}
