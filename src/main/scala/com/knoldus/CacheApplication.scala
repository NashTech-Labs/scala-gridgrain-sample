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
  val cache = ignite.getOrCreateCache("tripleCache")
  cache.loadCache(null)
  val cursor = cache.query(new SqlFieldsQuery(
    "select entity, pred1, val1 from Triple;"))
  println(cursor.getAll())
}

class TripleStore extends CacheStoreAdapter[Long, Triple] with CacheStore[Long, Triple]{

  @SpringResource(resourceName = "dataSource")
  private var dataSource: DataSource = _

  override def loadCache(clo: IgniteBiInClosure[Long, Triple], @Nullable objects: Object*): Unit = {

    println(">> Loading cache from store...")

    val conn = dataSource.getConnection()

    val st = conn.prepareStatement("select * from dph")
    val rs: ResultSet = st.executeQuery()

    while (rs.next()) {

      val triple = Triple(rs.getString(1), rs.getString(3), rs.getString(4))
      println("Triple here is :::::", triple)
      clo.apply(rs.getInt(9).toLong, triple)

    }
  }

  override def load(key: Long): Triple = {
    println(">> Loading triple from store...")

    val conn = dataSource.getConnection()
    val st = conn.prepareStatement("select * from dph where id = ?")
    st.setLong(1, key)
    val rs = st.executeQuery()
    val ss = if (rs.next()) Triple(rs.getString(1), rs.getString(2), rs.getString(3)) else null
    println("ss data in load :", ss)
    ss
  }

  override def delete(a: Object): Unit = {}


  override def write(e: Cache.Entry[_ <: Long, _ <: Triple]): Unit = {}

}