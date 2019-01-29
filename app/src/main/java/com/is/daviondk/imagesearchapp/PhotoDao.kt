package com.`is`.daviondk.imagesearchapp

import android.arch.persistence.room.*


@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
    fun getAll(): List<Photo>


    @Query("SELECT * FROM photo WHERE id = :id")
    fun getById(id: String?): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)

    @Update
    fun update(photo: Photo)

    @Delete
    fun delete(photo: Photo)

    @Query("SELECT COUNT(*) FROM photo WHERE id = :id")
    fun exist(id: String?): Boolean

}