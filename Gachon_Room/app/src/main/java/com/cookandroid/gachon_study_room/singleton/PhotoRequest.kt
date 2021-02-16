package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.cookandroid.gachon_study_room.ui.fragment.MainFragment
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PhotoRequest(context: Context) {
    private lateinit var login: Job
 //   private lateinit var session: Job
    private val id by lazy { MySharedPreferences.getUserId(context) }
    private val pw by lazy { MySharedPreferences.getUserPass(context) }

    fun requestLogin() = runBlocking {
        login = CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://sso.gachon.ac.kr/svc/tk/Login.do")
                var urlConn = url.openConnection() as HttpURLConnection

                // urlConn 설정
                urlConn.requestMethod = "POST"
                urlConn.setRequestProperty("Accept-Charset", "UTF-8")
                urlConn.setRequestProperty(
                        "Content_Type",
                        "application/x-www-form-urlencoded"
                )
                urlConn.doOutput = true

                val osw = OutputStreamWriter(urlConn.outputStream)
                var sendMsg = "user_id=$id&user_password=$pw&id=www"
                osw.write(sendMsg)
                osw.flush()

                if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("test", "login 연결성공")

                    val streamReader = InputStreamReader(urlConn.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while(true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }

                    Log.d("test", "content $content")
                    // 스트림과 커넥션 해제
//                    Log.d("test", "receiveMsg $receiveMsg")
                    buffered.close()

                } else {    // 통신이 실패한 이유를 찍기위한 로그
                    Log.d("통신 결과", "에러" + urlConn.responseCode)
                }
            } catch (e: Exception) {
                Log.d("error", e.printStackTrace().toString() + "dd")
            }
        }
        login.join()
        Log.d("test", "login 후")
//        session = CoroutineScope(Dispatchers.IO).launch {
//
//            try {
//                val url = URL("https://info.gachon.ac.kr/exsignon/sso/sso_index.jsp")
//                var urlConn = url.openConnection() as HttpURLConnection
//
//                // urlConn 설정
//                urlConn.requestMethod = "POST"
//                urlConn.setRequestProperty("Accept-Charset", "UTF-8")
//                urlConn.setRequestProperty(
//                        "Content_Type",
//                        "application/x-www-form-urlencoded"
//                )
//                if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.d("test", "session 연결성공")
//                    val streamReader = InputStreamReader(urlConn.inputStream)
//                    val buffered = BufferedReader(streamReader)
//
//                    val content = StringBuilder()
//                    while(true) {
//                        val line = buffered.readLine() ?: break
//                        content.append(line)
//                    }
//
//                    Log.d("test", "content $content")
//
//                    // 스트림과 커넥션 해제
//                    buffered.close()
//                    urlConn.disconnect()
//
//                } else {    // 통신이 실패한 이유를 찍기위한 로그
//                    Log.d("통신 결과", "에러" + urlConn.responseCode)
//                }
//            } catch (e: Exception) {
//                Log.d("test", e.printStackTrace().toString() + "session")
//            }
//        }
//        session.join()

    }

}