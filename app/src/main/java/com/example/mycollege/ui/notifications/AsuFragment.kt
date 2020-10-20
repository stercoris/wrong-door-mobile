package com.example.mycollege.ui.notifications

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mycollege.*
import com.example.mycollege.ui.home.request
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_games.view.*
import java.util.*


class AsuFragment : Fragment() {

    lateinit var root:View
    @SuppressLint("HardwareIds")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//        Thread{
//            var studnames = ArrayList<String>()
//            for(student in con.getStudents()){
//                studnames.add(student.name + " " + student.id)
//                println("${student.groupid} == ${student.name} == ${student.id}")
//            }
//            var cmt = context
//            val adapter =
//                context?.let { ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, studnames) }
//            (activity as MainActivity).runOnUiThread{
//                studspn.adapter = adapter
//            }
//
//
//
//        }.start()
        root = inflater.inflate(R.layout.fragment_home, container, false)

        root.sendButton.setOnClickListener(this::sendCustomCommand)
        var androidid = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        request = IntCom(androidid)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun sendCustomCommand(i: View) {

        //Создаю новый объект Message(%тект сообщения%, %тип сообщения%)
        val com: Message = Message(commandBox.text.toString(), MessageType.Text)

        //Если сообщение пустое или это пуставя команда, то
        if (com.body.isBlank() || (com.body.substring(0, 1) == "/" && com.body.substring(1)
                .isBlank())
        ) {
            commandBox.text.clear()
            return
        }

        //Проверка на шутдовн(можно выключить комп на котором всё хостица)
        if (com.body.contains("shutdown") and !com.body.contains("/m")) {
            val context: Context = this.context ?: return
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Если хочешь вырубить комп,то вырубай через Быстрые")
            val ok = DialogInterface.OnClickListener { _, _ -> }
            dialog.setPositiveButton("понятна", ok)
            var dial: AlertDialog = dialog.create()
            dial.show()
            return
        }

        var type = cmdTypes.selectedItem.toString()
        //Разные типы команd

        when {
            type == "PsExec" -> com.type = MessageType.PsExec

            type == "CMD" ->    com.type = MessageType.CMD


            else -> return
        }



        //Уверен ли пользователь
        if (com.type != MessageType.Text) {

            val context: Context = this.context ?: return
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Точно выполнить??аа")
            val ok = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        request.send(com)
                    }
                }
            }
            dialog.setPositiveButton("да", ok)
            dialog.setNegativeButton("не", ok)
            var dial: AlertDialog = dialog.create()
            dial.show()

        } else {
            request.send(com)
            commandBox.text.clear()
        }
    }

    var oldtext: Int = 0;

    private fun getOutp() {
        Thread {
            if (activity != null) {
                val main: MainActivity = activity as MainActivity

                var lastLineVisible =
                    scrollerOutp.scrollY + scrollerOutp.height + 200 >= conteiner.height || conteiner.childCount <= 1

                var utomes = request.getChat()
                if (utomes.size != oldtext) {

                    var newmes = utomes.slice(utomes.size - 50 until utomes.size)

                    oldtext = utomes.size
                    updateContainer(newmes)

                    if (lastLineVisible) {
                        main.runOnUiThread(java.lang.Runnable {
                            scrollerOutp.post(Runnable {
                                scrollerOutp.isSmoothScrollingEnabled = false;
                                scrollerOutp.fullScroll(
                                    ScrollView.FOCUS_DOWN
                                )
                                scrollerOutp.isSmoothScrollingEnabled = true;

                            })
                        })
                    }


                    Thread.sleep(300)
                }


            }
        }.start()
    }

    private fun updateContainer(newmes: List<UsernameToMsg>) {
        for (utom in newmes) {
            var tv = TextView(root.context)
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tv.text = ">${utom.username}: ${utom.message}"
            tv.isCursorVisible = true

            //Server отображается красным
            if (utom.username == "/Server") {
                tv.setTextColor(Color.parseColor("#FF0000"))
            } else {
            }
            tv.setTextColor(Color.parseColor("#00FF37"))

            tv.setPadding(10, 10, 10, 10)

            (activity as MainActivity).runOnUiThread(java.lang.Runnable {
                conteiner.addView(tv)
            })
        }
    }


    private fun updateUi() {
        Thread {
            while (scrollerOutp != null) {
                try {
                    getOutp()
                } finally {
                }
                Thread.sleep(800)
            }
        }.start()
    }

}










