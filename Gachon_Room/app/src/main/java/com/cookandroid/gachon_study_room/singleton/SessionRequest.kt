package com.cookandroid.gachon_study_room.singleton

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SessionRequest {
    private lateinit var session: Job
    fun request() = runBlocking {
        session = CoroutineScope(Dispatchers.Default).launch {

            try {
                val url = URL("https://info.gachon.ac.kr/exsignon/sso/sso_index.jsp")
                var urlConn = url.openConnection() as HttpURLConnection

                // urlConn 설정
                urlConn.requestMethod = "POST"
                urlConn.setRequestProperty("Accept-Charset", "UTF-8")
                urlConn.setRequestProperty(
                        "Content_Type",
                        "application/x-www-form-urlencoded"
                )
                if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("test", "session 연결성공")
                    val streamReader = InputStreamReader(urlConn.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while(true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }

                    Log.d("test", "content $content")

                    // 스트림과 커넥션 해제
                    buffered.close()

                } else {    // 통신이 실패한 이유를 찍기위한 로그
                    Log.d("통신 결과", "에러" + urlConn.responseCode)
                }
            } catch (e: Exception) {
                Log.d("test", e.printStackTrace().toString() + "session")
            }
        }
    }
}