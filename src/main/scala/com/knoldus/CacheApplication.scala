package com.knoldus

import java.util
import javax.sql.DataSource

import org.apache.ignite.cache.store.CacheStore
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.apache.ignite.lang.IgniteBiInClosure
import org.apache.ignite.resources.SpringResource
import org.jetbrains.annotations.Nullable


/**
  * Created by shivansh on 22/8/16.
  */
object CacheApplication {

}

class TripleStore extends CacheStoreAdapter[Long, Triple] with CacheStore[Long, Triple]{

  @SpringResource(resourceName = "dataSource")
  private var dataSource: DataSource = _

  override def loadCache(clo: IgniteBiInClosure[Long, Triple], @Nullable objects: Object*): Unit = {

    System.out.println(">> Loading cache from store...")

    val conn = dataSource.getConnection()

    val st = conn.prepareStatement("select * from PERSON")
    val rs = st.executeQuery()


    while (rs.next()) {

      val triple = Triple(rs.getString(1), rs.getString(3), rs.getString(4))
      clo.apply(rs.getInt(9).toLong, triple)

    }
  }

  override def load(key: Long): Triple = {
    System.out.println(">> Loading person from store...");

    val conn = dataSource.getConnection()
    val st = conn.prepareStatement("select * from dph where id = ?")
    st.setLong(1, key)
    val rs = st.executeQuery()
    val ss = if (rs.next()) new Triple(rs.getString(1), rs.getString(2), rs.getString(3)) else null
    ss
  }

  override def deleteAll(list: java.util.Collection[_]): Unit = {}

}