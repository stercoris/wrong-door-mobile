package com.example.mycollege.ui.dashboard

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.mycollege.IntCom
import com.example.mycollege.Message
import com.example.mycollege.MessageType
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlin.concurrent.thread


class DashboardFragment : Fragment() {

    private lateinit var req: IntCom

    private lateinit var dashboardViewModel: DashboardViewModel
    @SuppressLint("HardwareIds")
    private lateinit var androidid: String
    @SuppressLint("HardwareIds")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        var root = inflater.inflate(com.example.mycollege.R.layout.fragment_dashboard, container, false)
        //При нажати на колесо(пока не нужно!!!)
//        root.rltwhl.setOnClickListener(this::spin)
        root.pcShutdown.setOnClickListener(this::sendButtonCom)
        root.pcWpsKill.setOnClickListener(this::sendButtonCom)
        root.pcPsKill.setOnClickListener(this::sendButtonCom)
        root.pcExKill.setOnClickListener(this::sendButtonCom)
        root.pcChKill.setOnClickListener(this::sendButtonCom)
        root.logOut.setOnClickListener(this::sendButtonCom)
        root.pcStKill.setOnClickListener(this::sendButtonCom)
        root.pcVsKill.setOnClickListener(this::sendButtonCom)
        root.website.setOnClickListener(this::sendButtonCom)
        root.lockwin.setOnClickListener(this::sendButtonCom)
        root.zapas.setOnClickListener(this::sendButtonCom)
        root.sosat.setOnClickListener(this::sendButtonCom)
        root.teacherAlert.setOnClickListener(this::alertTeacher)
        root.deleteName.setOnClickListener {
            nameBox.text.clear()
        }
        //Коммуникация с сервером
        androidid = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        req = IntCom(androidid)


        return root
    }

    override fun onStart() {
        super.onStart()
        var nums: Array<String> = arrayOf()
        for (i in 1..30)
            nums += if(i.toString().length == 1) "0$i" else i.toString()
        var adapter: ArrayAdapter<String> =
            activity?.let { ArrayAdapter(it, R.layout.simple_spinner_dropdown_item, nums) }!!
        pcNum.adapter = adapter


        adapter =  activity?.let { ArrayAdapter(it, R.layout.simple_spinner_dropdown_item, arrayOf<String>("Z","C","A","T","V","X","L")) }!!
        pcIndex.adapter = adapter
    }


    private fun alertTeacher(but: View){
        val pcIndex: String = pcIndex.selectedItem.toString()
        val pcNum: String = pcNum.selectedItem.toString()
        var num: String
        Thread{
            for(i in 1..30){
                num = if(i.toString().length == 1) "0$i" else "$i"

                //Пятая строка
                var com: Message = Message("-d -s -i \\\\$pcIndex$num cmd /C \"rundll32.exe user32.dll,LockWorkStation\"", MessageType.PsExec)
                //req.send(com)
                println(com.body)
                Thread.sleep(20)
            }
        }.start()

    }
    private fun sendButtonCom(but: View){
        val pcIndex: String = pcIndex.selectedItem.toString()
        val pcNum: String = pcNum.selectedItem.toString()
        if(pcNum.isBlank() || pcIndex.isBlank())
            return
        var com: Message
        var pcName = if(nameBox.text.isNotEmpty()) nameBox.text else "$pcIndex$pcNum"

        com = when(but.tag){

            //Первая строка
            "1" -> Message("shutdown /s /m \\\\$pcName /t 0",MessageType.CMD)
            "2"  -> Message("taskkill /s $pcName /pid wps.exe",MessageType.CMD)
            "3"   -> Message("taskkill /s $pcName /pid photoshop.exe",MessageType.CMD)


            //Вторая строка
            "4"   -> Message("taskkill /s $pcName /pid explorer.exe",MessageType.CMD)
            "5"   -> Message("taskkill /s $pcName /pid chrome.exe",MessageType.CMD)
            "6"   -> Message("-d -s -i \\\\$pcName cmd /C \"shutdown -l\"",MessageType.PsExec)


            //Третья строка
            "7"   -> Message("taskkill /s $pcName /pid student.exe",MessageType.CMD)
            "8"   -> Message("taskkill /s $pcName /pid devenv.exe",MessageType.CMD)
            "9"   -> Message("-d -s -i \\\\$pcName \"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe\" \"games.mail.ru\"",MessageType.PsExec)

            //Четвертая строка
            "10"   -> Message("-d -s -i \\\\$pcName cmd /C \"rundll32.exe user32.dll,LockWorkStation\"",MessageType.PsExec)
            "11"   -> Message("-d -s -i \\\\$pcName \"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe\"" +
                    "\"https://static.wikia.nocookie.net/counterstrike/images/3/3c/Cs_1.6_background.png/revision/latest?cb=20140922174445&path-prefix=ru\"",MessageType.PsExec)
            "12"   -> Message("-d -s -i \\\\$pcName \"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe\"" +
                    "\"https://www.google.com/search?q=%D0%BA%D0%B0%D0%BA+%D0%B6%D0%B5+%D1%8F+%D0%BB%D1%8E%D0%B1%D0%BB%D1%8E+%D1%81%D0%BE%D1%81%D0%B0%D1%82%D1%8C!\"",MessageType.PsExec)

            else -> return
        }


        val context: Context = this.context ?: return
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Вы уверены?")
        val ok = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->req.send(com)
            }
        }
        dialog.setPositiveButton("да",ok)
        dialog.setNegativeButton("не",ok)
        var dial:AlertDialog = dialog.create()
        dial.show()
    }

//    private fun spin(btn: View){
//        rotateTo(Random.nextInt(1,30))
//    }

//    private fun rotateTo(endPoint: Int)
//    {
//        println(endPoint)
//        var endangle: Float = ((30f - endPoint) * 12f)+360*5  + Random.nextInt(- 470,730).toFloat() / 100
//        println(endangle)
//        val rotate: ObjectAnimator = ObjectAnimator.ofFloat(rltwhl, "rotation", rltwhl.rotation%360, endangle)
//        rotate.duration = 8000
//        rotate.start()
//        Thread{
//            while (rotate.isRunning && rltwhl != null){
//                var currentnum = 30 - (rltwhl.rotation%360 - 7.25)/12
//                println(currentnum)
//                (activity as MainActivity).runOnUiThread{
//                    if(pcNum != null){
//                        var addzero = currentnum.toInt().toString()
//                        if (addzero.length == 1)
//                        {
//                            addzero = "0$addzero"
//                        }
//                        pcNum.setText(addzero)
//
//                    }
//                }
//                Thread.sleep(30)
//            }
//        }.start()
//    }
}
