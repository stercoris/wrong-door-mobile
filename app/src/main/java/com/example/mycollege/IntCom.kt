package com.example.mycollege

import com.example.mycollege.ui.home.request
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


const val apisite = "http://wrongdoor.ddns.net/"

class IntCom {
    var secretkey : String

    constructor(uid: String)
    {
        secretkey = uid
    }

    fun getStudents(): List<Student> {
        val mURL = URL("${apisite}college/getStudents/index.php?")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println(response.toString())
                return Gson().fromJson(response.toString(),object : TypeToken<List<Student>>() {}.type)
            }
        }
    }

    fun getGroupToId() : Map<String,String> {
        val mURL = URL("${apisite}college/getGroupToId/")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                var groups : Map<String,String> = Gson().fromJson(response.toString(),object : TypeToken<Map<String,String>>() {}.type)
                return groups
            }
        }
    }


    fun getTeachers() : List<Teacher>
    {
        val mURL = URL("${apisite}college/getTeacherToId/")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush()
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                var teachers : List<Teacher> = Gson().fromJson(response.toString(),object : TypeToken<List<Teacher>>() {}.type)
                return teachers
            }
        }
    }

    fun getAccess() : Boolean {
        var reqParam = URLEncoder.encode("uniqueid", "UTF-8") + "=" + URLEncoder.encode(secretkey, "UTF-8")
        val mURL = URL("${apisite}api/checkaccess?$reqParam")
        println("Запрос на получение допуска")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                return response.toString() == "granted"
            }
        }
    }

    fun send(com: Message){
        Thread {
            var message = com;
            var reqParam =  URLEncoder.encode("uniqueid", "UTF-8") + "=" + URLEncoder.encode(secretkey, "UTF-8")
                reqParam += "&" + URLEncoder.encode("com", "UTF-8") + "=" + URLEncoder.encode(com.body, "UTF-8")
                reqParam += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("${com.type}", "UTF-8")


            val mURL = URL("${apisite}api/add?$reqParam")
            with(mURL.openConnection() as HttpURLConnection) {
                requestMethod = "POST"

                val wr = OutputStreamWriter(outputStream);
                wr.flush();
                if(responseCode == 200){
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                    }
                }
            }
        }.start()
    }



    fun getChat() : List<UsernameToMsg>{
        var reqParam = URLEncoder.encode("uniqueid", "UTF-8") + "=" + URLEncoder.encode(secretkey, "UTF-8")
        val mURL = URL("${apisite}api/getChat?$reqParam")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()

                //Перевод джсон объекта в лист класса UsernameToMsg
                var messages : List<UsernameToMsg> = Gson().fromJson(response.toString(),object : TypeToken<List<UsernameToMsg>>() {}.type)
                return(messages)
            }
        }
    }

    fun getOutput() : List<UsernameToMsg>{
        var reqParam = URLEncoder.encode("uniqueid", "UTF-8") + "=" + URLEncoder.encode(secretkey, "UTF-8")
        val mURL = URL("${apisite}api/getOutput?$reqParam")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()

                //Перевод джсон объекта в лист класса UsernameToMsg
                var messages : List<UsernameToMsg> = Gson().fromJson(response.toString(),object : TypeToken<List<UsernameToMsg>>() {}.type)
                return(messages)
            }
        }
    }


    fun isOnline(): Boolean{
        val mURL = URL("${apisite}api/isOnline?")
        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream);
            wr.flush();
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                return if(response.toString() == "Online") (true) else (false)

            }
        }
    }



    class Student
    {
        var name: String = ""
        var groupid: String = ""
        var id: String = ""
    }

    class Teacher()
    {
        var id: String = ""
        var secname: String = ""
        var name: String = ""
    }

}

class UsernameToMsg()
{
    var username: String = "";
    var message: String = "";
}

class Message()
{
    constructor(body: String,id: Int) : this() {
        this.body = body
        this.type = id
    }
    var body: String = ""
    var type: Int = 0
}

// ХУяль оно в жопе от основного блока, как сделать так, чтоб можно былло без ссылки обращаться к вложенному классу!!!!! Пока тока так
class  MessageType{
    companion object {
        const val CMD: Int = 0
        const val PsExec: Int = 1
        const val PsLogOn: Int = 2
        const val Text: Int = 3
    }
}