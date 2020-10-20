package com.example.mycollege.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.mycollege.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

lateinit var request : IntCom


class HomeFragment : Fragment() {
    lateinit var root:View
    private lateinit var homeViewModel: HomeViewModel
    @SuppressLint("HardwareIds")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)

        root.sendButton.setOnClickListener(this::sendCustomCommand)
        var androidid = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID)
        request = IntCom(androidid)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun sendCustomCommand(i: View){

        //Создаю новый объект Message(%тект сообщения%, %тип сообщения%)
        val com: Message = Message(commandBox.text.toString(), MessageType.Text)

        //Если сообщение пустое
        if(com.body.isBlank())
            return

        com.type = MessageType.Text
        request.send(com)
        commandBox.text.clear()
    }

    var oldtext: Int = 0;

    private fun getChat(){
        Thread {
            if(activity != null){
                val main: MainActivity = activity as MainActivity

                var lastLineVisible = scrollerOutp.scrollY + scrollerOutp.height + 200 >= conteiner.height || conteiner.childCount <= 1

                var utomes = request.getChat()
                if(utomes.size != oldtext){

                    var newmes = utomes.slice(utomes.size - 50 until utomes.size)

                    oldtext = utomes.size
                    updateContainer(newmes)

                    if(lastLineVisible){
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

    private fun updateContainer(newmes : List<UsernameToMsg>)
    {
        for(utom in newmes){
            var tv = TextView(root.context)
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tv.text = ">${utom.username}: ${utom.message}"
            tv.isCursorVisible = true

            //Server отображается красныy
            tv.setPadding(10,10,10,10)

            (activity as MainActivity).runOnUiThread(java.lang.Runnable {
                conteiner.addView(tv)
            })
        }
    }


    private fun updateUi()
    {
        Thread {
            while (scrollerOutp != null) {
                try {
                    getChat()
                }
                finally {}
                Thread.sleep(800)
            }
        }.start()
    }

}
