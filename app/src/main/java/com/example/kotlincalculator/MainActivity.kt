package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun numberAction(view: View) {
        if (view is Button){
            if (view.text=="."){
                if (canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal=false
            }
            else
                workingsTV.append(view.text)

            canAddOperation=true

        }
    }
    fun operationAction(view: View) {
        if (view is Button && canAddOperation){
            workingsTV.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }
    fun delAction(view: View) {
        val length = workingsTV.length()
        if (length>0)
            workingsTV.text=workingsTV.text.subSequence(0,length-1)
    }
    fun allClearAction(view: View) {
        workingsTV.text=""
        resultsTV.text=""
    }
    fun equalsAction(view: View) {
        resultsTV.text = calcresults()
    }

    private fun calcresults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty())return ""
        val timesDivision = timesDivCalculate(digitsOperators)
        if (timesDivision.isEmpty())return ""
        val result = addsubtract(timesDivision)

        return result.toString()
    }

    private fun addsubtract(passedlist: MutableList<Any>): Float {
        var result = passedlist[0] as Float

        for (i in passedlist.indices){
            if (passedlist[i] is Char && i != passedlist.lastIndex){
                val operator = passedlist[i]
                val nextDigit = passedlist[i+1] as Float
                when(operator){
                    '+'-> result+=nextDigit
                    '-'-> result+=nextDigit
                }
            }
        }

        return result
    }

    private fun timesDivCalculate(passedlist: MutableList<Any>): MutableList<Any> {
         var list = passedlist
        while (list.contains('x') || list.contains('/')){
            list=calctimesdiv(list)
        }
        return list
    }

    private fun calctimesdiv(passedlist: MutableList<Any>): MutableList<Any> {
        val newlist= mutableListOf<Any>()
        var restartIndex = passedlist.size
        for (i in passedlist.indices){
            if(passedlist[i] is Char && i != passedlist.lastIndex && i<restartIndex){
                val operator = passedlist[i]
                val prevDigit = passedlist[i-1] as Float
                val nextDigit = passedlist[i+1] as Float
                when(operator){
                    'x' -> {
                        newlist.add(prevDigit * nextDigit)
                    }
                    '/' -> {
                        newlist.add(prevDigit / nextDigit)
                    }
                    else -> {
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }
            }
            if (i>restartIndex)
                newlist.add(passedlist[i])
        }

        return newlist
    }

    private fun digitsOperators(): MutableList<Any>{
        val list= mutableListOf<Any>()
        var currendigit=""
        for (character in workingsTV.text){
            if (character.isDigit() || character =='.')
                currendigit+=character
            else{
                list.add(currendigit.toFloat())
                currendigit=""
                list.add(character)
            }

        }
        if (currendigit!="")
            list.add(currendigit.toFloat())

        return list
    }
}