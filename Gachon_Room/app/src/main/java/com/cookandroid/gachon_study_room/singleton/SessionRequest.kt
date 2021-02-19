package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.android.volley.RequestQueue
import kotlinx.coroutines.*
import java.io.*
import java.net.*


object SessionRequest {
    private lateinit var que: RequestQueue
    var imgBitmap: Bitmap? = null
    private lateinit var session: Job
    fun request(context: Context, img: ImageView) = runBlocking {
        var sessionId = "_ga=GA1.3.191231926.1591856132; _gid=GA1.3.1356681213.1613715483"
        var manager: CookieManager
        var cManager: CookieManager
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                println("로그인연결중")
                val url = URL("https://sso.gachon.ac.kr/svc/tk/Login.do")
                var urlConn = url.openConnection() as HttpURLConnection

                // urlConn 설정
                urlConn.requestMethod = "POST"
                urlConn.setRequestProperty(
                        "Content_Type",
                        "application/x-www-form-urlencoded"
                )
//            urlConn.setRequestProperty("Cookie",sessionId)
                urlConn.doOutput = true

                val osw = OutputStreamWriter(urlConn.outputStream)
                var sendMsg = "user_id=${MySharedPreferences.getUserId(context)}&user_password=${MySharedPreferences.getUserPass(context)}&id=www"
                osw.write(sendMsg)
                osw.flush()

                val cookiesTest: Map<*, *> = urlConn.headerFields
                val headerKeys = arrayOfNulls<String>(cookiesTest.size)
                val headerValues = arrayOfNulls<String>(cookiesTest.size)
                for (i in 0 until cookiesTest.size) {
                    headerKeys[i] = urlConn.getHeaderFieldKey(i)
                    headerValues[i] = urlConn.getHeaderField(i)
                    println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
                }

                manager = CookieManager()
                CookieHandler.setDefault(manager)

                if (manager.cookieStore.cookies.size > 0) {
                    System.out.println("From cookieManager : " + manager.cookieStore.cookies);
                }

                if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
                    print("로그인성공")
                    try{
                        val url = URL("https://info.gachon.ac.kr/exsignon/sso/sso_index.jsp")
                        val conn = (url.openConnection() as HttpURLConnection).apply {
                            requestMethod = "POST"
                            setRequestProperty(
                                    "Content_Type",
                                    "application/x-www-form-urlencoded"
                            )

                        }
                        conn.connect()
                        val cookiesTest: Map<*, *> = conn.headerFields
                        val headerKeys = arrayOfNulls<String>(cookiesTest.size)
                        val headerValues = arrayOfNulls<String>(cookiesTest.size)
                        for (i in 0 until cookiesTest.size) {
                            headerKeys[i] = conn.getHeaderFieldKey(i)
                            headerValues[i] = conn.getHeaderField(i)
                            println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
                        }

                        cManager = CookieManager()
                        CookieHandler.setDefault(cManager)


                        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                            println("hang 연결서공")
                            val streamReader = InputStreamReader(conn.inputStream)
                            val buffered = BufferedReader(streamReader)

                            val content = StringBuilder()
                            while (true) {
                                val line = buffered.readLine() ?: break
                                content.append(line)
                            }
                            print("연결")
                            Log.d("test", "학사행정" + content.toString())
                        }
                    } catch (e: Exception){Log.d("error", "error!")}

//                    val streamReader = InputStreamReader(urlConn.inputStream)
//                    val buffered = BufferedReader(streamReader)
//
//                    val content = StringBuilder()
//                    while (true) {
//                        val line = buffered.readLine() ?: break
//                        content.append(line)
//                    }
//                    print("연결")
//                Log.d("test", "로그인" + content)
                    // 스트림과 커넥션 해제
//                    Log.d("test", "receiveMsg $receiveMsg")
//                    buffered.close()
                    urlConn.disconnect()

                } else {    // 통신이 실패한 이유를 찍기위한 로그
                    print("통신 결과" + "에러" + urlConn.responseCode)
                }
            } catch (e: Exception) {
                print("error" + e.printStackTrace().toString() + "dd")
            }
        }
        job.join()
        val auth = CoroutineScope(Dispatchers.IO).launch {
            try {
                print("auth 연결중")
                val url = URL("https://sso.gachon.ac.kr/svc/tk/Auth.do?ifa=N&RelayState=%2Fexsignon%2Fsso%2Fsso_index.jsp&ac=N&id=www&")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty(
                            "Content_Type",
                            "application/x-www-form-urlencoded"
                    )

                }
                conn.connect()
                val cookiesTest: Map<*, *> = conn.headerFields
                val headerKeys = arrayOfNulls<String>(cookiesTest.size)
                val headerValues = arrayOfNulls<String>(cookiesTest.size)
                for (i in 0 until cookiesTest.size) {
                    headerKeys[i] = conn.getHeaderFieldKey(i)
                    headerValues[i] = conn.getHeaderField(i)
                    println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
                }
                manager = CookieManager()
                Log.d("test", "쿠키매니저 ${CookieHandler.setDefault(manager)}")
                CookieHandler.setDefault(manager)

                if (manager.cookieStore.cookies.size > 0) {
                    System.out.println("From cookieManager : " + manager.cookieStore.cookies);
                }

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    print("auth 연결서공")
                    val streamReader = InputStreamReader(conn.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    print("연결")
                    Log.d("test","Auth"+ content.toString())
                    conn.disconnect()
                }

            } catch (e: Exception) {
                println("오류")
            }
        }
        auth.join()

        var hang = CoroutineScope(Dispatchers.IO).launch {
            try{
            val url = URL("https://info.gachon.ac.kr/exsignon/sso/sso_index.jsp")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty(
                        "Content_Type",
                        "application/x-www-form-urlencoded"
                )

            }
            conn.connect()
            val cookiesTest: Map<*, *> = conn.headerFields
            val headerKeys = arrayOfNulls<String>(cookiesTest.size)
            val headerValues = arrayOfNulls<String>(cookiesTest.size)
            for (i in 0 until cookiesTest.size) {
                headerKeys[i] = conn.getHeaderFieldKey(i)
                headerValues[i] = conn.getHeaderField(i)
                println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
            }

            cManager = CookieManager()
            CookieHandler.setDefault(cManager)


            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                println("hang 연결서공")
                val streamReader = InputStreamReader(conn.inputStream)
                val buffered = BufferedReader(streamReader)

                val content = StringBuilder()
                while (true) {
                    val line = buffered.readLine() ?: break
                    content.append(line)
                }
                print("연결")
                Log.d("test", "학사행정" + content.toString())
            }
            } catch (e: Exception){Log.d("error", "error!")}
        }
        hang.join()
        var final = CoroutineScope(Dispatchers.IO).launch{
            try {
                var url = URL("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty(
                            "Content_Type",
                            "image/jpeg;charset=utf-8"
                    )

                }

                val cookiesTest: Map<*, *> = conn.headerFields
                val headerKeys = arrayOfNulls<String>(cookiesTest.size)
                val headerValues = arrayOfNulls<String>(cookiesTest.size)
                for (i in 0 until cookiesTest.size) {
                    headerKeys[i] = conn.getHeaderFieldKey(i)
                    headerValues[i] = conn.getHeaderField(i)
                    println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
                }

                manager = CookieManager()
                CookieHandler.setDefault(manager)

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    println("사진얻기 연결서공")
                    val streamReader = InputStreamReader(conn.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    print("연결")
                    Log.d("test", content.toString())
                    val `is`: InputStream = conn.inputStream
                  imgBitmap = BitmapFactory.decodeStream(`is`)
                    if(imgBitmap ==null) {
                        Log.d("test","널입니다.")
                    }
                }
            } catch (e: Exception){

            }
        }
        final.join()
        img.setImageBitmap(imgBitmap)
    }




}