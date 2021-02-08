package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun init() {
        super.init()
        Log.d("test", MySharedPreferences.getUserId(requireContext()))
        logout()
        http()
    }

    private fun http()  {
        CoroutineScope(Dispatchers.Main).launch {
            val html = CoroutineScope(Dispatchers.Default).async {
                try {
                    val url = URL("https://sso.gachon.ac.kr/svc/tk/Login.do")
                    var urlConn = url.openConnection() as HttpURLConnection
                    // urlConn 설정
                    urlConn.requestMethod = "POST"
                    urlConn.setRequestProperty("Accept-Charset", "UTF-8")
                    urlConn.setRequestProperty(
                        "Context_Type",
                        "application/x-www-form-urlencoded;cahrset=UTF-8"
                    );
                    val osw = OutputStreamWriter(urlConn.outputStream)
                    var sendMsg = "user_id="+MySharedPreferences.getUserId(requireContext())+"&user_password="+MySharedPreferences.getUserPass(
                        requireContext()
                    )+"&id=www"
                    osw.write(sendMsg)
                    osw.flush()

                    Log.d("test", osw.toString())
                    if (urlConn != null) {
                        var str:String
                        val tmp = InputStreamReader(urlConn.inputStream, "UTF-8")
                        val reader = BufferedReader(tmp)
                        val buffer = StringBuffer()
                        while (reader.readLine().also { str = it } != null) {
                            buffer.append(str)
                        }
                        var receiveMsg = buffer.toString()
                        Log.d("receiveTest", receiveMsg)
                    }
                    else {    // 통신이 실패한 이유를 찍기위한 로그
                        Log.d("통신 결과", "에러" + urlConn.responseCode )
                    }
                } catch (e: Exception) {
                    Log.d("error", e.printStackTrace().toString() + "dd")
                }
            }.await()

        }
    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            MySharedPreferences.clearUser(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            toast("로그아웃 되었습니다.")
            requireActivity().finish()
        }
    }

}