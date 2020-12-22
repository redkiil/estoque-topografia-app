package com.augusto.qrcode_estoque

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.*

class RequestListAdapter (private var context: Context, private var itemList: MutableList<ListModel>): BaseExpandableListAdapter() {
    init {
        itemList.forEach {
            it.items.add(Item(-1,-1))
        }
    }
    override fun getGroup(groupPosition: Int): Any {
        return groupPosition
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = convertView

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.request_list_item, null)
        }

        val mainInfoText = convertView?.findViewById<TextView>(R.id.mainInfoText)
        val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm",Locale.getDefault())
        val data = dateFormat.format(itemList[groupPosition].date)
        val stringMainInfo = "Protocol: <b>${itemList[groupPosition].protocol}</b> | Data: <b>${data}</b>"
        mainInfoText?.text = HtmlCompat.fromHtml(stringMainInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val subInfoText = convertView?.findViewById<TextView>(R.id.subInfoText)
        val stringSubInfo = "Total de itens:<b>(${itemList[groupPosition].items.size-1})</b> - Frota/C.C: <b>${itemList[groupPosition].usedby}</b>"
        subInfoText?.text = HtmlCompat.fromHtml(stringSubInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val myIcon = convertView?.findViewById<ImageView>(R.id.theicon)

        if(isExpanded){
            myIcon?.setImageResource(R.drawable.ic_057_minus)
            convertView?.setBackgroundResource(R.drawable.group_expandable_style)
        }else{
            myIcon?.setImageResource(R.drawable.ic_067_plus)
            convertView?.setBackgroundResource(R.drawable.child_expandable_style)
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return itemList[groupPosition].items.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val convertView: View?
        if(itemList[groupPosition].items.size-1 == childPosition){
                convertView = LayoutInflater.from(context).inflate(R.layout.request_list_item_edit, parent, false)

                val textRetiredBy = convertView.findViewById<TextView>(R.id.extraInfo)
                val stringRetiredBy = "Retirado por: <b>${itemList[groupPosition].retiredby}</b>"
                textRetiredBy.text = HtmlCompat.fromHtml(stringRetiredBy, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val button = convertView.findViewById<ImageButton>(R.id.editButton)
                button.setOnClickListener {
                    val intent = Intent(context, RequestActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("data", itemList[groupPosition])
                    context.startActivity(intent)
                }

        }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.request_list_subitem, null)

                val textView = convertView.findViewById<TextView>(R.id.textItem)
                val textItem = try {
                    itemList[groupPosition].itemsinfo[childPosition].name
                } catch (e: IndexOutOfBoundsException) {
                    "Não informado"
                }
                val stringTextItem = "&#8226; ${textItem}<b>(${itemList[groupPosition].items[childPosition].code})</b> - Quantidade:<b> ${itemList[groupPosition].items[childPosition].quant}</b>"
                textView.text = HtmlCompat.fromHtml(stringTextItem, HtmlCompat.FROM_HTML_MODE_LEGACY)

        }


        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return itemList.size
    }


}
