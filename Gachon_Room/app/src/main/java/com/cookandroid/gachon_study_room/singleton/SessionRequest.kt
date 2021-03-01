//package com.cookandroid.gachon_study_room.singleton
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.util.Log
//import android.widget.ImageView
//import com.android.volley.RequestQueue
//import kotlinx.coroutines.*
//import java.io.*
//import java.net.*
//
//
//object SessionRequest {
//    private lateinit var que: RequestQueue
//    var imgBitmap: Bitmap? = null
//    private lateinit var session: Job
//    var cookies = "JSESSIONID=2rNal8q0eAabTiMylZzEVfrDtfk5RfrLBQ5S69GxhvjRUksFJLfjNoNh9CfSsVYv.Z2NfZG9tYWluL25nY3dhczJfaGFrc2Ex"
//    fun request(context: Context, img: ImageView) = runBlocking {
//        var sessionId = "GCSessionId=03E4DC41A17395DB79D73752719754ED"
//        var manager: CookieManager
//        var cManager: CookieManager
//        val job = CoroutineScope(Dispatchers.IO).launch {
//            try {
//                println("로그인연결중")
//                var url = URL("https://sso.gachon.ac.kr/svc/tk/Login.do")
//                var urlConn = (url.openConnection() as HttpURLConnection).apply{
//                    // urlConn 설정
//                   requestMethod = "POST"
//                    setRequestProperty(
//                            "Content_Type",
//                            "application/x-www-form-urlencoded"
//                    )
////                    setRequestProperty("Cookie", "JSESSIONID=pIyaPxEFk5GJW1BxlIt1Zm092XyenHWfByayw9dBnxsfxu9lJEiho1nSQgGWeljZ.ngcsso2_servlet_engine1")
//                    setRequestProperty("Accept-Encoding", "gzip, deflate, br")
//                    setRequestProperty("Connection", "keep-alive")
//                    setRequestProperty("User-Agent", "PostmanRuntime/7.26.10")
//                    doOutput = true
//                    doInput = true
//                    useCaches = false
//                }
//                urlConn.connect()
//                val cookiesHeader: String = urlConn.getHeaderField("Set-Cookie")
//                Log.d("test","쿠키매너지 $cookiesHeader")
//                // 응답 헤더의 정보를 모두 출력
//                for ((key, value1) in urlConn.headerFields.entries) {
//                    if(key == "Set-Cookie") {
//                        cookies = value1.toString()
//                        Log.d("test", cookies)
//                    }
//                    for (value in value1) {
//                        Log.d("test", "첫번째 응답헤더 출력" + ("$key : $value"))
//                    }
//                }
//
//                val osw = OutputStreamWriter(urlConn.outputStream)
//                var sendMsg = "user_id=${MySharedPreferences.getUserId(context)}&user_password=${MySharedPreferences.getUserPass(context)}&id=www"
//                osw.write(sendMsg)
//                osw.flush()
//
//                manager = CookieManager()
//                CookieHandler.setDefault(manager)
//
//
//                if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
//
//                    Log.d("test", "로그인연결성공")
//                } else {    // 통신이 실패한 이유를 찍기위한 로그
//                    print("통신 결과" + "에러" + urlConn.responseCode)
//                }
//            } catch (e: Exception) {
//                print("error" + e.printStackTrace().toString() + "dd")
//            }
//        }
//        job.join()
////        val auth = CoroutineScope(Dispatchers.IO).launch {
////            try {
////                print("auth 연결중")
////                val url = URL("https://sso.gachon.ac.kr/svc/tk/Auth.do?ifa=N&RelayState=%2Fexsignon%2Fsso%2Fsso_index.jsp&ac=N&id=www&")
////                val conn = (url.openConnection() as HttpURLConnection).apply {
////                    requestMethod = "GET"
////                    setRequestProperty(
////                            "Content_Type",
////                            "application/x-www-form-urlencoded"
////                    )
////
////                }
////                conn.connect()
////                val cookiesTest: Map<*, *> = conn.headerFields
////                val headerKeys = arrayOfNulls<String>(cookiesTest.size)
////                val headerValues = arrayOfNulls<String>(cookiesTest.size)
////                for (i in 0 until cookiesTest.size) {
////                    headerKeys[i] = conn.getHeaderFieldKey(i)
////                    headerValues[i] = conn.getHeaderField(i)
////                    println(i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
////                }
////                manager = CookieManager()
////                Log.d("test", "쿠키매니저 ${CookieHandler.setDefault(manager)}")
////                CookieHandler.setDefault(manager)
////
////                if (manager.cookieStore.cookies.size > 0) {
////                    System.out.println("From cookieManager : " + manager.cookieStore.cookies);
////                }
////
////                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
////                    print("auth 연결서공")
////                    val streamReader = InputStreamReader(conn.inputStream)
////                    val buffered = BufferedReader(streamReader)
////
////                    val content = StringBuilder()
////                    while (true) {
////                        val line = buffered.readLine() ?: break
////                        content.append(line)
////                    }
////                    print("연결")
////                    Log.d("test", "Auth" + content.toString())
////                    conn.disconnect()
////                }
////
////            } catch (e: Exception) {
////                println("오류")
////            }
////        }
////        auth.join()
////
////
////        var hang = CoroutineScope(Dispatchers.IO).launch {
////            try{
////            val url = URL("https://info.gachon.ac.kr/exsignon/sso/sso_index.jsp")
////            val conn = (url.openConnection() as HttpURLConnection).apply {
////                requestMethod = "POST"
////                setRequestProperty(
////                        "Content_Type",
////                        "application/x-www-form-urlencoded"
////                )
////                setRequestProperty("Cookie", cookies)
////            }
////            val cookiesTest: Map<*, *> = conn.headerFields
////            val headerKeys = arrayOfNulls<String>(cookiesTest.size)
////            val headerValues = arrayOfNulls<String>(cookiesTest.size)
////            for (i in 0 until cookiesTest.size) {
////                headerKeys[i] = conn.getHeaderFieldKey(i)
////                headerValues[i] = conn.getHeaderField(i)
////                Log.d("test","학사행정 헤더" + i.toString() + ". " + headerKeys[i] + " : " + headerValues[i])
////            }
////
////            cManager = CookieManager()
////            CookieHandler.setDefault(cManager)
////
////
////            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
////                Log.d("test","hang 연결서공")
////                val streamReader = InputStreamReader(conn.inputStream)
////                val buffered = BufferedReader(streamReader)
////                val content = StringBuilder()
////                while (true) {
////                    val line = buffered.readLine() ?: break
////                    content.append(line)
////                }
////                print("연결")
////                Log.d("test", "학사행정" + content.toString())
////            }
////            } catch (e: Exception){Log.d("error", "error!")}
////        }
////        hang.join()
//////        Glide.with(context).load("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826").into(img)
////        var final = CoroutineScope(Dispatchers.IO).launch{
////            try {
////                var url = URL("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826")
////                val conn = (url.openConnection() as HttpURLConnection).apply {
////                    requestMethod = "GET"
////                    setRequestProperty(
////                            "Content_Type",
////                            "image/jpeg;charset=utf-8"
////                    )
////                    setRequestProperty("Cookie", "JSESSIONID=djME0Ptx8qXnsgPVzJ2abwsnqcJBxE7wBKRDi5IkGsaubhkIJCb6e0ws1FrnJiQE.Z2NfZG9tYWluL25nY3dhczNfaGFrc2Ey")
////                }
////
////                manager = CookieManager()
////                CookieHandler.setDefault(manager)
////
////                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
////                    Log.d("test", "사진얻기 연결서공")
////                    val streamReader = InputStreamReader(conn.inputStream)
////                    val buffered = BufferedReader(streamReader)
//////     Glide.with(context).load("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826").into(img)
//////                    GlideApp.with(context).load("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826").placeholder(R.drawable.ic_launcher_background).error(R.drawable.left_id).override(1024).into(img)
////                    val content = StringBuilder()
////                    while (true) {
////                        val line = buffered.readLine() ?: break
////                        content.append(line)
////                    }
////                    var input = conn.inputStream
////                    var bitmap = BitmapFactory.decodeStream(input)
////                    Log.d("test", "bitmap$bitmap")
//////                    GlideApp.with(context).asBitmap().load(bitmap).into(img)
////                    print("연결")
////                    Log.d("test", content.toString())
////                }
////            } catch (e: Exception){
////
////            }
////        }
////        final.join()
////        img.setImageBitmap(imgBitmap)
//    }
//
//
//
//
//}