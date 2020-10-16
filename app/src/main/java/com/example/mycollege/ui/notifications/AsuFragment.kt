package com.example.mycollege.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.mycollege.IntCom
import com.example.mycollege.MainActivity
import com.example.mycollege.R
import kotlinx.android.synthetic.main.fragment_games.*
import java.util.*


class AsuFragment : Fragment() {


    @SuppressLint("HardwareIds")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_games, container, false)
        var androidid = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID)

        var con = IntCom(androidid)
        Thread{
            var studnames = ArrayList<String>()
            for(student in con.getStudents()){
                studnames.add(student.name)
                println("${student.groupid} == ${student.name} == ${student.id}")
            }
            var cmt = context
            val adapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, studnames) }
            (activity as MainActivity).runOnUiThread{
                studspn.adapter = adapter
            }



        }.start()

        return root
    }

}

