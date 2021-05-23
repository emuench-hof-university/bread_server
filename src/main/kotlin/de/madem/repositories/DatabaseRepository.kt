package de.madem.repositories

import de.madem.model.DBDish
import de.madem.model.database.DBDishTable
import de.madem.system.Environment
import org.ktorm.database.Database
import org.ktorm.entity.map
import org.ktorm.entity.sequenceOf
import java.sql.DriverManager

object DatabaseRepository {
    private val database : Database
    val users: DbUserRepository

    init {
        database = Database.connect{
            DriverManager.getConnection(getDatabaseConnectionUrl())
        }
        users = DbUserRepository(database)
    }

    fun test() : List<DBDish>{
        return database.sequenceOf(DBDishTable).map { it }
    }

    //#region private functions
    private fun getDatabaseConnectionUrl() : String{


        val baseUrl = "jdbc:mysql://localhost:3306/breaddb"
        val urlParams = mapOf<String,Any>(
            "user" to Environment.dbuser,
            "password" to Environment.dbpwd,
            "driver" to "com.mysql.jdbc.Driver",
            "useSSL" to false,
            "serverTimezone" to "Europe/Berlin",
            "allowPublicKeyRetrieval" to true
        )
        //"verifyServerCertificate" to false,
        //"useSSL" to true,
        //"requireSSL" to true,
        //TODO: Add valid user + password and ensure that ssl works

        return "$baseUrl?${urlParams.toList().joinToString("&"){ (key,value) -> "$key=$value" }}"
    }
    //#endregion
}